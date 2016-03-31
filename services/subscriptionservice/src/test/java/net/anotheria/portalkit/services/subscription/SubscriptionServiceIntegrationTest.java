package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 23:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SubscriptionServiceConfiguration.class)
public class SubscriptionServiceIntegrationTest {

	public static final String TRANSACTION_ID = "transactionId";
	public static final long SUBSCRIPTION_ID = 1;
	public static final String ACCOUNT_ID = "accountId";
	public static final String PRODUCT_ID = "productId";
	public static final long   TIMESTAMP = System.currentTimeMillis();
	public static final String ACTION = "an action";
	public static final String MESSAGE = "amessage";

	public static final String ACCOUNT_ID2 = "anotherAccountId";
	public static final String NON_EXISTING_ACCOUNTID = "non-existing";


	@Autowired private SubscriptionService subscriptionService;

	@Test public void testStorageOfTransactionEntries() throws Exception{
		TransactionLogEntry entry1 = new TransactionLogEntryBuilder().transactionId(TRANSACTION_ID)
				.subscriptionId(SUBSCRIPTION_ID)
				.accountId(ACCOUNT_ID)
				.productId(PRODUCT_ID)
				.timestamp(TIMESTAMP)
				.action(ACTION)
				.message(MESSAGE).build();

		subscriptionService.addTransactionLogEntry(entry1);

		TransactionLogEntry entry2 = new TransactionLogEntryBuilder().transactionId(TRANSACTION_ID)
				.subscriptionId(SUBSCRIPTION_ID)
				.accountId(ACCOUNT_ID)
				.productId(PRODUCT_ID)
				.timestamp(TIMESTAMP)
				.action("Some other action")
				.message("Some other message").build();

		subscriptionService.addTransactionLogEntry(entry2);

		TransactionLogEntry entry3 = new TransactionLogEntryBuilder().transactionId(TRANSACTION_ID)
				.subscriptionId(SUBSCRIPTION_ID)
				.accountId(ACCOUNT_ID2)
				.productId(PRODUCT_ID)
				.timestamp(TIMESTAMP)
				.action(ACTION)
				.message(MESSAGE).build();

		subscriptionService.addTransactionLogEntry(entry3);

		//now retrieve
		List<TransactionLogEntry> entries = subscriptionService.getTransactionLogEntries(new AccountId(ACCOUNT_ID));
		assertEquals(2, entries.size());

		assertEquals(0, subscriptionService.getTransactionLogEntries(new AccountId(NON_EXISTING_ACCOUNTID)).size());
		assertEquals(1, subscriptionService.getTransactionLogEntries(new AccountId(ACCOUNT_ID2)).size());

		//technically we should test for fields here, but for now we believe it works, since the field mapping is tested in other tests.


	}
}
