package com.spring.login.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.login.application.appuser.AppUser;
import com.spring.login.application.registration.token.ConfirmationToken;
import com.spring.login.application.registration.token.ConfirmationTokenService;
import com.spring.login.application.repository.AppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

	private final AppUserRepository appUserRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	private final static String USER_NOT_FOUND_MSG = "user with name %s is not found";

	@Override
	public UserDetails loadUserByUsername(String email) {
		// TODO Auto-generated method stub
		return appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
	}
	
	public String signUpUser(AppUser appUser) {
		boolean userExists=appUserRepository.findByEmail(appUser.getEmail()).isPresent();
		if(userExists) {
			throw new IllegalStateException("email already taken");
		}
		
		String encodedPassword=bCryptPasswordEncoder.encode(appUser.getPassword());
		appUser.setPassword(encodedPassword);
		appUserRepository.save(appUser);
		
		String token=UUID.randomUUID().toString();
		//TODO: send confirmation token
		ConfirmationToken confirmationToken=new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15),
				appUser
				);
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		
//		TODO:send Email
		
		
		return token;
	}

	public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
