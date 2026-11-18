package dev.backend.manager.controller;

import dev.backend.manager.client.BadRequestException;
import dev.backend.manager.client.ProductsRestClient;
import dev.backend.manager.entity.Product;
import dev.backend.manager.controller.payload.NewProductPayload;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {
    private final ProductsRestClient productsRestClient;

    @GetMapping("list")
    public String getProductsList(
            @RequestParam(name = "filter", required = false) String filter,
            Model model
    ) {
//        System.out.println("filter: " + filter);
        model.addAttribute("products", productsRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
//        System.out.println(model.getAttribute("filter"));
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String getNewProductPage() {
        return "catalogue/products/create";
    }

//    @PostMapping("/create")
//    public String createProduct(@Valid NewProductPayload payload, BindingResult bindingResult, Model model) {
//        if(bindingResult.hasErrors()) {
//            model.addAttribute("payload", payload);
//            model.addAttribute("errors", bindingResult.getFieldErrors()
//                    .stream()
//                    .collect(Collectors.toMap(
//                            FieldError::getField,
//                            fieldError ->
//                                    List.of(fieldError.getDefaultMessage(), fieldError.getRejectedValue()))
//                    )
//            );
//            return "/catalogue/products/create";
//        }
//
//        Product product = productsRestClient.createProduct(payload.title(), payload.details());
//        return "redirect:/catalogue/products/%d".formatted(product.id());
//    }

    @PostMapping("/create")
    public String createProduct(NewProductPayload payload,  Model model, HttpServletResponse response) {
        try {
            Product product = productsRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", e.getErrors());
//            return "/catalogue/products/create";
            return "catalogue/products/create";
        }
    }
}