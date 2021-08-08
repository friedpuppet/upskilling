package ua.yelisieiev;

import ua.yelisieiev.config.DBClientConfig;

import java.io.IOException;

import static ua.yelisieiev.config.ConfigParameterName.*;

public class Runner {
    public static void main(String[] args) throws IOException {
        DBClientConfig config = new DBClientConfig();
        config.setParam(DB_DRIVER, "org.h2.Driver");
        config.setParam(DB_URL, "jdbc:h2:./testdb");
        config.setParam(DB_LOGIN, "root");
        config.setParam(DB_PASSWORD, "GOD");
        config.checkParameters();
        DBClient dbClient = new DBClient(config);
        dbClient.runDialog();
    }
}
