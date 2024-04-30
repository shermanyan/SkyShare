package servlet;

import com.google.gson.Gson;

import database.JDBCConnector;
import resources.Group;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/GroupServlet")
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor for GroupServlet.
	 */
	public GroupServlet() {
		super();
	}

	/**
	 * Handles the HTTP GET request.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if there is an error processing the request
	 * @throws IOException      if there is an error writing the response
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ArrayList<Group> groups = JDBCConnector.getGroups();
			PrintWriter pw = response.getWriter();
			Gson gson = new Gson();

			pw.write(gson.toJson(groups));
			pw.flush();

		} catch (Exception e) {
			throw new ServletException("Failed to get groups", e);
		}
	}

	/**
	 * Handles the HTTP POST request.
	 *
	 * @param request  user_ID, group_ID, and join: true - join group, false - leave
	 *                 group
	 * @param response "Success!"
	 * @throws ServletException if there is an error processing the request
	 * @throws IOException      if there is an error writing the response
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userID = Integer.parseInt(request.getParameter("user_ID"));
		int groupID = Integer.parseInt(request.getParameter("group_ID"));
		boolean join = Boolean.parseBoolean(request.getParameter("join"));

		if (join) {
			JDBCConnector.joinGroup(userID, groupID);
		} else {
			JDBCConnector.leaveGroup(userID, groupID);
		}

		response.getWriter().write("Success!");
	}
}