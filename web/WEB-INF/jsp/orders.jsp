<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 1:04
  To change this template use File | Settings | File Templates.
--%>

<t:genericpage>
  <jsp:attribute name="title">
    <title>Замовлення</title>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${requestScope.orders ne null}">
      <table>
        <tr>
          <th>№ замовлення</th>
          <th>Дата</th>
          <th>Замовник</th>
          <th>Товар</th>
          <th>Кількість</th>
          <th>Вартість</th>
          <th>Валюта</th>
          <th>Статус</th>
        </tr>
        <c:set value="${requestScope.orders}" var="orders"/>
        <c:forEach items="${orders}" var="order">
          <c:url value="/customer" var="customerURL">
            <c:param name="email" value="${order.customer.email}"/>
          </c:url>
          <c:url value="/order/status" var="setStatusRejectedURL">
            <c:param name="id" value="${order.id}"/>
            <c:param name="status" value="REJECTED"/>
          </c:url>
          <c:url value="/order/status" var="setStatusInProcessURL">
            <c:param name="id" value="${order.id}"/>
            <c:param name="status" value="IN_PROCESS"/>
          </c:url>
          <c:url value="/order/status" var="setStatusProcessedURL">
            <c:param name="id" value="${order.id}"/>
            <c:param name="status" value="PROCESSED"/>
          </c:url>
          <tr>
            <td><c:out value="${order.orderNumber}"/></td>
            <td><c:out value="${order.date}"/></td>
            <td>
              <a href='<c:out value="${customerURL}" escapeXml="false"/>'>
                <c:out value="${order.customer.name} ${order.customer.surname}"/>
              </a>
            </td>
            <td>
              <c:forEach items="${order.items}" var="item">
                <c:url value="/item" var="itemURL">
                  <c:param name="id" value="${item.key.id}"/>
                </c:url>
                <a href='<c:out value="${itemURL}" escapeXml="false"/>'>
                    <c:out value="${item.key.id}"/><br>
                </a>
              </c:forEach>
            </td>
            <td>
              <c:forEach items="${order.items}" var="item">
                <c:out value="${item.value}"/><br>
              </c:forEach>
            </td>
            <td>
              <c:forEach items="${order.receipt}" var="price">
                <c:out value="${price.key}"/><br>
              </c:forEach>
            </td>
            <td>
              <c:forEach items="${order.receipt}" var="price">
                <c:out value="${price.value}"/><br>
              </c:forEach>
            </td>
            <td>
              <c:choose>
                <c:when test="${order.status.value eq -1}">
                  <c:set var="status" value="Відмовлено"/>
                </c:when>
                <c:when test="${order.status.value eq -0}">
                  <c:set var="status" value="В процесі"/>
                </c:when>
                <c:when test="${order.status.value eq 1}">
                  <c:set var="status" value="Оброблено"/>
                </c:when>
              </c:choose>
              <c:out value="${status}"></c:out>
            </td>
            <td>
              <a href='<c:out value="${setStatusRejectedURL}" escapeXml="false"/>'>
                Відмовити
              </a>
            </td>
            <td>
              <a href='<c:out value="${setStatusInProcessURL}" escapeXml="false"/>'>
                В процесі
              </a>
            </td>
            <td>
              <a href='<c:out value="${setStatusProcessedURL}" escapeXml="false"/>'>
                Оброблено
              </a>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>
