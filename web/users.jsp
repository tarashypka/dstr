<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 17.05.16
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>dstr: Користувачі</title>
  <style>
    table,th,td {
      border: 1px solid black;
    }
  </style>
</head>
<body>
  <%-- User Add/Edit logic --%>
  <c:if test="${requestScope.error ne null}">
    <strong style="color: red;">
      <c:out value="${requestScope.error}"></c:out>
    </strong>
  </c:if>
  <c:if test="${requestScope.success ne null}">
    <strong style="color: green;">
      <c:out value="${requestScope.success}"></c:out>
    </strong>
  </c:if>
  <c:url value="/addUser" var="addURL"></c:url>
  <c:url value="/editUser" var="editURL"></c:url>

  <%-- Edit Request --%>
  <c:if test="${requestScope.user ne null}">
    <form action='<c:out value="${editURL}"></c:out>' method="post">
      ID:
      <input type="text" value="${requestScope.user.id}" readonly="readonly" name="id"><br>
      Name:
      <input type="text" value="${requestScope.user.name}" name="name"><br>
      Email:
      <input type="text" value="${requestScope.user.email}" name="email"><br>
      Password:
      <input type="text" value="${requestScope.user.password}" name="password"><br>
      <input type="submit" value="Редагувати користувача">
    </form>
  </c:if>

  <%-- Add Request --%>
  <c:if test="${requestScope.user eq null}">
    <form action='<c:out value="${addURL}"></c:out>' method="post">
      Name:
      <input type="text" name="name"><br>
      Email:
      <input type="text" name="email"><br>
      Password:
      <input type="text" name="password"><br>
      <input type="submit" value="Створити користувача">
    </form>
  </c:if>

  <%-- Users List Logic --%>
  <c:if test="${not empty requestScope.users}">
    <table>
      <tbody>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Password</th>
          <th>Edit</th>
          <th>Delete</th>
        </tr>
        <c:forEach items="${requestScope.users}" var="user">
          <c:url value="/editUser" var="editURL">
            <c:param name="id" value="${user.id}"></c:param>
          </c:url>
          <c:url value="/deleteUser" var="deleteURL">
            <c:param name="id" value="${user.id}"></c:param>
          </c:url>
          <tr>
            <td><c:out value="${user.id}"></c:out></td>
            <td><c:out value="${user.name}"></c:out></td>
            <td><c:out value="${user.email}"></c:out></td>
            <td><c:out value="${user.password}"></c:out></td>
            <td><a href='<c:out value="${editURL}" escapeXml="true"></c:out>'>Edit</a></td>
            <td><a href='<c:out value="${deleteURL}" escapeXml="true"></c:out>'>Delete</a></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:if>
</body>
</html>
