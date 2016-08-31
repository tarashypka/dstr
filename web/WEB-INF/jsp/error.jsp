<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage>
  <jsp:attribute name="title">Error</jsp:attribute>

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
</t:genericpage>