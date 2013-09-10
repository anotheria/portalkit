package net.anotheria.portalkit.services.online.event;

import junit.framework.Assert;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.portalkit.services.common.AccountId;
import net.anotheria.portalkit.services.common.eventing.ServiceEventData;
import net.anotheria.portalkit.services.online.events.OnlineActivityESOperation;
import net.anotheria.portalkit.services.online.events.OnlineActivityServiceEventConsumer;
import net.anotheria.portalkit.services.online.events.OnlineActivityServiceEventSupplier;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityCleanUpEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityLoginEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityLogoutEvent;
import net.anotheria.portalkit.services.online.events.data.OnlineActivityUpdateEvent;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Junit test for events stuff.
 *
 * @author h3llka
 */
public class EventingTest {


	@BeforeClass
	public static void before() {
		MetaFactory.reset();
	}

	@AfterClass
	public static void after() {
		MetaFactory.reset();
	}

	@Test
	public void flowTest() {


		OnlineActivityServiceEventSupplier supplier = new OnlineActivityServiceEventSupplier();

		TestConsumer consumer = new TestConsumer();


		final AccountId id = new AccountId("123123123123123123");

		supplier.accountLoggedIn(id, System.nanoTime());
		Assert.assertEquals(consumer.login.get(), 1);


		supplier.accountActivityChange(id, System.nanoTime());
		Assert.assertEquals(consumer.update.get(), 1);

		supplier.accountLoggedOut(id, System.nanoTime());
		Assert.assertEquals(consumer.logOut.get(), 1);


		supplier.cleanUp(Arrays.asList(id));
		Assert.assertEquals(consumer.cleanUp.get(), 1);

	}


	@Test
	public void errorCases() {
		TestConsumer consumer = new TestConsumer();
		try {
			consumer.serviceEvent(null);
			Assert.fail("Illegal Argument!");
		} catch (RuntimeException e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}

		consumer.serviceEvent(new ServiceEventData() {
			@Override
			public String getOperationType() {
				return "";
			}
		});

		Assert.assertEquals(consumer.login.get(), 0);
		Assert.assertEquals(consumer.update.get(), 0);
		Assert.assertEquals(consumer.logOut.get(), 0);
		Assert.assertEquals(consumer.cleanUp.get(), 0);


		//Should not be changed!
		consumer.serviceEvent(new OnlineActivityEvent(System.nanoTime(), OnlineActivityESOperation.ACCOUNT_LOGGED_IN) {
		});
		Assert.assertEquals(consumer.login.get(), 0);

		//Should not be changed!
		consumer.serviceEvent(new OnlineActivityEvent(System.nanoTime(), OnlineActivityESOperation.ACCOUNT_LOGGED_OUT) {
		});
		Assert.assertEquals(consumer.logOut.get(), 0);

		//Should not be changed!
		consumer.serviceEvent(new OnlineActivityEvent(System.nanoTime(), OnlineActivityESOperation.AUTO_CLEANUP) {
		});
		Assert.assertEquals(consumer.cleanUp.get(), 0);

		//Should not be changed!
		consumer.serviceEvent(new OnlineActivityEvent(System.nanoTime(), OnlineActivityESOperation.ACCOUNT_ACTIVITY_UPDATE) {
		});
		Assert.assertEquals(consumer.update.get(), 0);


	}


	/**
	 * Operations consumer for Junit.
	 */
	public class TestConsumer extends OnlineActivityServiceEventConsumer {
		/**
		 * Counter login.
		 */
		protected AtomicInteger login = new AtomicInteger(0);
		/**
		 * Counter logout.
		 */
		protected AtomicInteger logOut = new AtomicInteger(0);
		/**
		 * Counter update.
		 */
		protected AtomicInteger update = new AtomicInteger(0);
		/**
		 * Counter cleanUp.
		 */
		protected AtomicInteger cleanUp = new AtomicInteger(0);

		@Override
		public void accountLoggedIn(OnlineActivityLoginEvent event) {
			login.incrementAndGet();
		}

		@Override
		public void accountLoggedOut(OnlineActivityLogoutEvent event) {
			logOut.incrementAndGet();
		}

		@Override
		public void accountActivityUpdate(OnlineActivityUpdateEvent event) {
			update.incrementAndGet();
		}

		@Override
		public void accountActivityCleanUp(OnlineActivityCleanUpEvent event) {
			cleanUp.incrementAndGet();
		}


		@Override
		public void serviceEvent(ServiceEventData eventData) {
			super.serviceEvent(eventData);
		}
	}

}
