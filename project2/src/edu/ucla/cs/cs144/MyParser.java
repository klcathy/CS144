/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

class ItemClass {
    String name;
    String currently;
    String first_bid;
    String buy_price;
    int num_bids;
    String started;
    String ends;
    String description;
    String sellerID;
    String location;
    String country;
    Double longitude;
    Double latitude;
}

class BidderClass {
    int rating;
    String location;
    String country; 
}

class BidClass {
    String bidderID;
    String time;
    String amount;
}

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

    static Map<Integer, ItemClass> itemHT = new HashMap<Integer, ItemClass>();
    static Map<Integer, String[]> itemCategoryHT = new HashMap<Integer, String[]>();
    static Map<String, Integer> sellerHT = new HashMap<String, Integer>();
    static Map<String, BidderClass> bidderHT = new HashMap<String, BidderClass>();
    static Map<String, BidClass> bidHT = new HashMap<String, BidClass>();

    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }

    // Helper function for reformatting date and time
    static String formatDate(String str) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String outputDate = new String();
        try {
            Date parsed = parseFormat.parse(str);
            outputDate = outputFormat.format(parsed);
        }
        catch (ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + str + "\"");
        }
        return outputDate;
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element root = doc.getDocumentElement();
        // store 1 xml doc into item hashmap
        Element[] items = getElementsByTagNameNR(root, "Item");

        for (int i = 0; i < items.length; i ++) {
            int item_id = Integer.parseInt(items[i].getAttribute("ItemID"));

            // Get attributes of Item
            ItemClass ic = new ItemClass();
            ic.name = getElementTextByTagNameNR(items[i], "Name");
            ic.currently = strip(getElementTextByTagNameNR(items[i], "Currently"));
            ic.first_bid = strip(getElementTextByTagNameNR(items[i], "First_Bid"));
            ic.buy_price = strip(getElementTextByTagNameNR(items[i], "Buy_Price"));
            ic.num_bids = Integer.parseInt(getElementTextByTagNameNR(items[i], "Number_of_Bids"));
            ic.started = formatDate(getElementTextByTagNameNR(items[i], "Started"));
            ic.ends = formatDate(getElementTextByTagNameNR(items[i], "Ends"));
            ic.description = getElementTextByTagNameNR(items[i], "Description");
            if ((ic.description).length() > 4000)
                ic.description = (ic.description).substring(0, 3999); // truncate string to 4000 chars
            String userID = (getElementByTagNameNR(items[i], "Seller")).getAttribute("UserID");
            ic.sellerID = userID;
            ic.location = getElementTextByTagNameNR(items[i], "Location");
            ic.country = getElementTextByTagNameNR(items[i], "Country");
            ic.longitude = Double.parseDouble(getElementByTagNameNR(items[i], "Location").getAttribute("Longitude"));
            ic.latitude = Double.parseDouble(getElementByTagNameNR(items[i], "Location").getAttribute("Latitude"));

            // Get attributes of ItemCategory
            Element[] category = getElementsByTagNameNR(items[i], "Category");
            String[] category_str = new String[category.length];

            for (int j = 0; j < category.length; j++) {
                category_str[j] = category[j].getTextContent();
            }

            // Get attributes of Seller
            int seller_rating = Integer.parseInt((getElementByTagNameNR(items[i], "Seller")).getAttribute("Rating"));

            // Get attributes of bidder
            BidderClass bdc = new BidderClass();
            Element bids = getElementByTagNameNR(items[i], "Bids");
            Element[] bids_arr = getElementsByTagNameNR(bids, "Bid");

            BidClass bc = new BidClass();

            for (int k = 0; k < bids_arr.length; k++) {
                Element bidder = getElementByTagNameNR(bids_arr[k], "Bidder");
                String bidder_id = bidder.getAttribute("UserID");
                bdc.rating =  Integer.parseInt(bidder.getAttribute("Rating"));
                bdc.location = getElementTextByTagNameNR(bidder, "Location");
                bdc.country = getElementTextByTagNameNR(bidder, "Country");
                bidderHT.put(bidder_id, bdc);

                // Get attributes of bid
                String time = formatDate(getElementTextByTagNameNR(bids_arr[k], "Time"));
                bc.time = time;
                bc.amount = strip(getElementTextByTagNameNR(bids_arr[k], "Amount"));
                bc.bidderID = bidder_id;
                String bid_id = bidder_id + Integer.toString(item_id) + time;
                bidHT.put(bid_id, bc);
            }

            itemHT.put(item_id, ic);
            itemCategoryHT.put(item_id, category_str);
            sellerHT.put(userID, seller_rating);
        }

        /*
        System.out.println("bidder");
        for(Map.Entry<String, Integer> entry : bidderHT.entrySet()) {
            String id = entry.getKey();
            System.out.println(id);
            System.out.println(bidderHT.get(id));
        }
        System.out.println("Seller");
        for(Map.Entry<String, Integer> entry : sellerHT.entrySet()) {
            String id = entry.getKey();
            System.out.println(id);
            System.out.println(sellerHT.get(id));
        }
        */

        /*
        for(Map.Entry<Integer, ItemClass> entry : itemHT.entrySet()) {
            int id = entry.getKey();
            System.out.println(id);
            System.out.println((itemHT.get(id)).name);
            System.out.println((itemHT.get(id)).currently);
            System.out.println((itemHT.get(id)).first_bid);
            System.out.println((itemHT.get(id)).buy_price);
            System.out.println((itemHT.get(id)).num_bids);
            System.out.println((itemHT.get(id)).started);
            System.out.println((itemHT.get(id)).ends);
            System.out.println((itemHT.get(id)).description);
            System.out.println((itemHT.get(id)).sellerID);
        }
        */

        /*
        //Test itemCategory
        String[] test = itemCategoryHT.get(1044269494);
        for (int k = 0; k < test.length; k++)
            System.out.println(test[k]);
        */

        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
