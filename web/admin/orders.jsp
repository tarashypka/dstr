<%@ page import="com.hazelcast.core.Hazelcast" %>
<%@ page import="java.util.List" %>
<%@ page import="model.order.Order" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 3:40
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"
         language="java" pageEncoding="UTF-8" session="true" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>dstr: Замовлення</title>
  <style>
    .err div {
      color: red;
      position: fixed;
      left: 230px;
    }
    form {
      position: fixed;
      left: 25px;
    }
    form input {
      width: 200px;
      height: 25px;
    }
    form[name=butt1] {
      top: 20px;
    }
    form[name=butt2] {
      top: 20px;
      left: 230px;
    }
    form[name=butt3] {
      top: 20px;
      left: 435px;
    }
    form[name=inp1] {
      top: 50px;
      margin-top: 5px;
    }
    form[name=inp1] input {
      margin-top: 5px;
    }
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

<c:url value="/admin/showItems" var="showItemsURL"></c:url>
<c:url value="/admin/showCustomers" var="showCustomersURL"></c:url>
<c:url value="/admin/showOrders" var="showOrdersURL"></c:url>

<form action='<c:out value="${showItemsURL}"></c:out>' method="get" name="butt1">
  <input type="submit" value="Товари">
</form>

<form action='<c:out value="${showCustomersURL}"></c:out>' method="get" name="butt2">
  <input type="submit" value="Користувачі">
</form>

<form action='<c:out value="${showOrdersURL}"></c:out>' method="get" name="butt3">
  <input type="submit" value="Замовлення">
</form>

<%
  List<Order> orders =
          Hazelcast.getHazelcastInstanceByName("HZ_CONFIG").getList("ORDERS");
  request.setAttribute("orders", orders);
%>

<%-- Orders List Logic --%>
<c:if test="${not empty requestScope.orders}">
  <table>
    <tr>
      <th>ID</th>
      <th>№ замовлення</th>
      <th>Дата</th>
      <th>Замовник</th>
      <th>Товар</th>
      <th>Кількість</th>
      <th>Вартість</th>
      <th>Валюта</th>
    </tr>
    <c:forEach items="${requestScope.orders}" var="order">
      <tr>
        <td><c:out value="${order.id}"></c:out></td>
        <td><c:out value="${order.orderNumber}"></c:out></td>
        <td><c:out value="${order.date}"></c:out></td>
        <td>
            <c:out value="${order.customer.name}"></c:out>
            <c:out value="${order.customer.surname}"></c:out>
        </td>
        <td>
          <c:forEach items="${order.items}" var="item">
            <c:url value="/showItem" var="showItemURL">
              <c:param name="id" value="${item.key}"></c:param>
            </c:url>
            <a href='<c:out value="${showItemURL}" escapeXml="true"></c:out>'>
                <c:out value="${item.key}"></c:out></br>
            </a>
          </c:forEach>
        </td>
        <td>
          <c:forEach items="${order.items}" var="item">
            <c:out value="${item.value}"></c:out></br>
          </c:forEach>
        </td>
        <td>
          <c:forEach items="${order.receipt}" var="price">
            <c:out value="${price.key}"></c:out></br>
          </c:forEach>
        </td>
        <td>
          <c:forEach items="${order.receipt}" var="price">
            <c:out value="${price.value}"></c:out></br>
          </c:forEach>
        </td>
      </tr>
    </c:forEach>
  </table>
</c:if>
</body>
</html>