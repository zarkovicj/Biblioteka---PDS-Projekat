package org.example.loansservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Reader ID is required")
    @Column(name = "reader_id", nullable = false)
    private Long readerId;

    @NotBlank(message = "Book title is required")
    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;  // null dok ne bude vracena

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @PrePersist
    public void prePersist() {
        if (loanDate == null) {
            loanDate = LocalDate.now();
        }
        if (dueDate == null) {
            dueDate = loanDate.plusDays(14); // 14 dana rok
        }
        if (status == null) {
            status = LoanStatus.ACTIVE;
        }
    }
}
