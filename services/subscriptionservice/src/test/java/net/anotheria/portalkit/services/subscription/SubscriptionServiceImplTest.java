package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.subscription.persistence.subscription.SubscriptionDO;
import net.anotheria.portalkit.services.subscription.persistence.subscription.SubscriptionPersistenceService;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryEntity;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryPersistenceService;
import net.anotheria.util.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 17:16
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceImplTest {

	public static final String TRANSACTION_ID = "transactionId";
	public static final String SUBSCRIPTION_ID = "subscriptionId";
	public static final String ACCOUNT_ID = "accountId";
	public static final String PRODUCT_ID = "productId";
	public static final long   TIMESTAMP = System.currentTimeMillis();
	public static final String ACTION = "an action";
	public static final String MESSAGE = "amessage";

	@InjectMocks
	private SubscriptionServiceImpl service;

	@Mock
	private TransactionLogEntryPersistenceService persistenceService;

	@Mock
	private SubscriptionPersistenceService subscriptionPersistenceService;


	@Test public void testAddTransactionLogEntry() throws Exception{
		TransactionLogEntry entry = new TransactionLogEntryBuilder().transactionId(TRANSACTION_ID)
				.subscriptionId(SUBSCRIPTION_ID)
				.accountId(ACCOUNT_ID)
				.productId(PRODUCT_ID)
				.timestamp(TIMESTAMP)
				.action(ACTION)
				.message(MESSAGE).build();

		service.addTransactionLogEntry(entry);

		ArgumentCaptor<TransactionLogEntryEntity> captor = ArgumentCaptor.forClass(TransactionLogEntryEntity.class);
		verify(persistenceService).addTransactionLogEntry(captor.capture());

		assertEquals(captor.getValue().getAccountId(), ACCOUNT_ID);
		assertEquals(captor.getValue().getAction(), ACTION);
		assertEquals(captor.getValue().getMessage(), MESSAGE);
		assertEquals(captor.getValue().getTimestamp(), TIMESTAMP);
		assertEquals(captor.getValue().getProductId(), PRODUCT_ID);
		assertEquals(captor.getValue().getSubscriptionId(), SUBSCRIPTION_ID);
		assertEquals(captor.getValue().getTransactionId(), TRANSACTION_ID);
	}

	@Test
	public void testAddSubscription() throws Exception {

		Subscription subscription = new Subscription();
		subscription.setAccountId(new AccountId(ACCOUNT_ID));
		subscription.setPurchaseTimestamp(System.currentTimeMillis());
		subscription.setActive(true);
		subscription.setPreparedForCancelation(false);
		subscription.setCancelationTimestamp(0);
		subscription.setProlongationCount(0);
		subscription.setAmountInCents(100);
		subscription.setCancelationPeriodInMillis(TimeUnit.DAY.getMillis(2));
		subscription.setCurrency("CH");
		subscription.setDuration("30D");
		subscription.setExpirationTimestamp(System.currentTimeMillis() + TimeUnit.DAY.getMillis(30));
		subscription.setProductId("6");
		subscription.setLastProlongationTimestamp(0);
		subscription.setSubscriptionId("subscriptionId");

		service.addSubscription(subscription);

		ArgumentCaptor<SubscriptionDO> captor = ArgumentCaptor.forClass(SubscriptionDO.class);
		verify(subscriptionPersistenceService).saveSubscription(captor.capture());
		verify(subscriptionPersistenceService, atLeastOnce()).saveSubscription(any(SubscriptionDO.class));

/*		assertEquals(captor.getValue().getAccountId(), );
		assertEquals(captor.getValue().getPurchaseTimestamp(), );
		assertEquals(captor.getValue().isActive(), );
		assertEquals(captor.getValue().isPreparedForCancelation(), );
		assertEquals(captor.getValue().getCancelationTimestamp(), );
		assertEquals(captor.getValue().getProlongationCount(), );
		assertEquals(captor.getValue().getAmountInCents(), );
		assertEquals(captor.getValue().getCancelationPeriodInMillis(), );
		//assertEquals(captor.getValue().g, );
		assertEquals(captor.getValue().getAccountId(), );
		assertEquals(captor.getValue().getAccountId(), );
		assertEquals(captor.getValue().getAccountId(), );
		assertEquals(captor.getValue().getAccountId(), );
		assertEquals(captor.getValue().getAccountId(), );*/
	}

	@Test
	public void testGetActiveSubscriptionForAccount() {

	}
}
