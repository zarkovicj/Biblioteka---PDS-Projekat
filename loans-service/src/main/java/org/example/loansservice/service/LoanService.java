package org.example.loansservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.loansservice.client.ReaderClient;
import org.example.loansservice.dto.LoanDetailsDTO;
import org.example.loansservice.dto.ReaderDTO;
import org.example.loansservice.model.Loan;
import org.example.loansservice.model.LoanStatus;
import org.example.loansservice.repository.LoanRepository;
import org.example.loansservice.service.ReaderServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LoanService {

    private final LoanRepository loanRepository;
    private final ReaderServiceClient readerServiceClient;

    private static final int MAX_ACTIVE_LOANS = 3;

    public Loan createLoan(Loan loan) {
        log.info("Creating loan for readerId: {}, book: {}", loan.getReaderId(), loan.getBookTitle());
        ReaderDTO reader = readerServiceClient.getReaderById(loan.getReaderId());

        if (!reader.getActive()) {
            throw new IllegalArgumentException("Reader with ID " + loan.getReaderId() + " is not active");
        }

        long activeLoans = countActiveLoansForReader(loan.getReaderId());
        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new IllegalArgumentException("Reader already has " + MAX_ACTIVE_LOANS + " active loans");
        }

        Loan savedLoan = loanRepository.save(loan);
        log.info("Loan created successfully with ID: {}", savedLoan.getId());

        return savedLoan;
    }



    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public List<Loan> getLoansByReaderId(Long readerId) {
        return loanRepository.findByReaderId(readerId);
    }

    public List<Loan> getLoansByStatus(LoanStatus status) {
        return loanRepository.findByStatus(status);
    }

    public Optional<Loan> returnBook(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loan -> {
                    loan.setReturnDate(LocalDate.now());
                    loan.setStatus(LoanStatus.RETURNED);
                    return loanRepository.save(loan);
                });
    }

    public boolean deleteLoan(Long id) {
        if (loanRepository.existsById(id)) {
            loanRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countActiveLoansForReader(Long readerId) {
        return loanRepository.countByReaderIdAndStatus(readerId, LoanStatus.ACTIVE);
    }

    public LoanDetailsDTO getLoanDetails(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + loanId + " not found"));

        ReaderDTO reader = readerServiceClient.getReaderById(loan.getReaderId());

        LoanDetailsDTO details = new LoanDetailsDTO();
        details.setLoanId(loan.getId());
        details.setBookTitle(loan.getBookTitle());
        details.setLoanDate(loan.getLoanDate());
        details.setDueDate(loan.getDueDate());
        details.setReturnDate(loan.getReturnDate());
        details.setStatus(loan.getStatus());

        if (reader != null) {
            details.setReaderName(reader.getFirstName() + " " + reader.getLastName());
            details.setReaderEmail(reader.getEmail());
        } else {
            details.setReaderName("Unknown");
            details.setReaderEmail("Unknown");
        }

        return details;
    }
}
