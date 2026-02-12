package com.aws.poc.ticketingsystem.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

/**
 * Entity class representing a comment on a ticket チケットに対するコメントを表すエンティティクラス
 * Maps to the ticket_comments_tbl database table
 * ticket_comments_tblデータベーステーブルにマッピングされます
 */
@Entity
@Table(name = "ticket_comments_tbl")
public class TicketCommentsTbl {
    
    /**
     * Default constructor デフォルトコンストラクタ
     */
    public TicketCommentsTbl() {
    }

    /**
     * Parameterized constructor for creating a comment すべてのフィールドを持つコメントを作成するためのパラメータ化されたコンストラクタ
     * @param commentId Unique identifier for the comment コメントの一意識別子
     * @param comments The comment text コメントテキスト
     * @param author Author who wrote the comment コメントを書いた作成者
     * @param createdDate Date and time when the comment was created コメントが作成された日時
     */
    public TicketCommentsTbl(Integer commentId, String comments, String author, LocalDateTime createdDate) {
        this.commentId = commentId;
        this.comments = comments;
        this.author = author;
        this.createdDate = createdDate;
    }

    // Primary key - Unique identifier for the comment 主キー - コメントの一意識別子
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    // Many-to-one relationship with ticket チケットとの多対一の関係
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference
    private TicketTbl ticket;

    // The comment text コメントテキスト
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    // Author who wrote the comment コメントを書いた作成者
    @Column(name = "author")
    private String author;

    // Date and time when the comment was created コメントが作成された日時
    @Column(name = "created_date")
    private LocalDateTime createdDate;

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

    /**
     * Get the ticket ID of the associated ticket 関連付けられたチケットのチケットIDを取得
     * @return Ticket ID or null if no ticket is associated チケットID、チケットが関連付けられていない場合はnull
     */
    public Integer getTicketId() {
        return ticket != null ? ticket.getTicketId() : null;
    }

    /**
     * Get the comment text コメントテキストを取得
     * @return Comment text コメントテキスト
     */
    public String getComments() {
        return comments;
    }

    /**
     * Set the comment text コメントテキストを設定
     * @param comments Comment text to set 設定するコメントテキスト
     */
    public void setComments(String comments) {
        this.comments = comments;
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
}
