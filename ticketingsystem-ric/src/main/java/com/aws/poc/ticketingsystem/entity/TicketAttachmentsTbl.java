package com.aws.poc.ticketingsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

/**
 * Entity class representing an attachment for a ticket チケットの添付ファイルを表すエンティティクラス
 * Maps to the ticket_attachments_tbl database table
 * ticket_attachments_tblデータベーステーブルにマッピングされます
 */
@Entity
@Table(name = "ticket_attachments_tbl")
public class TicketAttachmentsTbl {

    /**
     * Default constructor デフォルトコンストラクタ
     */
    public TicketAttachmentsTbl() {
    }

    /**
     * Parameterized constructor for creating an attachment すべてのフィールドを持つ添付ファイルを作成するためのパラメータ化されたコンストラクタ
     * @param attachmentId Unique identifier for the attachment 添付ファイルの一意識別子
     * @param filePath Path to the attached file 添付ファイルのパス
     * @param ticket Associated ticket 関連付けられたチケット
     */
    public TicketAttachmentsTbl(int attachmentId, String filePath, TicketTbl ticket) {
        this.attachmentId = attachmentId;
        this.filePath = filePath;
        this.ticket = ticket;
    }

    // Primary key - Unique identifier for the attachment 主キー - 添付ファイルの一意識別子
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="attachment_id")
    private int attachmentId;

    // Path to the attached file 添付ファイルのパス
    @Column(name="file_path")
    private String filePath;

    // Many-to-one relationship with ticket チケットとの多対一の関係
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference
    private TicketTbl ticket;

    /**
     * Get the attachment ID 添付ファイルIDを取得
     * @return Attachment ID 添付ファイルID
     */
    public int getAttachmentId() {
        return attachmentId;
    }

    /**
     * Set the attachment ID 添付ファイルIDを設定
     * @param attachmentId Attachment ID to set 設定する添付ファイルID
     */
    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    /**
     * Get the file path ファイルパスを取得
     * @return File path ファイルパス
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Set the file path ファイルパスを設定
     * @param filePath File path to set 設定するファイルパス
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Get the associated ticket 関連付けられたチケットを取得
     * @return Associated ticket 関連付けられたチケット
     */
    public TicketTbl getTicket() {
        return ticket;
    }

    /**
     * Set the associated ticket 関連付けられたチケットを設定
     * @param ticket Ticket to associate 関連付けるチケット
     */
    public void setTicket(TicketTbl ticket) {
        this.ticket = ticket;
    }
}
