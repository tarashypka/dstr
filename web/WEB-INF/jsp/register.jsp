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
    <title>Реєстрація</title>
  </jsp:attribute>

  <jsp:attribute name="error">
    <c:if test="${requestScope.error ne null}">
      <c:out value="${requestScope.error}"/>
    </c:if>
  </jsp:attribute>

  <jsp:body>
    <c:url value="/register" var="registerURL"></c:url>

    <form action='<c:out value="${registerURL}"></c:out>' method="post">
      <input type="text" value="${requestScope.customer.name}"
             placeholder="Ім'я" name="name"><br>
      <input type="text" value="${requestScope.customer.surname}"
             placeholder="Прізвище" name="surname"><br>
      <input type="text" value="${requestScope.customer.email}"
             placeholder="Електронна пошта" name="email"><br>
      <input type="password" placeholder="Пароль" name="password"><br>
      <input type="submit" value="Зарегіструватись">
    </form>
  </jsp:body>
</t:genericpage>