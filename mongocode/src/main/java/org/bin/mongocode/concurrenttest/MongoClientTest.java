package org.bin.mongocode.concurrenttest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.bin.mongocode.config.Config;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class MongoClientTest {
	
	private static String uri = Config.uri;
	private static String dbName = Config.dbName;
	
	private static int threadCount = Config.ThreadCount;
	
	private static long totalTime = 0;
	private static CountDownLatch latch = new CountDownLatch(threadCount);

	public static void main(String[] args) throws IOException {
		
		MongoClient mongoClient = new MongoClient(new MongoClientURI(uri));
//		mongoClient.setOptions(options);
		final MongoDatabase db = mongoClient.getDatabase(dbName);
		
		long startTime = System.currentTimeMillis();
		 
		for (int a = 0; a < threadCount; a++) {
			new Thread(new Runnable() {
				public void run() {
					FindIterable<Document> iterator = db.getCollection("contact").find();
					Document document = iterator.first();
//					try {
//						Thread.sleep(4000L);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					System.out.println(document.toJson());
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
