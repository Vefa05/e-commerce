package com.demo.controller;

import com.demo.dao.ProductDao;
import com.demo.dao.UserDao;
import com.demo.dao.OrderDao; // Ensure you import the OrderDao
import com.demo.model.Order; // Import the Order model
import com.demo.model.Product;
import com.demo.model.User;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.openSession();
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user"); // Get the logged-in user
        Long productId = Long.valueOf(request.getParameter("productId"));

        try {
            ProductDao productDao = new ProductDao(session);
            UserDao userDao = new UserDao(session);
            OrderDao orderDao = new OrderDao(session); // Create this DAO for order operations

            Product product = productDao.getProductById(productId);
            if (product != null && user.getWallet() >= product.getPrice()) {
                // Deduct price from wallet
                user.setWallet(user.getWallet() - product.getPrice());
                userDao.updateUser(user); // Update user's wallet in database

                // Create and save the order based on fuctions on the Order module
                Order order = new Order();
                order.setUserId(user.getId());
                order.setProductId(product.getId());
                order.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
                orderDao.saveOrder(order); // Save the order in the database

                httpSession.setAttribute("user", user); // Update user in session
                request.setAttribute("product", product);
                request.getRequestDispatcher("orderConfirmation.jsp").forward(request, response);
            } else {

                response.sendRedirect(request.getContextPath() + "/products?error=Insufficient wallet balance.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?error=Purchase failed.");
        } finally {
            session.close();
        }
    }
}
