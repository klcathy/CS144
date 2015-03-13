package edu.ucla.cs.cs144;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ItemServlet extends HttpServlet implements Servlet {

    public ItemServlet() {}

    public static Document stringToDocument(String xml)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //xml.replaceAll(">\\s+<", "><");
            //ByteArrayInputStream encXML = new ByteArrayInputStream(xml.getBytes());
            StringReader reader = new StringReader(xml);
            InputSource is = new InputSource();
            is.setCharacterStream(reader);
            Document doc = builder.parse(is);
            return doc;

        } catch (Exception e) {

        }

        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Open a session
        HttpSession session = request.getSession(true);

        String query = "";

        if (request.getParameter("id") != null) {
            query = request.getParameter("id");
        }
        request.setAttribute("id", query);
        String xml = AuctionSearchClient.getXMLDataForItemId(query);
        request.setAttribute("xml", xml);

        // Item does not exist
        if (xml == null || xml.isEmpty()) {
            request.setAttribute("xml", "");
            request.getRequestDispatcher("/itemResults.jsp").forward(request, response);
            return;
        }

        ItemResult item = new ItemResult();

        try {
            Document doc = stringToDocument(xml);
            Element root = doc.getDocumentElement();
            item = MyParser.parseXML(root);

        } catch (Exception e) {

        }

        // Set session attributes
        session.setAttribute("itemId", item.getItemID());
        session.setAttribute("name", item.getName());
        session.setAttribute("buyPrice", item.getBuyPrice());

        request.setAttribute("Item", item);
        request.getRequestDispatcher("/itemResults.jsp").forward(request, response);

    }

}
