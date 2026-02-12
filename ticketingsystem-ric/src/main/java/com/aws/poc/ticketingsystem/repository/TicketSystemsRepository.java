package com.aws.poc.ticketingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aws.poc.ticketingsystem.entity.TicketSystemsTbl;

/**
 * Repository interface for Ticket Systems entity operations これはチケットシステムエンティティ操作のためのリポジトリインターフェースです
 * Manages different systems that tickets can be associated with
 * チケットが関連付けられる異なるシステムを管理します
 */
public interface TicketSystemsRepository extends JpaRepository<TicketSystemsTbl, Integer> {
   
}
