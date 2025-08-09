package com.janmjay.expansetracker.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilterDTO {
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
    private String sortField; // date, amount , type
    private String sortOrder; // ascending or descending

}
