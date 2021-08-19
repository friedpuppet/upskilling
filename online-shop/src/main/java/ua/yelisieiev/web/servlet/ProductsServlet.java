package ua.yelisieiev.web.servlet;

import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import ua.yelisieiev.entity.Product;
import ua.yelisieiev.service.ProductServiceException;
import ua.yelisieiev.service.ProductsService;
import ua.yelisieiev.service.ServiceLocator;
import ua.yelisieiev.web.PageWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class ProductsServlet extends HttpServlet {
    private final ProductsService productsService;
    private final PageWriter pageWriter = new PageWriter();

    public ProductsServlet() {
        productsService = ServiceLocator.getService(ProductsService.class);
    }

    public ProductsServlet(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer writer = resp.getWriter();

        try {
            String searchExpression = req.getParameter("search");
            List<Product> products;
            if (searchExpression == null) {
                products = productsService.getAll();
            } else {
                products = productsService.getAllFiltered(searchExpression);
            }
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("products", products);
            pageWriter.writePage("/products.html", pageData, writer);
        } catch (TemplateNotFoundException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Template not found: " + e.getMessage());
        } catch (TemplateException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Can't apply a template: " + e.getMessage());
        } catch (ProductServiceException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
