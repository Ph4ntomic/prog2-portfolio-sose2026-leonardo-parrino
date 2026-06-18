package filter.ast;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import filter.ast.builder.AstBuilderPattern;
import filter.ast.builder.AstBuilderVisitor;
import filter.ast.builder.AstBuilders;
import filter.ast.eval.Evaluator;
import filter.ast.nodes.CompOp;
import filter.ast.nodes.Expr;
import filter.ast.nodes.Value;
import filter.model.Genre;
import filter.model.MediaItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AstTest {

  @Test
  @DisplayName("Pattern builder maps precedence and in-list expressions to the compact AST")
  void patternBuilderCreatesExpectedComplexAst() {
    var query = "genre in (\"rock\", \"jazz\") or year <= 1990 and not artist == \"Beatles\"";

    var expected =
        new Expr.Or(
            new Expr.InList("genre", List.of(new Value.Str("rock"), new Value.Str("jazz"))),
            new Expr.And(
                new Expr.Comparison("year", CompOp.LE, new Value.Num(1990)),
                new Expr.Not(new Expr.Comparison("artist", CompOp.EQ, new Value.Str("Beatles")))));

    assertEquals(expected, pattern(query));
  }

  @Test
  @DisplayName("Visitor keeps left/right operands in source order for chained AND nodes")
  void visitorPreservesLeftAssociativeOperandOrder() {
    var query = "year < 1970 and artist == \"Beatles\" and genre == \"rock\"";

    var expected =
        new Expr.And(
            new Expr.And(
                new Expr.Comparison("year", CompOp.LT, new Value.Num(1970)),
                new Expr.Comparison("artist", CompOp.EQ, new Value.Str("Beatles"))),
            new Expr.Comparison("genre", CompOp.EQ, new Value.Str("rock")));

    assertEquals(expected, visitor(query));
  }

  @Test
  @DisplayName("Parentheses override the default AND-before-OR precedence")
  void parenthesesOverridePrecedence() {
    var query = "(year <= 1990 or artist == \"Beatles\") and year > 1960";

    var expected =
        new Expr.And(
            new Expr.Or(
                new Expr.Comparison("year", CompOp.LE, new Value.Num(1990)),
                new Expr.Comparison("artist", CompOp.EQ, new Value.Str("Beatles"))),
            new Expr.Comparison("year", CompOp.GT, new Value.Num(1960)));

    assertAll(
        () -> assertEquals(expected, pattern(query)), () -> assertEquals(expected, visitor(query)));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "artist == \"Beatles\"",
        "year == 1965",
        "not artist != \"Nirvana\"",
        "artist == \"Beatles\" and year == 1965",
        "genre in (\"rock\", \"jazz\")",
        "not (genre in (\"rock\", \"jazz\") or year >= 2000)"
      })
  @DisplayName("Pattern and visitor builders produce equal ASTs for representative queries")
  void bothBuildersAgreeOnRepresentativeQueries(String query) {
    assertEquals(pattern(query), visitor(query));
  }

  @Test
  @DisplayName("String literals are unquoted and simple escape sequences are decoded")
  void stringLiteralsAreUnquotedAndUnescaped() {
    var expected = new Expr.Comparison("title", CompOp.EQ, new Value.Str("A \"quoted\" title"));

    assertAll(
        () -> assertEquals(expected, pattern("title == \"A \\\"quoted\\\" title\"")),
        () -> assertEquals(expected, visitor("title == \"A \\\"quoted\\\" title\"")));
  }

  @Test
  @DisplayName("Simplify eliminates double negations deeply")
  void simplifyEliminatesDoubleNegationsDeeply() {
    var beatles = new Expr.Comparison("artist", CompOp.EQ, new Value.Str("Beatles"));
    var rock = new Expr.Comparison("genre", CompOp.EQ, new Value.Str("rock"));

    var original =
        new Expr.And(
            new Expr.Not(new Expr.Not(beatles)), new Expr.Not(new Expr.Not(new Expr.Not(rock))));

    var expected = new Expr.And(beatles, new Expr.Not(rock));

    assertEquals(expected, AstBuilders.simplify(original));
  }

  @Test
  @DisplayName("AstBuilders.fromQuery returns the normalized AST")
  void fromQueryNormalizesDoubleNot() {
    var expected = new Expr.Comparison("artist", CompOp.EQ, new Value.Str("Beatles"));

    var patternExpr =
        AstBuilders.fromQuery("not not artist == \"Beatles\"", new AstBuilderPattern()::translate);
    var visitorExpr =
        AstBuilders.fromQuery("not not artist == \"Beatles\"", new AstBuilderVisitor()::translate);

    assertAll(() -> assertEquals(expected, patternExpr), () -> assertEquals(expected, visitorExpr));
  }

  @Test
  @DisplayName("Evaluator works with the AST produced by the pattern builder")
  void evaluatorUsesPatternBuiltAst() {
    var query = "artist == \"Beatles\" and year == 1965";
    var expr = AstBuilders.fromQuery(query, new AstBuilderPattern()::translate);

    assertAll(
        () ->
            assertTrue(
                Evaluator.matches(new MediaItem("Help!", "Beatles", Genre.ROCK, 1965), expr)),
        () ->
            assertFalse(
                Evaluator.matches(
                    new MediaItem("Smells Like Teen Spirit", "Nirvana", Genre.GRUNGE, 1991),
                    expr)));
  }

  private static Expr pattern(String query) {
    return new AstBuilderPattern().translate(AstBuilders.parse(query));
  }

  private static Expr visitor(String query) {
    return new AstBuilderVisitor().translate(AstBuilders.parse(query));
  }
}
