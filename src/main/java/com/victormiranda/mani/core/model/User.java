package com.victormiranda.mani.core.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User implements UserDetails, ManiModel  {

	@Id
	private Integer id;

	private String name;

	private String password;

	@OneToMany(mappedBy = "user")
	private Set<BankLogin> bankLoginSet;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<BankLogin> getBankLoginSet() {
		return bankLoginSet;
	}

	public void setBankLoginSet(Set<BankLogin> bankLoginSet) {
		this.bankLoginSet = bankLoginSet;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Collection<GrantedAuthority> originalRoles = new HashSet<>();

		originalRoles.add(new SimpleGrantedAuthority("user"));
		return originalRoles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


}
