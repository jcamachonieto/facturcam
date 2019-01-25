package com.efactura.home;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dropbox.core.DbxException;
import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;
import com.efactura.user.model.UserEntity;
import com.efactura.user.service.IUserDataProvider;
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
	
	@Autowired
    private IUserDataProvider userDataProvider;
	
	@Value("${email}")
	private String email;
	
	private static final String TITLE_MESSAGE = "Iniciar sesión";
 
    @GetMapping("/")
    public String homePage(Model model) {
    	UserEntity user = userDataProvider.findByEmail(email);
        if (user == null) {
        	model.addAttribute("message", MessageDto.builder()
        			.title(TITLE_MESSAGE)
        			.text("Usuario no encontrado")
        			.type(MessageConstants.TYPE_ERROR)
        			.build());
        	session.invalidate();
        	return getLoginInfo(model);
        }
        if (user.getExpirationDate().getTime() < new Date().getTime()) {
        	model.addAttribute("message", MessageDto.builder()
        			.title(TITLE_MESSAGE)
        			.text("Necesita renovar su licencia")
        			.type(MessageConstants.TYPE_ERROR)
        			.build());
        	session.invalidate();
        	return getLoginInfo(model);
        }
        
        session.setAttribute("user", user);
        
    	boolean dobackup = false;
    	
    	if (user.getBackup() == null) {
    		dobackup = true;
    	} else {
        	Calendar nextBackupCalendar = Calendar.getInstance();
        	nextBackupCalendar.setTime(user.getBackup());
            if (user.getBackupPeriod().equals("d")) {
            	nextBackupCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            if (user.getBackupPeriod().equals("w")) {
            	nextBackupCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (user.getBackupPeriod().equals("m")) {
            	nextBackupCalendar.add(Calendar.MONTH, 1);
            }
            if (user.getBackupPeriod().equals("y")) {
            	nextBackupCalendar.add(Calendar.YEAR, 1);
            }
            
            if (nextBackupCalendar.before(Calendar.getInstance())) {
            	dobackup = true;
            }
    	}
            
    	String databaseFile = fileUtils.getFullPathDatabase();
        if (new File(databaseFile).exists()
        	&& dobackup) {
        	try {
        		dropBoxBackupUtil.AutomaticBackupDatabase(databaseFile, user);
        			user.setBackup(new Date());
        			user = userDataProvider.save(user);
        	} catch (Exception ex) {
        		ex.printStackTrace();
        	}
    	}
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    	Date lastBackup = dropBoxBackupUtil.getLastBackup(user);
    	model.addAttribute("lastBackup", lastBackup != null ? sdf.format(lastBackup) : "");
        return "home";
    }
    
    @GetMapping("/loginFailure")
    public String getLoginInfo(Model model) {
    	model.addAttribute("message", MessageDto.builder()
    			.title(TITLE_MESSAGE)
    			.text(MessageConstants.GENERIC_TEXT_ERROR)
    			.type(MessageConstants.TYPE_ERROR)
    			.build());
        return "/loginError";
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
