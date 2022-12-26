package com.vyatsu.task14.controllers;

import com.vyatsu.task14.entities.Filter;
import com.vyatsu.task14.entities.Product;
import com.vyatsu.task14.entities.StrParam;
import com.vyatsu.task14.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private ProductsService productsService;
    private final StrParam filterMessage = new StrParam();
    private final Filter filter = new Filter();
    private final StrParam AddMessage = new StrParam();
    private Pattern pattern = Pattern.compile("");
    private final ArrayList<Long> Isredact = new ArrayList<>();
    private boolean showFilter = false;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public String showProductsList(Model model) {
        Product product = new Product();

        model.addAttribute("isRedact", Isredact);
        model.addAttribute("filter", filter);
        model.addAttribute("addMess", AddMessage);
        model.addAttribute("products", productsService.getFilteredProducts(pattern, filter));
        model.addAttribute("product", product);
        model.addAttribute("showFilter", showFilter);
        return "products";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute(value = "product")Product product) {
        if(productsService.getAllProducts().stream().filter(p -> p.getId() == product.getId()).collect(Collectors.toList()).size() == 0){
            productsService.add(product);
            AddMessage.setValue("");
        }
        else{
            AddMessage.setValue("Указанный id занят.");
        }
        return "redirect:/products";
    }

    @GetMapping("/show/{id}")
    public String showOneProduct(Model model, @PathVariable(value = "id") Long id) {
        Product product = productsService.getById(id);
        model.addAttribute("product", product);
        return "product-page";
    }

    @GetMapping("/drop/{id}")
    public  String dropProduct(Model model, @PathVariable(value = "id") Long id){
        Product product = productsService.getAllProducts().stream()
                .filter(p -> p.getId() == id).findFirst().orElse(null);
        if(product != null) {
            productsService.remove(product);
        }
        return "redirect:/products";
    }
    @PostMapping ("/filter")
    public String FilterList( @ModelAttribute(value = "filter") Filter f){
        pattern = Pattern.compile(f.getPattern());
        filter.setPattern(f.getPattern());
        filter.setMax(f.getMax());
        filter.setMin(f.getMin());

        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    public  String editProduct(Model model, @PathVariable(value = "id") Long id){
        Isredact.add(id);
        return "redirect:/products";
    }
    @PostMapping("/edit")
    public  String editProduct(@ModelAttribute(value = "product1") Product product){
        Product edited = productsService.getAllProducts().stream().filter(p -> p.getId() == product.getId()).findFirst().orElse(null);
        edited.setTitle(product.getTitle());
        edited.setPrice(product.getPrice());
        Isredact.remove(product.getId());
        return "redirect:/products";
    }
    @GetMapping("/filter")
    public  String showFilter(Model model){
        showFilter = !showFilter;
        return "redirect:/products";
    }
}
