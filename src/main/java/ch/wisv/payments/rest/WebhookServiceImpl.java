package ch.wisv.payments.rest;

import ch.wisv.payments.model.Order;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

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

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(order.getWebhookUrl());

            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("message", "OrderStatus update!"));
            params.add(new BasicNameValuePair("publicReference", order.getPublicReference()));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            httpclient.execute(httpPost);
            log.info("Webhook " + order.getWebhookUrl() + " called for Order " + order.getPublicReference());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
