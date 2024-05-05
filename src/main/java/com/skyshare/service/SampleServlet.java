package com.skyshare.service;


import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SampleServlet extends HttpServlet {
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Dummy data for the table
        List<String[]> tableData = new ArrayList<>();
        tableData.add(new String[]{"Name", "Age", "City"});
        tableData.add(new String[]{"John Doe", "30", "New York"});
        tableData.add(new String[]{"Jane Smith", "25", "Los Angeles"});
        tableData.add(new String[]{"Mike Johnson", "35", "Chicago"});

        // Set response content type
        response.setContentType("text/html");

        // Create PrintWriter to write HTML response
        PrintWriter out = response.getWriter();

        // Generate HTML table
        out.println("<html>");
        out.println("<head><title>Table Data</title></head>");
        out.println("<body>");
        out.println("<h2>Table Data</h2>");
        out.println("<table border=\"1\">");
        for (String[] row : tableData) {
            out.println("<tr>");
            for (String cell : row) {
                out.println("<td>" + cell + "</td>");
            }
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}
