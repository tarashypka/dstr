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
          <c:if test="${sessionScope.customer.role eq 'customer'}">
            <th>Кількість</th>
            <th>Загальна вартість</th>
          </c:if>
          <th>Залишилось</th>
          <th>Зарезервовано</th>
          <th>Продано</th>
        </tr>
        <c:forEach items="${sessionScope.items}" var="item">
          <c:url value="/item" var="itemURL">
            <c:param name="id" value="${item.key.id}"/>
          </c:url>
          <tr>
            <td>
              <a href='<c:out value="${itemURL}"/>'>
                <c:out value="${item.key.id}"/>
              </a>
            </td>
            <td><c:out value="${item.key.category}"/></td>
            <td><c:out value="${item.key.price}"/></td>
            <td><c:out value="${item.key.currency}"/></td>
            <c:if test="${sessionScope.customer.role eq 'customer'}">
              <td><c:out value="${item.value}"/></td>
              <td><c:out value="${item.value * item.key.price}"/></td>
            </c:if>
            <td><c:out value="${item.key.status.stocked}"/></td>
            <td><c:out value="${item.key.status.reserved}"/></td>
            <td><c:out value="${item.key.status.sold}"/></td>
            <c:if test="${sessionScope.customer.role eq 'admin'}">
              <c:url value="/item/edit" var="itemEditURL">
                <c:param name="id" value="${item.key.id}"/>
              </c:url>
              <c:url value="/item/delete" var="itemDeleteURL">
                <c:param name="id" value="${item.key.id}"/>
              </c:url>
              <td>
                <a href='<c:out value="${itemEditURL}" escapeXml="false"/>'>
                  Редагувати
                </a>
              </td>
              <td>
                <a href='<c:out value="${itemDeleteURL}" escapeXml="false"/>'>
                  Видалити
                </a>
              </td>
            </c:if>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>