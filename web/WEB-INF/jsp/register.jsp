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
    <title>Registration</title>
  </jsp:attribute>

  <jsp:body>
    <c:url var="registerURL" value="/controller">
      <c:param name="action" value="register"/>
    </c:url>
    <c:set var="customer" value="${requestScope.customer}"/>
    <form action="${registerURL}" method="post">
      <input type="text" name="name" value="${customer.name}" placeholder="Name?"><br>
      <input type="text" name="surname" value="${customer.surname}" placeholder="Surname?"><br>
      <input type="text" name="email" value="${customer.email}" placeholder="Email?"><br>
      <input type="password" name="password" placeholder="Password?"><br>
      <input type="submit" value="Register">
    </form>
  </jsp:body>
</t:genericpage>