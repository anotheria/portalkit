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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 17:16
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceImplTest {

	public static final String TRANSACTION_ID = "transactionId";
	public static final long SUBSCRIPTION_ID = 1;
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

		long currentTime = System.currentTimeMillis();

		Subscription subscription = new Subscription();
		subscription.setAccountId(new AccountId(ACCOUNT_ID));
		subscription.setPurchaseTimestamp(currentTime);
		subscription.setActive(true);
		subscription.setPreparedForCancelation(false);
		subscription.setCancelationTimestamp(0);
		subscription.setProlongationCount(0);
		subscription.setAmountInCents(100);
		subscription.setCancelationPeriodInMillis(TimeUnit.DAY.getMillis(2));
		subscription.setCurrency("CH");
		subscription.setDuration("30D");
		subscription.setExpirationTimestamp(currentTime + TimeUnit.DAY.getMillis(30));
		subscription.setProductId("6");
		subscription.setLastProlongationTimestamp(0);
		subscription.setSubscriptionId(1);

		service.addSubscription(subscription);

		ArgumentCaptor<SubscriptionDO> captor = ArgumentCaptor.forClass(SubscriptionDO.class);
		verify(subscriptionPersistenceService).saveSubscription(captor.capture());
		verify(subscriptionPersistenceService, atLeastOnce()).saveSubscription(any(SubscriptionDO.class));

		assertEquals(captor.getValue().getAccountId(), ACCOUNT_ID);
		assertEquals(captor.getValue().getPurchaseTimestamp(), currentTime);
		assertEquals(captor.getValue().isActive(), true);
		assertEquals(captor.getValue().isPreparedForCancelation(), false);
		assertEquals(captor.getValue().getCancelationTimestamp(), 0);
		assertEquals(captor.getValue().getProlongationCount(), 0);
		assertEquals(captor.getValue().getAmountInCents(), 100);
		assertEquals(captor.getValue().getCancelationPeriodInMillis(), TimeUnit.DAY.getMillis(2));
		assertEquals(captor.getValue().getCurrency(), "CH");
		assertEquals(captor.getValue().getDuration(), "30D");
		assertEquals(captor.getValue().getExpirationTimestamp(), currentTime + TimeUnit.DAY.getMillis(30));
		assertEquals(captor.getValue().getProductId(), "6");
		assertEquals(captor.getValue().getLastProlongationTimestamp(), 0);
		assertEquals(captor.getValue().getSubscriptionId(), 1);
	}

	@Test
	public void testGetActiveSubscriptionForAccount() throws Exception {

		long currentTime = System.currentTimeMillis();

		Subscription subscription = new Subscription();
		subscription.setAccountId(new AccountId(ACCOUNT_ID));
		subscription.setPurchaseTimestamp(currentTime);
		subscription.setActive(true);
		subscription.setPreparedForCancelation(false);
		subscription.setCancelationTimestamp(0);
		subscription.setProlongationCount(0);
		subscription.setAmountInCents(100);
		subscription.setCancelationPeriodInMillis(TimeUnit.DAY.getMillis(2));
		subscription.setCurrency("CH");
		subscription.setDuration("30D");
		subscription.setExpirationTimestamp(currentTime + TimeUnit.DAY.getMillis(30));
		subscription.setProductId("6");
		subscription.setLastProlongationTimestamp(0);
		subscription.setSubscriptionId(1);

		when(subscriptionPersistenceService.getActiveSubscriptionForAccount(anyString())).thenReturn(subscription.toDO());

		service.addSubscription(subscription);
		verify(subscriptionPersistenceService, atLeastOnce()).saveSubscription(any(SubscriptionDO.class));

		Subscription activeSubscription = service.getActiveSubscriptionForAccount(new AccountId(ACCOUNT_ID));

		verify(subscriptionPersistenceService, atLeastOnce()).getActiveSubscriptionForAccount(ACCOUNT_ID);

		assertEquals(true, activeSubscription.isActive());
	}

	@Test
	public void testGetSubscriptionForAccount() throws Exception {

		long currentTime = System.currentTimeMillis();

		Subscription subscription = new Subscription();
		subscription.setAccountId(new AccountId(ACCOUNT_ID));
		subscription.setPurchaseTimestamp(currentTime);
		subscription.setActive(true);
		subscription.setPreparedForCancelation(false);
		subscription.setCancelationTimestamp(0);
		subscription.setProlongationCount(0);
		subscription.setAmountInCents(100);
		subscription.setCancelationPeriodInMillis(TimeUnit.DAY.getMillis(2));
		subscription.setCurrency("CH");
		subscription.setDuration("30D");
		subscription.setExpirationTimestamp(currentTime + TimeUnit.DAY.getMillis(30));
		subscription.setProductId("6");
		subscription.setLastProlongationTimestamp(0);
		subscription.setSubscriptionId(1);

		when(subscriptionPersistenceService.getActiveSubscriptionForAccount(anyString())).thenReturn(subscription.toDO());

		service.addSubscription(subscription);
		verify(subscriptionPersistenceService, atLeastOnce()).saveSubscription(any(SubscriptionDO.class));

		Subscription activeSubscription = service.getActiveSubscriptionForAccount(new AccountId(ACCOUNT_ID));

		verify(subscriptionPersistenceService, atLeastOnce()).getActiveSubscriptionForAccount(ACCOUNT_ID);
	}

	@Test
	public void testSubscriptions() throws Exception {

		when(subscriptionPersistenceService.getSubscriptions()).thenReturn(getSubscriptions());

		List<Subscription> subscriptions = service.getSubscriptions();

		verify(subscriptionPersistenceService, atLeastOnce()).getSubscriptions();

		assertEquals(2, subscriptions.size());
	}

	@Test
	public void testUpdateSubscription() throws Exception {

		long currentTime = System.currentTimeMillis();

		Subscription subscription = new Subscription();
		subscription.setAccountId(new AccountId(ACCOUNT_ID));
		subscription.setPurchaseTimestamp(currentTime);
		subscription.setActive(true);
		subscription.setPreparedForCancelation(false);
		subscription.setCancelationTimestamp(0);
		subscription.setProlongationCount(0);
		subscription.setAmountInCents(100);
		subscription.setCancelationPeriodInMillis(TimeUnit.DAY.getMillis(2));
		subscription.setCurrency("CH");
		subscription.setDuration("30D");
		subscription.setExpirationTimestamp(currentTime + TimeUnit.DAY.getMillis(30));
		subscription.setProductId("6");
		subscription.setLastProlongationTimestamp(0);
		subscription.setSubscriptionId(1);

		service.addSubscription(subscription);
		subscription.setActive(false);
		service.updateSubscription(subscription);

		ArgumentCaptor<SubscriptionDO> captor = ArgumentCaptor.forClass(SubscriptionDO.class);
		verify(subscriptionPersistenceService).updateSubscription(captor.capture());
		verify(subscriptionPersistenceService, atLeastOnce()).saveSubscription(any(SubscriptionDO.class));

		assertEquals(captor.getValue().getAccountId(), ACCOUNT_ID);
		assertEquals(captor.getValue().getPurchaseTimestamp(), currentTime);
		assertEquals(captor.getValue().isActive(), false);
		assertEquals(captor.getValue().isPreparedForCancelation(), false);
		assertEquals(captor.getValue().getCancelationTimestamp(), 0);
		assertEquals(captor.getValue().getProlongationCount(), 0);
		assertEquals(captor.getValue().getAmountInCents(), 100);
		assertEquals(captor.getValue().getCancelationPeriodInMillis(), TimeUnit.DAY.getMillis(2));
		assertEquals(captor.getValue().getCurrency(), "CH");
		assertEquals(captor.getValue().getDuration(), "30D");
		assertEquals(captor.getValue().getExpirationTimestamp(), currentTime + TimeUnit.DAY.getMillis(30));
		assertEquals(captor.getValue().getProductId(), "6");
		assertEquals(captor.getValue().getLastProlongationTimestamp(), 0);
		assertEquals(captor.getValue().getSubscriptionId(), 1);
	}

	private List<SubscriptionDO> getSubscriptions() {

		List<SubscriptionDO> subscriptions = new ArrayList<SubscriptionDO>();
		SubscriptionDO subscription = new SubscriptionDO();
		subscription.setAccountId("1");
		subscription.setSubscriptionId(1);

		subscriptions.add(subscription);

		subscription = new SubscriptionDO();
		subscription.setAccountId("2");
		subscription.setSubscriptionId(2);

		subscriptions.add(subscription);

		return subscriptions;
	}
}
