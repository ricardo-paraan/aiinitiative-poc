package com.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private Integer attachmentId;
    private Integer ticketId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String contentType;
}