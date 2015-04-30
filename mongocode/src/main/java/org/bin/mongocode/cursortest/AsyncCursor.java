package org.bin.mongocode.cursortest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;

public class AsyncCursor {

	private static long totalTime = 0;
	// private static CountDownLatch latch = new CountDownLatch(3618692);
	private static CountDownLatch latch = new CountDownLatch(30000 * 3);
	static final AtomicInteger i = new AtomicInteger(1);

	public static void main(String[] args) {

		final long startTime = System.currentTimeMillis();
		// System.out.println(Thread.currentThread().getId());

		for (int a = 0; a < 3; a++) {
			new Runnable() {
				public void run() {
					MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://10.68.52.201:27018"));
					MongoDatabase database = mongoClient.getDatabase("center1");
					MongoCollection<Document> collection = database.getCollection("contact");

					FindIterable<Document> iterable = collection.find();
					iterable.forEach(new Block<Document>() {
						public void apply(Document t) {
							// try {
							// Thread.currentThread().sleep(10L);
							// } catch (InterruptedException e) {
							// e.printStackTrace();
							// }

							// System.out.println(Thread.currentThread().getId());
							latch.countDown();
						}
					}, null);
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