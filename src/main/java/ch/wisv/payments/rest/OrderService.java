package ch.wisv.payments.rest;

import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.model.OrderRequest;

import java.util.List;

public interface OrderService {

    Order createOrderFromRequest(OrderRequest request);

    Order getOrderByPublicReference(String publicReference);

    Order getOrderByProviderReference(String providerReference);

    List<Order> getAllOrders();

    List<Order> getOrdersByProductId(int productId);

    List<Order> getOrdersByCommittee(Committee committee);

    List<Order> getOrdersByCreationDateDesc();

    Float getTransactionCostByCommittee(Committee committee);
}
