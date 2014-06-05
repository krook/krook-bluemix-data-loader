package net.bluemix.krook.read;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bluemix.krook.City;

/**
 * Servlet implementation class ReaderServlet
 */
@WebServlet("/read")
public class ReaderServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("%s=%s%n", envName, env.get(envName));
		}
		
		ArrayList<City> pList = PostgreSQLDataReader.read();
		ArrayList<City> mList = MongoDBDataReader.read();
		
		request.setAttribute("pList", pList);
		request.setAttribute("mList", mList);
		request.getRequestDispatcher("/WEB-INF/read.jsp").forward(request, response);
	}

}
