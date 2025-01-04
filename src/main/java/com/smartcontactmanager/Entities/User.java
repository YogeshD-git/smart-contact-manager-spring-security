package com.smartcontactmanager.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;



@Entity
@Table(name="USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int user_id;
	
	@NotBlank(message="Name field required!")
	@Size(min = 2,max = 20,message="minimum 2 & maximum 20 charectors allowed!")
	private String user_name;
	
	@Column(unique = true)
	@NotBlank(message="Email field required!")
	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	private String user_email;
	
	@NotBlank(message="Password field required!")
	//@Size(min = 8,max = 20,message="minimum 8 & maximum 15 charectors allowed!")
	private String user_password;
	
	private String user_role;
	
	private boolean user_status;
	
	private String imageUrl;
	
	@Column(length = 250)
	@Size(max = 250,message="maximum 250 charectors allowed!")
	private String user_desc;
	
	//declaring list of contacts(One to Many)
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user",orphanRemoval = true)
	List<Contact> user_Contacts = new ArrayList<>();
	
	
	public List<Contact> getUser_Contacts() {
		return user_Contacts;
	}

	public void setUser_Contacts(List<Contact> user_Contacts) {
		this.user_Contacts = user_Contacts;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public boolean isUser_status() {
		return user_status;
	}
	public void setUser_status(boolean user_status) {
		this.user_status = user_status;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getUser_desc() {
		return user_desc;
	}
	public void setUser_desc(String user_desc) {
		this.user_desc = user_desc;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_name=" + user_name + ", user_email=" + user_email
				+ ", user_password=" + user_password + ", user_role=" + user_role + ", user_status=" + user_status
				+ ", imageUrl=" + imageUrl + ", user_desc=" + user_desc + ", user_Contacts=" + user_Contacts + "]";
	}
	
	
}
