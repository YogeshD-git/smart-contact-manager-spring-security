package com.smartcontactmanager.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smartcontactmanager.Entities.Contact;
import com.smartcontactmanager.Entities.User;
import com.smartcontactmanager.Helper.Message;
import com.smartcontactmanager.Repository.ContactRepository;
import com.smartcontactmanager.Repository.UserRepository;

import net.bytebuddy.asm.Advice.This;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	///
	/// method to adding comman data to response
	///
	@ModelAttribute
	public void addCommanData(Model model, Principal principal) {
		String username = principal.getName();

		// get user by username(email)
		User user = userRepository.getUserByUserName(username);
		model.addAttribute("user", user);
	}

	///
	/// Show dashboard home page
	///
	@RequestMapping("/dashboard")
	public String userDashboard(Model model, Principal principal) {
		String username = principal.getName();
		// get user by username(email)
		User user = userRepository.getUserByUserName(username);
		model.addAttribute("title", user.getUser_name().substring(0, user.getUser_name().lastIndexOf(' ')).toUpperCase() + "'s Dashboard");
		return "user/dashboard";
	}

	///
	/// Show form add contact
	///
	@GetMapping("/add-contacts")
	public String AddContactsForm(Model model) {
		model.addAttribute("title", "Add contacts");
		model.addAttribute("contact", new Contact());
		return "user/add-contacts";
	}

	///
	/// process add contact
	///
	@PostMapping("/process-addcontact")
	public String ProcessAddContact(@ModelAttribute Contact contact, @RequestParam("imgUrl") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			String username = principal.getName();
			User user = userRepository.getUserByUserName(username);

			// processing and uploading logic of profile image
			if (file.isEmpty()) {
				contact.setCon_imgUrl("default.png");
			} else {
				contact.setCon_imgUrl(file.getOriginalFilename());
				// where to upload (Path)
				File filePath = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(filePath.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}
			contact.setUser(user);
			user.getUser_Contacts().add(contact);
			userRepository.save(user);
//			if(3>2)
//			{
//				throw new Exception();
//			}
			session.setAttribute("message", new Message("success", "Contact Saved!"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("danger", "Ooops, Something went wrong while adding contact!"));
		}

		return "user/add-contacts";
	}

	///
	/// Show all contacts of user
	///
	@GetMapping("/view-contacts/{page}")
	public String showAllContacts(@PathVariable("page") int page, Model model, Principal principal) {
		model.addAttribute("title", "View Contacts");
		String username = principal.getName();

		User user = userRepository.getUserByUserName(username);
		// List<Contact> contactExist = user.getUser_Contacts();

		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contactPages = this.contactRepository.findContactByUser(user.getUser_id(), pageable);
		// model.addAttribute("contactExist",contactExist);
		model.addAttribute("contacts", contactPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("TotalPages", contactPages.getTotalPages());

		return "user/view-contacts";
	}

	///
	/// To Show Specific Contact
	///
	@GetMapping("/view-contactDetail/{conid}")
	public String showSpecificContact(@PathVariable("conid") int contactID, Model model, Principal principal) {

		String username = principal.getName();

		User user = userRepository.getUserByUserName(username);

		Optional<Contact> contactDetailOptnl = this.contactRepository.findById(contactID);
		Contact contact = contactDetailOptnl.get();
		// authenticate before sending data
		if (user.getUser_id() == contact.getUser().getUser_id()) {
			model.addAttribute("title", contact.getCon_name());
			model.addAttribute("contact", contact);
		}

		return "user/view-contactDetail";
	}

	///
	/// Delete Contact handler
	///
	@GetMapping("/delete-contact/{cid}")
	public String deleteContact(@PathVariable("cid") int contactID, Principal principal, Model model,
			HttpSession session) {

		String username = principal.getName();

		User user = userRepository.getUserByUserName(username);

		Optional<Contact> contactOptnl = this.contactRepository.findById(contactID);
		Contact contact = contactOptnl.get();

		try {
			// authenticate before sending data
			if (user.getUser_id() == contact.getUser().getUser_id()) {
				model.addAttribute("title", contact.getCon_name());

				this.contactRepository.delete(contact);

				// sometimes delete wont work
				// user.getUser_Contacts().remove(contact);
				// override equals method in entity contact
				// this.userRepository.save(user);
				// and on user entity's contact list and orphan true

				session.setAttribute("message", new Message("success", "Contact deleted successfully!"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "redirect:/user/view-contacts/0";
	}

	///
	/// Open Edit contact form
	///
	@PostMapping("/edit-contact/{cid}")
	public String openEditForm(@PathVariable("cid") int cid, Model model) {

		model.addAttribute("title", "Edit Contact");
		Contact contact = this.contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		return "user/edit-contact";
	}

	///
	/// process Edit contact
	///
	@PostMapping("/process-editcontact")
	public String processEditContact(@ModelAttribute Contact contact, @RequestParam("imgUrl") MultipartFile newImgFile,
			Model model, HttpSession session, Principal principal) {
		// get old contact detail
		Contact oldConDetail = this.contactRepository.findById(contact.getCon_id()).get();

		try {
			String username = principal.getName();
			User user = this.userRepository.getUserByUserName(username);

			if (!newImgFile.isEmpty()) {
				// delete old file before updating
//				File deleteFilePath = new ClassPathResource("static/image").getFile();
//				File fileToDelete = new File(deleteFilePath,oldConDetail.getCon_imgUrl()); 
//				fileToDelete.delete();

				File filePath = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(filePath.getAbsolutePath() + File.separator + newImgFile.getOriginalFilename());
				Files.copy(newImgFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setCon_imgUrl(newImgFile.getOriginalFilename());
			} else {
				// if img is empty then set old con img
				contact.setCon_imgUrl(oldConDetail.getCon_imgUrl());
			}
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("success", "Your contact details updated successfully!"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/user/view-contactDetail/" + contact.getCon_id();
	}

	///
	// User Profile handler
	///
	@GetMapping("/user-profile")
	public String userProfileHandler(Model model) {
		model.addAttribute("title", "Your Profile");
		return "user/user-profile";

	}

	///
	// Setting
	///
	@GetMapping("/setting")
	public String openSetting(Model model) {
		model.addAttribute("title", "User Setting");
		return "user/setting";

	}

	///
	// change password
	///
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			Principal principal,
			HttpSession session) {
		
		String currentLoggedUser = principal.getName();
		User currenUser = this.userRepository.getUserByUserName(currentLoggedUser);
		
		if(bCryptPasswordEncoder.matches(oldPassword, currenUser.getUser_password()))
		{
			currenUser.setUser_password(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currenUser);
			session.setAttribute("message", new Message("success", "Password successfully changed!"));
		}
		else
		{
			session.setAttribute("message", new Message("danger", "Password not matched with old password"));
			return "redirect:/user/setting";
			
		}
		return "redirect:/user/dashboard";

	}

}
