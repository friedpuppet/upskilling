package ua.yelisieiev.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.yelisieiev.config.DBClientConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static ua.yelisieiev.config.ConfigParameterName.*;


class DBClientConfigTest {
    DBClientConfig config = new DBClientConfig();

    @BeforeEach
    void setUp() {
        System.clearProperty(DB_DRIVER.toString());
        System.clearProperty(DB_URL.toString());
        System.clearProperty(DB_LOGIN.toString());
        System.clearProperty(DB_PASSWORD.toString());
    }

    //TODO: can't test for env vars with bare junit, should find a solution
//    @DisplayName("On set up ENV - load all available params from ENV - show them")
//    @Test
//    void loadFromEnv() {
//        config.loadFromEnv();
//    }

    @DisplayName("On prepared system properties - load - check values")
    @Test
    void loadFromSystemProps() {
        System.setProperty(DB_URL.toString(), "myurl:this:that");
        System.setProperty(DB_LOGIN.toString(), "john");

        config.loadFromSystemProperties();
        assertEquals("myurl:this:that", config.getParam(DB_URL));
        assertEquals("john", config.getParam(DB_LOGIN));
    }

    @DisplayName("On prepared properties file - load - check values")
    @Test
    void loadFromApplicationProperties() throws IOException {
        try {
            createPropertiesFile();
            config.loadFromApplicationProperties();
            assertEquals("myurl:this:that", config.getParam(DB_URL));
            assertEquals("john", config.getParam(DB_LOGIN));
        } finally {
            deletePropertiesFile();
        }
    }

    @DisplayName("Load from properties file, ENV, system properties - get the correct mix")
    @Test
    void test_loadFromMixedSources() throws IOException {
        createPropertiesFile();
        System.setProperty(DB_LOGIN.toString(), "root");
        System.setProperty(DB_PASSWORD.toString(), "GOD");
        config.load();
        assertEquals("myurl:this:that", config.getParam(DB_URL));
        assertEquals("root", config.getParam(DB_LOGIN));
        assertEquals("GOD", config.getParam(DB_PASSWORD));
    }

    @DisplayName("On loaded config - get param value")
    @Test
    void getParam() throws IOException {
        createPropertiesFile();
        System.setProperty(DB_PASSWORD.toString(), "GOD");
        config.load();
        String dbUrl = config.getParam(DB_LOGIN);
        assertEquals("john", dbUrl);
    }

    @DisplayName("On empty config - set param - get same param value")
    @Test
    void test_onEmptyConfig_setParam_getParam() {
        config.setParam(DB_LOGIN, "root");
        String dbUrl = config.getParam(DB_LOGIN);
        assertEquals("root", dbUrl);
    }

    @DisplayName("On empty config - get a param - get empty param value")
    @Test
    void test_onEmptyConfig_getParam_checkIsEmpty() {
        String dbUrl = config.getParam(DB_LOGIN);
        assertNull(dbUrl);
    }

    @DisplayName("On partial config - throw Exception")
    @Test
    void test_partialConfig_throwException() throws IOException {
        createPropertiesFile();
        assertThrows(RuntimeException.class, config::load);
    }

    private void deletePropertiesFile() {
        File file = new File("application.properties");
        @SuppressWarnings("unused")
        boolean deleteResult = file.delete();
    }

    private void createPropertiesFile() throws IOException {
        deletePropertiesFile();
        Properties properties = new Properties();
        properties.setProperty(DB_DRIVER.toString(), "org.h2.Driver");
        properties.setProperty(DB_URL.toString(), "myurl:this:that");
        properties.setProperty(DB_LOGIN.toString(), "john");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("application.properties"))) {
            properties.store(writer, "Default parameters");
        }
    }
}