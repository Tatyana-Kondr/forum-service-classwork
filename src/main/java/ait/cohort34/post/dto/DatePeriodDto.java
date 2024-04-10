package ait.cohort34.post.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DatePeriodDto {
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
