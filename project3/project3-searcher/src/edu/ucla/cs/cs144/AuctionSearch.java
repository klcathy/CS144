package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

import javax.xml.transform.Result;

public class AuctionSearch implements IAuctionSearch {
	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */

    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    public AuctionSearch() {
        try {
            searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1"))));
            parser = new QueryParser("content", new StandardAnalyzer());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public TopDocs performSearch(String queryString, int n) throws IOException, ParseException {
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }

    public Document getDocument(int docId) throws IOException {
        return searcher.doc(docId);
    }
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {

		SearchResult[] searchResults = null;

        try {

            // Retrieve all matching documents
            TopDocs topDocs = performSearch(query, numResultsToSkip + numResultsToReturn);

            // Obtain the scoreDoc (documentId, relevanceScore) array from topDocs
            ScoreDoc[] hits = topDocs.scoreDocs;

            // Determine how many results to return
            if (topDocs.totalHits < (numResultsToSkip + numResultsToReturn))
                numResultsToReturn = Math.max(0, topDocs.totalHits - numResultsToSkip);

            searchResults = new SearchResult[numResultsToReturn];

            for (int i = 0; i < numResultsToReturn; i++) {
                Document doc = getDocument(hits[i + numResultsToSkip].doc);
                String itemId = doc.get("iid");
                String name = doc.get("name");

                searchResults[i] = new SearchResult(itemId, name);
            }
        } catch (ParseException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

		return searchResults;
	}

    public String getLine(SearchRegion region) {
        double lx = region.getLx();
        double ly = region.getLy();
        double rx = region.getRx();
        double ry = region.getRy();

        String lowerLeft = "Point(" + lx + "," + ly + ")";
        String upperRight = "Point(" + rx + "," + ry + ")";

        return "LineString(" + lowerLeft + ", " + upperRight + ")";
    }

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {

        SearchResult[] searchResults = null;
        ArrayList<SearchResult> intermediateResults = new ArrayList<SearchResult>();
        Connection conn = null;
        int start = 0, added = 0, skipped = 0;

        try {
            conn = DbManager.getConnection(true);

            searchResults = basicSearch(query, start, numResultsToReturn);

            String line = getLine(region);

            PreparedStatement spatialCheckItem = conn.prepareStatement(
                "SELECT MBRContains(" + line + ", location) AS SpatialContains FROM ItemLocation WHERE iid = ?"
            );

            while (added < numResultsToReturn && searchResults.length > 0) {
                for (int i = 0; i < searchResults.length; i++) {
                    // Check if item is spatially contained in region
                    String itemId = searchResults[i].getItemId();
                    spatialCheckItem.setString(1, itemId);
                    ResultSet containsRS = spatialCheckItem.executeQuery();

                    if (containsRS.next() && containsRS.getBoolean("SpatialContains")) {
                        // Enough results have been found
                        if (added >= numResultsToReturn)
                            break;
                        // Still skipping results
                        if (skipped < numResultsToSkip) {
                            skipped++;
                        }
                        // Add to results
                        else {
                            intermediateResults.add(searchResults[i]);
                            added++;
                        }
                    }
                    containsRS.close();
                }

                // Look up next starting point for basicSearch
                start += numResultsToReturn;
                searchResults = basicSearch(query, start, numResultsToReturn);
            }

            spatialCheckItem.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        // Convert ArrayList to array
        SearchResult[] finalResults = new SearchResult[added];
        for (int i = 0; i < added; i++) {
            finalResults[i] = intermediateResults.get(i);
        }

		return finalResults;
	}

    public String getXMLTag(String tag, String content) {
        String combined = "";

        combined = "<" + tag + ">" + content + "</" + tag + ">" + "\n";

        return combined;
    }

    static String formatDate(String str) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        String outputDate = "";
        try {
            Date parsed = parseFormat.parse(str);
            outputDate = outputFormat.format(parsed);
        }
        catch (Exception pe) {
            System.out.println("ERROR: Cannot parse \"" + str + "\"");
        }
        return outputDate;
    }

	public String getXMLDataForItemId(String itemId) {
        Connection conn = null;
        String data = "";

        try {
            conn = DbManager.getConnection(true);

            Statement itemStmt = conn.createStatement();
            ResultSet itemRS = itemStmt.executeQuery("SELECT * FROM Item WHERE iid = " + itemId);

            if (itemRS.next()) {
                // Item
                data += "<Item ItemID='";
                data = data + itemId + "'>" + "\n";

                // Name
                String name = itemRS.getString("name");
                data += getXMLTag("Name", name);

                // Category
                Statement itemCatStmt = conn.createStatement();
                ResultSet itemCatRS = itemCatStmt.executeQuery("SELECT * FROM ItemCategory WHERE iid = " + itemId);
                while (itemCatRS.next()) {
                    String category = itemCatRS.getString("category");
                    data += getXMLTag("Category", category);
                }

                // Currently
                String currently = itemRS.getString("currently");
                data += getXMLTag("Currently", "$"+currently);

                // First Bid
                String first_bid = itemRS.getString("first_bid");
                data += getXMLTag("First_Bid", "$"+first_bid);

                // Number of Bids
                String num_bids = itemRS.getString("num_bids");
                data += getXMLTag("Number_of_Bids", num_bids);

                // Bids
                Statement bidStmt = conn.createStatement();
                ResultSet bidRS = bidStmt.executeQuery("SELECT * FROM Bid WHERE iid = " + itemId);
                data += "<Bids>\n";
                while (bidRS.next()) {
                    data += "<Bid>\n";
                    String bidder_id = bidRS.getString("bid");
                    String time = formatDate(bidRS.getString("time"));
                    String amount = bidRS.getString("amount");

                    Statement bidderStmt = conn.createStatement();
                    ResultSet bidderRS = bidderStmt.executeQuery("SELECT * FROM Bidder WHERE bid = '" + bidder_id + "'");
                    if (bidderRS.next()) {
                        String rating = bidderRS.getString("rating");
                        String location = bidderRS.getString("location");
                        String country = bidderRS.getString("country");

                        data = data + "<Bidder Rating='" + rating + "' UserID='" + bidder_id + "'>\n";
                        data += getXMLTag("Location", location);
                        data += getXMLTag("Country", country);
                        data += "</Bidder>\n";
                    }
                    bidderStmt.close();
                    bidderRS.close();

                    data += getXMLTag("Time", time);
                    data += getXMLTag("Amount", "$"+amount);
                    data += "</Bid>\n";
                }
                data += "</Bids>\n";

                // Location
                String location = itemRS.getString("location");
                String latitude = itemRS.getString("latitude");
                String longitude = itemRS.getString("longitude");
                if (latitude.equals(""))
                    data += getXMLTag("Location", location);
                else {
                    data = data + "<Location Latitude='" + latitude + "' Longitude ='" + longitude +
                            "'>" + location + "</Location>" + "\n";
                }

                // Country
                String country = itemRS.getString("country");
                data += getXMLTag("Country", country);

                // Started
                String started = formatDate(itemRS.getString("started"));
                data += getXMLTag("Started", started);

                // Ends
                String ends = formatDate(itemRS.getString("ends"));
                data += getXMLTag("Ends", ends);

                // Seller
                String seller = itemRS.getString("sid");
                Statement sellerStmt = conn.createStatement();
                ResultSet sellerRS = sellerStmt.executeQuery("SELECT * FROM Seller WHERE sid = '" + seller + "'");
                if (sellerRS.next()) {
                    String rating = sellerRS.getString("rating");
                    data = data + "<Seller Rating='" + rating + "' UserID='" + seller + "' />" + "\n";
                }

                // Description
                String description = itemRS.getString("description");
                data += getXMLTag("Description", description);

                itemCatRS.close();
                itemCatStmt.close();
                sellerRS.close();
                sellerStmt.close();
            }

            // Do I need all these closes?
            itemRS.close();
            itemStmt.close();
            conn.close();


        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Message: " + e.getMessage());

        }

		return data;
	}
	
	public String echo(String message) {
		return message;
	}

}
