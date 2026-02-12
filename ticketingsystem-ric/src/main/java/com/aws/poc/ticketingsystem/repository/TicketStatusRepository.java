package com.aws.poc.ticketingsystem.repository;

import com.aws.poc.ticketingsystem.entity.TicketStatusTbl;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Ticket Status entity operations これはチケットステータスエンティティ操作のためのリポジトリインターフェースです
 * Provides CRUD operations for ticket status management
 * チケットステータス管理のためのCRUD操作を提供します
 */
public interface TicketStatusRepository extends JpaRepository<TicketStatusTbl, Integer> {

}
