package ch.wisv.payments.rest.repository;

import ch.wisv.payments.model.Product;
import ch.wisv.payments.model.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductGroupRepository extends JpaRepository<ProductGroup, Integer> {

    Optional<ProductGroup> findOneByProductsKey(String key);

}
