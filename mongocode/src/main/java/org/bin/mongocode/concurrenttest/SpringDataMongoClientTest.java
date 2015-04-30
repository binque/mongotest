package org.bin.mongocode.concurrenttest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.bin.mongocode.config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class SpringDataMongoClientTest {
	
	private static int threadCount = Config.ThreadCount;
	private static long totalTime = 0;
	private static MongoTemplate mongoTemplate;
	private static ApplicationContext applicationContext;
	private static DBCollection collection;
	private static CountDownLatch latch = new CountDownLatch(threadCount);

	public static void main(String[] args) throws IOException {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		mongoTemplate = (MongoTemplate) applicationContext.getBean("mongoTemplate");
		collection = mongoTemplate.getDb().getCollection("contact");

		long startTime = System.currentTimeMillis();
		 
		for (int a = 0; a < threadCount; a++) {
			new Thread(new Runnable() {
				public void run() {
					DBCursor dbCursor = collection.find();
					dbCursor.next();
//					System.out.println(dbCursor.next());
//					try {
//						Thread.sleep(4000L);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					latch.countDown();
				}
			}).start();
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
