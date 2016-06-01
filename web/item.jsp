<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 7:05
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Товар</title>
  </jsp:attribute>

  <jsp:attribute name="style">
    <style>
      #body table {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
      #body table, tr, th, td {
        border: 1px solid black;
        text-align: center;
      }
    </style>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${requestScope.item ne null}">
      <table>
        <tr>
          <th>ID</th>
          <td><c:out value="${item.id}"></c:out></td>
        </tr>
        <tr>
          <th>Категорія</th>
          <td><c:out value="${item.category}"></c:out></td>
        </tr>
        <tr>
          <th>Ціна</th>
          <td><c:out value="${item.price}"></c:out></td>
        </tr>
        <tr>
          <th>Валюта</th>
          <td><c:out value="${item.currency}"></c:out></td>
        </tr>
        <tr>
          <th>Залишилось</th>
          <td><c:out value="${item.left}"></c:out></td>
        </tr>
        <c:forEach items="${item.extendedFields}" var="field">
          <tr>
            <th><c:out value="${item.key}"></c:out></th>
            <td><c:out value="${item.value}"></c:out></td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>


