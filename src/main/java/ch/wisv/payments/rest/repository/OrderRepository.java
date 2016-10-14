package ch.wisv.payments.rest.repository;

import ch.wisv.payments.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByProviderReference(String providerReference);

    Optional<Order> findByPublicReference(String publicReference);

    List<Order> findAllByProductsId(int id);
}
