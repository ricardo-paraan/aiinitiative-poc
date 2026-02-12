package com.fullstack.service;

import com.fullstack.entity.TicketComment;
import com.fullstack.repository.TicketCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketCommentService {
    
    @Autowired
    private TicketCommentRepository ticketCommentRepository;
    
    // Get all comments
    public List<TicketComment> getAllComments() {
        return ticketCommentRepository.findAll();
    }
    
    // Get comment by ID
    public Optional<TicketComment> getCommentById(Integer id) {
        return ticketCommentRepository.findById(id);
    }
    
    // Get comments by ticket ID
    public List<TicketComment> getCommentsByTicketId(Integer ticketId) {
        return ticketCommentRepository.findByTicket_TicketId(ticketId);
    }
    
    // Create new comment
    public TicketComment createComment(TicketComment comment) {
        return ticketCommentRepository.save(comment);
    }
    
    // Update comment
    public TicketComment updateComment(Integer id, TicketComment commentDetails) {
        Optional<TicketComment> comment = ticketCommentRepository.findById(id);
        if (comment.isPresent()) {
            TicketComment existingComment = comment.get();
            existingComment.setComments(commentDetails.getComments());
            if (commentDetails.getAuthor() != null) {
                existingComment.setAuthor(commentDetails.getAuthor());
            }
            return ticketCommentRepository.save(existingComment);
        }
        return null;
    }
    
    // Delete comment
    public boolean deleteComment(Integer id) {
        if (ticketCommentRepository.existsById(id)) {
            ticketCommentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}