package com.gpb.service;

import com.gpb.entity.AccountRequest;
import com.gpb.entity.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Value("${account.create-url}")
    private String createAccountUrl = "http://example.com/createAccount";

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(restTemplate, createAccountUrl);
    }

    @Test
    @DisplayName("Test createAccount when account creation is successful")
    public void testCreateAccountWhenAccountCreationIsSuccessfulThenReturnSuccessResponse() {

        long chatId = 12345L;
        String accountName = "My first awesome account";
        AccountRequest accountRequest = new AccountRequest(chatId, accountName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountRequest> entity = new HttpEntity<>(accountRequest, headers);
        ResponseEntity<Response> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.postForEntity(eq(createAccountUrl), eq(entity), eq(Response.class), anyMap()))
                .thenReturn(responseEntity);


        Response response = accountService.createAccount(chatId, accountName);


        assertEquals("Account successfully created", response.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when HTTP error occurs")
    public void testCreateAccountWhenHttpErrorOccursThenReturnErrorResponse() {

        long chatId = 12345L;
        String accountName = "My first awesome account";
        AccountRequest accountRequest = new AccountRequest(chatId, accountName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountRequest> entity = new HttpEntity<>(accountRequest, headers);
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");

        when(restTemplate.postForEntity(eq(createAccountUrl), eq(entity), eq(Response.class), anyMap()))
                .thenThrow(httpClientErrorException);


        Response response = accountService.createAccount(chatId, accountName);


        assertEquals("Account creation failed due to HTTP error: 400 Bad Request", response.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when general exception occurs")
    public void testCreateAccountWhenGeneralExceptionOccursThenReturnErrorResponse() {

        long chatId = 12345L;
        String accountName = "My first awesome account";
        AccountRequest accountRequest = new AccountRequest(chatId, accountName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountRequest> entity = new HttpEntity<>(accountRequest, headers);
        RuntimeException runtimeException = new RuntimeException("General error");

        when(restTemplate.postForEntity(eq(createAccountUrl), eq(entity), eq(Response.class), anyMap()))
                .thenThrow(runtimeException);


        Response response = accountService.createAccount(chatId, accountName);


        assertEquals("An error occurred during account creation: General error", response.getMessage());
    }
}
