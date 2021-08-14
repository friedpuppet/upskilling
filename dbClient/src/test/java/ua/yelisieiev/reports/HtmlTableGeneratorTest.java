package ua.yelisieiev.reports;


class HtmlTableGeneratorTest extends AbstractGeneratorTest{

    @Override
    ReportGenerator getGenerator() {
        return new HtmlTableGenerator();
    }

    @Override
    String getExpectedTable() {
        return "<table>" +
                "<tr><th>First</th><th>Second</th><th>Third</th></tr>" +
                "<tr><td>1</td><td>Bob</td><td>12</td></tr>" +
                "<tr><td>2</td><td>Alice</td><td>22</td></tr>" +
                "<tr><td>3</td><td>Jess</td><td>13</td></tr>" +
                "</table>";
    }
}