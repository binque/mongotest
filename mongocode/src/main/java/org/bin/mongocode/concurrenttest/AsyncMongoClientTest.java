package org.bin.mongocode.concurrenttest;

import java.util.concurrent.CountDownLatch;

import org.bin.mongocode.config.Config;
import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;

public class AsyncMongoClientTest {

	private static String uri = Config.uri;
	private static String dbName = Config.dbName;

	private static int threadCount = Config.ThreadCount;

	private static long totalTime = 0;
	private static CountDownLatch latch = new CountDownLatch(threadCount);

	public static void main(String[] args) {

		final long startTime = System.currentTimeMillis();
		MongoClient mongoClient = MongoClients.create(new ConnectionString(uri));
		
//		ConnectionString connectionString = new ConnectionString(Config.uri);
//		MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
//                .clusterSettings(ClusterSettings.builder()
//                        .applyConnectionString(connectionString)
//                        .build())
//.connectionPoolSettings(ConnectionPoolSettings.builder()
//                                      .applyConnectionString(connectionString)
//                                      .build())
//.serverSettings(ServerSettings.builder().build())
//.credentialList(connectionString.getCredentialList())
//.sslSettings(SslSettings.builder()
//                .applyConnectionString(connectionString)
//                .build())
//.socketSettings(SocketSettings.builder()
//                      .applyConnectionString(connectionString)
//                      .build())
//.build()));
		
		MongoDatabase database = mongoClient.getDatabase(dbName);
		final MongoCollection<Document> collection = database.getCollection("contact");

		for (int a = 0; a < threadCount; a++) {
			new Thread(new Runnable() {
				public void run() {
					FindIterable<Document> iterable = collection.find();
					iterable.first(new SingleResultCallback<Document>() {
						public void onResult(Document result, Throwable t) {
//							try {
//								Thread.sleep(4000L);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							System.out.println(result.toJson());
							latch.countDown();
						}
					});
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
