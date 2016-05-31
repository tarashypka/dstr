<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 22:38
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
    <jsp:attribute name="title">
    <title>Домашня сторінка</title>
  </jsp:attribute>

  <jsp:attribute name="buttons">
    <c:url value="/login" var="loginURL"></c:url>
    <c:url value="/logout" var="logoutURL"></c:url>
    <c:url value="/register" var="registrationURL"></c:url>

    <c:if test="${sessionScope.customer eq null}">
      <div>
        <form class="button1" action='<c:out value="${loginURL}"></c:out>' method="get">
          <input type="submit" value="Увійти в систему">
        </form>

        <form class="button2" action='<c:out value="${registrationURL}"></c:out>' method="get">
          <input type="submit" value="Зарегіструватись">
        </form>
      </div>
    </c:if>
    <c:if test="${sessionScope.customer ne null}">
      <div>
        <form class="button1" action='<c:out value="${logoutURL}"></c:out>' method="post">
          <input type="submit" value="Вийти із системи">
        </form>
      </div>
    </c:if>
  </jsp:attribute>
</t:genericpage>
