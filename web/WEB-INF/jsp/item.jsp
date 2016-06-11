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

  <jsp:body>
    <c:if test="${requestScope.item ne null}">
      <table>
        <tr>
          <th>ID</th>
          <td><c:out value="${requestScope.item.id}"/></td>
        </tr>
        <tr>
          <th>Категорія</th>
          <td><c:out value="${requestScope.item.category}"/></td>
        </tr>
        <tr>
          <th>Ціна</th>
          <td><c:out value="${requestScope.item.price}"/></td>
        </tr>
        <tr>
          <th>Валюта</th>
          <td><c:out value="${requestScope.item.currency}"/></td>
        </tr>
        <tr>
          <th>Залишилось</th>
          <td><c:out value="${item.left}"/></td>
        </tr>
        <tr>
          <th>Продано</th>
          <td><c:out value="${requestScope.sold}"/></td>
        </tr>
        <c:forEach items="${item.extendedFields}" var="field">
          <tr>
            <th><c:out value="${item.key}"/></th>
            <td><c:out value="${item.value}"/></td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>