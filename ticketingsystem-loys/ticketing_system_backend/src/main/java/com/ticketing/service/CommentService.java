package com.ticketing.service;

import com.ticketing.dto.CommentDTO;
import com.ticketing.model.Ticket;
import com.ticketing.model.TicketComment;
import com.ticketing.repository.CommentRepository;
import com.ticketing.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // Get all comments for a ticket
    public List<CommentDTO> getCommentsByTicketId(Integer ticketId) {
        return commentRepository.findByTicket_TicketId(ticketId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Add comment to ticket
    public CommentDTO addComment(Integer ticketId, CommentDTO commentDTO) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

        TicketComment comment = new TicketComment();
        comment.setTicket(ticket);
        comment.setCommentText(commentDTO.getCommentText());
        comment.setAuthor(commentDTO.getAuthor());

        TicketComment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    // Delete comment
    public void deleteComment(Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    // Convert Entity to DTO
    private CommentDTO convertToDTO(TicketComment comment) {
        return new CommentDTO(
                comment.getCommentId(),
                comment.getTicket().getTicketId(),
                comment.getCommentText(),
                comment.getAuthor()
        );
    }
}