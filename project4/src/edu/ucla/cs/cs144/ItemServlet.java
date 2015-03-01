package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ItemServlet extends HttpServlet implements Servlet {

    public ItemServlet() {}

    public static Document stringToDocument(String xml)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader reader = new StringReader(xml);
            InputSource is = new InputSource(reader);
            Document doc = builder.parse(is);
            return doc;

        } catch (Exception e) {

        }

        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String query = "";

        if (request.getParameter("id") != null) {
            query = request.getParameter("id");
        }

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

        request.setAttribute("Item", item);
        request.getRequestDispatcher("/itemResults.jsp").forward(request, response);

    }

}
