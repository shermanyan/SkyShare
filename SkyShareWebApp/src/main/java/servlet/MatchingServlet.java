package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.JDBCConnector;
import resources.Group;

/**
 * Servlet implementation class MatchingServlet
 */
@WebServlet("/MatchingServlet")
public class MatchingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String departureTimeString = request.getParameter("departureTime");
			Timestamp departureTime = Timestamp.valueOf(departureTimeString);			
			
			String pickupLocation = request.getParameter("pickupLocation");
			ArrayList<Group> groups = JDBCConnector.getFilteredGroups(departureTime, pickupLocation);
			
			String groupsJson = new Gson().toJson(groups);
			
			PrintWriter pw = response.getWriter();
			Gson gson = new Gson();
			pw.write(gson.toJson(groups));

		} catch (Exception e) {
			throw new ServletException("Failed to get groups", e);
		}
	}


}
