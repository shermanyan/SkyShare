package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;

import database.JDBCConnector;

/**
 * This class represents a servlet that handles database requests.
 */
@WebServlet("/DatabaseServlet")
public class DatabaseServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    /**
     * Handles the GET request and retrieves all data from the database.
     *
     * @param req  the HttpServletRequest object that contains the request
     *             information
     * @param resp the HttpServletResponse object that contains the response
     *             information
     * @throws ServletException if the servlet encounters a problem
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = JDBCConnector.getAllData();

        // Get user, password, and url from environment variables
        String user = System.getenv("JDBC_USER");
        String password = System.getenv("JDBC_PASSWORD");
        String url = System.getenv("JDBC_URL");

        // Add user, password, and url to data
        data.put("user", user);
        data.put("password", password);
        data.put("url", url);

        // Convert data to JSON
        String json = gson.toJson(data);

        // Write JSON to response
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }
}