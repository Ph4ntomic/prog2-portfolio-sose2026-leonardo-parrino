package filter.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;

import filter.ast.builder.AstBuilderPattern;
import filter.ast.builder.AstBuilderVisitor;
import filter.ast.builder.AstBuilders;
import filter.ast.eval.Evaluator;
import filter.ast.nodes.Expr;
import filter.ast.printer.AstPrinter;
import filter.model.Genre;
import filter.model.MediaItem;
import net.jqwik.api.*;

public class RoundtripPropertiesTest {

  @Property
  void patternBuilderRoundtripIsStable(@ForAll("simpleQueries") String query) {
    var firstPrint = AstPrinter.toString(pattern(query));
    var secondPrint = AstPrinter.toString(pattern(firstPrint));

    assertEquals(firstPrint, secondPrint);
  }

  @Property
  void visitorBuilderRoundtripIsStable(@ForAll("simpleQueries") String query) {
    var firstPrint = AstPrinter.toString(visitor(query));
    var secondPrint = AstPrinter.toString(visitor(firstPrint));

    assertEquals(firstPrint, secondPrint);
  }

  @Property
  void visitorAndPatternBuildersAgree(@ForAll("simpleQueries") String query) {
    assertEquals(AstPrinter.toString(pattern(query)), AstPrinter.toString(visitor(query)));
  }

  @Property
  void crossedRoundtripFromVisitorToPatternIsStable(@ForAll("simpleQueries") String query) {
    var visitorPrint = AstPrinter.toString(visitor(query));
    var reparsedWithPattern = AstPrinter.toString(pattern(visitorPrint));

    assertEquals(visitorPrint, reparsedWithPattern);
  }

  @Property
  void andEvaluationIsCommutative(
      @ForAll("atoms") String leftQuery,
      @ForAll("atoms") String rightQuery,
      @ForAll("mediaItems") MediaItem item) {
    var left = pattern(leftQuery);
    var right = pattern(rightQuery);

    assertEquals(
        Evaluator.matches(item, new Expr.And(left, right)),
        Evaluator.matches(item, new Expr.And(right, left)));
  }

  @Property
  void simplifyKeepsDoubleNegationSemantics(
      @ForAll("simpleQueries") String query, @ForAll("mediaItems") MediaItem item) {
    var original = pattern(query);
    var doubleNegated = new Expr.Not(new Expr.Not(original));
    var simplified = AstBuilders.simplify(doubleNegated);

    assertEquals(original, simplified);
    assertEquals(Evaluator.matches(item, original), Evaluator.matches(item, simplified));
  }

  @Provide
  Arbitrary<String> fields() {
    return Arbitraries.of("title", "artist", "genre", "year");
  }

  @Provide
  Arbitrary<String> values() {
    return Arbitraries.oneOf(stringLiterals(), numberLiterals());
  }

  @Provide
  Arbitrary<String> stringLiterals() {
    return Arbitraries.strings()
        .withChars("abcxyz")
        .ofMinLength(1)
        .ofMaxLength(5)
        .map(s -> "\"" + s + "\"");
  }

  @Provide
  Arbitrary<String> numberLiterals() {
    return Arbitraries.integers().between(1900, 2025).map(Object::toString);
  }

  @Provide
  Arbitrary<String> comparisons() {
    Arbitrary<String> ops = Arbitraries.of("==", "!=", "<", "<=", ">", ">=");

    Arbitrary<String> stringComp =
        Combinators.combine(fields(), ops, stringLiterals())
            .as((f, op, lit) -> f + " " + op + " " + lit);

    Arbitrary<String> numberComp =
        Combinators.combine(Arbitraries.of("year"), ops, numberLiterals())
            .as((f, op, lit) -> f + " " + op + " " + lit);

    return Arbitraries.oneOf(stringComp, numberComp);
  }

  @Provide
  Arbitrary<String> inLists() {
    return Combinators.combine(fields(), values().list().ofMinSize(1).ofMaxSize(4))
        .as((field, literals) -> field + " in (" + String.join(", ", literals) + ")");
  }

  @Provide
  Arbitrary<String> atoms() {
    return Arbitraries.oneOf(comparisons(), inLists());
  }

  @Provide
  Arbitrary<String> simpleQueries() {
    return Arbitraries.recursive(
        this::atoms,
        expression -> {
          var negated = expression.map(query -> "not " + query);
          var grouped = expression.map(query -> "(" + query + ")");
          var binary =
              Combinators.combine(expression, Arbitraries.of("and", "or"), expression)
                  .as((left, op, right) -> left + " " + op + " " + right);

          return Arbitraries.oneOf(negated, grouped, binary);
        },
        3);
  }

  @Provide
  Arbitrary<MediaItem> mediaItems() {
    return Combinators.combine(
            plainTexts(),
            plainTexts(),
            Arbitraries.of(Genre.values()),
            Arbitraries.integers().between(1900, 2025))
        .as(MediaItem::new);
  }

  @Provide
  Arbitrary<String> plainTexts() {
    return Arbitraries.strings()
        .withChars("abcxyz ")
        .ofMinLength(1)
        .ofMaxLength(12)
        .filter(text -> !text.isBlank());
  }

  private static Expr pattern(String query) {
    return AstBuilders.fromQuery(query, new AstBuilderPattern()::translate);
  }

  private static Expr visitor(String query) {
    return AstBuilders.fromQuery(query, new AstBuilderVisitor()::translate);
  }
}
