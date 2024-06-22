package com.gpb.service;

import com.gpb.entity.TransferRequest;
import com.gpb.entity.TransferResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class TransferService {
    private final RestTemplate restTemplate;
    private final String transferUrl;

    public TransferService(RestTemplate restTemplate, @Value("${transfer.url}") String transferUrl) {
        this.restTemplate = restTemplate;
        this.transferUrl = transferUrl;
    }

    public TransferResponse transfer(long chatId, String recepientName, String amount) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            TransferRequest transferRequest = new TransferRequest(chatId, recepientName, amount);

            HttpEntity<TransferRequest> entity = new HttpEntity<>(transferRequest, headers);

            ResponseEntity<TransferResponse> responseEntity = restTemplate.exchange(
                    transferUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<TransferResponse>() {
                    }
            );
            return handleResponse(responseEntity);

        } catch (HttpClientErrorException e) {
            log.error("HTTP error during account creation: {}", e.getResponseBodyAsString(), e);
            return new TransferResponse("Account creation failed due to HTTP error: " + e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred during account creation", e);
            return new TransferResponse("An error occurred during account creation: " + e.getMessage());
        }
    }

    private static TransferResponse handleResponse(ResponseEntity<TransferResponse> responseEntity) {
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            return responseEntity.getBody();
        }
        return new TransferResponse("Transfer failed with status: " + responseEntity.getStatusCode());
    }
}
