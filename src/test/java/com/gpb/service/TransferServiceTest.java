package com.gpb.service;

import com.gpb.entity.TransferRequest;
import com.gpb.entity.TransferResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Value("${transfer.url}")
    private String transferUrl;

    @InjectMocks
    private TransferService transferService;
    private long chatId;
    private String recipientName;
    private String amount;

    @BeforeEach
    public void setUp() {
        transferService = new TransferService(restTemplate, transferUrl);
        chatId = 12345L;
        recipientName = "Иван";
        amount = "1250.40";
    }

    @Test
    @DisplayName("Test transfer when transfer is successful then return transfer response")
    public void testTransferWhenTransferIsSuccessfulThenReturnTransferResponse() {
        TransferRequest transferRequest = new TransferRequest(chatId, recipientName, amount);
        TransferResponse expectedResponse = new TransferResponse("Transfer successful");

        mockRestTemplateExchange(transferRequest, expectedResponse, HttpStatus.OK);

        TransferResponse actualResponse = transferService.transfer(chatId, recipientName, amount);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Test transfer when transfer is not successful then return transfer response")
    public void testTransferWhenTransferIsNotSuccessfulThenReturnTransferResponse() {
        TransferRequest transferRequest = new TransferRequest(chatId, recipientName, amount);
        TransferResponse expectedResponse = new TransferResponse("Transfer failed with status: 500 INTERNAL_SERVER_ERROR");

        mockRestTemplateExchange(transferRequest, expectedResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        TransferResponse actualResponse = transferService.transfer(chatId, recipientName, amount);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Test transfer when transfer fails due to HTTP error then return transfer response with error")
    public void testTransferWhenTransferFailsDueToHttpErrorThenReturnTransferResponseWithError() {
        TransferRequest transferRequest = new TransferRequest(chatId, recipientName, amount);
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");

        mockRestTemplateExchangeWithException(transferRequest, httpClientErrorException);

        TransferResponse actualResponse = transferService.transfer(chatId, recipientName, amount);

        assertEquals("Account creation failed due to HTTP error: 400 Bad Request", actualResponse.getMessage());
    }

    @Test
    @DisplayName("Test transfer when generalexception occurs")
    public void testTransferWhenExceptionOccursThenReturnErrorMessage() {
        TransferRequest transferRequest = new TransferRequest(chatId, recipientName, amount);
        RuntimeException runtimeException = new RuntimeException("Unknown error");

        mockRestTemplateExchangeWithException(transferRequest, runtimeException);

        TransferResponse actualResponse = transferService.transfer(chatId, recipientName, amount);

        assertEquals("An error occurred during account creation: Unknown error", actualResponse.getMessage());
    }

    private void mockRestTemplateExchange(TransferRequest transferRequest, TransferResponse expectedResponse, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransferRequest> entity = new HttpEntity<>(transferRequest, headers);

        ResponseEntity<TransferResponse> responseEntity = new ResponseEntity<>(expectedResponse, status);

        when(restTemplate.exchange(
                eq(transferUrl),
                eq(HttpMethod.POST),
                eq(entity),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);
    }

    private void mockRestTemplateExchangeWithException(TransferRequest transferRequest, Exception exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransferRequest> entity = new HttpEntity<>(transferRequest, headers);

        when(restTemplate.exchange(
                eq(transferUrl),
                eq(HttpMethod.POST),
                eq(entity),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);
    }
}