package com.elte.jfirbj.backend.payload.response;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse {
	private String accessToken;
	private final String type = "Bearer";
	private Long id;
	private String username;
	private String email;
	private List<String> roles;

	public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
		this.accessToken = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
}
