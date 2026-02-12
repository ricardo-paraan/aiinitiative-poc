package com.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Integer ticketId;
    private String title;
    private String author;
    private String description;
    private Integer statusId;
    private String statusName;
    private Integer categoryId;
    private String categoryName;
    private Integer systemId;
    private String systemName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<CommentDTO> comments;
    private List<AttachmentDTO> attachments;
}