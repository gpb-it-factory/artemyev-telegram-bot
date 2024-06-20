package com.gpb.service;

import com.gpb.entity.AccountRequest;
import com.gpb.entity.AccountResponse;
import com.gpb.entity.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Value("${account.create-url}")
    private String createAccountUrl;

    @InjectMocks
    private AccountService accountService;
    private long chatId;
    private String accountName;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(restTemplate, createAccountUrl);
        chatId = 12345L;
        accountName = "Акционный";
    }

    @Test
    @DisplayName("Test createAccount when account creation is successful")
    public void testCreateAccountWhenAccountCreationIsSuccessfulThenReturnSuccessResponse() {
        mockPostForEntity(HttpStatus.NO_CONTENT);

        Response response = accountService.createAccount(chatId, accountName);

        assertEquals("Account successfully created", response.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when HTTP error occurs")
    public void testCreateAccountWhenHttpErrorOccursThenReturnErrorResponse() {
        mockPostForEntityWithException(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        Response response = accountService.createAccount(chatId, accountName);

        assertEquals("Account creation failed due to HTTP error: 400 Bad Request", response.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when general exception occurs")
    public void testCreateAccountWhenGeneralExceptionOccursThenReturnErrorResponse() {
        mockPostForEntityWithException(new RuntimeException("General error"));

        Response response = accountService.createAccount(chatId, accountName);

        assertEquals("An error occurred during account creation: General error", response.getMessage());
    }

    @Test
    @DisplayName("Test getCurrentBalance when balance retrieval is successful")
    public void testGetCurrentBalanceWhenBalanceRetrievalIsSuccessfulThenReturnSuccessResponse() {
        AccountResponse accountResponse = new AccountResponse(UUID.randomUUID(), "Акционный", BigDecimal.valueOf(1000));
        List<AccountResponse> accountResponseList = Collections.singletonList(accountResponse);
        ResponseEntity<List<AccountResponse>> responseEntity = new ResponseEntity<>(accountResponseList, HttpStatus.OK);

        when(restTemplate.exchange(eq(createAccountUrl), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<AccountResponse>>>any(), anyMap()))
                .thenReturn(responseEntity);

        Response response = accountService.getCurrentBalance(chatId);

        assertEquals("Your accounts:\nAccount ID: " + accountResponse.getAccountId() + ", Name: " + accountResponse.getAccountName() + ", Balance: " + accountResponse.getAmount() + "\n", response.getMessage());
    }

    @Test
    @DisplayName("Test getCurrentBalance when HTTP error occurs")
    public void testGetCurrentBalanceWhenHttpErrorOccursThenReturnErrorResponse() {
        mockExchangeWithException(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        Response response = accountService.getCurrentBalance(chatId);

        assertEquals("Balance retrieval failed due to HTTP error: 400 Bad Request", response.getMessage());
    }

    @Test
    @DisplayName("Test getCurrentBalance when general exception occurs")
    public void testGetCurrentBalanceWhenGeneralExceptionOccursThenReturnErrorResponse() {
        mockExchangeWithException(new RuntimeException("General error"));

        Response response = accountService.getCurrentBalance(chatId);

        assertEquals("An error occurred during balance retrieval: General error", response.getMessage());
    }

    private void mockPostForEntity(HttpStatus status) {
        AccountRequest accountRequest = new AccountRequest(chatId, accountName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountRequest> entity = new HttpEntity<>(accountRequest, headers);
        ResponseEntity<Response> responseEntity = new ResponseEntity<>(status);

        when(restTemplate.postForEntity(eq(createAccountUrl), eq(entity), eq(Response.class), anyMap()))
                .thenReturn(responseEntity);
    }

    private void mockPostForEntityWithException(Exception exception) {
        AccountRequest accountRequest = new AccountRequest(chatId, accountName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountRequest> entity = new HttpEntity<>(accountRequest, headers);

        when(restTemplate.postForEntity(eq(createAccountUrl), eq(entity), eq(Response.class), anyMap()))
                .thenThrow(exception);
    }

    private void mockExchangeWithException(Exception exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(eq(createAccountUrl), eq(HttpMethod.GET), eq(entity), ArgumentMatchers.<ParameterizedTypeReference<List<AccountResponse>>>any(), anyMap()))
                .thenThrow(exception);
    }
}
