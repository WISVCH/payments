package ch.wisv.payments.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

/**
 * Copyright (c) 2016  W.I.S.V. 'Christiaan Huygens'
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
