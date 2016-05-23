<%@ page import="com.hazelcast.core.Hazelcast" %>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jws.soap.SOAPBinding" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 17.05.16
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"
         language="java" pageEncoding="UTF-8" session="true" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>dstr: Користувачі</title>
  <style>
    #worker, #ip, #session {
      position: fixed;
      left: 5px;
    }
    #worker {
      bottom: 45px;
    }
    #ip {
      bottom: 25px;
    }
    #session {
      bottom: 5px;
    }
    .err div {
      color: red;
      position: fixed;
      left: 230px;
    }
    #err1 {
      top: 65px;
    }
    #err2 {
      top: 95px;
    }
    #err3 {
      top: 125px;
    }
    .success {
      color: green;
      position: fixed;
      top: 185px;
      left: 25px;
    }
    form {
      position: fixed;
      left: 25px;
    }
    form input {
      width: 200px;
      height: 25px;
    }
    form[name=butt1] {
      top: 25px;
    }
    form[name=butt2] {
      top: 25px;
      left: 230px;
    }
    form[name=inp1] {
      top: 50px;
      margin-top: 5px;
    }
    form[name=inp1] input {
      margin-top: 5px;
    }
    table {
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
    table,th,td {
      border: 1px solid black;
    }
  </style>
</head>
<body>
  <div id="worker">worker: no workers</div>
  <div id="ip">ip: localhost</div>
  <div id="session">session: <%= request.getSession().getId() %></div>

  <c:url value="/addUser" var="addURL"></c:url>
  <c:url value="/editUser" var="editURL"></c:url>
  <c:url value="/showUsers" var="showUsersURL"></c:url>

  <form action='<c:out value="${addURL}"></c:out>' method="get" name="butt1">
    <input type="submit" value="Створити нового користувача">
  </form>

  <%-- User Add/Edit logic --%>
  <c:if test="${requestScope.errtype ne null}">
    <strong class="err">
      <c:choose>
        <c:when test="${requestScope.errtype eq 'name'}">
          <div id="err1">Ім'я введено невірно</div>
        </c:when>
        <c:when test="${requestScope.errtype eq 'email'}">
          <div id="err2">Електронну пошту введено невірно</div>
        </c:when>
        <c:when test="${requestScope.errtype eq 'psswd'}">
          <div id="err3">Занадто короткий пароль</div>
        </c:when>
      </c:choose>
    </strong>
  </c:if>
  <c:if test="${requestScope.success ne null}">
    <strong class="success">
      <c:out value="${requestScope.success}"></c:out>
    </strong>
  </c:if>

  <%-- Edit Request --%>
  <c:if test="${requestScope.user ne null}">
    <form action='<c:out value="${editURL}"></c:out>' method="post" name="inp1">
      <input type="text" value="${requestScope.user.id}"
             readonly="readonly" name="id"><br>
      <input type="text" value="${requestScope.user.name}"
             placeholder="Ім'я" name="name"><br>
      <input type="text" value="${requestScope.user.email}"
             placeholder="Електронна пошта" name="email"><br>
      <input type="text" value="${requestScope.user.password}"
             placeholder="Пароль" name="password"><br>
      <input type="submit" value="Редагувати користувача">
    </form>
  </c:if>

  <%-- Add Request --%>
  <c:if test="${requestScope.user eq null}">
    <form action='<c:out value="${addURL}"></c:out>' method="post" name="inp1">
      <input type="text" value="${requestScope.addusr.name}"
             placeholder="Ім'я" name="name"><br>
      <input type="text" value="${requestScope.addusr.email}"
             placeholder="Електронна пошта" name="email"><br>
      <input type="text" value="${requestScope.addusr.password}"
             placeholder="Пароль" name="password"><br>
      <input type="submit" value="Створити користувача">
    </form>
  </c:if>

  <form action='<c:out value="${showUsersURL}"></c:out>' method="get" name="butt2">
    <input type="submit" value="Вивести всіх користувачів">
  </form>

  <%
    List<User> users = Hazelcast.getHazelcastInstanceByName("USERS").getList("USERS");
    request.setAttribute("users", users);
  %>

  <%-- Users List Logic --%>
  <c:if test="${not empty requestScope.users}">
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
            <td>
              <a href='<c:out value="${editURL}" escapeXml="true"></c:out>'>
                Редагувати
              </a>
            </td>
            <td>
              <a href='<c:out value="${deleteURL}" escapeXml="true"></c:out>'>
                Видалити
              </a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:if>
</body>
</html>