package ua.yelisieiev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.yelisieiev.config.DBClientConfig;

import java.io.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static ua.yelisieiev.config.ConfigParameterName.*;

/**
 * read input from console and send to database
 *
 * java -jar -Durl=... -Duser=root db-client.jar
 * Запускаем приложение.
 * При запуске указывает настройки подключения к базе через переменные окружения.
 * Если переменные не указаны, смотрим настройки в файле application.properties -> with all db configuration
 *
 * Принимает на вход команды DDL, DML. Выполняет их на подключенной базе данных.
 *
 * // ?
 * CREATE DATABASE users;
 * USE users;
 * CREATE TABLE ...
 * //
 *
 * INSERT
 * SELECT
 * UPDATE
 * DELETE
 *
 *
 * На Update команды выводит количество измененных строк.
 * На Select команды
 * 1. Выводит результат (таблицу) в консоль (можно заиспользовать сторонюю библиотеку)
 * 2. Генерирует отчет о запросе в формате html и кладет его в папке /reports (или по пути указаному в конфигурации)
 *
 * <table>
 *   <tr>
 *     <th>id</th>
 *     <th>name</th>
 *   </tr>
 *   <tr>
 *     <td>1</td>
 *     <td>Tolik</td>
 *   </tr>
 *   <tr>
 *     <td>2</td>
 *     <td>Sasha</td>
 *   </tr>
 *   <tr>
 *     <td>3</td>
 *     <td>Andrey</td>
 *   </tr>
 * </table>
 * **/
public class DBClientTest {
/**
 create with error
 drop with error
 select with 20 columns
 select with lots of rows
 */

  private DBClient dbClient;

    @BeforeEach
    void create_ClientWithInMemoryDb() throws IOException {
        DBClientConfig config = new DBClientConfig();
        config.setParam(DB_DRIVER, "org.h2.Driver");
        config.setParam(DB_URL, "jdbc:h2:mem:testdb");
        config.setParam(DB_LOGIN, "root");
        config.setParam(DB_PASSWORD, "GOD");
        config.checkParameters();

        dbClient = new DBClient(config);
        dbClient.setEcho(true);
        createSchemaTwoTables();
    }

    private void createSchemaTwoTables() throws IOException {
        String sql = """
                drop all objects
                
                create table kids (id int, name varchar(50), parent_id int)
                create table parents (id int, name varchar(50), salary number)
                
                insert into parents values(1, 'John', 1000)
                insert into parents values(2, 'Jess', 2000)
                insert into parents values(3, 'Samantha', 1200)
                insert into parents values(4, 'Bill', 1800)
                insert into parents values(5, 'Bob', 1700)
                
                insert into kids values(1, 'Billy', 1)
                insert into kids values(2, 'Jenny', 2)
                insert into kids values(3, 'Mila', 2)
                insert into kids values(4, 'Scarlett', 3)
                insert into kids values(5, 'Yova', 4)
                insert into kids values(6, 'Vova', 5)
                insert into kids values(7, 'Lenin', 1)
                insert into kids values(8, 'Stalin', 2)
                
                exit
                """;

        ByteArrayInputStream mockIn = new ByteArrayInputStream(sql.getBytes());
        dbClient.setInput(mockIn);
        dbClient.setOutput(OutputStream.nullOutputStream());
        dbClient.runDialog();
        dbClient.setInput(System.in);
        dbClient.setOutput(System.out);
    }

    @DisplayName("With an inmemory db run correct SELECT")
    @Test
    void test_correctSelect() throws IOException {
        String sql = """
                select * from kids where parent_id = 2
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("Jenny"));
        assertThat(outBytes.toString(), containsString("Mila"));
        assertThat(outBytes.toString(), containsString("Stalin"));
    }

    @DisplayName("With an inmemory db run correct UPDATE")
    @Test
    void test_correctUpdate() throws IOException {
        String sql = """
                update kids set name = 'Vovan' where name='Vova'
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("Rows affected: 1"));
    }

    @DisplayName("With an inmemory db run correct DELETE")
    @Test
    void test_correctDelete() throws IOException {
        String sql = """
                delete from kids where name='Vova'
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("Rows affected: 1"));
    }

    @DisplayName("With an inmemory db run correct INSERT")
    @Test
    void test_correctInsert() throws IOException {
        String sql = """
                insert into kids values(10, 'Tonya', 1)
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("Rows affected: 1"));
    }

    @DisplayName("With an inmemory db run erroneous SELECT")
    @Test
    void test_errorSelect() throws IOException {
        String sql = """
                select * from planets
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("JdbcSQLSyntaxErrorException"));
    }

    @DisplayName("With an inmemory db run erroneous UPDATE")
    @Test
    void test_errorUpdate() throws IOException {
        String sql = """
                update kids set name = 'Vovan where name='Vova'
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("JdbcSQLSyntaxErrorException"));
    }

    @DisplayName("With an inmemory db run erroneous DELETE")
    @Test
    void test_errorDelete() throws IOException {
        String sql = """
                delete from kids wherewhere name='Vova'
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("JdbcSQLSyntaxErrorException"));
    }

    @DisplayName("With an inmemory db run erroneous INSERT")
    @Test
    void test_errorInsert() throws IOException {
        String sql = """
                insert into kids values(10, 'Tonya', 1, 0)
                exit
                """;
        dbClient.setInput(new ByteArrayInputStream(sql.getBytes()));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        dbClient.setOutput(outBytes);
        dbClient.runDialog();
        assertThat(outBytes.toString(), containsString("JdbcSQLSyntaxErrorException"));
    }
}
