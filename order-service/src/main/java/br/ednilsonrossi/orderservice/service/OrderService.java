package br.ednilsonrossi.orderservice.service;

import br.ednilsonrossi.orderservice.dto.OrderItemDto;
import br.ednilsonrossi.orderservice.dto.OrderRequestDto;
import br.ednilsonrossi.orderservice.model.Order;
import br.ednilsonrossi.orderservice.model.OrderItem;
import br.ednilsonrossi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final WebClient webClient;

    public void placeOrder(OrderRequestDto request) {
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
        Boolean inStock = webClient.get()
                .uri("http://localhost:8003/api/inventory/" + request.getOrderItemDtoList().get(0).getSkuCode())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(inStock){
            repository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock.");
        }

    }

}
