package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryEntity;
import net.anotheria.portalkit.services.subscription.persistence.transaction.TransactionLogEntryPersistenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
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
}
