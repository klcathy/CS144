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

    static ItemResult parseXML(Element root) throws Exception {
        ItemResult item = new ItemResult();

        // Get all properties of current Item
        String item_id = getAttributeText(root, "ItemID");
        String name = getElementTextByTagNameNR(root, "Name");
        String currently = getElementTextByTagNameNR(root, "Currently");
        String first_bid = getElementTextByTagNameNR(root, "First_Bid");
        String buy_price = getElementTextByTagNameNR(root, "Buy_Price");
        String num_bids = getElementTextByTagNameNR(root, "Number_of_Bids");
        String started = formatDate(getElementTextByTagNameNR(root, "Started"));
        String ends = formatDate(getElementTextByTagNameNR(root, "Ends"));
        String description = getElementTextByTagNameNR(root, "Description");
        description = description.replace("\"", "\\\""); // what's this for?
        String seller_id = (getElementByTagNameNR(root, "Seller")).getAttribute("UserID");
        String location = getElementTextByTagNameNR(root, "Location");
        Element locationElement = getElementByTagNameNR(root, "Location");
        String longitude = locationElement.getAttribute("Longitude");
        String latitude = locationElement.getAttribute("Latitude");
        String item_country = getElementTextByTagNameNR(root, "Country");

        // Construct the Seller
        String seller_rating = (getElementByTagNameNR(root, "Seller")).getAttribute("Rating");

        ItemResult ir = new ItemResult(item_id, name, currently, first_bid, num_bids, started, ends, seller_id, seller_rating, description);
        ir.setBuyPrice(buy_price);
        ir.setLocationInfo(location, item_country, longitude, latitude);

        return ir;
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


        //return ir;
      //  }
        //return new ItemResult();
    }
}
