# Project conventions

## Branches
- `develop` is the default and integration branch — all PRs must target `develop`
- `master` is the release branch — only release commits go there via the maven-release-plugin

## Maven
- Java 21, root BOM at `pom.xml` manages all dependency versions
- Services live under `services/`, each with its own `pom.xml` inheriting the root
- Run tests per service: `mvn test -pl services/<name> -am`

## Dependencies
- H2 database is not used in this project — do not add it
- PostgreSQL JDBC driver version is managed in the root BOM
