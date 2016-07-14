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
    <title>New order</title>
  </jsp:attribute>

  <jsp:body>
    <c:url var="customerController" value="/controller/customer"/>
    <c:set var="order" value="${sessionScope.order}"/>
    <c:if test="${order ne null and order.items ne null}">
      <table id="order">
        <tr>
          <th>ID</th>
          <th>Amount</th>
          <th>Price</th>
          <th>Currency</th>
        </tr>
        <c:forEach var="item" items="${order.items}">
          <tr>
            <td>${item.key.id}</td>
            <td>${item.value}</td>
            <td>${item.key.price * item.value}</td>
            <td>${item.key.currency}</td>
          </tr>
        </c:forEach>
        <tr>
          <form action="${customerController}" method="post">
            <input type="hidden" name="action" value="makeOrder">
            <td><input type="submit" value="Зробити замовлення"></td>
          </form>
          <td>
            <c:forEach var="price" items="${order.receipt}">
              ${price.value} ${price.key}<br>
            </c:forEach>
          </td>
        </tr>
      </table>
    </c:if>

    <c:if test="${sessionScope.items ne null}">
      <table id="items">
        <tr>
          <th>ID</th>
          <th>Category</th>
          <th>Price</th>
          <th>Currency</th>
          <th>Left</th>
        </tr>
        <c:forEach items="${sessionScope.items}" var="item">
          <c:url var="showItemURL" value="/controller">
            <c:param name="action" value="showItem"/>
            <c:param name="id" value="${item.id}"/>
          </c:url>
          <tr>
            <td>
              <a href="${showItemURL}">${item.id}</a>
            </td>
            <td><c:out value="${item.category}" default="Category?"/></td>
            <td><c:out value="${item.price}" default="Price?"/></td>
            <td><c:out value="${item.currency}" default="Currency?"/></td>
            <td><c:out value="${item.status.stocked}" default="How much in stock?"/></td>
            <form action="${customerController}" method="post">
              <input type="hidden" name="action" value="addOrderItem">
              <input type="hidden" name="id" value="${item.id}">
              <td><input type="number" name="quantity" placeholder="How much?"></td>
              <td><input type="submit" value="Add item"></td>
            </form>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>