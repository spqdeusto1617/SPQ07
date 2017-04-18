package com.pasapalabra.game.dao.mongodb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

/** Class that makes easy to connect to mongo with Morphia ODM
 * @author Guti
 *
 */
public class MongoConnection {

	public final Morphia morphia = new Morphia();
	public Datastore datastore;
	
	
	/** Instanciates MongoConnection
	 * @param packageMap Package where model is located and will be mapped.
	 * @param mongoClient Client connected to Mongo DB Server.
	 * @param dbName Name of the database.
	 */
	public MongoConnection(String packageMap, MongoClient mongoClient, String dbName) {
		this.morphia.mapPackage(packageMap);
		this.datastore = morphia.createDatastore(mongoClient, dbName);
		
	}
	
	
	/** Maps other model package
	 * @param packageMap Package to be mapped.
	 */
	public void mapNewPackage(String packageMap) {
		if(this.datastore == null) return;
		this.morphia.mapPackage(packageMap);
		this.datastore.ensureIndexes();
	}
	
}
