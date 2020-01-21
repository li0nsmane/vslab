package hska.iwi.eShopMaster.controller;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import hska.iwi.eShopMaster.controller.manager.UserManagerImpl;
import hska.iwi.eShopMaster.controller.oauth.Oauth;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.database.dataobjects.UserRegistration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

public class RegisterAction extends ActionSupport {

    /**
     *
     */
    private static final long serialVersionUID = 3655805600703279195L;
    private String username;
    private String password1;
    private String password2;
    private String firstname;
    private String lastname;
    
    private Role role = null;
    
    @Override
    public String execute() throws Exception {
        
        // Return string:
        String result = "input";

        UserManagerImpl userManager = new UserManagerImpl();

   		this.role = userManager.getRoleByLevel(1); // 1 -> regular User, 2-> Admin
        if ((this.username == null) || (this.firstname == null) || (this.lastname == null) || (this.password1 == null) || (this.password2 == null)
                || this.password1.equals(this.password2)) {
            addActionError(getText("error.register.invalid"));
        }
   		if (!userManager.doesUserAlreadyExist(this.username)) {
    		    	
	        // save it to database
	        userManager.registerUser(this.username, this.firstname, this.lastname, this.password1, this.password2);
	            // User has been saved successfully to databse:
	        	addActionMessage("user registered, please login");
	        	addActionError("user registered, please login");
				Map<String, Object> session = ActionContext.getContext().getSession();
				session.put("message", "user registered, please login");
	            result = "success";
	        
    	}
    	else {
    		addActionError(getText("error.username.alreadyInUse"));
    	}
        System.out.println(result);
        return result;

    }
    
	@Override
	public void validate() {
		if (getFirstname().length() == 0) {
			addActionError(getText("error.firstname.required"));
		}
		if (getLastname().length() == 0) {
			addActionError(getText("error.lastname.required"));
		}
		if (getUsername().length() == 0) {
			addActionError(getText("error.username.required"));
		}
		if (getPassword1().length() == 0) {
			addActionError(getText("error.password.required"));
		}
		if (getPassword2().length() == 0) {
			addActionError(getText("error.password.required"));
		}
		
		if (!getPassword1().equals(getPassword2())) {
			addActionError(getText("error.password.notEqual"));
		}
	}

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return (this.username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword1() {
        return (this.password1);
    }

    public void setPassword1(String password) {
        this.password1 = password;
    }
    
    public String getPassword2() {
        return (this.password2);
    }

    public void setPassword2(String password) {
        this.password2 = password;
    }
    
    public Role getRole() {
        return (this.role);
    }
    
    public void setRole(Role role) {
        this.role = role;
    }

}
