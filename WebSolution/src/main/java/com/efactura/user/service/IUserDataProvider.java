package com.efactura.user.service;

import org.springframework.data.repository.CrudRepository;

import com.efactura.user.model.UserEntity;

public interface IUserDataProvider extends CrudRepository<UserEntity, String> {

	UserEntity findByEmail(String email);
	
}