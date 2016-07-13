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

  <jsp:attribute name="error">
    <c:if test="${requestScope.error ne null}">
      <c:out value="${requestScope.error}"/>
    </c:if>
  </jsp:attribute>

  <jsp:body>
    <c:url var="controller" value="/controller"/>
    <form action="${controller}" method="post">
      <input type="hidden" name="action" value="login">
      <input type="text" name="email"
             placeholder="Електронна пошта?"><br>
      <input type="password" name="password"
             placeholder="Пароль?"><br>
      <input type="submit" value="Увійти в систему">
    </form>
  </jsp:body>
</t:genericpage>