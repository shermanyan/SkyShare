package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.JDBCConnector;
import resources.User;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Handles the HTTP POST request for user sign up.
	 *
	 * @param request  username, password, and phoneNumber in JSON format
	 * @param response either the user object or an error message in JSON format
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Sign Up Attempt");
		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();

		User user = new Gson().fromJson(request.getReader(), User.class);

		try {
			int result = JDBCConnector.addUser(user);

			if (result == -1) {
				
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				pw.write("Username already exists");
				System.out.println("Username already exists");
			} else if (result == -2) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				pw.write("Phone number already exists");
				System.out.println("Phone number already exists");
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
				pw.write(gson.toJson(user));
				System.out.println("Signup Success" + user.userID);
				
			}
			pw.flush();

		} catch (Exception e) {
			throw new ServletException("Signup failed", e);
		}

	}

}
