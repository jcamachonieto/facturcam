package com.efactura.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
 
	@GetMapping("/client")
    public String homePage(Model model) {
        return "client";
    }
}
