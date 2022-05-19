package com.elte.jfirbj.backend.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.elte.jfirbj.backend.models.enums.RoleEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(	name = "users")
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;
	
	@NotBlank
	@Size(max = 20)
	private String firstName;
	
	@NotBlank
	@Size(max = 20)
	private String lastName;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	@JsonIgnore
	private String password;

	@JsonIgnore
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<RoleEnum> roles;

	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationTime;
	
	@Column
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	public User(UserBuilder userBuilder) {
		this.username = userBuilder.userName;
		this.firstName = userBuilder.firstName;
		this.lastName = userBuilder.lastName;
		this.email = userBuilder.email;
		this.password = userBuilder.password;
	}

	public static class UserBuilder
	{
		private String userName;
		private String firstName;
		private String lastName;
		private String email;
		private String password;

		public UserBuilder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public UserBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public UserBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public UserBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}

		public User build() {
			User user =  new User(this);
			return user;
		}
	}
}
