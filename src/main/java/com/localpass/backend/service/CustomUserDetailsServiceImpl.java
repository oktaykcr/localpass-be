package com.localpass.backend.service;

import com.localpass.backend.model.user.Role;
import com.localpass.backend.model.user.RoleEnum;
import com.localpass.backend.model.user.User;
import com.localpass.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("login failed");
            // TODO: USER NOT FOUND EXCEPTION
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    public User save(User user){
        User registeredUser = new User();
        registeredUser.setUsername(user.getUsername());
        registeredUser.setPassword(passwordEncoder.encode(user.getPassword()));
        registeredUser.setEnabled(true);
        registeredUser.setRoles(Arrays.asList(new Role(RoleEnum.USER.getRole())));
        return userRepository.save(registeredUser);
    }

    private Set<GrantedAuthority> getAuthorities(User user){
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
