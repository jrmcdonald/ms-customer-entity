package com.jrmcdonald.ext.spring.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

    Authentication getAuthentication();

    String getCustomerId();
}
