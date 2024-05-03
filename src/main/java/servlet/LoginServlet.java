package servlet;

/**
 * Servlet implementation class LoginServlet
 */
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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new LoginServlet.
     */
    public LoginServlet() {
        super();
    }

    /**
     * Handles the HTTP POST method for user login.
     *
     * @param request  username and password in JSON format
     * @param response either "Incorrect username or password" or the User object in
     *                 JSON format
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Login");
        User user = new Gson().fromJson(request.getReader(), User.class);
        PrintWriter pw = response.getWriter();
        String username = user.username;
        String password = user.password;
        Gson gson = new Gson();

        try {
            user = JDBCConnector.loginUser(username, password);
            if (User.validateUser(user) == false) {
                System.out.println("Login Failed");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                pw.write(gson.toJson("Incorrect username or password"));
            } else {
                System.out.println("LoginSucess:" + user.userID);
                response.setStatus(HttpServletResponse.SC_OK);
                pw.write(gson.toJson(user));
            }
            pw.flush();
        } catch (Exception e) {
            throw new ServletException("Login failed", e);
        }
    }
}
