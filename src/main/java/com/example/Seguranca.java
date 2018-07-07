package com.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Component
public class Seguranca implements UserDetailsService {

	@Autowired
	private UserRepository userrep;

	@Autowired
	private RoleRepository rolerep;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User usuario = userrep.findByEmail(email);
		System.out.println("------" + usuario.getName());
		System.out.println("------" + usuario.getLastName());
		System.out.println("------" + usuario.getPassword());
		System.out.println("------" + authorities(usuario));
		UserDetailsBuilder builder = null;

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}

		// System.out.println("EPAAAAAAA" + my.getPassword());
		// return new MyUserPrincipal(usuario);
		return new UsuarioSistema(usuario.getName(), usuario.getEmail(), usuario.getPassword(), authorities(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(User usuario) {
		return authorities(rolerep.findByUsuariosIn(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(List<Role> roles) {
		Collection<GrantedAuthority> auths = new ArrayList<>();
		for (Role role : roles) {
			auths.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
		}
		return auths;
	}
}