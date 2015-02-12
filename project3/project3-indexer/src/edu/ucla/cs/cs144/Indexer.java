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

    public IndexWriter getIndexWriter(boolean create)  {
        if (indexWriter == null) {
            try {
                Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1"));
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
                config.setOpenMode(create ? IndexWriterConfig.OpenMode.CREATE : IndexWriterConfig.OpenMode.APPEND);
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

    public void indexItem(String itemId, String name, String description, String categories) throws IOException {
        // APPEND mode
        IndexWriter writer = getIndexWriter(false);

        String fullSearchableText = name + " " + description + " " + categories;

        Document doc = new Document();

        doc.add(new StringField("iid", itemId, Field.Store.YES));
        doc.add(new StringField("name", name, Field.Store.YES));
        doc.add(new TextField("content", fullSearchableText, Field.Store.NO));

        writer.addDocument(doc);
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
            getIndexWriter(true);

            String itemId, name, description, categories;

            // Item table
            Statement itemStmt = conn.createStatement();
            ResultSet itemRS = itemStmt.executeQuery("SELECT iid, name, description FROM Item");

            // Category table
            PreparedStatement categoryStmt = conn.prepareStatement(
                "SELECT category FROM ItemCategory WHERE iid = ?"
            );

            // Index items with corresponding categories
            while (itemRS.next()) {

                // Individual keywords
                categories = "";
                itemId = itemRS.getString("iid");
                name = itemRS.getString("name");
                description = itemRS.getString("description");
                categoryStmt.setString(1, itemId);
                ResultSet categoryRS = categoryStmt.executeQuery();

                // Concatenate all categories
                while (categoryRS.next()) {
                    categories += categoryRS.getString("category") + " ";
                }

                // Index item
                indexItem(itemId, name, description, categories);

                categoryRS.close();
            }

            // Do I need all these closes?
            itemRS.close();
            itemStmt.close();
            categoryStmt.close();
            closeIndexWriter();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e);
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
