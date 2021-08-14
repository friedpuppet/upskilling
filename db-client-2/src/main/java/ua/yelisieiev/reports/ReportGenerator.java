package ua.yelisieiev.reports;

import ua.yelisieiev.exceptions.SQLFetchException;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;

public interface ReportGenerator {
    void generate(ResultSet resultSet, Writer writer) throws IOException, SQLFetchException;
}
