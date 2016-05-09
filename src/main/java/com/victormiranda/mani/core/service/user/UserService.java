package com.victormiranda.mani.core.service.user;

import com.victormiranda.mani.core.model.User;

import java.util.Optional;

public interface UserService {

	Optional<User> getCurrentUser();
	Optional<Integer> getCurrentUserId();
}
