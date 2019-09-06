package net.anotheria.portalkit.services.authentication;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 2019-09-06 08:57
 */
@DistributeMe()
@FailBy(strategyClass= RetryCallOnce.class)
public interface SecretKeyAuthenticationService extends AuthenticationService {
}
