package com.smartcontactmanager.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontactmanager.Entities.Contact;
import com.smartcontactmanager.Entities.User;
import com.smartcontactmanager.Helper.Message;
import com.smartcontactmanager.Repository.UserRepository;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping(value={"/","/home"})
	public String Main(Model model) {
		 model.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	
	@GetMapping("/about")
	public String About(Model model) {
		 model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		 model.addAttribute("title","Signup - Smart Contact Manager");
		 model.addAttribute("user",new User());
		return "signup";
	}
	@GetMapping("/signin")
	public String signin(Model model) {
		 model.addAttribute("title","Signin - Smart Contact Manager");
		return "signin";
	}
	@PostMapping("/signupaction")
	public String signupaction(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam(value="agreement",defaultValue = "false") Boolean agreement,
			Model model,
			HttpSession session)
	{
		try {
			if(!agreement)
			{
				//System.out.println("You should agreed to our terms and condition!");
				throw new Exception("You must agreed to our terms and condition before proceed!");
			}
			else
			{
				if(result.hasErrors())
				{
					System.out.println("errors ="+result.toString());
					model.addAttribute("user",user);
					return "signup";
				}
					
				user.setUser_role("ROLE_USER");
				user.setUser_status(true);
				user.setImageUrl("default.png");
				user.setUser_password(passwordEncoder.encode(user.getUser_password()));
				userRepository.save(user);
				
				model.addAttribute("user",new User());
				session.setAttribute("message", new Message("alert-success", "Successfully registered!"));
				return "redirect:/signin";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("alert-danger", "Oops! "+e.getMessage()));
			return "signup";
		}
		
	}

}
