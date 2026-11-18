package dev.backend.manager.controller;

import dev.backend.manager.client.BadRequestException;
import dev.backend.manager.client.ProductsRestClient;
import dev.backend.manager.entity.Product;
import dev.backend.manager.controller.payload.UpdateProductPayload;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
    private final ProductsRestClient productsRestClient;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") Integer id) {
        return productsRestClient.findProduct(id)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping()
    public String getProduct() {
//    public String getProduct(@PathVariable Integer id, Model model) { // TODO: написать в obsidian что такое не работает из-за отсутствия parameters в pom.xml
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(
            @ModelAttribute(value = "product", binding = false) Product product,
            UpdateProductPayload payload,
            Model model,
            HttpServletResponse response
    ) {
        try {
            productsRestClient.updateProduct(product.id(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch(BadRequestException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", e.getErrors());
//            return "redirect:/catalogue/products/%d/edit".formatted(product.getId());
            return "catalogue/products/edit";
        }

    }

    @PostMapping("delete")
//    public String deleteProduct(@PathVariable("productId") Integer id) {
    public String deleteProduct(@ModelAttribute("product") Product product) {
        productsRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(
            NoSuchElementException exception,
            Model model,
            HttpServletResponse response,
            Locale locale
    ) {
        System.out.println("locale: " + locale);
        System.out.println("locale: " + Locale.GERMANY);
        System.out.println("exception.getMessage(): " + exception.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", messageSource.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale));
        return "errors/404";
    }
}
