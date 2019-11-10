package com.huayou.samlboot2.spring.mvc;

/**
 * @author
 */
import com.huayou.samlboot2.spring.security.SAMLUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping("/home")
    public ModelAndView home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth==null){
            return new ModelAndView("login");
        }
        System.out.println("auth : "+auth.toString());
        ModelAndView homeView = new ModelAndView("home");
        if (SAMLAuthenticationToken.class.isAssignableFrom(auth.getClass())){
            SAMLUserDetails user = (SAMLUserDetails) auth.getPrincipal();
            homeView.addObject("userId", user.getUsername());
            homeView.addObject("samlAttributes", user.getAttributes());
        }
        return new ModelAndView("home");
    }
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute( "loginError"  , true);
        return "login";
    }

}
