package ch.wisv.payments.rest;

import ch.wisv.payments.exception.EmptyOrderException;
import ch.wisv.payments.exception.ProductLimitExceededException;
import ch.wisv.payments.model.*;
import ch.wisv.payments.rest.repository.OrderRepository;
import ch.wisv.payments.rest.repository.ProductGroupRepository;
import ch.wisv.payments.rest.repository.ProductRepository;
import lombok.Synchronized;
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
    private ProductGroupRepository productGroupRepository;

    private String defaultReturnUrl;

    @Autowired
    public OrderServiceImpl(ProductRepository productRepository,
            OrderRepository orderRepository, ProductGroupRepository productGroupRepository
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productGroupRepository = productGroupRepository;

        //TODO move to properties
        this.defaultReturnUrl = "https://ch.tudelft.nl/payments/complete";
    }

    @Override
    @Synchronized
    public Order createOrderFromRequest(OrderRequest request) throws RuntimeException {
        List<Product> products = request.getProductKeys().stream()
                .map(key -> productRepository.findOneByKey(key))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());

        if (products.isEmpty()) {
            throw new EmptyOrderException("No (valid) products in this order");
        }

        // Throw exceptions if product limits are exceeded
        validateProductLimits(products, request.getProductKeys().size());

        Order order = new Order(products, request.getName(), request.getEmail());
        order.setMailConfirmation(request.isMailConfirmation());
        order.setMethod(MollieMethodEnum.valueOf(request.getMethod()));
        order.setWebhookUrl(request.getWebhookUrl());

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

    private void validateProductLimits(List<Product> products, int orderSize) {
        Map<Product, Integer> orderMap = new HashMap<>();

        for (Product product : products) {
            Optional<ProductGroup> productGroup = productGroupRepository.findOneByProductsKey(product.getKey());

            if (orderMap.containsKey(product)) {
                Integer i = orderMap.get(product);
                orderMap.put(product, i + 1);
            } else {
                orderMap.put(product, 1);
            }

            long productsSold = getProductsSold(product);

            // Check if the available number of products isn't exceeded
            if (product.getAvailableProducts() >= productsSold + orderMap.get(product) || product.getAvailableProducts() == 0) {
                // Check if the limit per order is exceeded
                if (product.getLimitPerOrder() < orderMap.get(product) && product.getLimitPerOrder() != 0) {
                    throw new ProductLimitExceededException("Product limit of " + product.getName() + " per order exceeded");
                }
                // Check if the limit per group isn't exceeded
                productGroup.ifPresent(group -> {
                    if (group.getGroupLimit() != 0) {
                        Long productGroupCount = group.getProducts().stream()
                                .map(this::getProductsSold)
                                // Count the number sold of all products in this group
                                .reduce((long) orderSize, (totalCount, productCount) -> totalCount + productCount);
                        if (productGroupCount > group.getGroupLimit()) {
                            throw new ProductLimitExceededException("Can't order more tickets of " + product.getName());
                        }
                    }
                });
            } else {
                throw new ProductLimitExceededException("Can't order more tickets of " + product.getName());
            }
        }
    }

    private Long getProductsSold(Product product) {
        List<Order> ordersWithProduct = orderRepository.findAllByProductsId(product.getId());

        return ordersWithProduct.stream().
                filter(o -> o.getStatus().equals(OrderStatus.WAITING) || o.getStatus().equals(OrderStatus.PAID)).
                flatMap(order -> order.getProducts().stream()).
                filter(p -> p.getId() == product.getId()).
                count();
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

    @Override
    public List<Order> getOrdersByCommittee(Committee committee) {
        return orderRepository.findAllByProductsCommittee(committee);
    }

    @Override
    public Float getTransactionCostByCommittee(Committee committee) {
        List<Order> orders = getOrdersByCommittee(committee);
        float cost = 0;
        float costPerOrder = 0.35F;

        for (Order order : orders) {
            if (!order.getProducts().stream().allMatch(product -> product.getCommittee().equals(committee))) {
                long committeeCount = order.getProducts().stream().map(Product::getCommittee).distinct().count();
                cost += costPerOrder / committeeCount;
            } else {
                cost += costPerOrder;
            }
        }
        return cost;
    }

    private class OrderNotFoundException extends RuntimeException {
    }
}