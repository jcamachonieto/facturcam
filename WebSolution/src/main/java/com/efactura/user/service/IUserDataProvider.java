package com.efactura.user.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efactura.user.model.UserEntity;

@Repository
public interface IUserDataProvider extends JpaRepository<UserEntity, String> {

	UserEntity findByEmail(String email);
	
}