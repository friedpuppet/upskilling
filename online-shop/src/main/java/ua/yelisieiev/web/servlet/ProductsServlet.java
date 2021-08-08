package ua.yelisieiev.web.servlet;

import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import ua.yelisieiev.service.ProductServiceException;
import ua.yelisieiev.service.ProductsService;
import ua.yelisieiev.entity.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import ua.yelisieiev.web.PageGenerator;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class ProductsServlet extends HttpServlet {
    private final ProductsService productsService;

    public ProductsServlet(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer writer = resp.getWriter();

        try {
            List<Product> products = productsService.getAll();

            PageGenerator.generateProductsPage(products, writer);
        } catch (TemplateNotFoundException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Template not found: " + e.getMessage());
        } catch (TemplateException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Can't apply a template: " + e.getMessage());
        } catch (ProductServiceException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
