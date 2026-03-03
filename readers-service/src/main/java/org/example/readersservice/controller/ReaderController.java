package org.example.readersservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.readersservice.model.Reader;
import org.example.readersservice.repository.ReaderRepository;
import org.example.readersservice.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/readers")
@Slf4j
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;

    @PostMapping
    public ResponseEntity<?> createReader(@Valid @RequestBody Reader reader) {
        try {
            Reader createdReader = readerService.createReader(reader);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdReader);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        List<Reader> readers = readerService.getAllReaders();

        return ResponseEntity.ok(readers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReaderById(@PathVariable("id") Long id) {
        try {
            Reader reader = readerService.getReaderById(id);

            return ResponseEntity.ok(reader);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReader(@PathVariable("id") Long id,
                                          @Valid @RequestBody Reader updatedReader) {
        try {
            Reader reader = readerService.updateReader(id, updatedReader);

            return ResponseEntity.ok(reader);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReader(@PathVariable("id") Long id) {
        try {
            readerService.deleteReader(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/active/{status}")
    public ResponseEntity<List<Reader>> getReadersByActiveStatus(@PathVariable Boolean status) {
        List<Reader> readers = readerService.getReadersByActiveStatus(status);

        return ResponseEntity.ok(readers);
    }
}

