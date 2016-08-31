<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage>
  <jsp:attribute name="title">My Items</jsp:attribute>

  <jsp:body>
    <c:if test="${items ne null}">
      <table class="table table-bordered">
        <tr>
          <th>ID</th>
          <th>Category</th>
          <th>Price</th>
          <th>Amount</th>
          <th>Total cash</th>
        </tr>
        <c:forEach var="item" items="${items}">
          <c:url var="itemURL" value="/controller/customer">
            <c:param name="action" value="showItems"/>
            <c:param name="id" value="${item.key.id}"/>
          </c:url>
          <tr>
            <td><a href="${itemURL}">${item.key.id}</a></td>
            <td>${item.key.name}</td>
            <td>${item.key.cash} ${item.key.currency}</td>
            <td>${item.value}</td>
            <td>${item.value * item.key.cash}</td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </jsp:body>
</t:genericpage>