package net.anotheria.portalkit.services.account;

import org.distributeme.annotation.CombinedService;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.07.17 08:29
 */
@DistributeMe()
@FailBy(strategyClass=RetryCallOnce.class)
@CombinedService(services = {AccountService.class, AccountAdminService.class})
public class AccountAndAccountAdminService {
}
