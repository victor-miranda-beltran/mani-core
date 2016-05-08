package com.victormiranda.mani.core.security;

import org.springframework.security.core.userdetails.UserDetails;

public final class TokenUtils {

	private TokenUtils() {}

	public static final String X_AUTH_TOKEN = "x-auth-token";

	public static boolean validateToken(String authToken, UserDetails user) {
		return authToken != null;
	}

	public static String getUserFromToken(final String authToken) {
		if (authToken == null) {
			return null;
		}

		return "victor";
	}
}
