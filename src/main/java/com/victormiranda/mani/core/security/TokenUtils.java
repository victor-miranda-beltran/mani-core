package com.victormiranda.mani.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public final class TokenUtils {

	private TokenUtils() {}

	public static final String X_AUTH_TOKEN = "x-auth-token";

	private static final String MAGIC_KEY = "]#[;'/changemetoenvvariable#/[[";

	public static boolean validateToken(final String authToken, final UserDetails user) {
		final String[] parts = authToken.split(":");
		final String signature = parts[1];

		return signature.equals(getSignature(user));
	}

	public static String getUserFromToken(String authToken) {
		if (null == authToken) {
			return null;
		}

		String[] parts = authToken.split(":");
		return parts[0];
	}

	public static String createToken(final UserDetails actualUserDetails) {
		final StringBuilder tokenBuilder = new StringBuilder();

		tokenBuilder.append(actualUserDetails.getUsername());
		tokenBuilder.append(":");
		tokenBuilder.append(getSignature(actualUserDetails));
		tokenBuilder.append(":");
		tokenBuilder.append(encrypt(Integer.toString(new Random().nextInt())));

		return tokenBuilder.toString();
	}

	private static String getSignature(UserDetails userDetails) {
		final StringBuilder signatureBuilder = new StringBuilder();

		signatureBuilder.append(userDetails.getUsername());
		signatureBuilder.append(":");
		signatureBuilder.append(userDetails.getPassword());
		signatureBuilder.append(":");
		signatureBuilder.append(TokenUtils.MAGIC_KEY);

		return encrypt(signatureBuilder.toString());
	}

	private static String encrypt(String plainText) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("sha");
			return new String(Hex.encode(digest.digest(plainText.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No sha algorithm available");
		}
	}

}
