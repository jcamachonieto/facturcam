package com.efactura.home;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dropbox.core.DbxException;
import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;
import com.efactura.user.model.UserEntity;
import com.efactura.utils.DropBoxBackupUtil;
import com.efactura.utils.FileUtils;

@Controller
public class HomeController {
	
	@Autowired
	DropBoxBackupUtil dropBoxBackupUtil;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Autowired
	HttpSession session;
 
    @GetMapping("/")
    public String homePage(Model model) {
    	UserEntity userSession = (UserEntity) session.getAttribute("user");
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    	Date lastBackup = dropBoxBackupUtil.getLastBackup(userSession);
    	model.addAttribute("lastBackup", lastBackup != null ? sdf.format(lastBackup) : "");
        return "home";
    }
    
    @PostMapping("/backup")
    public String backup(Model model) {
    	String databaseFile = fileUtils.getFullPathDatabase();
    	boolean backup = false;
        try {
			if (new File(databaseFile).exists()) {
				UserEntity userSession = (UserEntity) session.getAttribute("user");
				backup = dropBoxBackupUtil.ManualBackupDatabase(databaseFile, userSession);
			}
		} catch (DbxException | IOException e) {
			e.printStackTrace();
		}
        if (backup) {
        	model.addAttribute("message", MessageDto.builder()
        			.title("Copia de seguridad")
        			.text("Copia de seguridad realizada correctamente en Dropbox")
        			.type(MessageConstants.TYPE_SUCCESS)
        			.build());
        } else {
        	model.addAttribute("message", MessageDto.builder()
        			.title("Copia de seguridad")
        			.text("No se ha podido realizar la copia de seguridad en Dropbox")
        			.type(MessageConstants.TYPE_ERROR)
        			.build());
        }
        return homePage(model);
    }
}
