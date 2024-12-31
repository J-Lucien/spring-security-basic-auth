package example.hello_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        List<String> items = List.of("Item 1", "Item 2", "Item 3");
        model.addAttribute("items", items);
        return "home";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}
