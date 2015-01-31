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

class Item {
    long itemId;
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

    public Item(long id) {
        this.itemId = id;
    }

    public void setRequiredAttributes(String name, String currentBid, String first_bid,
                                      int num_bids, String startTime, String endTime,
                                      String desc, String location, String country) {
        this.name = name;
        this.currently = currentBid;
        this.first_bid = first_bid;
        this.num_bids = num_bids;
        this.started = startTime;
        this.ends = endTime;
        this.description = desc;
        this.location = location;
        this.country = country;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setBuyPrice(String buyPrice) {
        this.buy_price = buyPrice;
    }

    public String toString() {
        String buyStr = (buy_price != null) ? String.valueOf(buy_price) : "\\N";
        String longitudeStr = (longitude != null) ? String.valueOf(this.longitude) : "\\N";
        String latitudeStr = (latitude != null) ? String.valueOf(this.latitude) : "\\N";

        return itemId + "\t" + name + "\t" + currently + "\t" + first_bid + "\t" +
                buyStr + "\t" + num_bids + "\t" + started + "\t" + ends + "\t" +
                description + "\t" + location + "\t" + latitudeStr + "\t" +
                longitudeStr + "\t" + country;
    }
}

class Bidder {
    int rating;
    String location;
    String country; 
}

class Bid {
    String bidderID;
    String time;
    String amount;
}

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

    static Map<Long, Item> itemHT = new HashMap<Long, Item>();
    static Map<Long, ArrayList<String>> itemCategoryHT = new HashMap<Long, ArrayList<String>>();
    static Map<String, Integer> sellerHT = new HashMap<String, Integer>();
    static Map<String, Bidder> bidderHT = new HashMap<String, Bidder>();
    static Map<String, Bid> bidHT = new HashMap<String, Bid>();

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

    /*  Writes the contents of a map to a tab-delimited file.
     */
    static void writeMapToFile(String filename, HashMap<Object, Object> map) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            PrintWriter printer = new PrintWriter(writer);
            for (Object o : map.values()) {
                printer.print(o);
                printer.println();
            }
            printer.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error printing tab-delimited file!");
        }
    }

    /*  Parses the date in the xml and returns a new formatted date
        suitable for mySQL.
     */
    static String formatDate(String str) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String outputDate = "";
        try {
            Date parsed = parseFormat.parse(str);
            outputDate = outputFormat.format(parsed);
        }
        catch (ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + str + "\"");
        }
        return outputDate;
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

        // Get all Item children of Items
        Element[] items = getElementsByTagNameNR(root, "Item");

        for (Element item : items) {
            long item_id = Integer.parseInt(item.getAttribute("ItemID"));

            // Construct the item
            Item newItem = new Item(item_id);
            newItem.name = getElementTextByTagNameNR(item, "Name");
            newItem.currently = strip(getElementTextByTagNameNR(item, "Currently"));
            newItem.first_bid = strip(getElementTextByTagNameNR(item, "First_Bid"));
            newItem.buy_price = strip(getElementTextByTagNameNR(item, "Buy_Price"));
            newItem.num_bids = Integer.parseInt(getElementTextByTagNameNR(item, "Number_of_Bids"));
            newItem.started = formatDate(getElementTextByTagNameNR(item, "Started"));
            newItem.ends = formatDate(getElementTextByTagNameNR(item, "Ends"));
            newItem.description = getElementTextByTagNameNR(item, "Description");
            if ((newItem.description).length() > 4000)
                newItem.description = (newItem.description).substring(0, 3999); // truncate string to 4000 chars
            String userID = (getElementByTagNameNR(item, "Seller")).getAttribute("UserID");
            newItem.sellerID = userID;
            newItem.location = getElementTextByTagNameNR(item, "Location");
            newItem.country = getElementTextByTagNameNR(item, "Country");
            newItem.longitude = Double.parseDouble(getElementByTagNameNR(item, "Location").getAttribute("Longitude"));
            newItem.latitude = Double.parseDouble(getElementByTagNameNR(item, "Location").getAttribute("Latitude"));

            // Get attributes of ItemCategory
            Element[] categories = getElementsByTagNameNR(item, "Category");
            ArrayList<String> category_list = new ArrayList<String>();

            for (Element category : categories) {
                category_list.add(category.getTextContent());
            }

            // Get attributes of Seller
            int seller_rating = Integer.parseInt((getElementByTagNameNR(item, "Seller")).getAttribute("Rating"));

            // Get all Bid children of Bids
            Element bidsRoot = getElementByTagNameNR(item, "Bids");
            Element[] bids = getElementsByTagNameNR(bidsRoot, "Bid");

            Bidder newBidder = new Bidder();
            Bid newBid = new Bid();

            for (Element bid : bids) {
                // Get attributes of the bidder
                Element bidder = getElementByTagNameNR(bid, "Bidder");
                String bidder_id = bidder.getAttribute("UserID");
                newBidder.rating =  Integer.parseInt(bidder.getAttribute("Rating"));
                newBidder.location = getElementTextByTagNameNR(bidder, "Location");
                newBidder.country = getElementTextByTagNameNR(bidder, "Country");
                bidderHT.put(bidder_id, newBidder);

                // Get attributes of the bid
                String time = formatDate(getElementTextByTagNameNR(bid, "Time"));
                newBid.time = time;
                newBid.amount = strip(getElementTextByTagNameNR(bid, "Amount"));
                newBid.bidderID = bidder_id;
                String bid_id = bidder_id + Long.toString(item_id) + time;
                bidHT.put(bid_id, newBid);

                // Reset variables???
            }

            itemHT.put(item_id, newItem);
            itemCategoryHT.put(item_id, category_list);
            sellerHT.put(userID, seller_rating);
            HashMap<Object, Object> map = new HashMap<Object, Object>(itemHT);
            writeMapToFile("items.dat", map);
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
        for(Map.Entry<Integer, Item> entry : itemHT.entrySet()) {
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
