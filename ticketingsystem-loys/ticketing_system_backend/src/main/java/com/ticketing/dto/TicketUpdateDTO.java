package com.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketUpdateDTO {
    private String title;
    private String description;
    private Integer statusId;
    private Integer categoryId;
    private Integer systemId;
}