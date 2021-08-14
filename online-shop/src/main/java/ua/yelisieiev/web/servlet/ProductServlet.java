package ua.yelisieiev.web.servlet;

import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.yelisieiev.entity.Product;
import ua.yelisieiev.service.ProductServiceException;
import ua.yelisieiev.service.ProductsService;
import ua.yelisieiev.service.SecurityService;
import ua.yelisieiev.web.PageWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class ProductServlet extends HttpServlet {
    private final ProductsService productsService;
    private final PageWriter pageWriter = new PageWriter();

    public ProductServlet(ProductsService productsService) {
        this.productsService = productsService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Product.Id productId = null;
        String stringId = req.getParameter("id");
        Writer writer = resp.getWriter();
        try {
            if (stringId == null) {
                pageWriter.writePage("/new_product.html", null, writer);
            } else {
                productId = new Product.Id(Integer.parseInt(stringId));
                Product product = productsService.get(productId);
                if (product == null) {
                    throw new ServletException("No product with id " + productId);
                }
                Map<String, Object> pageData = new HashMap<>();
                pageData.put("product", product);
                pageWriter.writePage("/edit_product.html", pageData, writer);
            }
        } catch (TemplateNotFoundException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Template not found: " + e.getMessage());
        } catch (TemplateException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Can't apply a template: " + e.getMessage());
        } catch (ProductServiceException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR,
                    "Can't retrieve product with id " + productId + ": " + e.getMessage());
        } catch (ServletException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PUT".equals(req.getParameter("_method"))) {
            doPut(req, resp);
            return;
        }
        if ("DELETE".equals(req.getParameter("_method"))) {
            doDelete(req, resp);
            return;
        }
        try {
            Product product = parseProductFromRequest(req);

            productsService.add(product);
            resp.sendRedirect("/products");
        } catch (ProductServiceException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Can't store product: " + e.getMessage());
        } catch (ServletException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product.Id productId = parseProductIdFromRequest(req);
            Product product = parseProductFromRequest(req);
            product.setId(productId);

            productsService.update(product);
            resp.sendRedirect("/products");
        } catch (ProductServiceException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Product.Id productId = parseProductIdFromRequest(req);
            Product product = productsService.get(productId);
            if (product == null) {
                throw new ServletException("No product with id: " + productId);
            }
            productsService.delete(productId);
            resp.sendRedirect("/products");
        } catch (ProductServiceException e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ServletException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        }
    }

    private Product parseProductFromRequest(HttpServletRequest req) throws ServletException {
        String name = req.getParameter("name");
        if (name == null) {
            throw new ServletException("Name is empty");
        }
        String priceString = req.getParameter("price");
        if (priceString == null) {
            throw new ServletException("Price is empty");
        }
        double price = Double.parseDouble(priceString);
        Product product = new Product(name, price);
        return product;
    }

    private Product.Id parseProductIdFromRequest(HttpServletRequest req) throws ServletException {
        String stringId = req.getParameter("id");
        if (stringId == null) {
            throw new ServletException("No id given");
        }
        return new Product.Id(Integer.parseInt(stringId));
    }
}
