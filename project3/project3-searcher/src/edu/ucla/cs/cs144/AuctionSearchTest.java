package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();

		String message = "Test message";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);
		
		String query = "superman";
		SearchResult[] basicResults = as.basicSearch(query, 0, 20);
		System.out.println("Basic Search Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
		for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}

		SearchRegion region =
		    new SearchRegion(33.774, -118.63, 34.201, -117.38); 
		SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
		System.out.println("Spatial Search");
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}

		spatialResults = as.spatialSearch(query, region, 0, 200);
		System.out.println("Spatial Search: " + query);
		System.out.println("Received " + spatialResults.length + " results");
		
		String itemId = "1497595357";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Add your own test here
        basicResults = as.basicSearch(query, 0, 70);
        System.out.println("Basic Search Query: " + query);
        System.out.println("Received " + basicResults.length + " results");

        query = "kitchenware";
        basicResults = as.basicSearch(query, 0, 1470);
        System.out.println("Basic Search Query: " + query);
        System.out.println("Received " + basicResults.length + " results");

        query = "star trek";
        basicResults = as.basicSearch(query, 0, 780);
        System.out.println("Basic Search Query: " + query);
        System.out.println("Received " + basicResults.length + " results");

        query = "camera";
        basicResults = as.basicSearch(query, 0, 1000);
        System.out.println("Basic Search Query: " + query);
        System.out.println("Received " + basicResults.length + " results");

		itemId = "1043374545";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		itemId = "1043495702";
		item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

	}
}
