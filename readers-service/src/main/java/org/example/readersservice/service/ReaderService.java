package org.example.readersservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.readersservice.model.Reader;
import org.example.readersservice.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReaderService {

    private final ReaderRepository readerRepository;

    public Reader createReader(Reader reader) {
        Optional<Reader> existingReader = readerRepository.findByEmail(reader.getEmail());
        if (existingReader.isPresent()) {
            throw new IllegalArgumentException("Reader with email " + reader.getEmail() + " already exists");
        }

        return readerRepository.save(reader);
    }

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public Reader getReaderById(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reader with ID " + id + " not found"));
    }

    public Reader updateReader(Long id, Reader updatedReader) {
        Reader existingReader = getReaderById(id);

        existingReader.setFirstName(updatedReader.getFirstName());
        existingReader.setLastName(updatedReader.getLastName());
        existingReader.setEmail(updatedReader.getEmail());
        existingReader.setMembershipDate(updatedReader.getMembershipDate());
        existingReader.setActive(updatedReader.getActive());

        return readerRepository.save(existingReader);
    }

    public void deleteReader(Long id) {
        if (!readerRepository.existsById(id)) {
            throw new IllegalArgumentException("Reader with ID " + id + " not found");
        }

        readerRepository.deleteById(id);
    }

    public List<Reader> getReadersByActiveStatus(Boolean active) {
        return readerRepository.findByActive(active);
    }
}
