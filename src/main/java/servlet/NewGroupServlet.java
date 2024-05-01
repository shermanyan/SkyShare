package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.JDBCConnector;
import resources.Group;

/**
 * Servlet implementation class NewGroupServlet
 */
@WebServlet("/NewGroupServlet")
public class NewGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewGroupServlet() {
		super();
	}

	/**
	 * Handles the HTTP POST request.
	 *
	 * @param request  departureTime and pickupLocation
	 * @param response groupID
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Group group = new Gson().fromJson(request.getReader(), Group.class);
		
		int groupID = JDBCConnector.addGroup(group);

		// Convert groupID to a String before writing it to the response
		response.getWriter().write(String.valueOf(groupID));
	}
}