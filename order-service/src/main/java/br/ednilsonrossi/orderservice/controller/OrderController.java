package br.ednilsonrossi.orderservice.controller;

import br.ednilsonrossi.orderservice.dto.OrderRequestDto;
import br.ednilsonrossi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Transactional
public class OrderController {

    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String handleOrder(@RequestBody OrderRequestDto requestDto){
        service.placeOrder(requestDto);
        return "Order placed successfully";
    }
}
