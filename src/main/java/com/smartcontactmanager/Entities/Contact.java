package com.smartcontactmanager.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.servlet.annotation.MultipartConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class Contact {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int con_id;
	private String con_name;
	private String con_nickName;
	private String con_email;
	private String con_phone;
	private String con_imgUrl;
	@Column(length = 500)
	private String con_desc;
	
	//declaring user(Many to One.. many contact can have one user. vice vers)
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public int getCon_id() {
		return con_id;
	}

	public void setCon_id(int con_id) {
		this.con_id = con_id;
	}

	public String getCon_name() {
		return con_name;
	}

	public void setCon_name(String con_name) {
		this.con_name = con_name;
	}

	public String getCon_nickName() {
		return con_nickName;
	}

	public void setCon_nickName(String con_nickName) {
		this.con_nickName = con_nickName;
	}

	public String getCon_email() {
		return con_email;
	}

	public void setCon_email(String con_email) {
		this.con_email = con_email;
	}

	public String getCon_phone() {
		return con_phone;
	}

	public void setCon_phone(String con_phone) {
		this.con_phone = con_phone;
	}

	public String getCon_imgUrl() {
		return con_imgUrl;
	}

	public void setCon_imgUrl(String con_imgUrl) {
		this.con_imgUrl = con_imgUrl;
	}

	public String getCon_desc() {
		return con_desc;
	}

	public void setCon_desc(String con_desc) {
		this.con_desc = con_desc;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Contact [con_id=" + con_id + ", con_name=" + con_name + ", con_nickName=" + con_nickName
				+ ", con_email=" + con_email + ", con_phone=" + con_phone + ", con_imgUrl=" + con_imgUrl + ", con_desc="
				+ con_desc + ", user=" + user + "]";
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.con_id==((Contact)obj).getCon_id();
	}

	
	
	
}
