package com.ticketing.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateDTO {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Author is required")
    private String author;
    
    private String description;
    
    @NotNull(message = "Status ID is required")
    private Integer statusId;
    
    @NotNull(message = "Category ID is required")
    private Integer categoryId;
    
    @NotNull(message = "System ID is required")
    private Integer systemId;
}