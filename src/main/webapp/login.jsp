<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/styles.css">
    <script>
        window.onload = function() {
            // Check if there's an error parameter in the URL
            const urlParams = new URLSearchParams(window.location.search);
            const error = urlParams.get('error');
            if (error) {
                alert(error); // Show the error message in an alert
            }
        };
    </script>
</head>
<body>
<h2>Login</h2>
<form action="UserServlet" method="post">
    <input type="hidden" name="action" value="login">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required>
    <br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>
    <br>
    <input type="submit" value="Login">
</form>
<p>Don’t have an account? <a href="index.jsp">Register here</a></p>
</body>
</html>
