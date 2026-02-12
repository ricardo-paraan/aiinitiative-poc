package com.aws.poc.ticketingsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_attachments_tbl")
public class TicketAttachmentsTbl {

    public TicketAttachmentsTbl() {
    }

    public TicketAttachmentsTbl(int attachmentId, String filePath) {
        this.attachmentId = attachmentId;
        this.filePath = filePath;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="attachment_id")
    private int attachmentId;

    @Column(name="file_path")
    private String filePath;

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
