package com.ssafy.gallery.config.auth;

import com.ssafy.gallery.user.model.Users;
import com.ssafy.gallery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        Users user = userRepository.findByUsername(username);

        // session.setAttribute("loginUser", user);
        return new PrincipalDetails(user);
    }
}