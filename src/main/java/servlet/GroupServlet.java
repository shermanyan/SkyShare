package servlet;

import com.google.gson.Gson;

import database.JDBCConnector;
import resources.Group;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
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

	class requestJson {
		public String user_ID;
		public String group_ID;
		public boolean join;
	}

	/**
	 * Handles the HTTP POST request.
	 *
	 * @param request  user_ID, group_ID, and join: true - join group, false - leave
	 *                 group
	 * @param response "Success!"
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Read the request body into a string
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String requestBody = sb.toString();

		// Print the request body
		System.out.println("Request body: " + requestBody);

		requestJson request_json = new Gson().fromJson(requestBody, requestJson.class);

		System.out.println("Parsed request: " + request_json);

		if (request_json.join) {
			JDBCConnector.joinGroup(Integer.parseInt(request_json.user_ID), Integer.parseInt(request_json.group_ID));
		} else {
			JDBCConnector.leaveGroup(Integer.parseInt(request_json.user_ID), Integer.parseInt(request_json.group_ID));
		}

		response.getWriter().write("Success!");
	}
}