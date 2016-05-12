package com.victormiranda.mani.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {

	@Autowired
	private UserDetailsService userService;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
		final String authToken = this.extractAuthTokenFromRequest(httpRequest);

		logger.info("URL = " + httpRequest.getRequestURL());

		if (StringUtils.isEmpty(authToken)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		final String username = TokenUtils.getUserFromToken(authToken);

		if (username != null) {
			final UserDetails user;
			try {
				user = this.userService.loadUserByUsername(username);
			} catch (Exception e) {
				filterChain.doFilter(servletRequest, servletResponse);
				logger.warn(e);
				return;
			}

			if (TokenUtils.validateToken(authToken, user)) {
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
		return httpRequest.getHeader(TokenUtils.X_AUTH_TOKEN);
	}

}
