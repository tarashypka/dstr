<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage>
  <jsp:attribute name="title">Item</jsp:attribute>

  <jsp:body>
    <c:if test="${item ne null}">
      <c:set var="status" value="${item.status}"/>
      <table class="table table-bordered table-striped">
        <tr>
          <th>Name</th>
          <td>${item.name}</td>
        </tr>
        <tr>
          <th>Tags</th>
          <td>
            <c:forEach var="tag" items="${item.tags}">
              <c:url var="itemsWithTagURL" value="/controller">
                <c:param name="action" value="showItemsWithTag"/>
                <c:param name="tag" value="${tag}"/>
              </c:url>
              <a href="${itemsWithTagURL}">${tag}</a>
            </c:forEach>
          </td>
        </tr>
        <tr>
          <th>Price</th>
          <td>${item.price.cash} [${item.price.currency}]</td>
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
      <a href="${editItemURL}" class="btn btn-default">Edit</a>
      <a href="${deleteItemURL}" class="btn btn-default">Delete</a>
    </c:if>
    </c:if>
  </jsp:body>
</t:genericpage>