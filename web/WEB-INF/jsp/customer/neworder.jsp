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
  <jsp:attribute name="title">New order</jsp:attribute>

  <jsp:body>
    <c:url var="customerController" value="/controller/customer"/>
    <c:if test="${order ne null and order.items ne null}">
      <form action="${customerController}" method="POST">
        <input type="hidden" name="action" value="dropOrderItem">
        <table>
          <tr>
            <th>ID</th>
            <th>Amount</th>
            <th>Price</th>
          </tr>
          <c:forEach var="item" items="${order.items}">
            <c:url var="itemURL" value="/controller">
              <c:param name="action" value="showItem"/>
              <c:param name="id" value="${item.key.id}"/>
            </c:url>
            <tr>
              <td><a href="${itemURL}">${item.key.id}</a></td>
              <td>${item.value}</td>
              <td>${item.key.price * item.value} ${item.key.currency}</td>
              <td><input type="submit" name="${item.key.id}" value="Drop item"></td></td>
            </tr>
          </c:forEach>
        </table>
      </form>
      Receipt:<br>
      <c:forEach var="price" items="${order.receipt}">
        ${price.value} ${price.key}<br>
      </c:forEach>
      <form action="${customerController}" method="post">
        <input type="hidden" name="action" value="newOrder">
        <input type="submit" value="Make order">
      </form>
      <form action="${customerController}" method="post">
        <input type="hidden" name="action" value="declineOrder">
        <input type="submit" value="Decline order">
      </form>
    </c:if>

    <c:if test="${items ne null}">
      <form action="${customerController}" method="POST">
        <input type="hidden" name="action" value="addOrderItems">
        <table>
          <tr>
            <th>ID</th>
            <th>Category</th>
            <th>Price</th>
            <th>Left</th>
          </tr>
          <c:forEach var="item" items="${items}">
            <c:url var="itemURL" value="/controller">
              <c:param name="action" value="showItem"/>
              <c:param name="id" value="${item.id}"/>
            </c:url>
            <tr>
              <td><a href="${itemURL}">${item.id}</a></td>
              <td>${item.category}</td>
              <td>${item.price} ${item.currency}</td>
              <td>${item.status.stocked}</td>
              <td><input type="number" name="${item.id}" placeholder="How much?"></td>
            </tr>
          </c:forEach>
        </table>
        <input type="submit" value="Add items to order">
      </form>
    </c:if>
  </jsp:body>
</t:genericpage>