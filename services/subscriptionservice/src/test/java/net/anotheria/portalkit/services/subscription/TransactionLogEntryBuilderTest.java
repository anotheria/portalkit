package net.anotheria.portalkit.services.subscription;

import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.03.16 16:22
 */
public class TransactionLogEntryBuilderTest {
	@Test public void testTransactionLogEntryBuilder(){
		TransactionLogEntryBuilder builder = new TransactionLogEntryBuilder();
		builder.accountId(new AccountId("accountId"))
				.subscriptionId("subID")
				.action("anAction")
				.message("Message")
				.productId("32F")
				.transactionId("tId");

		TransactionLogEntry entry = builder.build();
		assertEquals("accountId", entry.getAccountId().getInternalId());
		assertEquals("anAction", entry.getAction());
		assertEquals("subID", entry.getSubscriptionId());
		assertEquals("Message", entry.getMessage());
		assertEquals("32F", entry.getProductId());
		assertEquals("tId", entry.getTransactionId());

	}
}
