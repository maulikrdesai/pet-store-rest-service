package com.petstore.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petstore.models.ApiResponse;

@RestController
public class logout {

	@Autowired
	HttpServletRequest httpRequest;

	@RequestMapping(path = "/logout")
	public ApiResponse<Void> logoutUser() throws ServletException {
		httpRequest.logout();
		return new ApiResponse<Void>(HttpStatus.OK, "You have successfully logged out of the pet-store");
	}
}
