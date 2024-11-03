
package com.demo.controller;

import com.demo.dao.ProductDao;
import com.demo.model.Product;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false); //getting the httpsession which is stored on the server not visible on the client side
        String sessionToken = null;

        // Get the session token from the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionToken".equals(cookie.getName())) {//if sessionToken is found, sessionToken variable is set
                    sessionToken = cookie.getValue();
                    break;
                }
            }
        }

        if (httpSession != null && sessionToken != null) {   //if either is null, this means user is not logged in
            String storedToken = (String) httpSession.getAttribute("sessionToken");

            // Validate the token
            if (!sessionToken.equals(storedToken)) { //checking whether the storedToken on the server and the client side is the same or not, if not error is displayed
                response.sendRedirect("login.jsp?error=Session invalid.");
                return;
            }

            // Proceed with fetching products
            Session session = HibernateUtil.openSession();  //connection to the database
            try {
                ProductDao productDao = new ProductDao(session);  //setting the hibernate session for the productDao
                List<Product> productList = productDao.getAllProducts(); // Fetch all products
                System.out.println("Fetched Products: " + productList); // Log for debugging
                request.setAttribute("productList", productList); // Set the product list in request scope
                request.getRequestDispatcher("productList.jsp").forward(request, response); // Forward to JSP
            } catch (Exception e) {
                response.sendRedirect("error.jsp?error=Could not retrieve products."); // Error handling
            } finally {
                session.close();
            }
        } else {
            response.sendRedirect("login.jsp?error=Please log in.");
        }
    }
}
