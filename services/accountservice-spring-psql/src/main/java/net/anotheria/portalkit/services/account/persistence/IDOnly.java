package net.anotheria.portalkit.services.account.persistence;

/**
 * Spring Data projection that exposes only the account id. Used by the id-lookup queries to avoid
 * loading the whole {@link AccountEntity}.
 */
public interface IDOnly {
    String getId();
}
