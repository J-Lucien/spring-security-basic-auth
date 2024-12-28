package example.hello_security.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping({"/","/login"})
    public String login(@RequestParam(value = "error", required = false) String error, Model model){
        if( error != null)
            model.addAttribute("message","Username ou mot de passe incorrect.");
        return "login";
    }

    @GetMapping("/logout-success")
    public String logOut(Model model){
        model.addAttribute("message", "Vous vous êtes déconnecté avec succès.");
        return "login";
    }
}
