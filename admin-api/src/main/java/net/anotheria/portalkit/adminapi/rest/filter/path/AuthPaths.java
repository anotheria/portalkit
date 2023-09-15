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
            "/api/v1/admin-api/auth/login"
    };

    private static final List<String> DEFAULT_EXCLUSION_PATH_PREFIX = Collections.emptyList();

    /**
     * Optional auth path means, that endpoint is allowed for not logged user.
     */
    private static final String[] DEFAULT_OPTIONAL_AUTH_PATH = {};

    /**
     * Optional auth path means, that endpoint is allowed for not logged user.
     */
    private static final Pattern[] DEFAULT_OPTIONAL_AUTH_PATH_REGEX = {};

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

    private AuthPaths(ExcludedPaths excludedPaths, Set<String> optionalAuthPaths, Set<Pattern> optionalAuthPathPrefixes) {
        this.excludedPaths = excludedPaths;
        this.optionalAuthPaths = optionalAuthPaths;
        this.optionalAuthPathPrefixes = optionalAuthPathPrefixes;
    }

    public static AuthPaths getDefaults() {
        ExcludedPaths excludedPaths = new ExcludedPaths.Builder()
                .withExclusionPaths(Arrays.asList(DEFAULT_EXCLUSION_PATH))
                .withExclusionPathPrefixes(DEFAULT_EXCLUSION_PATH_PREFIX)
                .build();
        Set<String> optionalAuthPaths = Stream.of(DEFAULT_OPTIONAL_AUTH_PATH).collect(Collectors.toSet());
        Set<Pattern> optionalAuthPathPrefixes = Stream.of(DEFAULT_OPTIONAL_AUTH_PATH_REGEX).collect(Collectors.toSet());
        return new AuthPaths(excludedPaths, optionalAuthPaths, optionalAuthPathPrefixes);
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

    public enum Result {

        AUTH_REQUIRED,
        NO_AUTH,

        /**
         * Optional means that same path can be used for logged in and not logged user.
         * Authorize user if you can.
         */
        AUTH_OPTIONAL
    }
}
