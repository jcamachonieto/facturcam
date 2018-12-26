package com.efactura.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;
import com.efactura.user.model.UserEntity;
import com.efactura.user.service.IUserDataProvider;

@RestController
public class UserController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
    private IUserDataProvider userDataProvider;

	@GetMapping("/user")
	public ModelAndView homePage(Model model) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		model.addAttribute("user", user);
		return new ModelAndView("user");
	}

	@PostMapping("/user")
	public ModelAndView update(@ModelAttribute("user") @Valid UserEntity user, Model model) {
		UserEntity userSession = (UserEntity) session.getAttribute("user");
		user.setExpirationDate(userSession.getExpirationDate());
		user = userDataProvider.save(user);
		session.setAttribute("user", user);
		model.addAttribute("message",
				MessageDto.builder().text("Configuración modificada correctamente").type(MessageConstants.TYPE_SUCCESS).build());
		return homePage(model);
	}
}
