package com.spring.login.application.registration;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String>{

	@Override
	public boolean test(String t) {
		// TODO Regx to validate email
		return true;
	}

}
