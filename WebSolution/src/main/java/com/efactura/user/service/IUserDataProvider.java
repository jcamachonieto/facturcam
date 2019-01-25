package com.efactura.user.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.efactura.user.model.UserEntity;

@Repository
public interface IUserDataProvider extends CrudRepository<UserEntity, String> {

	UserEntity findByEmail(String email);
	
}