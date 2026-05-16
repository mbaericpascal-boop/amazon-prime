package com.keyce.amazon_prime.controller;

import com.keyce.amazon_prime.service.NotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotationController {

    @Autowired
    private NotationService notationService;

    // Soumettre une note
    @PostMapping("/film/{id}/noter")
    public String noter(@PathVariable Long id,
                        @RequestParam int note,
                        Authentication auth) {
        String email = auth.getName();
        try {
            notationService.noter(email, id, note);
        } catch (Exception e) {
            return "redirect:/film/" + id + "?erreurNote=true";
        }
        return "redirect:/film/" + id + "?note=true";
    }
}