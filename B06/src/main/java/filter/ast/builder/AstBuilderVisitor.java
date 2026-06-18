package filter.ast.builder;

import filter.FilterBaseVisitor;
import filter.FilterParser;
import filter.ast.nodes.CompOp;
import filter.ast.nodes.Expr;
import filter.ast.nodes.Value;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.BinaryOperator;

public class AstBuilderVisitor extends FilterBaseVisitor<Void> {

  private final Deque<Expr> expressions = new ArrayDeque<>();
  private final Deque<Value> values = new ArrayDeque<>();

  public Expr translate(FilterParser.QueryContext ctx) {
    expressions.clear();
    values.clear();

    visit(ctx);

    if (expressions.size() != 1 || !values.isEmpty()) {
      throw new IllegalStateException(
          "Invalid AST builder state: "
              + expressions.size()
              + " expression(s), "
              + values.size()
              + " value(s)");
    }

    return expressions.pop();
  }

  @Override
  public Void visitQuery(FilterParser.QueryContext ctx) {
    visit(ctx.expr());
    return null;
  }

  @Override
  public Void visitExpr(FilterParser.ExprContext ctx) {
    visit(ctx.orExpr());
    return null;
  }

  @Override
  public Void visitOrExpr(FilterParser.OrExprContext ctx) {
    ctx.andExpr().forEach(this::visit);
    expressions.push(foldPoppedExpressions(ctx.andExpr().size(), Expr.Or::new));
    return null;
  }

  @Override
  public Void visitAndExpr(FilterParser.AndExprContext ctx) {
    ctx.notExpr().forEach(this::visit);
    expressions.push(foldPoppedExpressions(ctx.notExpr().size(), Expr.And::new));
    return null;
  }

  @Override
  public Void visitNotExpr(FilterParser.NotExprContext ctx) {
    if (ctx.NOT() != null) {
      visit(ctx.notExpr());
      expressions.push(new Expr.Not(expressions.pop()));
    } else {
      visit(ctx.primary());
    }
    return null;
  }

  @Override
  public Void visitPrimary(FilterParser.PrimaryContext ctx) {
    if (ctx.comparison() != null) {
      visit(ctx.comparison());
    } else {
      visit(ctx.expr());
    }
    return null;
  }

  @Override
  public Void visitComparison(FilterParser.ComparisonContext ctx) {
    var field = ctx.IDENTIFIER().getText();

    if (ctx.COMPOP() != null) {
      visit(ctx.literal());
      expressions.push(
          new Expr.Comparison(field, CompOp.fromSymbol(ctx.op.getText()), values.pop()));
    } else {
      visit(ctx.literalList());
      expressions.push(new Expr.InList(field, popValues(ctx.literalList().literal().size())));
    }

    return null;
  }

  @Override
  public Void visitLiteralList(FilterParser.LiteralListContext ctx) {
    ctx.literal().forEach(this::visit);
    return null;
  }

  @Override
  public Void visitLiteral(FilterParser.LiteralContext ctx) {
    if (ctx.STRING() != null) {
      values.push(new Value.Str(unquote(ctx.STRING().getText())));
    } else {
      values.push(new Value.Num(Integer.parseInt(ctx.NUMBER().getText())));
    }
    return null;
  }

  private Expr foldPoppedExpressions(int count, BinaryOperator<Expr> nodeFactory) {
    if (count < 1) {
      throw new IllegalArgumentException("Expected at least one expression");
    }

    var ordered = new ArrayDeque<Expr>();
    for (int i = 0; i < count; i++) {
      ordered.addFirst(expressions.pop());
    }

    var result = ordered.removeFirst();
    while (!ordered.isEmpty()) {
      result = nodeFactory.apply(result, ordered.removeFirst());
    }
    return result;
  }

  private List<Value> popValues(int count) {
    var ordered = new ArrayDeque<Value>();
    for (int i = 0; i < count; i++) {
      ordered.addFirst(values.pop());
    }
    return List.copyOf(ordered);
  }

  private static String unquote(String tokenText) {
    var raw = tokenText.substring(1, tokenText.length() - 1);
    var result = new StringBuilder(raw.length());

    for (int i = 0; i < raw.length(); i++) {
      char current = raw.charAt(i);
      if (current == '\\' && i + 1 < raw.length()) {
        result.append(raw.charAt(++i));
      } else {
        result.append(current);
      }
    }

    return result.toString();
  }
}
