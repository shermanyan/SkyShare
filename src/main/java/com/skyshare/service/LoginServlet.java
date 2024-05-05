package com.skyshare.service;


import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginServlet extends HttpServlet {
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
    private JdbcTemplate jdbcTemplate;

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // SQL query to fetch data from the table
        String sqlQuery = "SELECT name, age, city FROM users";

        // Execute the query and fetch results
        List<Map<String, Object>> tableData = jdbcTemplate.queryForList(sqlQuery);
        

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
        // Generate table header
        out.println("<tr>");
        out.println("<th>Name</th>");
        out.println("<th>Age</th>");
        out.println("<th>City</th>");
        out.println("</tr>");
        // Populate table with data
        for (Map<String, Object> row : tableData) {
            out.println("<tr>");
            out.println("<td>" + row.get("name") + "</td>");
            out.println("<td>" + row.get("age") + "</td>");
            out.println("<td>" + row.get("city") + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}
