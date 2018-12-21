package com.efactura.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.efactura.client.service.ClientDataProvider;

@Controller
public class ClientController {
	
	@Autowired
	private ClientDataProvider clientDataProvider;
 
	@GetMapping("/client")
    public ModelAndView homePage(Model model) {
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("data", clientDataProvider.getList(""));
        mav.setViewName("client");
        
        return mav;
    }
}
