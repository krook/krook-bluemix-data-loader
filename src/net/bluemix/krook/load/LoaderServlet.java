package net.bluemix.krook.load;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/load")
public class LoaderServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("%s=%s%n", envName, env.get(envName));
		}
		
		boolean success = (PostgreSQLDataLoader.load() && MongoDBDataLoader.load());
		request.setAttribute("success", success);
		request.getRequestDispatcher("/WEB-INF/load.jsp").forward(request, response);
	}

}
