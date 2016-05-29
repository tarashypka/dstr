<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 7:05
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>dstr: Товар</title>
  <style>
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

  <%-- Items List Logic --%>
  <c:if test="${requestScope.item ne null}">
    <table>
      <tr>
        <th>ID</th>
        <td><c:out value="${requestScope.item.id}"></c:out></td>
      </tr>
      <tr>
        <th>Категорія</th>
        <td><c:out value="${requestScope.item.category}"></c:out></td>
      </tr>
      <tr>
        <th>Ціна</th>
        <td><c:out value="${requestScope.item.price}"></c:out></td>
      </tr>
      <tr>
        <th>Валюта</th>
        <td><c:out value="${requestScope.item.currency}"></c:out></td>
      </tr>
      <c:forEach items="${requestScope.item.extendedFields}" var="extField">
        <tr>
          <th><c:out value="${extField.key}"></c:out></th>
          <td><c:out value="${extField.value}}"></c:out></td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</body>
</html>
