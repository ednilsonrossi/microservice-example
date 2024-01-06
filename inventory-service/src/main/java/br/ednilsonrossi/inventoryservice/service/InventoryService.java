package br.ednilsonrossi.inventoryservice.service;

import br.ednilsonrossi.inventoryservice.dto.InventoryResponseDto;
import br.ednilsonrossi.inventoryservice.model.Inventory;
import br.ednilsonrossi.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        Optional<Inventory> optional = repository.findBySkuCode(skuCode);
        if(optional.isPresent()){
            var inventory = optional.get();
            return inventory.getQuantity() > 0;
        }
        throw new IllegalArgumentException("The sku-code not exists.");
    }

    @Transactional(readOnly = true)
    public List<InventoryResponseDto> isAllInStock(List<String> skuCodeList) {
        List<InventoryResponseDto> response = new ArrayList<>();
        for(Inventory inventory : repository.findBySkuCodeIn(skuCodeList)){
            response.add(InventoryResponseDto.builder()
                    .skuCode(inventory.getSkuCode())
                    .inStock(inventory.getQuantity() > 0)
                    .quantity(inventory.getQuantity())
                    .build());
        }
        return response;
    }
}
