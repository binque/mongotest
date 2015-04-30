package org.bin.mongocode.config;

import com.mongodb.MongoClientOptions;

public class Config {
	
	private Config(){};
	
	public static int ThreadCount = 200;
	
	//not for test with spring mongo data
//	private static String uri = "mongodb://localhost:27017";
//	private static String dbName = "center0";
	
	public static String uri = "mongodb://10.68.52.201:27018";
	public static String dbName = "center1";
	
//	public static MongoClientOptions options = new MongoClientOptions.Builder().connectionsPerHost(1000).build();
}
