package com.app.pandemicnet.config;

import com.app.pandemicnet.model.User;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

@Component
public class UserIdAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
                                       RequestAuthorizationContext context) {
        Authentication authentication = authenticationSupplier.get();
        HttpServletRequest request = context.getRequest();

        String path = request.getRequestURI(); // e.g., /api/users/123
        String[] segments = path.split("/");
        String idFromPath = segments[segments.length - 1]; // assumes URL ends in {id}

        String currentUserId = String.valueOf(((User) authentication.getPrincipal()).getId()); // adjust to your user model

        boolean isAuthorized = idFromPath.equals(currentUserId);
        return new AuthorizationDecision(isAuthorized);
    }
}

