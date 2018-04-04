package by.issoft.service.impl;

import by.issoft.entity.UserData;
import by.issoft.entity.UserRoleEnum;
import by.issoft.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service(value = "userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDataService userDataService;

    @Autowired
    public UserDetailsServiceImpl(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserData userData = userDataService.findUserDataByLogin(login);
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(UserRoleEnum.USER.name()));
        return new User(userData.getUserLogin(), userData.getUserPassword(), roles);
    }
}
