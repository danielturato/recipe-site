package com.danielturato.recipe.core;

import com.danielturato.recipe.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = new User(annotation.username(), "password", new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoles()));
        context.setAuthentication(auth);

        return context;
    }
}
