package ch.wisv.payments.admin;

import ch.wisv.payments.rest.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private OrderService orderService;

    @Autowired
    public DashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/")
    String index(Model model) {
        model.addAttribute("orders", orderService.getAllOrders().stream().limit(10).collect(Collectors.toList()));
        return "index";
    }

    @RequestMapping("/orders")
    String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());

        return "orders";
    }

    @GetMapping("/login")
    String login(Model model) {
        return "login";
    }
}
