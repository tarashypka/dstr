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
  <jsp:attribute name="title">Item</jsp:attribute>

  <jsp:body>
    <c:if test="${item ne null}">
      <c:set var="status" value="${item.status}"/>
      <table>
        <tr>
          <th>ID</th>
          <td>${item.id}</td>
        </tr>
        <tr>
          <th>Category</th>
          <td>${requestScope.item.category}</td>
        </tr>
        <tr>
          <th>Price</th>
          <td>${item.price} ${item.currency}</td>
        </tr>
        <tr>
          <th>Left</th>
          <td>${status.stocked}</td>
        </tr>
        <c:if test="${sessionScope.customer.role eq 'admin'}">
          <tr>
            <th>Reserved</th>
            <td>${status.reserved}</td>
          </tr>
          <tr>
            <th>Sold</th>
            <td>${status.sold}</td>
          </tr>
        </c:if>
        <c:forEach var="field" items="${item.extendedFields}">
          <tr>
            <th>${field.key}</th>
            <td>${field.value}</td>
          </tr>
        </c:forEach>
      </table>
    <c:if test="${sessionScope.customer.role eq 'admin'}">
      <c:url var="editItemURL" value="/controller/admin">
        <c:param name="action" value="editItem"/>
        <c:param name="id" value="${item.id}"/>
      </c:url>
      <c:url var="deleteItemURL" value="/controller/admin">
        <c:param name="action" value="deleteItem"/>
        <c:param name="id" value="${item.id}"/>
      </c:url>
      <a href="${editItemURL}">Edit</a>
      <a href="${deleteItemURL}">Delete</a>
    </c:if>
    </c:if>
  </jsp:body>
</t:genericpage>