package net.anotheria.portalkit.adminapi.rest;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.adminapi.api.auth.provider.AuthProvider;
import net.anotheria.portalkit.adminapi.api.auth.provider.AuthProviderFactory;
import net.anotheria.portalkit.adminapi.rest.filter.auth.AuthResult;
import net.anotheria.portalkit.adminapi.rest.filter.path.AuthPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Monitor(category = "filter")
public class SecurityFilter implements Filter {

    private AuthPaths authPaths;
    private AuthProvider authorization;

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.authPaths = AuthPaths.getDefaults();
        this.authorization = AuthProviderFactory.getAuthProvider();
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

        AuthResult authResult = null;
        if (!authResult.isAuthorized() && !optionalAuth) {
            response.sendError(authResult.getAuthError().getStatusCode(), authResult.getAuthError().getMsg());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

}
