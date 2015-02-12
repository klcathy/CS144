package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

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

    public String getPolygon(SearchRegion region) {
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

            String polygon = getPolygon(region);

            PreparedStatement spatialCheckItem = conn.prepareStatement(
                "SELECT MBRContains(" + polygon + ", location) AS SpatialContains FROM ItemLocation WHERE iid = ?"
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

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
