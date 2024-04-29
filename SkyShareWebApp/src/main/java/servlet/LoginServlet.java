package final_project_website_test;
// TODO: This will need to change

import javax.servlet.*;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.gson.Gson;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
		
		User user = gson.fromJson(request.getReader(), User.class);
		String username = user.username;
		String password = user.password;
		
		JsonObject jsonResponse = new JsonObject();
		
		if (infoMissing(username, password)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
			jsonResponse.addProperty("error", "Username or password is missing");
			jsonResponse.addProperty("success", false);
		}
		
		else {
			if (JDBCConnector.loginUser(username, password)) {
				String user_id = JDBCConnector.getUserID(username, password);
				
				HttpSession session = request.getSession();
				session.setAttribute("user_id", user_id);
				
				jsonResponse.addProperty("success", true);
				jsonResponse.addProperty("user_id", user_id);
			}
		}
		pw.write(gson.toJson(jsonResponse));
		pw.close();
	}
	
    public boolean infoMissing(String username, String password) {
    	if (username == null || username.isBlank() || password == null || password.isBlank()) {
    		return true;
    	} return false;
    }
}