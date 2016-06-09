<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 17.05.16
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Товари</title>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${sessionScope.items ne null}">
      <table>
        <tr>
          <th>ID</th>
          <th>Категорія</th>
          <th>Ціна</th>
          <th>Валюта</th>
          <c:choose>
            <c:when test="${sessionScope.customer.role eq 'customer'}">
              <th>Кількість</th>
              <th>До оплати</th>
            </c:when>
            <c:when test="${sessionScope.customer.role eq 'admin'}">
              <th>Залишилось</th>
            </c:when>
          </c:choose>
        </tr>
        <c:forEach items="${sessionScope.items}" var="item">
          <c:url value="/item" var="itemURL">
            <c:param name="id" value="${item.key.id}"></c:param>
          </c:url>
          <tr>
            <td>
              <a href='<c:out value="${itemURL}"></c:out>'>
                <c:out value="${item.key.id}"></c:out>
              </a>
            </td>
            <td><c:out value="${item.key.category}"></c:out></td>
            <td><c:out value="${item.key.price}"></c:out></td>
            <td><c:out value="${item.key.currency}"></c:out></td>
            <c:choose>
              <c:when test="${sessionScope.customer.role eq 'customer'}">
                <td><c:out value="${item.value}"></c:out></td>
                <td><c:out value="${item.value * item.key.price}"></c:out></td>
              </c:when>
              <c:when test="${sessionScope.customer.role eq 'admin'}">
                <td><c:out value="${item.key.left}"></c:out></td>
                <c:url value="/item/edit" var="itemEditURL">
                  <c:param name="id" value="${item.key.id}"></c:param>
                </c:url>
                <c:url value="/item/delete" var="itemDeleteURL">
                  <c:param name="id" value="${item.key.id}"></c:param>
                </c:url>
                <td>
                  <a href='<c:out value="${itemEditURL}" escapeXml="false"></c:out>'>
                    Редагувати
                  </a>
                </td>
                <td>
                  <a href='<c:out value="${itemDeleteURL}" escapeXml="false"></c:out>'>
                    Видалити
                  </a>
                </td>
              </c:when>
            </c:choose>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>