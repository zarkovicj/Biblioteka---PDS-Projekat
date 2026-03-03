package org.example.loansservice.service;

import org.example.loansservice.dto.ReaderDTO;
import org.example.loansservice.model.Loan;
import org.example.loansservice.model.LoanStatus;
import org.example.loansservice.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ReaderServiceClient readerServiceClient;

    @InjectMocks
    private LoanService loanService;

    private Loan loan;
    private ReaderDTO readerDTO;


    @BeforeEach
    void setUp() {
        loan = new Loan();
        loan.setReaderId(1L);
        loan.setBookTitle("Clean Code");

        readerDTO = new ReaderDTO();
        readerDTO.setId(1L);
        readerDTO.setFirstName("Jovan");
        readerDTO.setLastName("Zarkovic");
        readerDTO.setEmail("jz@gmail.com");
        readerDTO.setActive(true);
    }

    @Test
    void createLoanWithValidReader() {
        // Arrange
        when(readerServiceClient.getReaderById(1L)).thenReturn(readerDTO);
        when(loanRepository.countByReaderIdAndStatus(1L, LoanStatus.ACTIVE)).thenReturn(0L);

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan savedLoan = invocation.getArgument(0);
            savedLoan.setId(1L);
            savedLoan.setStatus(LoanStatus.ACTIVE);
            savedLoan.setLoanDate(LocalDate.now());
            savedLoan.setDueDate(LocalDate.now().plusDays(14));
            return savedLoan;
        });

        // Act
        Loan result = loanService.createLoan(loan);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getBookTitle());
        assertEquals(LoanStatus.ACTIVE, result.getStatus());
        verify(readerServiceClient).getReaderById(1L);
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void createLoanWithNonExistentReader() {
        // Arrange
        when(readerServiceClient.getReaderById(1L))
                .thenThrow(new IllegalArgumentException("Reader with ID 1 not found"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanService.createLoan(loan)
        );
        assertEquals("Reader with ID 1 not found", exception.getMessage());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void createLoanWithInactiveReader() {
        // Arrange
        readerDTO.setActive(false);
        when(readerServiceClient.getReaderById(1L)).thenReturn(readerDTO);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loanService.createLoan(loan)
        );
        assertEquals("Reader with ID 1 is not active", exception.getMessage());
        verify(loanRepository, never()).save(any());
    }


}
