package com.smartcontactmanager.Configuartion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontactmanager.Entities.User;
import com.smartcontactmanager.Repository.UserRepository;

public class CustomUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repository.getUserByUserName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found!");
		}
		CustomUserDetails customUserDetails= new CustomUserDetails(user);
		return customUserDetails;
	}

}
