package br.ednilsonrossi.orderservice.service;

import br.ednilsonrossi.orderservice.dto.InventoryResponseDto;
import br.ednilsonrossi.orderservice.dto.OrderItemDto;
import br.ednilsonrossi.orderservice.dto.OrderPlacedEventDto;
import br.ednilsonrossi.orderservice.dto.OrderRequestDto;
import br.ednilsonrossi.orderservice.model.Order;
import br.ednilsonrossi.orderservice.model.OrderItem;
import br.ednilsonrossi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEventDto> kafkaTemplate;

    public String placeOrder(OrderRequestDto request) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        order.setOrderItemList(new ArrayList<>());
        for (OrderItemDto dto : request.getOrderItemDtoList()) {
            order.getOrderItemList().add(new OrderItem(
                    dto.getId(),
                    dto.getSkuCode(),
                    dto.getPrice(),
                    dto.getQuantity()
            ));
        }

        /*
         * Neste ponto verifica-se se um item do pedido está disponível em estoque, contudo o
         * pedido possui N itens, e seria necessário realizar um loop para cada produto existente
         * no pedido.
         * O problema dessa abordagem é ser uma comunicação sincrona e demandar muito tempo
         * para finalizar uma lista, visto que são diversas requisições.
         * Uma alternativa viável é formatar invetory-service para tratar uma lista de skuCode
         * ao invés de apenas um.
         */
        /*
        Boolean inStock = webClientBuilder.build().get()
                .uri("http://localhost:8003/api/inventory/" + request.getOrderItemDtoList().get(0).getSkuCode())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(inStock){
            repository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock.");
        }
         */

        /*
         * Aqui tratando uma lista de itens existentes no pedido.
         */

        List<String> skuCodes = order.getOrderItemList().stream()
                .map(orderItem -> orderItem.getSkuCode())
                .toList();

        InventoryResponseDto[] inventoryResponseDtoArray = webClientBuilder.build().get()
                .uri("http://inventory-ms/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodeList", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponseDto[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseDtoArray)
                .allMatch(inventoryResponseDto -> inventoryResponseDto.isInStock());

        if(allProductsInStock){
            repository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEventDto(order.getOrderNumber()));
            return "Order placed successfully";
        }else{
            throw new IllegalArgumentException("Product is not in stock.");
        }
    }

}
