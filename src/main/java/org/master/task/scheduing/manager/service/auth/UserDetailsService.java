package org.master.task.scheduing.manager.service.auth;

import jakarta.annotation.Resource;
import org.master.task.scheduing.manager.dao.impl.auth.UserRepository;
import org.master.task.scheduing.manager.entity.auth.Permission;
import org.master.task.scheduing.manager.entity.auth.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Resource
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<Permission> permissions = new ArrayList<>();
        user.getPermissions().stream().map(permissions::add);
        user.getRole().getPermissions().stream().map(permissions::add);
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermissionCode())).collect(Collectors.toList())
        );
    }
}
