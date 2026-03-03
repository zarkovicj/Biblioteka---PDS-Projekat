package org.example.loansservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.loansservice.client.ReaderClient;
import org.example.loansservice.dto.ReaderDTO;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ReaderServiceClient {

    private final ReaderClient readerClient;

    @CircuitBreaker(name = "readerService", fallbackMethod = "getReaderFallback")
    @Retry(name = "readerService")
    public ReaderDTO getReaderById(Long readerId) {
        log.info("Feign call: GET /api/readers/{}", readerId);

        ReaderDTO reader = readerClient.getReaderById(readerId);

        log.info("Feign response: reader {} found", readerId);

        return reader;
    }

    public ReaderDTO getReaderFallback(Long readerId, Exception e) {
        log.error("Fallback triggered for readerId: {}, reason: {}", readerId, e.getMessage());

        if (e instanceof FeignException.NotFound) {
            log.warn("Reader {} not found (404)", readerId);
            throw new IllegalArgumentException("Reader with ID " + readerId + " not found");
        }

        log.error("Circuit Breaker OPEN - Readers service unavailable");
        throw new IllegalArgumentException("Readers service currently unavailable.");
    }
}
