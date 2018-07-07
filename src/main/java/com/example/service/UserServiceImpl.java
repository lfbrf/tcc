package com.example.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		Set<Role> roles = new HashSet<Role>();
		List<Role> ro = roleRepository.findAll();
		Role r = roleRepository.findByRole("USER");
		for (Role s : ro) {
			if (s.getRole().equals("USER")) {
				roles.add(s);
				user.setActive(1);
			}
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		System.out.println("EIIIIiOIIIIIOOOOO");
		user.setRoles(roles);
		if (user.getActive() == 1)
			userRepository.save(user);
	}

	@Override
	public void saveManager(User user) {
		Set<Role> roles = new HashSet<Role>();
		List<Role> ro = roleRepository.findAll();
		Role r = roleRepository.findByRole("MANAGER");
		for (Role s : ro) {
			if (s.getRole().equals("MANAGER")) {
				roles.add(s);
				user.setActive(1);
			}
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(roles);
		if (user.getActive() == 1)
			userRepository.save(user);
	}

}
