package com.casestudy.tg;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PopulatePricesDbApplication.class)
/***
 * Only one test in this suite.
 * @author AJ Sanchez (a.sanchez.824@gmail.com)
 *
 */
public class PopulatePricesDbApplicationTests {

	@Autowired 
	PriceEntryRepository repository;
	
	@Autowired
	MongoProperties properties;
	
	
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
	// expected pairs are (pid[i], price[i]), i = pid.length
	// where pid.length == pid.length
	
	@Test
	/***
	 * 
	 * >>Test Case checkPairs:
	 * Are all pairs (product_id, price) just inserted
	 * in the DB the expected ones?
	 * 
	 * The following test cases can be exercised with simple
	 * changes to this code:
     *
	 * (a) Internal sanity check: pid and price have the same length.
	 * Delete at least one element from either pid or price.
	 * 
	 * (b) Happy case: DB exists and expected pairs are
	 * in the DB and viceversa. Default.
	 * 
	 * (c) A pair in the DB is not in the expected set: 
	 * delete a pair (pid[i], price[i]) for at least one i
	 * 
	 * (d) A pair in (pid[i], price[i]) for at least one i is
	 * not in the DB: add at least one pair (pid[j], price[j])
	 * to the current set
	 * 
	 */
	public void checkPairs() {
		
		String errorMessage;
		
		// internal sanity check before testing
		errorMessage = "Lengths of pid[" + 
		               pid.length + 
		               "] and price[" + 
		               price.length + 
		               "] do not match!";
		assertTrue(errorMessage, pid.length == price.length);
		
		// create HashMap containing all expected pairs
		HashMap<Long, CurrentPrice> map = new HashMap<>();
		
		for (int i = 0; i < pid.length; ++i){
			map.put(pid[i], new CurrentPrice(price[i], "USD"));
		}
		
		// get all pairs from DB
		List<PriceEntry> result = repository.findAll();
		
		// print out summary of test intention
		printTestInfo(pid, price, properties.getUri());
		
		// size of map and result set must be the same
		errorMessage = "Expected lenghts of  map[" + 
		               map.size() + 
		               "] and result set[" + 
		               result.size() + 
		               "] do not match";
		assertTrue(errorMessage, map.size() == result.size());
		
		// every element in result set must be a (pid[i], price[i]) for some i
		CurrentPrice price;
		long id;
		for (PriceEntry entry : result){
			id = entry.getPid();
			price = map.get(id);
			// id not in test set
			if (price == null)
				fail("id in result set is not in test set: " + id);
			// id is in test set
			errorMessage = "actual price: " + 
			               entry.getCurrentPrice() + 
			               " expected price: " + 
			               price +
			               " for product id: " +
			               id;
			assertTrue(errorMessage, 
					   price.getValue() == entry.getCurrentPrice().getValue() && 
					   price.getCurrencyCode().equals(entry.getCurrentPrice().getCurrencyCode()));
		}	
    }
	
	/***
	 * Prints out a summary explaining the intention of this test. <br>
	 * NOTE: Assumes condition pid.length == price.length is true
	 * 
	 * @param pid array containing all product ids
	 * @param price array containing all prices
	 * @param uri Uniform Resource Identifier for the DB
	 * 
	 */
	private void printTestInfo(long[] pid, float[] price, String uri) {
		System.out.println("|\n|\n|");
		System.out.println("***Testing with DB at URI: "+ uri);
		System.out.println("***Determining if DB has the following pairs and only these pairs:");
		for(int i = 0; i < pid.length ; ++i){
			System.out.print("(" + pid[i] + ", " + price[i] + ") ");
		}
		System.out.println();
		System.out.println("|\n|\n|");
	}	
}
