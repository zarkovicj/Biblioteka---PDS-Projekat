package org.example.loansservice.client;

import org.example.loansservice.dto.ReaderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "readers-service")
public interface ReaderClient {

    @GetMapping("/api/readers/{id}")
    ReaderDTO getReaderById(@PathVariable("id") Long id);
}
