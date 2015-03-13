package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;

public class SessionServlet extends HttpServlet implements Servlet {

    public SessionServlet() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Open a previous session
        HttpSession session = request.getSession();

        // Get credit card information
        String credit_card_num = request.getParameter("credit_card");
        session.setAttribute("credit_card_num", credit_card_num);

    }

}
