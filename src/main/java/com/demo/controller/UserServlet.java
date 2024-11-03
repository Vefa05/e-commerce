

package com.demo.controller;

import com.demo.model.User;
import com.demo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);}

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            logoutUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = sanitizeInput(request.getParameter("username"));  //sanitization of the username and password parameters comes from the index.jsp
        String password = sanitizeInput(request.getParameter("password"));
        if (!isValidInput(username) || !isValidInput(password)) { //checking username or password  contains alphanumeric and underscore
            response.sendRedirect("login.jsp?error=Invalid input.");
            return;
        }
        // Log registration attempt
        System.out.println("Attempting to register user: " + username);

        Session session = HibernateUtil.openSession();  //creating hibernate session
        Transaction transaction = session.beginTransaction();

        try {
            User user = new User(); //in the modules part we refer the functions on the User
            user.setUsername(username);
            user.setPassword(password); // Use hashed password
            user.setWallet(500);
            // Check for existing user
            User existingUser = (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            if (existingUser != null) {
                response.sendRedirect("index.jsp?error=Username already exists.");
                return;
            }

            session.save(user);  //adding the user to the users
            transaction.commit();// Committing the transaction if everything succeeds
            System.out.println("User registered successfully: " + username);
            response.sendRedirect("login.jsp?msg=Registration successful! Please log in."); //redirection to login.jsp for login
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); //when the error is detected, preserving the previous state of the database on all changes
            }
            System.err.println("Registration failed for user: " + username + ". Error: " + e.getMessage());
            response.sendRedirect("index.jsp?error=Registration failed.");
        } finally {
            session.close();
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = sanitizeInput(request.getParameter("username"));
        String password = sanitizeInput(request.getParameter("password"));
        if (!isValidInput(username) || !isValidInput(password)) {
            response.sendRedirect("login.jsp?error=Invalid input.");
            return;
        }
        Session session = HibernateUtil.openSession();
        try {
            User user = (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();  //retriving the row where username is given on the login form

            // Check password against the hashed password
            if (user != null && user.checkPassword(password)) { // Use hashedPassword comparison
                HttpSession httpSession = request.getSession();
                String sessionToken = UUID.randomUUID().toString(); // Generate session token
                httpSession.setAttribute("user", user);
                httpSession.setAttribute("sessionToken", sessionToken);  //setting the sessionToken on the server-side session

                // Send the session token as a cookie
                Cookie cookie = new Cookie("sessionToken", sessionToken); //setting the sessionToken on the client-side via cookies
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie); //when logining cookie is added on the response when we redicred to /products

                response.sendRedirect(request.getContextPath() + "/products"); // Redirect to products page
            } else {
                response.sendRedirect("login.jsp?error=Invalid username or password.");
            }
        } catch (Exception e) {
            response.sendRedirect("login.jsp?error=An error occurred during login.");
        } finally {
            session.close();
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate(); // Invalidate the session on logout in the server side
        }
        // Remove the session token cookie      also removing the sessionToken on the client-side
        Cookie cookie = new Cookie("sessionToken", null);
        cookie.setMaxAge(0); // Set to expire immediately
        cookie.setPath("/");
        response.addCookie(cookie);

        response.sendRedirect("index.jsp");
    }

    private boolean isValidInput(String input) {
        // Basic validation: check length and allowed characters
        return input != null && input.length() > 0 && input.matches("^[a-zA-Z0-9_]*$"); // Allows alphanumeric and underscore
    }
    private String sanitizeInput(String input) {
        // Simple sanitization: trim whitespace and remove HTML tags
        if (input != null) {
            return input.trim().replaceAll("<[^>]*>", ""); // Remove HTML tags
        }
        return null;
    }
}
