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
  <jsp:attribute name="title">Error</jsp:attribute>

  <jsp:attribute name="error">
    <c:choose>
      <c:when test="${statusCode eq 404}">
        <h3>Error Details</h3>
        <ul>
          <li>Status Code: 404</li>
          <li>Requested URI: ${requestUri} is not allowed or doesn't exist</li>
        </ul>
      </c:when>
      <c:when test="${statusCode eq 500}">
        <h3>Exception Details</h3>
        <ul>
          <li>Servlet Name: ${servletName}</li>
          <li>Requested URI: ${requestUri}</li>
          <li>Exception Name: ${exception}</li>
          <li>Exception Message:${exceptionMsg}</li>
        </ul>
      </c:when>
    </c:choose>
  </jsp:attribute>
</t:genericpage>