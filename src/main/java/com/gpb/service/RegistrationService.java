package com.gpb.service;

import com.gpb.entity.Request;
import com.gpb.entity.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {
    private final RestTemplate restTemplate;
    private static final String REGISTRATION_URL = "http://localhost:8080/api/users";

    public Response registerUser(long chatId) {
        Request request = new Request(chatId);
        return getResponseEntity(request);
    }

    private Response getResponseEntity(Request request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Request> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    REGISTRATION_URL,
                    HttpMethod.POST,
                    entity,
                    Response.class
            );

            return handleResponse(responseEntity);

        } catch (HttpClientErrorException e) {
            log.error("HTTP error during registration: {}", e.getResponseBodyAsString(), e);
            return new Response("Registration failed due to HTTP error: " + e.getStatusCode());
        } catch (Exception e) {
            log.error("An error occurred during registration", e);
            return new Response("An error occurred during registration: " + e.getMessage());
        }
    }

    private static Response handleResponse(ResponseEntity<Response> responseEntity) {
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        Response responseBody = responseEntity.getBody();

        if (responseBody == null || responseBody.getMessage() == null) {
            return new Response("Registration failed with status: " + statusCode);
        }
        return responseBody;
    }

}
