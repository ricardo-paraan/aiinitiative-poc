package com.ticketing.controller;

import com.ticketing.dto.CommentDTO;
import com.ticketing.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Get all comments for a ticket
    @GetMapping("/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId(@PathVariable Integer ticketId) {
        List<CommentDTO> comments = commentService.getCommentsByTicketId(ticketId);
        return ResponseEntity.ok(comments);
    }

    // Add comment to ticket
    @PostMapping("/tickets/{ticketId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Integer ticketId,
            @RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.addComment(ticketId, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // Delete comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}