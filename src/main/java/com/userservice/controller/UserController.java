package com.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.dto.request.RegisterRequest;
import com.userservice.dto.response.UserResponse;
import com.userservice.entity.User;
import com.userservice.response.APIResponse;
import com.userservice.security.CustomUserDetails;
import com.userservice.service.UserService;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		
		this.userService=userService;
	}

	
	
	@PostMapping("/register")
	public ResponseEntity<APIResponse<UserResponse>> register(@RequestBody RegisterRequest request){
		
		
		User savedUser=userService.register(request);
		
		
		UserResponse userResponse=new UserResponse(
				savedUser.getId(),
				savedUser.getName(),
				savedUser.getEmail(),
				savedUser.getCreatedAt());
		APIResponse<UserResponse>	response = APIResponse.success("user registered successfully", userResponse);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
		
	}
	
	

	    @GetMapping("/profile")
	    public ResponseEntity<String> profile(
	            @AuthenticationPrincipal CustomUserDetails userDetails) {

	        return ResponseEntity.ok(
	                "Welcome " + userDetails.getUsername()
	        );
	    }
	
	
	
	
	
	
	
	
	
}
