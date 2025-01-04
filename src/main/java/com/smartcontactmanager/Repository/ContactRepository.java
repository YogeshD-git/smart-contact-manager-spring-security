package com.smartcontactmanager.Repository;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.Entities.Contact;
import com.smartcontactmanager.Entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("from Contact as c where c.user.user_id =:user_id")
	public Page<Contact> findContactByUser(@Param("user_id") int user_id,Pageable pageable);
	
	@Query(value="select * from contact c where c.con_name= :name AND c.user_user_id =:user_user_id", nativeQuery=true)
    public List<Contact> getContactByNameAndUser(@Param("name") String name,@Param("user_user_id") int user_user_id);
 
	
}
