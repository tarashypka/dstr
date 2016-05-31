<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 30.05.16
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Помилка</title>
  </jsp:attribute>
  <jsp:attribute name="style">
    <style>
      #body {
        position: absolute;
        top: 140px;
        left: 100px;
        color: red;
        font-family: cursive;
        font-size: 22px;
      }
    </style>
  </jsp:attribute>

  <jsp:attribute name="buttons">
    <c:url value="/login" var="loginURL"></c:url>
    <c:url value="/register" var="registerURL"></c:url>

    <form class="button1" action='<c:out value="${loginURL}"></c:out>' method="get">
      <input type="submit" value="Увійти в систему">
    </form>
    <form class="button2" action='<c:out value="${registerURL}"></c:out>' method="get">
      <input type="submit" value="Зарегіструватись">
    </form>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${requestScope.statusCode eq 404}">
      <h3>Error Details</h3>
      <ul>
        <li>Status Code: 404</li>
        <li>
          Requested URI:
          <c:out value="${requestScope.requestUri}"></c:out>
          is not allowed or doesn't exist
        </li>
      </ul>
    </c:if>

    <c:if test="${requestScope.statusCode eq 500}">
      <h3>Exception Details</h3>
      <ul>
        <li>Servlet Name: <c:out value="${requestScope.servletName}"></c:out></li>
        <li>Requested URI: <c:out value="${requestScope.requestUri}"></c:out></li>
        <li>Exception Name: <c:out value="${requestScope.exception}"></c:out></li>
        <li>Exception Message: <c:out value="${requestScope.exceptionMsg}"></c:out></li>
      </ul>
    </c:if>
  </jsp:body>
</t:genericpage>