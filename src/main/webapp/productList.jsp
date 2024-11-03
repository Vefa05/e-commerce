<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Product List</title>
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
<h2>Welcome, ${user.username}</h2>
<p>Your wallet balance: ${user.wallet}</p>



<table>
  <tr>
    <th>ID</th>
    <th>Name</th>
    <th>Price</th>
    <th>Description</th>
    <th>Action</th>
  </tr>
  <c:forEach var="product" items="${productList}">
    <tr>
      <td>${product.id}</td>
      <td>${product.name}</td>
      <td>${product.price}</td>
      <td>${product.description}</td>
      <td>
        <form action="CheckoutServlet" method="post">
          <input type="hidden" name="productId" value="${product.id}" />
          <button type="submit">Buy</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

<p><a href="UserServlet?action=logout">Log Out</a></p>
</body>
</html>
