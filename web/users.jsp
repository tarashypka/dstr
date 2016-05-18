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
    form[name=button] input {
      position: fixed;
      top: 25px;
      left: 25px;
      width: 200px;
      height: 25px;
    }
    form[name=input] {
      position: fixed;
      top: 50px;
      left: 25px;
    }
    form[name=input] input {
      width: 200px;
      height: 25px;
      margin-top: 5px;
    }
    table {
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%)
    }
    table,th,td {
      border: 1px solid black;
    }
  </style>
</head>
<body>
  <c:url value="/addUser" var="addURL"></c:url>
  <c:url value="/editUser" var="editURL"></c:url>

  <form action='<c:out value="${addURL}"></c:out>' method="get" name="button">
    <input type="submit" value="Створити нового користувача">
  </form>


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

  <%-- Edit Request --%>
  <c:if test="${requestScope.user ne null}">
    <form action='<c:out value="${editURL}"></c:out>' method="post" name="input">
      <input type="text" value="${requestScope.user.id}" readonly="readonly" name="id"><br>
      <input type="text" value="${requestScope.user.name}" placeholder="Ім'я" name="name"><br>
      <input type="text" value="${requestScope.user.email}" placeholder="Електронна пошта" name="email"><br>
      <input type="text" value="${requestScope.user.password}" placeholder="Пароль" name="password"><br>
      <input type="submit" value="Редагувати користувача">
    </form>
  </c:if>

  <%-- Add Request --%>
  <c:if test="${requestScope.user eq null}">
    <form action='<c:out value="${addURL}"></c:out>' method="post" name="input">
      <input type="text" placeholder="Ім'я" name="name"><br>
      <input type="text" placeholder="Електронна пошта" name="email"><br>
      <input type="text" placeholder="Пароль" name="password"><br>
      <input type="submit" value="Створити користувача">
    </form>
  </c:if>

  <%-- Users List Logic --%>
  <c:if test="${not empty sessionScope.users}">
    <table>
      <tbody>
        <tr>
          <th>ID</th>
          <th>Ім'я</th>
          <th>Електронна пошта</th>
          <th>Пароль</th>
          <th>Редагувати</th>
          <th>Видалити</th>
        </tr>
        <c:forEach items="${sessionScope.users}" var="user">
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
            <td><a href='<c:out value="${editURL}" escapeXml="true"></c:out>'>Редагувати</a></td>
            <td><a href='<c:out value="${deleteURL}" escapeXml="true"></c:out>'>Видалити</a></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:if>
</body>
</html>