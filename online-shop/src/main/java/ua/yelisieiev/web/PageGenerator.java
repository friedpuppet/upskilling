package ua.yelisieiev.web;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ua.yelisieiev.entity.Product;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageGenerator {

    public static void generateProductsPage(List<Product> products, Writer writer) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(PageGenerator.class, "/templates");
        configuration.clearTemplateCache();
        Template template = configuration.getTemplate("/products.html");
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("products", products);
        template.process(pageData, writer);
    }

    public static void generateProductEditPage(Product product, Writer writer) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(PageGenerator.class, "/templates");
        configuration.clearTemplateCache();
        Template template = configuration.getTemplate("/edit_product.html");
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("product", product);
        template.process(pageData, writer);
    }

    public static void generateNewProductPage(Writer writer) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(PageGenerator.class, "/templates");
        configuration.clearTemplateCache();
        Template template = configuration.getTemplate("/new_product.html");
        Map<String, Object> pageData = new HashMap<>();
//        pageData.put("product", product);
        template.process(pageData, writer);
    }
}
