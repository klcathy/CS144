package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
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

import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

public class MyParser {
    static String formatDate(String str) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String outputDate = "";
        try {
            Date parsed = parseFormat.parse(str);
            outputDate = outputFormat.format(parsed);
        } catch (ParseException pe) {
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

    static String getAttributeText(Element e, String attr) {
        Node attributeNode = e.getAttributes().getNamedItem(attr);
        if (attributeNode != null) {
            return attributeNode.getNodeValue();
        } else {
            return null;
        }
    }

    static ItemResult parseXML(String xml) throws Exception {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder b;

        Document doc = null;
        try {
            b = fac.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            doc = b.parse(is);
        } catch (Exception e) {
            // do something
            //e.printStackTrace();
           // System.exit(3);
        }

        Element item = doc.getDocumentElement();

        // Get all Item children of Items
        //Element[] items = getElementsByTagNameNR(root, "Item");

        //for (Element item : items) {
//            String item_id = item.getAttribute("ItemID");
//
//            // Get all properties of current Item
//            String name = getElementTextByTagNameNR(item, "Name");
//            String currently = getElementTextByTagNameNR(item, "Currently");
//            String first_bid = getElementTextByTagNameNR(item, "First_Bid");
//            String buy_price = getElementTextByTagNameNR(item, "Buy_Price");
//            String num_bids = getElementTextByTagNameNR(item, "Number_of_Bids");
//            String started = formatDate(getElementTextByTagNameNR(item, "Started"));
//            String ends = formatDate(getElementTextByTagNameNR(item, "Ends"));
//            String description = getElementTextByTagNameNR(item, "Description");
//            description = description.replace("\"", "\\\""); // what's this for?
//            String seller_id = (getElementByTagNameNR(item, "Seller")).getAttribute("UserID");
//            String location = getElementTextByTagNameNR(item, "Location");
//            String item_country = getElementTextByTagNameNR(item, "Country");
//
//            Element locationElement = getElementByTagNameNR(item, "Location");
//            String longitude = locationElement.getAttribute("Longitude");
//            String latitude = locationElement.getAttribute("Latitude");
//
//            // Construct ItemCategory object for current Item
//            Element[] categories = getElementsByTagNameNR(item, "Category");
//            ArrayList<String> categoriesList = new ArrayList<String>();
//            for (Element category : categories) {
//                categoriesList.add(category.getTextContent());
//            }
//
//            // Construct the Seller
//            String seller_rating = (getElementByTagNameNR(item, "Seller")).getAttribute("Rating");
//
//            // Get all Bid children of Bids
//            Element bidsRoot = getElementByTagNameNR(item, "Bids");
//            Element[] bids = getElementsByTagNameNR(bidsRoot, "Bid");
//
//            for (Element bid : bids) {
//                // Construct the Bidder for current Bid
//                Element bidder = getElementByTagNameNR(bid, "Bidder");
//                String bidder_id = bidder.getAttribute("UserID");
//                String bidder_location = getElementTextByTagNameNR(bidder, "Location");
//                String country = getElementTextByTagNameNR(bidder, "Country");
//
//                // Construct the current Bid
//                String time = formatDate(getElementTextByTagNameNR(bid, "Time"));
//                String amount = getElementTextByTagNameNR(bid, "Amount");
//            }

            String item_id = "weeee";


//        // Get all properties of current Item
//        String name = getElementTextByTagNameNR(item, "name");
//        String currently = getElementTextByTagNameNR(item, "currently");
//        String first_bid = getElementTextByTagNameNR(item, "first_bid");
//        String buy_price = getElementTextByTagNameNR(item, "buy_price");
//        String num_bids = getElementTextByTagNameNR(item, "number_of_bids");
//        String started = formatDate(getElementTextByTagNameNR(item, "started"));
//        String ends = formatDate(getElementTextByTagNameNR(item, "ends"));
//        String description = getElementTextByTagNameNR(item, "description");
//        description = description.replace("\"", "\\\""); // what's this for?
//        String seller_id = (getElementByTagNameNR(item, "seller")).getAttribute("userid");
//        String location = getElementTextByTagNameNR(item, "location");
//        String item_country = getElementTextByTagNameNR(item, "country");
//
//        Element locationElement = getElementByTagNameNR(item, "location");
//        String longitude = locationElement.getAttribute("longitude");
//        String latitude = locationElement.getAttribute("latitude");
//
//        // Construct ItemCategory object for current Item
//        Element[] categories = getElementsByTagNameNR(item, "category");
//        ArrayList<String> categoriesList = new ArrayList<String>();
//        for (Element category : categories) {
//            categoriesList.add(category.getTextContent());
//        }
//
//        // Construct the Seller
//        String seller_rating = (getElementByTagNameNR(item, "seller")).getAttribute("rating");

        // Get all Bid children of Bids
//        Element bidsRoot = getElementByTagNameNR(item, "bids");
//        Element[] bids = getElementsByTagNameNR(bidsRoot, "bid");
//
//        for (Element bid : bids) {
//            // Construct the Bidder for current Bid
//            Element bidder = getElementByTagNameNR(bid, "bidder");
//            String bidder_id = bidder.getAttribute("userid");
//            String bidder_location = getElementTextByTagNameNR(bidder, "location");
//            String country = getElementTextByTagNameNR(bidder, "country");
//
//            // Construct the current Bid
//            String time = formatDate(getElementTextByTagNameNR(bid, "time"));
//            String amount = getElementTextByTagNameNR(bid, "amount");
//        }

//        ItemResult ir = new ItemResult(item_id, name, currently, first_bid, num_bids, started, ends, seller_id, seller_rating, description);
//
//        ir.setBuyPrice(buy_price);
//        ir.setLocationInfo(location, item_country, longitude, latitude);

        return new ItemResult("hey");
        //return ir;
      //  }
        //return new ItemResult();
    }
}
