package com.victormiranda.mani.core.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public final class TokenUtils {

	public static final String X_AUTH_TOKEN = "x-auth-token";

	public static boolean validateToken(String authToken, UserDetails user) {
		return true;
	}

	public static String getUserFromToken(String authToken) {
		return "victor";
	}
}
