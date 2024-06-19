package com.gpb.service;

import com.gpb.entity.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RegistrationService registrationService;

    @Value("${registration.url}")
    private String registrationUrl;
    private long chatId;
    private String userName;

    @BeforeEach
    public void setUp() {
        registrationService = new RegistrationService(restTemplate, registrationUrl);
        chatId = 12345L;
        userName = "Николай";
    }

    @Test
    @DisplayName("Test registerUser when registration is successful then return success message")
    public void testRegisterUserWhenRegistrationIsSuccessfulThenReturnSuccessMessage() {
        mockRestTemplateExchange(HttpStatus.NO_CONTENT);

        Response response = registrationService.registerUser(chatId, userName);

        assertEquals("Registration successful", response.getMessage());
    }

    @Test
    @DisplayName("Test registerUser when HTTP error occurs then return error message")
    public void testRegisterUserWhenHttpErrorOccursThenReturnErrorMessage() {
        mockRestTemplateExchangeWithException(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        Response response = registrationService.registerUser(chatId, userName);

        assertEquals("Registration failed due to HTTP error: 400 BAD_REQUEST", response.getMessage());
    }

    @Test
    @DisplayName("Test registerUser when general exception occurs")
    public void testRegisterUserWhenExceptionOccursThenReturnErrorMessage() {
        mockRestTemplateExchangeWithException(new RuntimeException("Unexpected error"));

        Response response = registrationService.registerUser(chatId, userName);

        assertEquals("An error occurred during registration: Unexpected error", response.getMessage());
    }

    private void mockRestTemplateExchange(HttpStatus status) {
        ResponseEntity<Response> responseEntity = new ResponseEntity<>(status);

        when(restTemplate.exchange(
                eq(registrationUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Response.class)
        )).thenReturn(responseEntity);
    }

    private void mockRestTemplateExchangeWithException(Exception exception) {
        when(restTemplate.exchange(
                eq(registrationUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Response.class)
        )).thenThrow(exception);
    }
}