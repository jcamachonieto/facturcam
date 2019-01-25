package com.efactura.login;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.dropbox.core.DbxException;
import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;
import com.efactura.user.model.UserEntity;
import com.efactura.user.service.IUserDataProvider;
import com.efactura.utils.DropBoxBackupUtil;
import com.efactura.utils.FileUtils;

@Controller
public class LoginController {
	
	private static final String TITLE_MESSAGE = "Iniciar sesión";

	private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    
    @Autowired
    private IUserDataProvider userDataProvider;
    
    @Autowired
    DropBoxBackupUtil dropBoxBackupUtil;
    
    @Autowired
	private FileUtils fileUtils;

    @SuppressWarnings("unchecked")
	@GetMapping("/login")
    public String getLoginPage(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
            .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);

        return "login";
    }

    @SuppressWarnings("rawtypes")
	@GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication, HttpSession session) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        String userInfoEndpointUri = client.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                .getTokenValue());

            HttpEntity<String> entity = new HttpEntity<String>("", headers);

            ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            
            UserEntity user = userDataProvider.findByEmail((String) userAttributes.get("email"));
            if (user == null) {
            	model.addAttribute("message", MessageDto.builder()
            			.title(TITLE_MESSAGE)
            			.text("Usuario no encontrado")
            			.type(MessageConstants.TYPE_ERROR)
            			.build());
            	session.invalidate();
            	// return getLoginInfo(model);
            }
            if (user.getExpirationDate().getTime() < new Date().getTime()) {
            	model.addAttribute("message", MessageDto.builder()
            			.title(TITLE_MESSAGE)
            			.text("Necesita renovar su licencia")
            			.type(MessageConstants.TYPE_ERROR)
            			.build());
            	session.invalidate();
            	// return getLoginInfo(model);
            }
            
            session.setAttribute("user", user);
            
            try {
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
	            	dropBoxBackupUtil.AutomaticBackupDatabase(databaseFile, user);
	            		user.setBackup(new Date());
	            		user = userDataProvider.save(user);
            	}
			} catch (DbxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return "redirect:/";
    }
}