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
    String itemId;
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
    String longitude;
    String latitude;

    public Item(String id) {
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

    public void setLatitudeLongitude(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setBuyPrice(String buyPrice) {
        this.buy_price = buyPrice;
    }

    public void setSellerID(String id) {
        this.sellerID = id;
    }

    public String toString() {
        String buyStr = (buy_price != null) ? String.valueOf(buy_price) : "\\N";
        String longitudeStr = (longitude != null) ? longitude : "\\N";
        String latitudeStr = (latitude != null) ? latitude : "\\N";

        return itemId + "\t" + name + "\t" + currently + "\t" + first_bid + "\t" +
                buyStr + "\t" + num_bids + "\t" + started + "\t" + ends + "\t" +
                description + "\t" + sellerID + "\t" + location + "\t" + country + "\t"
                + latitudeStr + "\t" + longitudeStr;
    }
}

class ItemCategory {
    String itemId;
    ArrayList<String> categories;

    public ItemCategory(String iid) {
        this.itemId = iid;
        categories = new ArrayList<String>();
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public String toString() {
        String output = "";
        for (String category : categories) {
            output += itemId + "\t" + category + "\n";
        }
        output = output.substring(0, output.length() - 1);      // remove last new line
        return output;
    }
}

class User {
    String userId;
    int rating;

    public User(String id) {
        this.userId = id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String toString() {
        return userId + "\t" + rating;
    }
}

class Bidder extends User {
    String location;
    String country;

    public Bidder(String id) {
        super(id);
    }

    public void setLocationInfo(String location, String country) {
        this.location = location;
        this.country = country;
    }

    public String toString() {
        return super.toString() + "\t" + location + "\t" + country;
    }
}

class Bid {
    String itemId;
    String bidderId;
    String time;
    String amount;

    public Bid(String iid, String bid, String time, String amount) {
        this.itemId = iid;
        this.bidderId = bid;
        this.time = time;
        this.amount = amount;
    }

    public String toString() {
        return itemId + "\t" + bidderId + "\t" + time + "\t" + amount;
    }
}

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

    static Map<String, Item> itemHT = new HashMap<String, Item>();
    //static Map<Long, ItemCategory> itemCategoryHT = new HashMap<Long, ItemCategory>();
    static ArrayList<ItemCategory> categoryList = new ArrayList<ItemCategory>();
    static Map<String, User> sellerHT = new HashMap<String, User>();
    static Map<String, Bidder> bidderHT = new HashMap<String, Bidder>();
    // static Map<String, Bid> bidHT = new HashMap<String, Bid>();
    static ArrayList<Bid> bidList = new ArrayList<Bid>();
    static int maxDescriptionLength = 4000;

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
    static void writeMapToFile(String filename, HashMap<String, Object> map) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            PrintWriter printer = new PrintWriter(writer);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                printer.print(entry.getValue());
                printer.println();
            }
            printer.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to tab-delimited file!");
        }
    }

    /*  Writes the contents of an arraylist to a tab-delimited file.
     */
    static void writeListToFile(String filename, ArrayList<Object> list) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            PrintWriter printer = new PrintWriter(writer);
            for (Object o : list) {
                printer.print(o);
                printer.println();
            }
            printer.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to tab-delimited file!");
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
            String item_id = item.getAttribute("ItemID");

            // Get all properties of current Item
            String name = getElementTextByTagNameNR(item, "Name");
            String currently = strip(getElementTextByTagNameNR(item, "Currently"));
            String first_bid = strip(getElementTextByTagNameNR(item, "First_Bid"));
            String buy_price = strip(getElementTextByTagNameNR(item, "Buy_Price"));
            int num_bids = Integer.parseInt(getElementTextByTagNameNR(item, "Number_of_Bids"));
            String started = formatDate(getElementTextByTagNameNR(item, "Started"));
            String ends = formatDate(getElementTextByTagNameNR(item, "Ends"));
            String description = getElementTextByTagNameNR(item, "Description");
            if (description.length() > maxDescriptionLength)
                description = description.substring(0, maxDescriptionLength - 1); // truncate string to 4000 chars
            description = description.replace("\"", "\\\"");
            String sellerID = (getElementByTagNameNR(item, "Seller")).getAttribute("UserID");
            String loc = getElementTextByTagNameNR(item, "Location");
            String item_country = getElementTextByTagNameNR(item, "Country");

            Element locationElement = getElementByTagNameNR(item, "Location");
            String longitude = locationElement.getAttribute("Longitude");
            String latitude = locationElement.getAttribute("Latitude");

            // Construct the current Item
            Item newItem = new Item(item_id);
            newItem.setRequiredAttributes(name, currently, first_bid, num_bids, started, ends, description, loc, item_country);
            newItem.setLatitudeLongitude(latitude, longitude);
            newItem.setBuyPrice(buy_price);
            newItem.setSellerID(sellerID);

            // Construct ItemCategory object for current Item
            Element[] categories = getElementsByTagNameNR(item, "Category");
            ItemCategory newItemCategory = new ItemCategory(item_id);
            for (Element category : categories) {
                newItemCategory.addCategory(category.getTextContent());
            }

            // Construct the Seller
            int seller_rating = Integer.parseInt((getElementByTagNameNR(item, "Seller")).getAttribute("Rating"));
            User seller = new User(sellerID);
            seller.setRating(seller_rating);

            // Get all Bid children of Bids
            Element bidsRoot = getElementByTagNameNR(item, "Bids");
            Element[] bids = getElementsByTagNameNR(bidsRoot, "Bid");
            Bidder newBidder;
            Bid newBid;

            for (Element bid : bids) {
                // Construct the Bidder for current Bid
                Element bidder = getElementByTagNameNR(bid, "Bidder");
                String bidder_id = bidder.getAttribute("UserID");
                newBidder = new Bidder(bidder_id);
                newBidder.setRating(Integer.parseInt(bidder.getAttribute("Rating")));
                String location = getElementTextByTagNameNR(bidder, "Location");
                String country = getElementTextByTagNameNR(bidder, "Country");
                newBidder.setLocationInfo(location, country);

                // Construct the current Bid
                String time = formatDate(getElementTextByTagNameNR(bid, "Time"));
                String amount = strip(getElementTextByTagNameNR(bid, "Amount"));
                // String bid_id = Long.toString(item_id) + bidder_id + time;
                newBid = new Bid(item_id, bidder_id, time, amount);

                bidderHT.put(bidder_id, newBidder);
                bidList.add(newBid);
                //bidHT.put(bid_id, newBid);
            }
            
            itemHT.put(item_id, newItem);
            categoryList.add(newItemCategory);
            sellerHT.put(sellerID, seller);
        }

        HashMap<String, Object> itemMap = new HashMap<String, Object>(itemHT);
        writeMapToFile("item.dat", itemMap);
        ArrayList<Object> itemCategoryList = new ArrayList<Object>(categoryList);
        writeListToFile("itemCategory.dat", itemCategoryList);
//      HashMap<Object, Object> itemCategoryMap = new HashMap<Object, Object>(itemCategoryHT);
//      writeMapToFile("itemCategory.dat", itemCategoryMap);
        HashMap<String, Object> bidderMap = new HashMap<String, Object>(bidderHT);
        writeMapToFile("bidder.dat", bidderMap);
        // HashMap<Object, Object> bidMap = new HashMap<Object, Object>(bidHT);
        // writeMapToFile("bid.dat", bidMap);
        ArrayList<Object> bidListOutput = new ArrayList<Object>(bidList);
        writeListToFile("bid.dat", bidListOutput);
        HashMap<String, Object> sellerMap = new HashMap<String, Object>(sellerHT);
        writeMapToFile("seller.dat", sellerMap);

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
