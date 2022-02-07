package com.farmaciasperuanas.pmmli.provider.config;

import com.farmaciasperuanas.pmmli.provider.entity.UserAccess;
import com.farmaciasperuanas.pmmli.provider.repository.UserAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserAccessRepository userAccessRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final UserAccess user = userAccessRepository.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User '" + username + "' not found");
            }

            UserDetails res =  org.springframework.security.core.userdetails.User//
                    .withUsername(username)//0
                    .password(user.getPassword())//
                    .authorities(user.getProfileUser().getName())//
                    .accountExpired(false)//
                    .accountLocked(false)//
                    .credentialsExpired(false)//
                    .disabled(false)//
                    .build();
            return res;
        } catch ( Exception e) {
            return null;
        }
    }

}
