package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    /**
     * method used to log-in an user
     * @return a page depending on success of authentication
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            logger.info("connexion de l'utilisateur"+SecurityContextHolder.getContext().getAuthentication().getName());
            return new ModelAndView("redirect:/site/search");
        }
        return new ModelAndView("login");
    }

    /**
     * method used to log-out an user
     * @param request request  from http  server
     * @param response response from http server
     * @return to home page
     */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            logger.info("L'utilisateur "+SecurityContextHolder.getContext().getAuthentication().getName()+" s'est déconnecté");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home";
    }
}
