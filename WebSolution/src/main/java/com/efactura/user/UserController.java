package com.efactura.user;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;
import com.efactura.user.model.UserEntity;
import com.efactura.user.service.IUserDataProvider;
import com.efactura.utils.FileUtils;

@RestController
public class UserController {

	@Autowired
	HttpSession session;

	@Autowired
	private IUserDataProvider userDataProvider;
	
	@Autowired
	FileUtils fileUtils;

	@GetMapping("/user")
	public ModelAndView homePage(Model model) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		model.addAttribute("user", user);
		try {
			byte[] logo = fileUtils.getLogo();
			if (logo != null) {
				model.addAttribute("logo", Base64.getEncoder().encodeToString(logo));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ModelAndView("user");
	}

	@PostMapping("/user")
	public ModelAndView update(@ModelAttribute("user") @Valid UserEntity user, Model model) {
		UserEntity userSession = (UserEntity) session.getAttribute("user");
		user.setExpirationDate(userSession.getExpirationDate());
		user.setBackup(userSession.getBackup());
		user.setBackupPeriod(userSession.getBackupPeriod());
		user = userDataProvider.save(user);
		session.setAttribute("user", user);
		model.addAttribute("message", MessageDto.builder().text("Configuración modificada correctamente")
				.type(MessageConstants.TYPE_SUCCESS).build());
		return homePage(model);
	}

	@PostMapping("/uploadFile")
	@ResponseBody
	public ResponseEntity<?> handleFileUpload(@RequestParam("logo") MultipartFile logo) {
		try {
			fileUtils.saveLogo(logo);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
