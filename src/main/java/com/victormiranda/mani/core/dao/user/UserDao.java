package com.victormiranda.mani.core.dao.user;

import com.victormiranda.mani.core.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {

	User findByName(final String name);
}
