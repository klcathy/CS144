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
        int numResultsToSkip = 0;
        int numResultsToReturn = 10;

        // Get parameters
        if (request.getParameter("q") != null) {
            query = request.getParameter("q");
        }
        if (request.getParameter("numResultsToSkip") != null) {
            numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        }
        if (request.getParameter("numResultsToReturn") != null) {
            numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        }

        // No negative int parameters allowed
        if (numResultsToSkip < 0)
            numResultsToSkip = 0;
        if (numResultsToReturn < 0)
            numResultsToReturn = 10;

        SearchResult[] results = AuctionSearchClient.basicSearch(query, numResultsToSkip, numResultsToReturn);

        // Pass back request data
        request.setAttribute("results", results);
        request.setAttribute("q", query);
        request.setAttribute("numResultsToSkip", numResultsToSkip);
        request.setAttribute("numResultsToReturn", numResultsToReturn);

        request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
    }
}
