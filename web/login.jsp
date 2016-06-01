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
    <title>Вхід</title>
  </jsp:attribute>

  <jsp:attribute name="style">
    <style>
      #body form {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
      #body form input {
        background-color: whitesmoke;
        width: 300px;
        height: 35px;
        font-family: cursive;
        font-size: large;
        text-align: center;
      }
      #body form input.text {
        background-color: whitesmoke;
        margin-top: 5px;
        border: solid 2px gray;
      }
      #body form input.text:hover, div#body form input.text:focus {
        color: darkgreen;
        border: solid 2px darkgreen;
      }
      #body form input.submit {
        background-color: olivedrab;
        color: white;
        margin-top: 20px;
        border: solid 2px darkgreen;
      }
      #body form input.submit:hover {
        background-color: darkolivegreen;
        border: solid 2px white;
        cursor: hand;
      }
      #body #error {
        top: 35%;
        left: 50%;
        transform: translate(-50%, -35%);
      }
    </style>
  </jsp:attribute>

  <jsp:attribute name="error">
    <c:if test="${requestScope.errtype ne null}">
      <c:choose>
        <c:when test="${requestScope.errtype eq 'email'}">
          Електронну пошту введено невірно
        </c:when>
        <c:when test="${requestScope.errtype eq 'password'}">
          Пароль введено невірно
        </c:when>
      </c:choose>
    </c:if>
  </jsp:attribute>

  <jsp:body>
    <c:url value="/login" var="loginURL"></c:url>

    <form action='<c:out value="${loginURL}"></c:out>' method="post">
      <input class="text" type="text" value="${requestScope.email}"
             placeholder="Електронна пошта" name="email"><br>
      <input class="text" type="password"
             placeholder="Пароль" name="password"><br>
      <input class="submit" type="submit" value="Увійти в систему">
    </form>
  </jsp:body>
</t:genericpage>