package com.petstore.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.petstore.models.ApiResponse;
import com.petstore.models.auth.UserCredential;
import com.petstore.models.auth.UserPrincipal;
import com.petstore.servlet.exception.AuthenticationFailedException;

@RestController
public class AuthController {

	private static final HashMap<String, Set<String>> USER_ROLES = new HashMap<>();

	static {
		USER_ROLES.put("user", new HashSet<>(Arrays.asList("USER")));
		USER_ROLES.put("admin", new HashSet<>(Arrays.asList("USER", "ADMIN")));
	}

	@Autowired
	HttpServletRequest httpRequest;

	@RequestMapping(path = "/logout")
	public ApiResponse<Void> logoutUser() throws ServletException {
		httpRequest.logout();
		return new ApiResponse<Void>(HttpStatus.OK, "You have successfully logged out of the pet-store");
	}

	@RequestMapping(path = "/login", method = { RequestMethod.POST })
	public ApiResponse<UserPrincipal> loginUser(@RequestBody UserCredential userCred) throws ServletException {
		String username = userCred.getUsername();
		String password = userCred.getPassword();
		if (!USER_ROLES.containsKey(username) || !password.equalsIgnoreCase(username))
			throw new AuthenticationFailedException("Username/Password does not matches");
		return new ApiResponse<UserPrincipal>(HttpStatus.OK, "Success",
				new UserPrincipal(httpRequest.getSession(), username, USER_ROLES.get(username)));
	}
}
