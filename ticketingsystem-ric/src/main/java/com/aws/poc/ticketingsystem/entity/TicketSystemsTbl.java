package com.aws.poc.ticketingsystem.entity;

import jakarta.persistence.*;

/**
 * Entity class representing system options for tickets チケットのシステムオプションを表すエンティティクラス
 * Maps to the ticket_systems_tbl database table
 * ticket_systems_tblデータベーステーブルにマッピングされます
 */
@Entity
@Table(name = "ticket_systems_tbl")
public class TicketSystemsTbl {

    /**
     * Default constructor デフォルトコンストラクタ
     */
    public TicketSystemsTbl() {
    }

    /**
     * Parameterized constructor for creating a ticket system すべてのフィールドを持つチケットシステムを作成するためのパラメータ化されたコンストラクタ
     * @param systemId Unique identifier for the system システムの一意識別子
     * @param systemName Name of the system システム名
     */
    public TicketSystemsTbl(int systemId, String systemName) {
        this.systemId = systemId;
        this.systemName = systemName;
    }

    // Primary key - Unique identifier for the system 主キー - システムの一意識別子
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="system_id")
    private int systemId;

    // Name of the system システム名
    @Column(name="system_name")
    private String systemName;

    /**
     * Get the system ID システムIDを取得
     * @return System ID システムID
     */
    public int getSystemId() {
        return systemId;
    }

    /**
     * Set the system ID システムIDを設定
     * @param systemId System ID to set 設定するシステムID
     */
    public void setSystemId(int systemId) {
        this.systemId = systemId;
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
}