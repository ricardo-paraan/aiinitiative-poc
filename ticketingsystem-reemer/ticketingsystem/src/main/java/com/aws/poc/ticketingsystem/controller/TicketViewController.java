package com.aws.poc.ticketingsystem.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.service.TicketService;

@Controller
public class TicketViewController {

    private final TicketService ticketService;

    public TicketViewController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Display the home page (dashboard)
     */
    @GetMapping("/")
    public String showDashboard() {
        return "index";
    }

    /**
     * Display the create ticket form
     */
    @GetMapping("/tickets/create")
    public String showCreateTicketForm(Model model) {
        // Create a new empty ticket object for the form
        TicketTbl ticket = new TicketTbl();
        model.addAttribute("ticket", ticket);
        return "create-ticket";
    }

    /**
     * Handle the form submission to create a new ticket
     */
    @PostMapping("/tickets/create")
    public String createTicket(@ModelAttribute("ticket") TicketTbl ticket, 
                               RedirectAttributes redirectAttributes) {
        try {
            // Set default values for fields not in the form
            ticket.setStatus("PENDING"); // Default status for new tickets
            ticket.setCreatedDate(LocalDateTime.now());
            ticket.setUpdateDate(LocalDateTime.now());
            
            // Set default values for fields that might not be used yet
            ticket.setAttachmentId(0); // Default to 0 if no attachment
            ticket.setCommentId(0); // Default to 0 if no comments yet
            
            // Save the ticket
            TicketTbl savedTicket = ticketService.saveTicket(ticket);
            
            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", 
                "Ticket created successfully! Ticket ID: " + savedTicket.getTicketId());
            
            // Redirect to dashboard
            return "redirect:/";
            
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error creating ticket: " + e.getMessage());
            
            // Redirect back to create form
            return "redirect:/tickets/create";
        }
    }
}