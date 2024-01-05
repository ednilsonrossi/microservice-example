package br.ednilsonrossi.orderservice.service;

import br.ednilsonrossi.orderservice.dto.OrderItemDto;
import br.ednilsonrossi.orderservice.dto.OrderRequestDto;
import br.ednilsonrossi.orderservice.model.Order;
import br.ednilsonrossi.orderservice.model.OrderItem;
import br.ednilsonrossi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public void placeOrder(OrderRequestDto request){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        order.setOrderItemList(new ArrayList<>());
        for(OrderItemDto dto : request.getOrderItemDtoList()){
            order.getOrderItemList().add(new OrderItem(
                    dto.getId(),
                    dto.getSkuCode(),
                    dto.getPrice(),
                    dto.getQuantity()
            ));
        }

        repository.save(order);
    }

}
