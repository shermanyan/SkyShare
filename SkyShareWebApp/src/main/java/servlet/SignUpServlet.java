package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;

import database.JDBCConnector;
import resources.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        
        User user = gson.fromJson(request.getReader(), User.class);
        
        String username = user.username;
        String password = user.password;
        String email = user.email;

        if (infoMissing(username, password, email)) {
        	
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            
            String error = "User info missing";
            pw.write(gson.toJson(error));
            pw.flush();
        }
        
        else {
        	int userID = JDBCConnector.signUpUser(username, password, email);
        	
        	// Username was already taken, send error
        	if (userID == -1) {
        		
        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        		
        		String error = "Username is taken";
        		pw.write(gson.toJson(error));
        		pw.flush();
        	}
        	
        	// Email already taken, send error
        	else if (userID == -2) {
	    	   
        		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        		
        		String error = "Email is already registered";
        		pw.write(gson.toJson(error));
        		pw.flush();
        	}
        	
        	// No errors
        	else {
        		response.setStatus(HttpServletResponse.SC_OK);
	           	
				String user_id = String.valueOf(userID);
				
				HttpSession session = request.getSession();
				session.setAttribute("user_id", user_id);
			
				pw.write(gson.toJson(user_id));
				pw.flush();
	       }
        }
    }
    
    public boolean infoMissing(String username, String password, String email) {
    	if (username == null || username.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
    		return true;
    	}
    	return false;
    }
}
