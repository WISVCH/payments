package ch.wisv.payments.rest;

import ch.wisv.payments.exception.EmptyOrderException;
import ch.wisv.payments.exception.ProductLimitExceededException;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderRequest;
import ch.wisv.payments.model.Product;
import ch.wisv.payments.rest.repository.OrderRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private String defaultReturnUrl;

    @Autowired
    public OrderServiceImpl(ProductRepository productRepository,
                            OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;

        //TODO move to properties
        this.defaultReturnUrl = "https://ch.tudelft.nl/payments/complete";
    }

    @Override
    public Order createOrderFromRequest(OrderRequest request) throws RuntimeException {
        List<Product> products = request.getProductKeys().stream()
                .map(key -> productRepository.findOneByKey(key))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new EmptyOrderException("No (valid) products in this order");
        }

        // Throw exceptions if product limits are exceeded
        validateProductLimits(products);

        Order order = new Order(products, request.getName(), request.getEmail());

        if (request.getReturnUrl() != null) {
            order.setReturnURL(request.getReturnUrl());
        } else {
            // Get the return URL for any of the products, or get the application default
            String returnUrl = products.stream()
                    .filter(p -> p.getReturnUrl() != null)
                    .map(Product::getReturnUrl).findAny()
                    .orElse(defaultReturnUrl);

            order.setReturnURL(returnUrl);
        }

        return orderRepository.save(order);
    }

    private void validateProductLimits(List<Product> products) {
        Map<Product, Integer> orderMap = new HashMap<>();

        for (Product product : products) {
            if (orderMap.containsKey(product)) {
                Integer i = orderMap.get(product);
                orderMap.put(product, i + 1);
            } else {
                orderMap.put(product, 1);
            }

            List<Order> ordersWithProduct = orderRepository.findAllByProductsId(product.getId());

            long productsSold = ordersWithProduct.stream().flatMap(order -> order.getProducts().stream()).count();

            if (product.getAvailableProducts() >= productsSold + orderMap.get(product) || product.getAvailableProducts() == 0) {
                if (product.getLimitPerOrder() < orderMap.get(product) && product.getLimitPerOrder() != 0) {
                    throw new ProductLimitExceededException("Product limit of " + product.getName() + " per order exceeded");
                }
            } else {
                throw new ProductLimitExceededException("Can't order more tickets of " + product.getName());
            }
        }
    }

    @Override
    public Order getOrderByPublicReference(String publicReference) {
        return orderRepository.findByPublicReference(publicReference).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order getOrderByProviderReference(String providerReference) {
        return orderRepository.findByProviderReference(providerReference).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByProductId(int productId) {
        return orderRepository.findAllByProductsId(productId);
    }

    private class OrderNotFoundException extends RuntimeException {
    }

    private class InvalidOrderException extends RuntimeException {
        InvalidOrderException(String s) {
        }
    }
}