package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class WebhookServiceImpl implements WebhookService {

    /**
     * Notify ... of a OrderStatus change.
     *
     * @param order of type Order
     */
    @Override
    public void sendOrderStatusChangeNotification(Order order) {
        // Skip if no webhook url is set.
        if (order.getWebhookUrl() == null) {
            return;
        }

        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = new JSONObject();
        jsonObject.appendField("message", "OrderStatus update!");
        jsonObject.appendField("publicReference", order.getPublicReference());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toJSONString(), headers);
        try {
            restTemplate.postForObject(order.getWebhookUrl(), entity, String.class);
            log.info("Webhook call " + order.getWebhookUrl() + " for Order " + order.getPublicReference());
        } catch (RestClientException e) {
            log.error("Failed! Webhook call " + order.getWebhookUrl() + " for Order " + order.getPublicReference() + " due to: " + e.getMessage());
        }
    }
}
