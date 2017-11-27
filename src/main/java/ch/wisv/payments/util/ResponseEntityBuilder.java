package ch.wisv.payments.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class ResponseEntityBuilder {

    public static ResponseEntity<?> createResponseEntity(HttpStatus httpStatus, String message, Object content) {
        LinkedHashMap<String, Object> responseBody = new LinkedHashMap<>();

        responseBody.put("status", httpStatus.toString());
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("message", message);

        if (content != null) {
            responseBody.put("content", content);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        return new ResponseEntity<>(responseBody, httpHeaders, httpStatus);
    }
}
