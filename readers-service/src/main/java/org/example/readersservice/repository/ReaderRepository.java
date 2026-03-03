package org.example.readersservice.repository;

import org.example.readersservice.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Optional<Reader> findByEmail(String email);

    List<Reader> findByActive(Boolean active);
}
