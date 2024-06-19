package com.gpb.service;

import com.gpb.entity.AccountRequest;
import com.gpb.entity.AccountResponse;
import com.gpb.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
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

    public Response getCurrentBalance(long chatId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            Map<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id", chatId);

            ResponseEntity<List<AccountResponse>> responseEntity = restTemplate.exchange(
                    createAccountUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<AccountResponse>>() {
                    },
                    uriVariables
            );
            return handleAccountListResponse(chatId, responseEntity);

        } catch (HttpClientErrorException e) {
            log.error("HTTP error during balance retrieval: {}", e.getResponseBodyAsString(), e);
            return new Response("Balance retrieval failed due to HTTP error: " + e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred during balance retrieval", e);
            return new Response("An error occurred during balance retrieval: " + e.getMessage());
        }
    }

    private static Response handleResponse(ResponseEntity<Response> responseEntity) {
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            return new Response("Account successfully created");
        }
        return new Response("Account creation failed with status: " + responseEntity.getStatusCode());
    }

    private static Response handleAccountListResponse(long chatId, ResponseEntity<List<AccountResponse>> responseEntity) {
        List<AccountResponse> accounts = responseEntity.getBody();
        if (accounts == null || accounts.isEmpty()) {
            return new Response("No accounts found for chat ID: " + chatId);
        }

        StringBuilder message = new StringBuilder("Your accounts:\n");
        for (AccountResponse account : accounts) {
            message.append("Account ID: ").append(account.getAccountId())
                    .append(", Name: ").append(account.getAccountName())
                    .append(", Balance: ").append(account.getAmount()).append("\n");
        }

        return new Response(message.toString());
    }
}
