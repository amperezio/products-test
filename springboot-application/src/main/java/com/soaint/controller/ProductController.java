package com.soaint.controller;

import com.soaint.entity.Product;
import com.soaint.repository.ProductRepository;
import org.springframework.hateoas.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;


@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/product")
    public Product saveProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") String productId) {

        Product emp = productRepository.getEmployeeById(productId);
        //Genero link para agregar al response
        final Link productLink = linkTo(methodOn(ProductController.class).getProduct(productId)).withSelfRel();
        emp.add(productLink);
        return emp;

    }

    @DeleteMapping("/product/{id}")
    public String deleteProduct(@PathVariable("id") String productId) {
        return  productRepository.delete(productId);
    }

    @PutMapping("/product/{id}")
    public String updateProduct(@PathVariable("id") String productId, @RequestBody Product product) {
        return productRepository.update(productId,product);
    }

    //Implementando Oauth, pero no alcance :(
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

}
