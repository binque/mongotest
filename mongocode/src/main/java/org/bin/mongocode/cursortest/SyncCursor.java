package org.bin.mongocode.cursortest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DBCursor;

public class SyncCursor {

	private static long totalTime = 0;
	private static MongoTemplate mongoTemplate;
	private static ApplicationContext applicationContext;
	private static CountDownLatch latch = new CountDownLatch(30000 * 10);

	public static void main(String[] args) throws IOException {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		mongoTemplate = (MongoTemplate) applicationContext.getBean("mongoTemplate");

		long startTime = System.currentTimeMillis();

		for (int a = 0; a < 10; a++) {
			new Runnable() {
				public void run() {

					// final AtomicInteger i = new AtomicInteger(1);
					DBCursor dbCursor = mongoTemplate.getDb()
							.getCollection("contact").find();

					while (dbCursor.hasNext()) {
						// try {
						// Thread.sleep(10L);
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }
						// System.out.println(i.incrementAndGet());
						latch.countDown();
						dbCursor.next();
					}
				}
			}.run();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		totalTime = System.currentTimeMillis() - startTime;
		printTime();
	}

	public static void printTime() {
		System.out.println("totalTime : " + totalTime / 1000.0);
	}
}
