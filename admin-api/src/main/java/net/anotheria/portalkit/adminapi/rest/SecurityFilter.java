package net.anotheria.portalkit.adminapi.rest;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.adminapi.api.auth.AdminAPIAuthenticationException;
import net.anotheria.portalkit.adminapi.api.auth.AdminAuthAPI;
import net.anotheria.portalkit.adminapi.rest.filter.auth.AuthError;
import net.anotheria.portalkit.adminapi.rest.filter.auth.AuthResult;
import net.anotheria.portalkit.adminapi.rest.filter.path.AuthPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Monitor(category = "filter")
public class SecurityFilter implements Filter {

    private AuthPaths authPaths;
    private AdminAuthAPI authAPI;

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.authPaths = AuthPaths.getDefaults();
        this.authAPI = APIFinder.findAPI(AdminAuthAPI.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest))
            return;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = req.getRequestURI();

        AuthPaths.Result requiredAuth = authPaths.authorizationRequired(path);
        if (requiredAuth == AuthPaths.Result.NO_AUTH) {
            filterChain.doFilter(req, response);
            return;
        }

        boolean optionalAuth = requiredAuth == AuthPaths.Result.AUTH_OPTIONAL;
        String httpAuthString = req.getHeader("authToken");

        AuthResult authResult = authorize(httpAuthString);
        if (!authResult.isAuthorized() && !optionalAuth) {
            response.sendError(authResult.getAuthError().getStatusCode(), authResult.getAuthError().getMsg());
            return;
        } else {
            APICallContext.getCallContext().setCurrentUserId(authResult.getLogin());
            APICallContext.getCallContext().setAttribute("AUTH_TOKEN", httpAuthString);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private AuthResult authorize(String authToken) {
        try {
            String login = authAPI.authenticateByToken(authToken);
            return new AuthResult(login);
        } catch (AdminAPIAuthenticationException e) {
            log.warn("Can't authenticate with token: <{}>", authToken, e);
            return new AuthResult(new AuthError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage()));
        }
    }

}
