package es.us.isa.ideas.app.security;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.us.isa.ideas.app.controllers.AbstractController;
import es.us.isa.ideas.app.controllers.ConfigurationController;

@Controller
@Transactional
@RequestMapping("/security")
public class LoginController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class
            .getName());

    // Supporting services ----------------------------------------------------
    @Autowired
    LoginService service;
    
    @Autowired
    ConfigurationController configurationController;

    // Constructors -----------------------------------------------------------
    public LoginController() {
        super();
    }

    // Login ------------------------------------------------------------------
    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request,
            @Valid @ModelAttribute Credentials credentials,
            BindingResult bindingResult,
            @RequestParam(required = false) boolean showError) {

        Assert.notNull(credentials);
        Assert.notNull(bindingResult);

        ModelAndView result;
        
        // Configure studioConfiguration
        configurationController.getConfiguration(request);

        DefaultSavedRequest originalRequest = (DefaultSavedRequest) request
                .getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        LOGGER.log(Level.INFO, "Login - originalRequest: " + originalRequest);

        String originalUrl;

        try {
            originalUrl = originalRequest.getRedirectUrl();
        } catch (Exception e) {
            originalUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/app/editor";
        }

        LOGGER.log(Level.SEVERE, "Login - redirecting to: " + originalUrl);

        result = new ModelAndView("security/login");

        result.addObject("credentials", credentials);
        result.addObject("showError", showError);
        result.addObject("originalRequestUrl", originalUrl);

        return result;
    }

    // LoginFailure -----------------------------------------------------------
    @RequestMapping("/loginFailure")
    public ModelAndView failure(@Valid @ModelAttribute Credentials credentials,
            BindingResult bindingResult, String originalUrl) {
        Assert.notNull(credentials);
        Assert.notNull(bindingResult);

        ModelAndView result;

        result = new ModelAndView("security/login_inview");
        result.addObject("credentials", credentials);
        result.addObject("showError", true);
        result.addObject("inview", true);
        result.addObject("originalRequestUrl", originalUrl);

        return result;
    }

}
