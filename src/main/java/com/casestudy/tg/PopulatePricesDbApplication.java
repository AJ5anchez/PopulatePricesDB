package com.casestudy.tg;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

@SpringBootApplication
public class PopulatePricesDbApplication implements CommandLineRunner{
	
	@Autowired
	PriceEntryRepository repository;
	
	@Autowired
	MongoProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(PopulatePricesDbApplication.class, args);
    }
    
    
    @Override
    /***
     * This method creates a DB for the URI defined in
     * the /config/application.properties file, which defines
     * the IP address of the MongoDB server, the port,
     * and the DB name. If this file is not present,
     * then the URI is mongodb://localhost:27017/test.
     * 
     * NOTE: You can test both cases by commenting
     * out the line where property spring.data.mongodb.uri
     * is defined in file application.properties.
     */
	public void run(String... args) throws Exception {
    	
    	// for this case ... start with a "clean slate"
    	repository.deleteAll();
    	
    	// create the entries that contain all pid given in
    	// the case study ...
    	long pid[] = {13860428L, 
    			      15117729L, 
    			      16483589L, 
    			      16696652L, 
    			      16752456L, 
    			      15643793L,
                      99999999L,
                      11111111L
    			     };
    	float price[] = {13.49f, 
    			         35.99f, 
    			         0.99f, 
    			         10.99f, 
    			         50.99f, 
    			         129.99f,
                         99.99f,
                         11.11f
    			        };
    	
    	// sanity check
    	//
    	// NOTE: see the following reference
    	// about compiling code with assertions and
    	// enabling them
    	// http://docs.oracle.com/javase/8/docs/technotes/guides/language/assert.html
    	assert pid.length == price.length;
    	
    	// insert all pairs in DB through the injected repository object
    	PriceEntry entry;
    	for (int i = 0; i < pid.length; ++i){
    		entry = new PriceEntry(pid[i], new CurrentPrice(price[i], "USD"));
        	repository.save(entry);
    	}
    	
    	// retrieve DB's URI from the MongoProperties object injected
    	// into this method
    	System.out.println("|\n|\n***Database created at URI: " + properties.getUri() + "\n|\n|");
    }
}
