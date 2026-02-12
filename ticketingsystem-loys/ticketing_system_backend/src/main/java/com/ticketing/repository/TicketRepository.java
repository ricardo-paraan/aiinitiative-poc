package com.ticketing.repository;

import com.ticketing.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Find tickets by status
    List<Ticket> findByStatus_StatusId(Integer statusId);

    // Find tickets by category
    List<Ticket> findByCategory_CategoryId(Integer categoryId);

    // Find tickets by system
    List<Ticket> findBySystem_SystemId(Integer systemId);

    // Find tickets by author
    List<Ticket> findByAuthor(String author);

    // Search tickets by title or description
    @Query("SELECT t FROM Ticket t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Ticket> searchByKeyword(@Param("keyword") String keyword);

    // Find tickets with multiple filters
    @Query("SELECT t FROM Ticket t WHERE " +
           "(:statusId IS NULL OR t.status.statusId = :statusId) AND " +
           "(:categoryId IS NULL OR t.category.categoryId = :categoryId) AND " +
           "(:systemId IS NULL OR t.system.systemId = :systemId) AND " +
           "(:author IS NULL OR t.author = :author)")
    List<Ticket> findByFilters(
        @Param("statusId") Integer statusId,
        @Param("categoryId") Integer categoryId,
        @Param("systemId") Integer systemId,
        @Param("author") String author
    );

    // Count tickets by status
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status.statusId = :statusId")
    Long countByStatusId(@Param("statusId") Integer statusId);

    // Count tickets by category
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.category.categoryId = :categoryId")
    Long countByCategoryId(@Param("categoryId") Integer categoryId);

    // Count tickets by system
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.system.systemId = :systemId")
    Long countBySystemId(@Param("systemId") Integer systemId);
}