package net.anotheria.portalkit.adminapi.rest.filter.path;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthPaths {

    /**
     * Default paths that don't need authorization.
     */
    private static final String[] DEFAULT_EXCLUSION_PATH = {
            "/api/v1/ping",
            "/api/v1/auth/login",
            "/api/v1/auth/thirdParty",
            "/api/v1/auth/loginByToken",
            "/api/v1/auth/loginByAdminToken", //TODO for what?
            "/api/v1/auth/passwordForgotten",
            "/api/v1/auth/passwordForgotten/confirmation",
            "/api/v1/auth/auto-login/send",
            "/api/v1/registration/register",
            "/api/v1/registration/npi/doctors",
            "/api/v1/countries",
            "/api/v1/registration/industries",
            "/api/v1/common/specialities",
            "/api/v1/registration/providers",
            "/api/v1/auth/confirmation/email",
            "/api/v1/auth/confirmation/newEmail",
            "/api/v1/media/",
            "/api/v1/common/healthcare",
            "/api/v1/common/profession/",
            "/api/v1/common/sendVerifyCode/",
            "/api/v1/common/checkVerifyCode/",
            "/api/v1/mock",
            "/api/v1/email/receive",
            "/api/v1/external-link/apple-app-site-association",
            "/api/v1/transcoding/started"
    };

    private static final List<String> DEFAULT_EXCLUSION_PATH_PREFIX = Arrays.asList("/api/v1/common/", "/api/v1/cms", "/api/v1/monitoring/request", "/api/v1/monitoring/mail", "/api/v1/external-link", "/api/v1/files/download");

    /**
     * Admin token path means that endpoint is allowed only with admin token.
     */
    private static final List<String> ADMIN_TOKEN_PATH_PREFIX = Arrays.asList("/api/v1/auth/account/step-2", "/api/v1/cache");

    /**
     * Optional auth path means, that endpoint is allowed for not logged user.
     */
    private static final String[] DEFAULT_OPTIONAL_AUTH_PATH = {};

    /**
     * Optional auth path means, that endpoint is allowed for not logged user.
     */
    private static final Pattern[] DEFAULT_OPTIONAL_AUTH_PATH_REGEX = {
            Pattern.compile("/api/v1/media/.+/download"),
            Pattern.compile("/api/v1/localization.+")
    };

    /**
     * Paths that don't need authorization.
     */
    private final ExcludedPaths excludedPaths;
    /**
     * Paths for which authorization is optional.
     */
    private final Set<String> optionalAuthPaths;
    /**
     * Path prefixes for which authorization is optional.
     */
    private final Set<Pattern> optionalAuthPathPrefixes;
    /**
     * Path prefixes that requires admin token.
     */
    private final Set<String> adminAuthPathsPrefixes;

    private AuthPaths(ExcludedPaths excludedPaths, Set<String> optionalAuthPaths, Set<Pattern> optionalAuthPathPrefixes, Set<String> adminAuthPathsPrefixes) {
        this.excludedPaths = excludedPaths;
        this.optionalAuthPaths = optionalAuthPaths;
        this.optionalAuthPathPrefixes = optionalAuthPathPrefixes;
        this.adminAuthPathsPrefixes = adminAuthPathsPrefixes;
    }

    public static AuthPaths getDefaults() {
        ExcludedPaths excludedPaths = new ExcludedPaths.Builder()
                .withExclusionPaths(Arrays.asList(DEFAULT_EXCLUSION_PATH))
                .withExclusionPathPrefixes(DEFAULT_EXCLUSION_PATH_PREFIX)
                .build();
        Set<String> optionalAuthPaths = Stream.of(DEFAULT_OPTIONAL_AUTH_PATH).collect(Collectors.toSet());
        Set<Pattern> optionalAuthPathPrefixes = Stream.of(DEFAULT_OPTIONAL_AUTH_PATH_REGEX).collect(Collectors.toSet());
        Set<String> adminAuthPaths = new HashSet<>(ADMIN_TOKEN_PATH_PREFIX);
        return new AuthPaths(excludedPaths, optionalAuthPaths, optionalAuthPathPrefixes, adminAuthPaths);
    }

    /**
     * Check is given path required authorization
     *
     * @param path path
     * @return {@link Result}
     */
    public Result authorizationRequired(String path) {
        if (excludedPaths.checkExcluded(path)) {
            return Result.NO_AUTH;
        }

        if (isOptionalAuthPath(path)) {
            return Result.AUTH_OPTIONAL;
        }

        return Result.AUTH_REQUIRED;
    }

    private boolean isOptionalAuthPath(String path) {
        if (optionalAuthPaths.contains(path)) {
            return true;
        }

        for (Pattern pattern : optionalAuthPathPrefixes) {
            if (pattern.matcher(path).matches()) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdminAuthPath(String path) {
        for (String adminAuthPathsPrefix : adminAuthPathsPrefixes) {
            return path.startsWith(adminAuthPathsPrefix);
        }
        return false;
    }

    public enum Result {

        AUTH_REQUIRED,
        NO_AUTH,

        /**
         * Optional means that same path can be used for logged in and not logged user.
         * Authorize user if you can.
         */
        AUTH_OPTIONAL
    }

    public static class Builder {
        private final Set<String> exclusionPaths = new HashSet<>();
        private final Set<String> exclusionPathPrefixes = new HashSet<>();
        private final Set<String> optionalAuthPaths = new HashSet<>();
        private final Set<Pattern> optionalAuthPathPrefixes = new HashSet<>();
        private final Set<String> adminAuthPaths = new HashSet<>();

        public Builder withExclusionPaths(Collection<String> exclusionPaths) {
            this.exclusionPaths.addAll(exclusionPaths);
            return this;
        }

        public Builder withExclusionPathPrefixes(Collection<String> exclusionPathPrefixes) {
            this.exclusionPathPrefixes.addAll(exclusionPathPrefixes);
            return this;
        }

        public Builder withOptionalAuthPaths(Collection<String> optionalAuthPaths) {
            this.optionalAuthPaths.addAll(optionalAuthPaths);
            return this;
        }

        public Builder withOptionalAuthPathPrefixes(Collection<Pattern> optionalAuthPathPrefixes) {
            this.optionalAuthPathPrefixes.addAll(optionalAuthPathPrefixes);
            return this;
        }

        public Builder withAdminAuthPaths(Collection<String> adminAuthPaths) {
            this.adminAuthPaths.addAll(adminAuthPaths);
            return this;
        }

        public AuthPaths build() {
            ExcludedPaths excludedPaths = new ExcludedPaths.Builder()
                    .withExclusionPaths(exclusionPaths)
                    .withExclusionPathPrefixes(exclusionPathPrefixes)
                    .build();
            return new AuthPaths(excludedPaths, optionalAuthPaths, optionalAuthPathPrefixes, adminAuthPaths);
        }
    }
}
