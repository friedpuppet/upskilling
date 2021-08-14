package ua.yelisieiev;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.postgresql.ds.PGSimpleDataSource;
import ua.yelisieiev.persistence.jdbc.JdbcProductPersistence;
import ua.yelisieiev.persistence.ProductPersistence;
import ua.yelisieiev.service.ProductsService;
import ua.yelisieiev.service.SecurityService;
import ua.yelisieiev.web.filters.SecurityFilter;
import ua.yelisieiev.web.servlet.LoginServlet;
import ua.yelisieiev.web.servlet.ProductServlet;
import ua.yelisieiev.web.servlet.ProductsServlet;
import ua.yelisieiev.web.servlet.DefaultServlet;

import java.util.EnumSet;

public class Starter {
    public static void main(String[] args) throws Exception {
//        Properties properties = new Properties();
//        properties.setProperty("DB_URL", "jdbc:h2:./online-shop/devdb");
//        properties.setProperty("DB_LOGIN", "root");
//        properties.setProperty("DB_PASSWORD", "GOD");

        // todo read this from a file
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String[] bdServers = {"instances.spawn.cc"};
        dataSource.setServerNames(bdServers);
        int[] bdPorts = {32090};
        dataSource.setPortNumbers(bdPorts);
        dataSource.setDatabaseName("foobardb");
        dataSource.setUser("spawn_admin_uBsj");
        dataSource.setPassword("4AXKnwjUxnEzFuMa");

        ProductPersistence productPersistence = new JdbcProductPersistence(dataSource);
        ProductsService productsService = new ProductsService(productPersistence);
        SecurityService securityService = new SecurityService(dataSource);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new DefaultServlet()), "");
        context.addServlet(new ServletHolder(new ProductsServlet(productsService)), "/products");
        context.addServlet(new ServletHolder(new ProductServlet(productsService)), "/product");
        context.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        context.addFilter(new FilterHolder(new SecurityFilter(securityService)), "/product", EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(9090);
        server.setHandler(context);
        server.start();
    }
}
