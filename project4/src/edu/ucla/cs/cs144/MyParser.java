package edu.ucla.cs.cs144;

import java.util.*;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class MyParser {
    static int maxDescriptionLength = 4000;

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

    static ItemResult parseXML(Element currentItem) throws Exception {

        // Get all properties of current Item
        String item_id = getAttributeText(currentItem, "ItemID");
        String name = getElementTextByTagNameNR(currentItem, "Name");
        String currently = getElementTextByTagNameNR(currentItem, "Currently");
        String first_bid = getElementTextByTagNameNR(currentItem, "First_Bid");
        String buy_price = getElementTextByTagNameNR(currentItem, "Buy_Price");
        String num_bids = getElementTextByTagNameNR(currentItem, "Number_of_Bids");
        String started = formatDate(getElementTextByTagNameNR(currentItem, "Started"));
        String ends = formatDate(getElementTextByTagNameNR(currentItem, "Ends"));
        String description = getElementTextByTagNameNR(currentItem, "Description");
        if (description.length() > maxDescriptionLength)
            description = description.substring(0, maxDescriptionLength); // truncate string to 4000 chars

        String location = getElementTextByTagNameNR(currentItem, "Location");
        Element locationElement = getElementByTagNameNR(currentItem, "Location");
        String longitude = locationElement.getAttribute("Longitude");
        String latitude = locationElement.getAttribute("Latitude");
        String item_country = getElementTextByTagNameNR(currentItem, "Country");

        // Get seller information
        String seller_id = (getElementByTagNameNR(currentItem, "Seller")).getAttribute("UserID");
        String seller_rating = (getElementByTagNameNR(currentItem, "Seller")).getAttribute("Rating");

        // Get all catgories of current Item
        Element[] categories = getElementsByTagNameNR(currentItem, "Category");
        ArrayList<String> categoriesList = new ArrayList<String>();
        for (Element category : categories) {
            categoriesList.add(category.getTextContent());
        }
        String[] categoryArray = new String[categoriesList.size()];
        categoryArray = categoriesList.toArray(categoryArray);

        // Get all Bid children of Bids
        Element bidsRoot = getElementByTagNameNR(currentItem, "Bids");
        Element[] bids = getElementsByTagNameNR(bidsRoot, "Bid");
        ArrayList<BidResult> bidList = new ArrayList<BidResult>();
        for (Element bid : bids) {
            // Construct the Bidder for current Bid
            Element bidder = getElementByTagNameNR(bid, "Bidder");
            String bidder_id = bidder.getAttribute("UserID");
            String bidder_rating = bidder.getAttribute("Rating");
            String bidder_location = getElementTextByTagNameNR(bidder, "Location");
            String country = getElementTextByTagNameNR(bidder, "Country");

            // Construct the current Bid
            String time = formatDate(getElementTextByTagNameNR(bid, "Time"));
            String amount = getElementTextByTagNameNR(bid, "Amount");

            BidResult currBid = new BidResult(bidder_id, bidder_rating, time, amount);
            currBid.setLocationInfo(bidder_location, country);
            bidList.add(currBid);
        }
        Collections.sort(bidList);
        BidResult[] bidArray = new BidResult[bidList.size()];
        bidArray = bidList.toArray(bidArray);

        ItemResult ir = new ItemResult(item_id, name, currently, first_bid, num_bids, started, ends, seller_id, seller_rating, description);
        ir.setBuyPrice(buy_price);
        ir.setLocationInfo(location, item_country, longitude, latitude);
        ir.setCategories(categoryArray);
        ir.setBids(bidArray);

        return ir;
    }
}
