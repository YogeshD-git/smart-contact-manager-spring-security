package com.smartcontactmanager.Controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.Entities.User;
import com.smartcontactmanager.Helper.Message;
import com.smartcontactmanager.Repository.UserRepository;
import com.smartcontactmanager.service.EmailService;

@Controller
public class ForgotPasswordController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// generating OTP of 4 digit
	Random random = new Random(1000);

	@RequestMapping("/forgot-password")
	public String openVerfiyEmailForm() {

		return "forgot-password";
	}

	@PostMapping("/send-otp")
	public String sendAndVerifyOtp(@RequestParam("email") String email,
			HttpSession session) {
		User user = userRepository.getUserByUserName(email);
		if(user==null)
		{
			session.setAttribute("message", new Message("danger","User not exist in Smart Contact Manager database!"));
			return "forgot-password";
		}
		else
		{
			int otp = random.nextInt(999999);
			
			String subject="OTP from Smart Contact Manager";
			String message = ""
					+"<div style='border:1px solid #e2e2e2; padding:20px'>"
					+"<h1>"
					+"OTP is "
					+"<b>"+otp
					+"</b>"
					+"</h1>"
					+"</div>";
			String recipient=email;
			
			boolean flag = this.emailService.sendEmail(subject,message,recipient);
			
			if(flag)
			{
				session.setAttribute("myotp", otp);
				session.setAttribute("email", email); 
				return "verify-otp";
			}
			else
			{
				session.setAttribute("message", new Message("danger","Something went wrong!"));
				return "forgot-password";
			}
		}		
	}
	
	//verify-OTP
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") int OTP,HttpSession session) {
		
		int myotp=(int)session.getAttribute("myotp");
		String email = (String)session.getAttribute("email");
		if(OTP==myotp)
		{
			User user = userRepository.getUserByUserName(email);
			if(user==null)
			{
				session.setAttribute("message", new Message("danger", "Something went wrong!"));			
				return "forgot-password";
			}
			else
			{
				return "password-change";
			}			
		}
		else
		{
			session.setAttribute("message", new Message("danger", "You have entered wrong OTP. Please verify again!"));
			return "verify-otp";
		}
		
	}
	//change-forgot-password
	@PostMapping("/change-forgot-password")
	public String changePassowrd(@RequestParam("newPassword") String newPassword,HttpSession session) {
		String email = (String) session.getAttribute("email");
		User user = userRepository.getUserByUserName(email);
		if(user==null)
		{
			session.setAttribute("message", new Message("danger","User not exist in Smart Contact Manager database!"));
			return "forgot-password";
		}
		else
		{
			user.setUser_password(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(user);
			return "redirect:/signin?change=password changed successfully";
		}

	}

}
