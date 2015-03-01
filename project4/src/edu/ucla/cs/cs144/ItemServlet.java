package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String query = "";
        String test = "";

        if (request.getParameter("id") != null) {
            query = request.getParameter("id");
        }

          String xml = AuctionSearchClient.getXMLDataForItemId(query);
//
//        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
//        DocumentBuilder b;
//
//        Document doc = null;
//        try {
//            b = fac.newDocumentBuilder();
//            InputSource is = new InputSource(new StringReader(xml));
//            doc = b.parse(is);
//        } catch (Exception e) {
//            // do something
//        }
        test = "zzzzzzzzzz";
        ItemResult result = null;
        try {
            result = MyParser.parseXML(xml);
            test = result.getItemId();

        }
        catch (Exception e) {
            // do something
            e.printStackTrace();
        }

        request.setAttribute("test", test);

        request.setAttribute("xml", xml);
        if (result != null) {
            request.setAttribute("result", result);
        } else {
            request.setAttribute("no", "why");
        }

        request.getRequestDispatcher("/getItemResults.jsp").forward(request, response);

    }
}
