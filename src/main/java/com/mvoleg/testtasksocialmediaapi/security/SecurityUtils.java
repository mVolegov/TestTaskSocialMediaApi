package com.mvoleg.testtasksocialmediaapi.security;

import com.mvoleg.testtasksocialmediaapi.exception.auth.ForbiddenException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class SecurityUtils {

    @Component
    public class CheckForAuthentication implements BiConsumer<String, String> {

        @Override
        public void accept(String username, String errorMessage) {
            String authenticatedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!authenticatedUserUsername.equals(username)) {
                throw new ForbiddenException(errorMessage);
            }
        }
    }
}
