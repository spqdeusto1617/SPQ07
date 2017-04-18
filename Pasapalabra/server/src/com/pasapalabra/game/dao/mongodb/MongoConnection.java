package com.pasapalabra.game.dao.mongodb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class MongoConnection {

	public final Morphia morphia = new Morphia();
	public Datastore datastore;
	
	
	public MongoConnection(String packageMap, MongoClient mongoClient, String dbName) {
		this.morphia.mapPackage(packageMap);
		this.datastore = morphia.createDatastore(mongoClient, dbName);
		this.datastore.ensureIndexes();
	}
	
}
