package ch.wisv.payments.rest.repository;

import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findOneByKey(String key);

    Set<Product> findByCommittee(Committee committee);
}
