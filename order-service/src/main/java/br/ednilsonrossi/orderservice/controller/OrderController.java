package br.ednilsonrossi.orderservice.controller;

import br.ednilsonrossi.orderservice.dto.OrderRequestDto;
import br.ednilsonrossi.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String handleOrder(@RequestBody OrderRequestDto requestDto){
        service.placeOrder(requestDto);
        return "Order placed successfully";
    }

    public String fallbackMethod(OrderRequestDto requestDto, RuntimeException runtimeException){
        return "Oops! Something went wrong.";
    }
}
