package servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.JDBCConnector;

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

		String departureTimeString = request.getParameter("departureTime");
		Timestamp departureTime = Timestamp.valueOf(departureTimeString);
		String pickupLocation = request.getParameter("pickupLocation");

	
		int groupID = JDBCConnector.addGroup(departureTime, pickupLocation);

		response.getWriter().write(groupID);
	}
}
