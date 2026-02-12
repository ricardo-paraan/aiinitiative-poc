package com.aws.poc.ticketingsystem.entity;

import jakarta.persistence.*;

/**
 * Entity class representing ticket status options チケットステータスオプションを表すエンティティクラス
 * Maps to the ticket_status_tbl database table
 * ticket_status_tblデータベーステーブルにマッピングされます
 */
@Entity
@Table(name = "ticket_status_tbl")
public class TicketStatusTbl {

    /**
     * Default constructor デフォルトコンストラクタ
     */
    public TicketStatusTbl() {
    }

    /**
     * Parameterized constructor for creating a ticket status すべてのフィールドを持つチケットステータスを作成するためのパラメータ化されたコンストラクタ
     * @param statusId Unique identifier for the status ステータスの一意識別子
     * @param status Status name (e.g., PENDING, ONGOING, RESOLVED) ステータス名（例：保留中、進行中、解決済み）
     */
    public TicketStatusTbl(int statusId, String status) {
        this.statusId = statusId;
        this.status = status;
    }

    // Primary key - Unique identifier for the status 主キー - ステータスの一意識別子
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="status_id")
    private int statusId;

    // Status name (e.g., PENDING, ONGOING, RESOLVED) ステータス名（例：保留中、進行中、解決済み）
    @Column(name="status")
    private String status;

    /**
     * Get the status ID ステータスIDを取得
     * @return Status ID ステータスID
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Set the status ID ステータスIDを設定
     * @param statusId Status ID to set 設定するステータスID
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    /**
     * Get the status name ステータス名を取得
     * @return Status name ステータス名
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status name ステータス名を設定
     * @param status Status name to set 設定するステータス名
     */
    public void setStatus(String status) {
        this.status = status;
    }
}