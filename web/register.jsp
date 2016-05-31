<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 7:48
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
    <jsp:attribute name="title">
    <title>Регістрація</title>
  </jsp:attribute>
  <jsp:attribute name="style">
    <style>
      div#body form {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
      div#body form input {
        background-color: whitesmoke;
        width: 300px;
        height: 35px;
        font-family: cursive;
        font-size: large;
        text-align: center;
      }
      div#body form input.text {
        background-color: whitesmoke;
        margin-top: 5px;
        border: solid 2px gray;
      }
      div#body form input.text:hover, div#body form input.text:focus {
        color: darkgreen;
        border: solid 2px darkgreen;
      }
      div#body form input.submit {
        background-color: olivedrab;
        color: white;
        margin-top: 20px;
        border: solid 2px darkgreen;
      }
      div#body form input.submit:hover {
        background-color: darkolivegreen;
        color: white;
        border: solid 2px ;
        cursor: hand;
      }
    </style>
  </jsp:attribute>

  <jsp:attribute name="buttons">
    <c:url value="/login" var="loginURL"></c:url>

    <form class="button1" action='<c:out value="${loginURL}"></c:out>' method="get">
      <input type="submit" value="Увійти в систему">
    </form>
  </jsp:attribute>

  <jsp:body>
    <c:url value="/register" var="registerURL"></c:url>

    <form action='<c:out value="${registerURL}"></c:out>' method="post">
      <input class="text" type="text" value="${requestScope.customer.name}"
             placeholder="Ім'я" name="name"><br>
      <input class="text" type="text" value="${requestScope.customer.surname}"
             placeholder="Прізвище" name="surname"><br>
      <input class="text" type="text" value="${requestScope.customer.email}"
             placeholder="Електронна пошта" name="email"><br>
      <input class="text" type="password"
             placeholder="Пароль" name="password"><br>
      <input class="submit" type="submit" value="Зарегіструватись">
    </form>
  </jsp:body>
</t:genericpage>