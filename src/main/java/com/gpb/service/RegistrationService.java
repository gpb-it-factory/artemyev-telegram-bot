package com.gpb.service;

import com.gpb.entity.Request;
import com.gpb.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RegistrationService {
    private final RestTemplate restTemplate;
    private final String registrationUrl;

    public RegistrationService(RestTemplate restTemplate, @Value("${registration.url}") String registrationUrl) {
        this.restTemplate = restTemplate;
        this.registrationUrl = registrationUrl;
    }

    public Response registerUser(long chatId, String userName) {
        Request request = new Request(chatId, userName);
        return getResponseEntity(request);
    }

    private Response getResponseEntity(Request request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Request> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    registrationUrl,
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
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            return new Response("Registration successful");
        }
        return new Response("Registration failed with status " + responseEntity.getStatusCode());
    }
}
