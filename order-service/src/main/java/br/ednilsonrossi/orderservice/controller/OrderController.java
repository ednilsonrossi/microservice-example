package br.ednilsonrossi.orderservice.controller;

import br.ednilsonrossi.orderservice.dto.OrderRequestDto;
import br.ednilsonrossi.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Transactional
public class OrderController {

    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> handleOrder(@RequestBody OrderRequestDto requestDto){
        return CompletableFuture.supplyAsync(() -> service.placeOrder(requestDto));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequestDto requestDto, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong.");
    }
}
