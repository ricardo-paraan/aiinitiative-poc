package com.aws.poc.ticketingsystem.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity class representing the status history of a ticket チケットのステータス履歴を表すエンティティクラス
 * Maps to the ticket_status_history_tbl database table
 * ticket_status_history_tblデータベーステーブルにマッピングされます
 */
@Entity
@Table(name = "ticket_status_history_tbl")
public class TicketStatusHistoryTbl {
    
    /**
     * Default constructor デフォルトコンストラクタ
     */
    public TicketStatusHistoryTbl() {
    }

    /**
     * Parameterized constructor for creating a status history record すべてのフィールドを持つステータス履歴レコードを作成するためのパラメータ化されたコンストラクタ
     * @param statusId Unique identifier for the status history record ステータス履歴レコードの一意識別子
     * @param ticket Associated ticket 関連付けられたチケット
     * @param status Status value ステータス値
     * @param updateDate Date and time when the status was updated ステータスが更新された日時
     */
    public TicketStatusHistoryTbl(Integer statusId, TicketTbl ticket, String status,
            LocalDateTime updateDate) {
        this.statusId = statusId;
        this.ticket = ticket;
        this.status = status;
        this.updateDate = updateDate;
    }

    // Primary key - Unique identifier for the status history record 主キー - ステータス履歴レコードの一意識別子
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId;

    // Many-to-one relationship with ticket チケットとの多対一の関係
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference
    private TicketTbl ticket;

    // Status value (e.g., PENDING, ONGOING, RESOLVED) ステータス値（例：保留中、進行中、解決済み）
    @Column(name = "status")
    private String status;

    // Date and time when the status was updated ステータスが更新された日時
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    /**
     * Get the status history ID ステータス履歴IDを取得
     * @return Status history ID ステータス履歴ID
     */
    public Integer getStatusId() {
        return statusId;
    }

    /**
     * Set the status history ID ステータス履歴IDを設定
     * @param statusId Status history ID to set 設定するステータス履歴ID
     */
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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
     * Get the status value ステータス値を取得
     * @return Status value ステータス値
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status value ステータス値を設定
     * @param status Status value to set 設定するステータス値
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the update date 更新日時を取得
     * @return Update date 更新日時
     */
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /**
     * Set the update date 更新日時を設定
     * @param updateDate Update date to set 設定する更新日時
     */
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
