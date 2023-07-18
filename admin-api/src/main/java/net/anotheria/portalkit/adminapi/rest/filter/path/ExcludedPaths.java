package net.anotheria.portalkit.adminapi.rest.filter.path;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ExcludedPaths {

    private final Set<String> exclusionPaths;
    private final Set<String> exclusionPathPrefixes;

    public ExcludedPaths(Set<String> exclusionPaths, Set<String> exclusionPathPrefixes) {
        this.exclusionPaths = exclusionPaths;
        this.exclusionPathPrefixes = exclusionPathPrefixes;
    }

    /**
     * Check is given path required authorization
     *
     * @param path path
     * @return {@code true} if path excluded
     */
    public boolean checkExcluded(String path) {
        if (exclusionPaths.contains(path)) {
            return true;
        }

        for (String exclusionPathPrefix : exclusionPathPrefixes) {
            if (path.startsWith(exclusionPathPrefix)) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        private final Set<String> exclusionPaths = new HashSet<>();
        private final Set<String> exclusionPathPrefixes = new HashSet<>();

        public Builder withExclusionPaths(Collection<String> exclusionPaths) {
            this.exclusionPaths.addAll(exclusionPaths);
            return this;
        }

        public Builder withExclusionPathPrefixes(Collection<String> exclusionPathPrefixes) {
            this.exclusionPathPrefixes.addAll(exclusionPathPrefixes);
            return this;
        }

        public ExcludedPaths build() {
            return new ExcludedPaths(exclusionPaths, exclusionPathPrefixes);
        }
    }
}
