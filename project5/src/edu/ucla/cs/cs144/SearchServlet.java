package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String query = "";
        int defaultSkip = 0;
        int defaultReturn = 10;
        int numResultsToSkip = 0;
        int numResultsToReturn = 10;

        // Get parameters
        if (request.getParameter("q") != null) {
            query = request.getParameter("q");
        }
        if (request.getParameter("numResultsToSkip") != null) {
            numResultsToSkip = stringToInt(request.getParameter("numResultsToSkip"), defaultSkip);
        }
        if (request.getParameter("numResultsToReturn") != null) {
            numResultsToReturn = stringToInt(request.getParameter("numResultsToReturn"), defaultReturn);
        }

        // No negative int parameters allowed
        if (numResultsToSkip < 0)
            numResultsToSkip = defaultSkip;
        if (numResultsToReturn < 0)
            numResultsToReturn = defaultReturn;

        SearchResult[] results = AuctionSearchClient.basicSearch(query, numResultsToSkip, numResultsToReturn);
        SearchResult[] moreResults = AuctionSearchClient.basicSearch(query, numResultsToSkip+numResultsToReturn, 1);

        // Pass back request data
        request.setAttribute("results", results);
        request.setAttribute("q", query);
        request.setAttribute("numResultsToSkip", numResultsToSkip);
        request.setAttribute("numResultsToReturn", numResultsToReturn);
        request.setAttribute("hasMore", moreResults.length == 1);
        request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
    }

    public int stringToInt(String s, int defaultVal) {
        int result;
        try {
            result = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            result = defaultVal;
        }
        return result;
    }
}
