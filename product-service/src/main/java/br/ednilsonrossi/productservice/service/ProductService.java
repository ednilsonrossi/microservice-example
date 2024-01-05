package br.ednilsonrossi.productservice.service;

import br.ednilsonrossi.productservice.dto.ProductRequestDto;
import br.ednilsonrossi.productservice.dto.ProductResponseDto;
import br.ednilsonrossi.productservice.model.Product;
import br.ednilsonrossi.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    public void createProduct(ProductRequestDto requestDto){
        Product product = Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .build();

        repository.save(product);
        log.info("Product {} is save", product.getId());
    }

    public List<ProductResponseDto> getAllProducts(){
        List<Product> products = repository.findAll();

        List<ProductResponseDto> response = new ArrayList<>();
        for (Product product : products){
            response.add(ProductResponseDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .build());
        }
        return response;
    }

}
