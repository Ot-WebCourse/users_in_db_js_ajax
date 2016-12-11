package users;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import users.models.Post;
import users.models.RegistrationForm;
import users.models.User;
import users.services.PostsService;
import users.services.UsersService;

@Controller
public class LoginController {

	@Autowired
	private UsersService usersService;
	
 

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(HttpSession session) {
    	Object l = session.getAttribute("userLogin");
        if(l!=null){
        	return "redirect:user-profile/"+l;
        }
    	return "login";
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
    	session.removeValue("userLogin");
        //session.invalidate();
    	
    	return "redirect:/login";
    }
    
   
          
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("login") String login,@RequestParam("password") String password, Model model, HttpSession session) {
        User user=usersService.login(login, password);
      
        if(user!=null){
        	//session.setAttribute("userId", userId);
        	session.setAttribute("userLogin", login);
        	
        	return "redirect:user-profile/"+login;
        	
        }
        else{
        	model.addAttribute("error_msg","Wrong login or password!");
        	
        	return "login";
        }

    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String regForm(RegistrationForm formData, Model model) {
    	model.addAttribute("form", formData);
    	
    	return "register";
    }
    
     	@RequestMapping(value = "/register", method = RequestMethod.POST)
public String register(@Valid RegistrationForm formData, BindingResult bindResult, Model model, HttpSession session) {
	
	if(bindResult.hasErrors()) {
		model.addAttribute("errors", bindResult.getFieldErrors());
		model.addAttribute("form", formData);
		return "register"; //registration form
	}
	

	Boolean registered=usersService.register(formData.getLogin(), formData.getEmail(), formData.getPass());
	if(registered){
		session.setAttribute("userLogin", formData.getLogin());

    	return "redirect:user-profile/"+formData.getLogin();
	}
	
	return "register"; //registration form
}

     	

}
