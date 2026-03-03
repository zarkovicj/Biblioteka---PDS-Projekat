package org.example.loansservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReaderDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate membershipDate;
    private Boolean active;
}
