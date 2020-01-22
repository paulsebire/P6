package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
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

    @Autowired
    private IClimbMetier iClimbMetier;


    /**
     * method used to log-in an user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            iClimbMetier.logger().info("connexion de l'utilisateur"+SecurityContextHolder.getContext().getAuthentication().getName());
            return new ModelAndView("redirect:/site/search");
        }
        return new ModelAndView("login");
    }

    /**
     * method used to log-out an user
     * @param request
     * @param response
     * @return to home page
     */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            iClimbMetier.logger().info("L'utilisateur "+SecurityContextHolder.getContext().getAuthentication().getName()+" s'est déconnecté");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home";
    }
}
