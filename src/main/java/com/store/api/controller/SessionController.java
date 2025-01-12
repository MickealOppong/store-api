package com.store.api.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping("/create")
    public String createSession(HttpSession session){
        session.setAttribute("username","guest");
        String sessionId = session.getId();
        return sessionId;
    }

    @GetMapping("/get")
    public String getSession(HttpSession session) {
        // Get the session attribute (username)
        String username =(String)session.getAttribute("username");

        // If no session exists, return an error message
        if (username == null) {
            return "No session found!";
        }

        // Return session data to the client
        return "Session found with username: " + username;
    }

    // Invalidate the session
    @GetMapping("/invalidate")
    public String invalidateSession(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return "Session invalidated!";
    }
}
