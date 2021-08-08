package ua.yelisieiev;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ua.yelisieiev.persistence.jdbc.JdbcConnector;
import ua.yelisieiev.persistence.jdbc.JdbcPersistence;
import ua.yelisieiev.persistence.Persistence;
import ua.yelisieiev.service.ProductsService;
import ua.yelisieiev.web.servlet.ProductServlet;
import ua.yelisieiev.web.servlet.ProductsServlet;

import java.util.Properties;

public class Starter {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("DB_URL", "jdbc:h2:./online-shop/devdb");
        properties.setProperty("DB_LOGIN", "root");
        properties.setProperty("DB_PASSWORD", "GOD");

        JdbcConnector jdbcConnector = new JdbcConnector(properties);
        Persistence persistence = new JdbcPersistence(jdbcConnector);
        ProductsService productsService = new ProductsService(persistence);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ProductsServlet(productsService)), "/products");
        context.addServlet(new ServletHolder(new ProductServlet(productsService)), "/product");

        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
    }
}
