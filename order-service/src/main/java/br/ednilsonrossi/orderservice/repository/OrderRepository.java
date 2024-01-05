package br.ednilsonrossi.orderservice.repository;

import br.ednilsonrossi.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
