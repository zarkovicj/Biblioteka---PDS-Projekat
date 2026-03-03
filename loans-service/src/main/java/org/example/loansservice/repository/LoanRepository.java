package org.example.loansservice.repository;

import org.example.loansservice.model.Loan;
import org.example.loansservice.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Sve pozajmice citaoca
    List<Loan> findByReaderId(Long readerId);

    // Sve pozajmice po statusu
    List<Loan> findByStatus(LoanStatus status);

    // Aktivne pozajmice citaoca
    List<Loan> findByReaderIdAndStatus(Long readerId, LoanStatus status);

    // Broj aktivnih pozajmica citaoca (za ogranicenje max 3)
    long countByReaderIdAndStatus(Long readerId, LoanStatus status);
}
