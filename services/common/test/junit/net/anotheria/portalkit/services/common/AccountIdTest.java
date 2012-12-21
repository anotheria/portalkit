package net.anotheria.portalkit.services.common;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 13.12.12 16:13
 */
public class AccountIdTest {
	@Test public void testIdUniqueness() throws Exception{
		int threadCount = 5;
		final ConcurrentHashMap<AccountId, AccountId> map = new ConcurrentHashMap<AccountId, AccountId>(threadCount*1000);
		final CountDownLatch start = new CountDownLatch(threadCount);
		final CountDownLatch finish = new CountDownLatch(threadCount);
		final CountDownLatch go = new CountDownLatch(1);
		for (int i=0; i<threadCount; i++){
			Thread t = new Thread(){
				public void run(){
					start.countDown();
					try{
						go.await();
					}catch(InterruptedException e){}
					for (int i=0; i<1000; i++){
						AccountId acc = AccountId.generateNew();
						map.put(acc, acc);
					}
					finish.countDown();
				}
			};
			t.start();
		}
		start.await();
		go.countDown();
		finish.await();
		//ensure we had no duplicates
		assertEquals(threadCount*1000, map.size());
	}
}
