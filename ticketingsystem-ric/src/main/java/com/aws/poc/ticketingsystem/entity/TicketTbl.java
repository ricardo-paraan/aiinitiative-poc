package com.aws.poc.ticketingsystem.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a ticket in the ticketing system チケットシステムのチケットを表すエンティティクラス
 * Maps to the ticket_tbl database table
 * ticket_tblデータベーステーブルにマッピングされます
 */
@Entity
@Table(name = "ticket_tbl")
@JsonIgnoreProperties({"commentId","attachmentId"})
public class TicketTbl {

    /**
     * Default constructor デフォルトコンストラクタ
     */
    public TicketTbl() {
    }

    /**
     * Parameterized constructor for creating a ticket with all fields すべてのフィールドを持つチケットを作成するためのパラメータ化されたコンストラクタ
     * @param ticketId Unique identifier for the ticket チケットの一意識別子
     * @param title Title of the ticket チケットのタイトル
     * @param author Author who created the ticket チケットを作成した作成者
     * @param systemName Name of the system related to the ticket チケットに関連するシステム名
     * @param category Category of the ticket チケットのカテゴリ
     * @param description Detailed description of the ticket チケットの詳細説明
     * @param attachmentId ID of the attachment 添付ファイルのID
     * @param status Current status of the ticket チケットの現在のステータス
     * @param commentId ID of the comment コメントのID
     * @param ticketCommentsList List of comments associated with the ticket チケットに関連付けられたコメントのリスト
     * @param createdDate Date and time when the ticket was created チケットが作成された日時
     * @param updateDate Date and time when the ticket was last updated チケットが最後に更新された日時
     */
    public TicketTbl(Integer ticketId, String title, String author, String systemName, String category,
            String description, Integer attachmentId, String status, Integer commentId,
            List<TicketCommentsTbl> ticketCommentsList, LocalDateTime createdDate, LocalDateTime updateDate) {
        this.ticketId = ticketId;
        this.title = title;
        this.author = author;
        this.systemName = systemName;
        this.category = category;
        this.description = description;
        this.attachmentId = attachmentId;
        this.status = status;
        this.commentId = commentId;
        this.ticketCommentsList = ticketCommentsList;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }

    // Primary key - Unique identifier for the ticket 主キー - チケットの一意識別子
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ticket_id")
    private Integer ticketId;

    // Title of the ticket チケットのタイトル
    @Column(name="title")
    private String title;

    // Author who created the ticket チケットを作成した作成者
    @Column(name="author")
    private String author;

    // Name of the system related to the ticket チケットに関連するシステム名
    @Column(name="system_name")
    private String systemName;

    // Category of the ticket (e.g., Bug, Feature Request) チケットのカテゴリ（例：バグ、機能リクエスト）
    @Column(name="category")
    private String category;

    // Detailed description of the ticket チケットの詳細説明
    @Column(name="description")
    private String description;

    // ID of the attachment 添付ファイルのID
    @Column(name="attachment_id")
    private Integer attachmentId;

    // Current status of the ticket (e.g., PENDING, ONGOING, RESOLVED) チケットの現在のステータス（例：保留中、進行中、解決済み）
    @Column(name="status")
    private String status;

    // ID of the comment コメントのID
    @Column(name="comment_id")
    private Integer commentId;

    // List of comments associated with this ticket このチケットに関連付けられたコメントのリスト
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TicketCommentsTbl> ticketCommentsList = new ArrayList<>();

    // Date and time when the ticket was created チケットが作成された日時
    @Column(name="created_date")
    private LocalDateTime createdDate;

    // Date and time when the ticket was last updated チケットが最後に更新された日時
    @Column(name="update_date")
    private LocalDateTime updateDate;

    /**
     * Get the ticket ID チケットIDを取得
     * @return Ticket ID チケットID
     */
    public Integer getTicketId() {
        return ticketId;
    }

    /**
     * Set the ticket ID チケットIDを設定
     * @param ticketId Ticket ID to set 設定するチケットID
     */
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * Get the ticket title チケットタイトルを取得
     * @return Ticket title チケットタイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the ticket title チケットタイトルを設定
     * @param title Ticket title to set 設定するチケットタイトル
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the author name 作成者名を取得
     * @return Author name 作成者名
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author name 作成者名を設定
     * @param author Author name to set 設定する作成者名
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the system name システム名を取得
     * @return System name システム名
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * Set the system name システム名を設定
     * @param systemName System name to set 設定するシステム名
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * Get the ticket category チケットカテゴリを取得
     * @return Ticket category チケットカテゴリ
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the ticket category チケットカテゴリを設定
     * @param category Ticket category to set 設定するチケットカテゴリ
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the ticket description チケット説明を取得
     * @return Ticket description チケット説明
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the ticket description チケット説明を設定
     * @param description Ticket description to set 設定するチケット説明
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the attachment ID 添付ファイルIDを取得
     * @return Attachment ID 添付ファイルID
     */
    public Integer getAttachmentId() {
        return attachmentId;
    }

    /**
     * Set the attachment ID 添付ファイルIDを設定
     * @param attachmentId Attachment ID to set 設定する添付ファイルID
     */
    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    /**
     * Get the ticket status チケットステータスを取得
     * @return Ticket status チケットステータス
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the ticket status チケットステータスを設定
     * @param status Ticket status to set 設定するチケットステータス
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the comment ID コメントIDを取得
     * @return Comment ID コメントID
     */
    public Integer getCommentId() {
        return commentId;
    }

    /**
     * Set the comment ID コメントIDを設定
     * @param commentId Comment ID to set 設定するコメントID
     */
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    /**
     * Get the creation date 作成日時を取得
     * @return Creation date 作成日時
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Set the creation date 作成日時を設定
     * @param createdDate Creation date to set 設定する作成日時
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the last update date 最終更新日時を取得
     * @return Last update date 最終更新日時
     */
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /**
     * Set the last update date 最終更新日時を設定
     * @param updateDate Last update date to set 設定する最終更新日時
     */
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Get the list of comments associated with this ticket このチケットに関連付けられたコメントのリストを取得
     * @return List of ticket comments チケットコメントのリスト
     */
    public List<TicketCommentsTbl> getTicketCommentsList() {
        return ticketCommentsList;
    }

    /**
     * Set the list of comments associated with this ticket このチケットに関連付けられたコメントのリストを設定
     * @param ticketCommentsList List of ticket comments to set 設定するチケットコメントのリスト
     */
    public void setTicketCommentsList(List<TicketCommentsTbl> ticketCommentsList) {
        this.ticketCommentsList = ticketCommentsList;
    }
}