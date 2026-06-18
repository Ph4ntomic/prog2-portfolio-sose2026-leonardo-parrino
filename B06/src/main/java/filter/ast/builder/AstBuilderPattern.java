package filter.ast.builder;

import filter.FilterParser;
import filter.ast.nodes.CompOp;
import filter.ast.nodes.Expr;
import filter.ast.nodes.Value;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AstBuilderPattern {

  public Expr translate(FilterParser.QueryContext ctx) {
    return switch (ctx) {
      case FilterParser.QueryContext query -> buildExpr(query.expr());
    };
  }

  private Expr buildExpr(FilterParser.ExprContext ctx) {
    return switch (ctx) {
      case FilterParser.ExprContext expr -> buildOrExpr(expr.orExpr());
    };
  }

  private Expr buildOrExpr(FilterParser.OrExprContext ctx) {
    return switch (ctx) {
      case FilterParser.OrExprContext orExpr ->
          foldLeft(orExpr.andExpr(), this::buildAndExpr, Expr.Or::new);
    };
  }

  private Expr buildAndExpr(FilterParser.AndExprContext ctx) {
    return switch (ctx) {
      case FilterParser.AndExprContext andExpr ->
          foldLeft(andExpr.notExpr(), this::buildNotExpr, Expr.And::new);
    };
  }

  private Expr buildNotExpr(FilterParser.NotExprContext ctx) {
    return switch (ctx) {
      case FilterParser.NotExprContext notExpr when notExpr.NOT() != null ->
          new Expr.Not(buildNotExpr(notExpr.notExpr()));
      case FilterParser.NotExprContext notExpr -> buildPrimary(notExpr.primary());
    };
  }

  private Expr buildPrimary(FilterParser.PrimaryContext ctx) {
    return switch (ctx) {
      case FilterParser.PrimaryContext primary when primary.comparison() != null ->
          buildComparison(primary.comparison());
      case FilterParser.PrimaryContext primary -> buildExpr(primary.expr());
    };
  }

  private Expr buildComparison(FilterParser.ComparisonContext ctx) {
    return switch (ctx) {
      case FilterParser.ComparisonContext comparison when comparison.COMPOP() != null ->
          new Expr.Comparison(
              comparison.IDENTIFIER().getText(),
              CompOp.fromSymbol(comparison.op.getText()),
              buildLiteral(comparison.literal()));
      case FilterParser.ComparisonContext comparison ->
          new Expr.InList(
              comparison.IDENTIFIER().getText(), buildLiteralList(comparison.literalList()));
    };
  }

  private List<Value> buildLiteralList(FilterParser.LiteralListContext ctx) {
    return switch (ctx) {
      case FilterParser.LiteralListContext literalList ->
          literalList.literal().stream().map(this::buildLiteral).toList();
    };
  }

  private Value buildLiteral(FilterParser.LiteralContext ctx) {
    return switch (ctx) {
      case FilterParser.LiteralContext literal when literal.STRING() != null ->
          new Value.Str(unquote(literal.STRING().getText()));
      case FilterParser.LiteralContext literal ->
          new Value.Num(Integer.parseInt(literal.NUMBER().getText()));
    };
  }

  private static <C> Expr foldLeft(
      List<C> contexts, Function<C, Expr> mapper, BiFunction<Expr, Expr, Expr> nodeFactory) {
    if (contexts.isEmpty()) {
      throw new IllegalArgumentException("Expected at least one parse-tree child");
    }

    var result = mapper.apply(contexts.getFirst());
    for (int i = 1; i < contexts.size(); i++) {
      result = nodeFactory.apply(result, mapper.apply(contexts.get(i)));
    }
    return result;
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
