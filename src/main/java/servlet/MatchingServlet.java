package servlet;

import java.io.BufferedReader;
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

	/**
	 * Handles the GET request for the MatchingServlet.
	 *
	 * @param request  departureTime and pickupLocation
	 * @param response Group objects in JSON format
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String departureTime = request.getParameter("departureTime");
		Timestamp departureTimestamp = Timestamp.valueOf(departureTime);
		String pickupLocation = request.getParameter("pickupLocation");

		ArrayList<Group> groups = JDBCConnector.getFilteredGroups(departureTimestamp, pickupLocation);

		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
		pw.write(gson.toJson(groups));
	}

}
