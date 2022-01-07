package ch.wisv.payments.admin;

import ch.wisv.payments.model.Order;
import ch.wisv.payments.rest.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private OrderService orderService;

    @Autowired
    public DashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/dashboard")
    String dashboard(Model model) {
        List<Order> orders = orderService.getOrdersByCreationDateDesc().stream().limit(10).collect(Collectors.toList());
        model.addAttribute("orders", orders);
        return "dashboard";
    }

    @GetMapping("/")
    String about() {
        return "about";
    }

    @GetMapping("/login")
    String login(Model model) {
        return "login";
    }
}
