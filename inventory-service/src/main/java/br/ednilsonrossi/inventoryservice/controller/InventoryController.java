package br.ednilsonrossi.inventoryservice.controller;

import br.ednilsonrossi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/{sku_code}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isInStock(@PathVariable("sku_code") String skuCode){
        return service.isInStock(skuCode);
    }

}
