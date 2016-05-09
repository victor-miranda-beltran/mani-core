package com.victormiranda.mani.core.service.user;

import com.victormiranda.mani.core.dao.user.UserDao;
import com.victormiranda.mani.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserDao userDao;

	@Autowired
	public UserServiceImpl(final UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userDao.findByName(username);

		if (user == null) {
			throw new UsernameNotFoundException("username not found");
		}

		return user;
	}

	@Override
	public Optional<User> getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return Optional.of(userDao.findByName(auth.getName()));
	}

	@Override
	public Optional<Integer> getCurrentUserId() {
		return getCurrentUser().map( u -> u.getId());
	}
}
