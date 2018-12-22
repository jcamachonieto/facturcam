package com.efactura.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.efactura.client.model.ClientDto;
import com.efactura.client.service.ClientDataProvider;

@RestController
public class ClientController {
	
	@Autowired
	private ClientDataProvider clientDataProvider;
	
	@GetMapping("/client")
    public ModelAndView homePage(Model model) {
		return new ModelAndView("client");
	}
 
	@RequestMapping(path="/client/list", method=RequestMethod.GET)
    public List<ClientDto> list(Model model) {
        return clientDataProvider.getList();
    }
}
