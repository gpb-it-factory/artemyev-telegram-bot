package com.gpb.service;

import com.gpb.entity.AccountRequest;
import com.gpb.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AccountService {
    private final RestTemplate restTemplate;
    private final String createAccountUrl;

    public AccountService(RestTemplate restTemplate, @Value("${account.create-url}") String createAccountUrl) {
        this.restTemplate = restTemplate;
        this.createAccountUrl = createAccountUrl;
    }

    public Response createAccount(long chatId, String accountName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            AccountRequest accountRequest = new AccountRequest(chatId, accountName);

            HttpEntity<AccountRequest> entity = new HttpEntity<>(accountRequest, headers);

            Map<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id", chatId);

            ResponseEntity<Response> responseEntity = restTemplate.postForEntity(
                    createAccountUrl,
                    entity,
                    Response.class,
                    uriVariables
            );

            return handleResponse(responseEntity);

        } catch (HttpClientErrorException e) {
            log.error("HTTP error during account creation: {}", e.getResponseBodyAsString(), e);
            return new Response("Account creation failed due to HTTP error: " + e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred during account creation", e);
            return new Response("An error occurred during account creation: " + e.getMessage());
        }
    }

    private static Response handleResponse(ResponseEntity<Response> responseEntity) {
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            return new Response("Account successfully created");
        }
        return new Response("Account creation failed with status: " + responseEntity.getStatusCode());
    }
}
