package edu.ucla.cs.cs144;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String q = " ";

        if (request.getParameter("q") != null) {
            q = request.getParameter("q");
        }

        try {
            // Set up HttpURLConnection
            URL url = new URL("http://google.com/complete/search?output=toolbar&q=" + q);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output = "";
                String line = "";

                while ((line = br.readLine()) != null) {
                    output += line;
                }

                PrintWriter out = response.getWriter();
                out.println(output);

                br.close();
                conn.disconnect();
            }
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }

        response.setContentType("text/xml");
    }
}
