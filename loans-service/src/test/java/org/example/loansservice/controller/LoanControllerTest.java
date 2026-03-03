package org.example.loansservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.example.loansservice.model.Loan;
import org.example.loansservice.model.LoanStatus;
import org.example.loansservice.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanService loanService;

    @Test
    void getAllLoans() throws Exception {
        // Arrange
        Loan loan1 = new Loan();
        loan1.setId(1L);
        loan1.setReaderId(1L);
        loan1.setBookTitle("Clean Code");
        loan1.setLoanDate(LocalDate.now());
        loan1.setDueDate(LocalDate.now().plusDays(14));
        loan1.setStatus(LoanStatus.ACTIVE);

        when(loanService.getAllLoans()).thenReturn(List.of(loan1));

        // Act & Assert
        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].bookTitle").value("Clean Code"));
    }

    @Test
    void createLoanWithInvalidReader() throws Exception {
        // Arrange
        Loan loan = new Loan();
        loan.setReaderId(999L);
        loan.setBookTitle("Test Book");

        when(loanService.createLoan(any(Loan.class)))
                .thenThrow(new IllegalArgumentException("Reader with ID 999 not found"));

        // Act & Assert
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Reader with ID 999 not found"));
    }

}
