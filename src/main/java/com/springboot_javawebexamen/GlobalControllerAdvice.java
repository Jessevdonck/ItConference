package com.springboot_javawebexamen;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, Principal principal, Authentication authentication) {
        if (principal != null && authentication != null) {
            model.addAttribute("username", principal.getName());

            String userRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_USER");
            model.addAttribute("userRole", userRole.substring(5).toLowerCase());
        }
    }
}
