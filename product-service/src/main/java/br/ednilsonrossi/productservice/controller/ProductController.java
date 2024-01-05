package br.ednilsonrossi.productservice.controller;

import br.ednilsonrossi.productservice.dto.ProductRequestDto;
import br.ednilsonrossi.productservice.dto.ProductResponseDto;
import br.ednilsonrossi.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequestDto productRequest){
        service.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts(){
        return service.getAllProducts();
    }

}
