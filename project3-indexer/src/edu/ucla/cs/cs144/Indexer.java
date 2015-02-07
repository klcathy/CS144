package edu.ucla.cs.cs144;

import java.util.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter()  {
        if (indexWriter == null) {
            try {
                Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1"));
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
                indexWriter = new IndexWriter(indexDir, config);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
 
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        try {
            Statement itemStmt = conn.createStatement();        // Item table
            Statement categoryStmt = conn.createStatement();    // Category table

            ResultSet itemRS = itemStmt.executeQuery("SELECT iid, name, description FROM Item");
            ResultSet categoryRS = categoryStmt.executeQuery("SELECT * FROM ItemCategory");

            // Stores item -> category relations
            HashMap<String, String> itemCategoryMap = new HashMap<String, String>();

            // Concatenate all categories of an item into one string
            while (categoryRS.next()) {
                String itemId = categoryRS.getString("iid");
                String currentCategory = categoryRS.getString("category");

                if (itemCategoryMap.containsKey(itemId)) {
                    itemCategoryMap.put(itemId, itemCategoryMap.get(itemId) + " " + currentCategory);
                } else {
                    itemCategoryMap.put(itemId, currentCategory);
                }
            }

            // Index items with correspodning categories
            while (itemRS.next()) {
                IndexWriter writer = getIndexWriter();
                Document doc = new Document();

                // Individual keyword search
                String itemId = itemRS.getString("iid");
                String name = itemRS.getString("name");
                String description = itemRS.getString("description");
                String categories = itemCategoryMap.get(itemId);

                // Basic keyword search
                String fullSearchableText = name + " " + description + " " + categories;

                doc.add(new StringField("iid", itemId, Field.Store.YES));
                doc.add(new TextField("name", name, Field.Store.YES));
                doc.add(new TextField("description", description, Field.Store.NO));
                doc.add(new TextField("category", categories, Field.Store.NO));
                doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
                writer.addDocument(doc);
            }

            // Do I need all these closes?
            itemRS.close();
            categoryRS.close();
            itemStmt.close();
            categoryStmt.close();

            closeIndexWriter();
        } catch (SQLException e) {
            System.out.println("Caught SQLException");
            System.out.println("--------------------");
            while (e != null) {
                System.out.println("Message: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Error: " + e.getErrorCode());
                System.out.println("--------------------");
                e = e.getNextException();
            }
        } catch (IOException e) {
            System.out.println(e);
        }


        /*
         * Add your code here to retrieve Items using the connection
         * and add corresponding entries to your Lucene inverted indexes.
             *
             * You will have to use JDBC API to retrieve MySQL data from Java.
             * Read our tutorial on JDBC if you do not know how to use JDBC.
             *
             * You will also have to use Lucene IndexWriter and Document
             * classes to create an index and populate it with Items data.
             * Read our tutorial on Lucene as well if you don't know how.
             *
             * As part of this development, you may want to add
             * new methods and create additional Java classes.
             * If you create new classes, make sure that
             * the classes become part of "edu.ucla.cs.cs144" package
             * and place your class source files at src/edu/ucla/cs/cs144/.
         *
         */


            // close the database connection
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
