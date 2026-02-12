package com.fullstack.controller;

import com.fullstack.entity.TicketComment;
import com.fullstack.service.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketCommentController {
    
    @Autowired
    private TicketCommentService ticketCommentService;
    
    // Get all comments
    @GetMapping
    public ResponseEntity<List<TicketComment>> getAllComments() {
        List<TicketComment> comments = ticketCommentService.getAllComments();
        return ResponseEntity.ok(comments);
    }
    
    // Get comment by ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketComment> getCommentById(@PathVariable Integer id) {
        Optional<TicketComment> comment = ticketCommentService.getCommentById(id);
        return comment.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // Get comments by ticket ID
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketComment>> getCommentsByTicketId(@PathVariable Integer ticketId) {
        List<TicketComment> comments = ticketCommentService.getCommentsByTicketId(ticketId);
        return ResponseEntity.ok(comments);
    }
    
    // Create new comment
    @PostMapping
    public ResponseEntity<TicketComment> createComment(@RequestBody TicketComment comment) {
        TicketComment createdComment = ticketCommentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
    
    // Update comment
    @PutMapping("/{id}")
    public ResponseEntity<TicketComment> updateComment(@PathVariable Integer id, @RequestBody TicketComment comment) {
        TicketComment updatedComment = ticketCommentService.updateComment(id, comment);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete comment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        boolean deleted = ticketCommentService.deleteComment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}