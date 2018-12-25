package com.efactura.login;

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

import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;
import com.efactura.user.model.UserEntity;
import com.efactura.user.model.UserSessionDto;
import com.efactura.user.service.IUserDataProvider;

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
            
            UserSessionDto userSessionDto = UserSessionDto.builder()
            		.name((String) userAttributes.get("name"))
            		.email((String) userAttributes.get("email"))
            		.databaseFile(user.getDatabaseFile())
            		.build();
            session.setAttribute("user", userSessionDto);
        }

        return "redirect:/";
    }
    
    @GetMapping("/loginFailure")
    public String getLoginInfo(Model model) {
    	model.addAttribute("message", MessageDto.builder()
    			.title(TITLE_MESSAGE)
    			.text(MessageConstants.GENERIC_TEXT_ERROR)
    			.type(MessageConstants.TYPE_ERROR)
    			.build());
        return getLoginPage(model);
    }
}