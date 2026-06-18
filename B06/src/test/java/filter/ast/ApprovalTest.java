package filter.ast;

import filter.ast.builder.AstBuilderPattern;
import filter.ast.builder.AstBuilderVisitor;
import filter.ast.builder.AstBuilders;
import filter.ast.printer.AstPrinter;
import java.util.List;
import java.util.StringJoiner;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

public class ApprovalTest {

  private static final List<String> QUERIES =
      List.of(
          "artist == \"Beatles\"",
          "year == 1965",
          "artist == \"Beatles\" and year == 1965",
          "year <= 1990 and artist == \"Beatles\" and year > 1960",
          "(year <= 1990 or artist == \"Beatles\") and year > 1960",
          "genre in (\"rock\", \"jazz\") or year <= 1990 and not artist == \"Beatles\"",
          "not not artist == \"Beatles\"",
          "not (genre in (\"rock\", \"jazz\") or year >= 2000)");

  @Test
  void approvesCanonicalAstPrintsForBothBuilders() {
    Approvals.verify(renderCanonicalAstPrints());
  }

  @Test
  void approvesSimplificationExamples() {
    var output = new StringJoiner(System.lineSeparator() + System.lineSeparator());

    List.of(
            "not not artist == \"Beatles\"",
            "not not not artist == \"Beatles\"",
            "not (not genre in (\"rock\", \"jazz\") and not not year >= 1990)")
        .forEach(
            query -> {
              var raw = new AstBuilderPattern().translate(AstBuilders.parse(query));
              var simplified = AstBuilders.fromQuery(query, new AstBuilderPattern()::translate);
              output.add(
                  "Query: "
                      + query
                      + System.lineSeparator()
                      + "Raw: "
                      + AstPrinter.toString(raw)
                      + System.lineSeparator()
                      + "Simplified: "
                      + AstPrinter.toString(simplified));
            });

    Approvals.verify(output.toString());
  }

  private static String renderCanonicalAstPrints() {
    var output = new StringJoiner(System.lineSeparator() + System.lineSeparator());

    for (var query : QUERIES) {
      var patternAst = AstBuilders.fromQuery(query, new AstBuilderPattern()::translate);
      var visitorAst = AstBuilders.fromQuery(query, new AstBuilderVisitor()::translate);

      output.add(
          "Query: "
              + query
              + System.lineSeparator()
              + "Pattern: "
              + AstPrinter.toString(patternAst)
              + System.lineSeparator()
              + "Visitor: "
              + AstPrinter.toString(visitorAst));
    }

    return output.toString();
  }
}
