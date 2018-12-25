package com.efactura.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.efactura.client.model.ClientDto;
import com.efactura.client.service.ClientDataProvider;
import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;

@RestController
public class ClientController {
	
	private static final String TITLE_MESSAGE = "Cliente";
	
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
	
	@DeleteMapping(value = "/client/{id}")
	public MessageDto deleteStudent(@PathVariable int id) {
		try {
			clientDataProvider.delete(id);
			return MessageDto.builder()
					.text("Cliente eliminado correctamente")
					.type(MessageConstants.TYPE_SUCCESS)
					.build();
		} catch (Exception e) {
			return MessageDto.builder()
					.text("Se ha producido un error al eliminar el cliente")
					.type(MessageConstants.TYPE_ERROR)
					.build();
		}
	}
}
