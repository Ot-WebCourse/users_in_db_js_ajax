package users.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import users.models.Post;
import users.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import users.repositories.UserRepository;


@Service
public class UsersService {
	
	@Autowired
	private UserRepository usersRepo;
	
	private List<User> users = new ArrayList<User>();
	
	@PostConstruct
	@Transactional
	public void createAdminUser() {	
		User userAdmin=usersRepo.findByLogin("admin");
		if(userAdmin==null){
			register("admin", "admin@mail.com", "qwerty");
		}
	}

    @Transactional(readOnly = true)
	public List<User> getUsers() {
		//return users; 
		return (List<User>) usersRepo.findAll(); 
	}
	
	@Transactional
	public Boolean register(String login, String email, String pass) {
		String passHash = new BCryptPasswordEncoder().encode(pass);
		
		User u = new User(login, email.toLowerCase(), passHash);
		User saved = usersRepo.save(u);
		//if(users.add(u)){
		if(saved!=null){
			return true;
		}
		return false;
	}
	
    @Transactional(readOnly = true)
	 public User getUserByLogin(String login) {
	    	/*List<User> allUsers=getUsers();
	    	
	    	for (User u : allUsers){
	    		if(u.getLogin().equalsIgnoreCase(login))
	    		{
	    			return u;
	    		}
	         	
	    	}*/
		 	User u=usersRepo.findByLogin(login);
		 	
		 	if(u!=null){return u;}
	    	
		 	return null;
	     }
    	
    	@Transactional(readOnly = true)
	    public User login(String login, String password) {
	        
	    	//User user = getUserByLogin(login);
	    	User user=usersRepo.findByLogin(login);
	           
	    	// if(user == null || !user.getPassword().equals(password)) { //without hash
	        if(user==null || !( new BCryptPasswordEncoder().matches(password, user.getPassword()) ) ){	 
	           	return null;
	        }
	            
	        return user;
	    }
	    
	    @Transactional
	    public void updateAvatar(String login,String filePath){
	    	User u=usersRepo.findByLogin(login); //select User from DB
    		u.setHasAvatar(true);
    		u.setAvatarSrc(filePath);
    		usersRepo.save(u); //update data in DB  about User
	    }

	    public String getUserLocation(String userAgent) {  
	    	 UserAgent userAgentStr = UserAgent.parseUserAgentString(userAgent);
	    	    Browser browser = userAgentStr.getBrowser();
	    	    String browserName = browser.getName();
	    	    //or 
	    	    // String browserName = browser.getGroup().getName();
	    	    Version browserVersion = userAgentStr.getBrowserVersion();
	    	    OperatingSystem os =  userAgentStr.getOperatingSystem();
	    	    String[] osArr=os.toString().split("_");
	    	    return "The user is using browser " + browserName + " - version " + browserVersion + ". Operating System - " + osArr[0] + " " + osArr[1];
	    	    //System.out.println("The user is using browser " + browserName + " - version " + browserVersion);
	            
	    }
	
	    @Transactional(readOnly = true)
		public boolean userExists(String login) {
			return usersRepo.findByLogin(login) != null;
		}
	
}
