package ch.wisv.payments.admin;

import ch.wisv.payments.model.Order;
import ch.wisv.payments.rest.OrderService;
import ch.wisv.payments.rest.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;
    private PaymentService paymentService;

    @Autowired
    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping
    String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());

        return "orders";
    }

    @PostMapping("/update/{orderReference}")
    String updateOrder(Model model, @PathVariable String orderReference, RedirectAttributes redirectAttributes) {
        try {
            Order order = paymentService.updateStatusByPublicReference(orderReference);
            redirectAttributes.addFlashAttribute("message", "Order " + order.getId() + " successfully updated!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/orders";
    }
}
