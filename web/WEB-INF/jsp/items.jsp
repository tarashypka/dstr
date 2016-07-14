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
    <title>Items</title>
  </jsp:attribute>

  <jsp:body>
    <c:if test="${requestScope.items ne null}">
      <table>
        <tr>
          <th>ID</th>
          <th>Category</th>
          <th>Price</th>
          <th>Currency</th>
          <th>Left</th>
          <th>Reserved</th>
          <th>Sold</th>
        </tr>
        <c:forEach var="item" items="${requestScope.items}">
          <c:set var="status" value="${item.status}"/>
          <c:url var="showItemURL" value="/controller">
            <c:param name="action" value="showItem"/>
            <c:param name="id" value="${item.id}"/>
          </c:url>
          <tr>
            <td><a href="${showItemURL}">${item.id}</a></td>
            <td>${item.category}</td>
            <td>${item.price}</td>
            <td>${item.currency}</td>
            <td>${status.stocked}</td>
            <td>${status.reserved}</td>
            <td>${item.status.sold}</td>
            <c:if test="${sessionScope.customer.role eq 'admin'}">
              <c:url var="editItemURL" value="/controller/admin">
                <c:param name="action" value="editItem"/>
                <c:param name="id" value="${item.id}"/>
              </c:url>
              <c:url var="deleteItemURL" value="/controller/admin">
                <c:param name="action" value="deleteItem"/>
                <c:param name="id" value="${item.id}"/>
              </c:url>
              <td><a href="${editItemURL}">Edit</a></td>
              <td><a href="${deleteItemURL}">Delete</a></td>
            </c:if>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>