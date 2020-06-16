package com.cargohub.security;

import com.cargohub.entities.UserEntity;
import com.cargohub.repositories.UserRepository;
import com.cargohub.security.jwt.JwtUser;
import com.cargohub.security.jwt.JwtUserFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        JwtUser jwtUser = JwtUserFactory.create(user);

        return jwtUser;
    }
}
