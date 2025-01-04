package com.smartcontactmanager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontactmanager.Entities.Contact;
import com.smartcontactmanager.Entities.User;
import com.smartcontactmanager.Repository.ContactRepository;
import com.smartcontactmanager.Repository.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchHandler(@PathVariable("query") String keyword,Principal principal) {
		
		System.out.println("serched"+keyword);
		
		User user = this.userRepository.getUserByUserName(principal.getName());
		List<Contact> contact = this.contactRepository.getContactByNameAndUser(keyword,user.getUser_id());
		
		return ResponseEntity.ok(contact);
		
		
	}
}
