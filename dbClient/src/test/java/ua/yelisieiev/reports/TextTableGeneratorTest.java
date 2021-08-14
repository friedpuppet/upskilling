package ua.yelisieiev.reports;

class TextTableGeneratorTest extends AbstractGeneratorTest {
    @Override
    ReportGenerator getGenerator() {
        return new TextTableGenerator();
    }

    @Override
    String getExpectedTable() {
        return """
                ┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
                │First                     │Second                   │Third                    │
                ├──────────────────────────┼─────────────────────────┼─────────────────────────┤
                │1                         │Bob                      │12                       │
                ├──────────────────────────┼─────────────────────────┼─────────────────────────┤
                │2                         │Alice                    │22                       │
                ├──────────────────────────┼─────────────────────────┼─────────────────────────┤
                │3                         │Jess                     │13                       │
                └──────────────────────────┴─────────────────────────┴─────────────────────────┘""";
    }

}