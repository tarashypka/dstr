<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage>
  <jsp:attribute name="title">New order</jsp:attribute>

  <jsp:body>
    <c:url var="customerController" value="/controller/customer"/>
    <c:if test="${order ne null and order.items ne null}">
      <form action="${customerController}" method="POST">
        <input type="hidden" name="action" value="dropOrderItem">
        <table class="table table-bordered table-striped">
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Amount</th>
            <th>Price</th>
          </tr>
          <tbody>
          <c:forEach var="item" items="${order.items}" varStatus="loop">
            <c:url var="itemURL" value="/controller">
              <c:param name="action" value="showItem"/>
              <c:param name="id" value="${item.key.id}"/>
            </c:url>
            <tr>
              <th scope="row">${loop.index}</th>
              <td><a href="${itemURL}">${item.key.name}</a></td>
              <td>${item.value}</td>
              <td>${item.key.cash * item.value} ${item.key.currency}</td>
              <td><button type="submit" name="${item.key.id}" class="btn btn-default btn-sm">X</button></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </form>
      <div class="container-fluid">
        <table class="table table-bordered table-striped table-nonfluid">
          <c:forEach var="cash" items="${order.receipt}">
            <tr><td>${cash.value}</td><td>${cash.key}</td></tr>
          </c:forEach>
          <tr>
            <td>
              <form action="${customerController}" method="POST">
                <input type="hidden" name="action" value="newOrder">
                <button type="submit" class="btn btn-default">Make order</button>
              </form>
            </td>
            <td>
              <form action="${customerController}" method="POST">
                <input type="hidden" name="action" value="declineOrder">
                <button type="submit" class="btn btn-default">Decline order</button>
              </form>
            </td>
          </tr>
        </table>
      </div>
      </form>
    </c:if>
    <c:if test="${items ne null}">
      <form action="${customerController}" method="POST">
        <input type="hidden" name="action" value="addOrderItems">
        <table class="table table-bordered table-striped">
          <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Left</th>
            <th><button type="submit" class="btn btn-default">Add items to order</button></th>
          </tr>
          <c:forEach var="item" items="${items}">
            <c:url var="itemURL" value="/controller">
              <c:param name="action" value="showItem"/>
              <c:param name="id" value="${item.id}"/>
            </c:url>
            <tr>
              <td><a href="${itemURL}">${item.name}</a></td>
              <td>${item.cash} [${item.currency}]</td>
              <td>${item.status.stocked}</td>
              <td><input type="number" name="${item.id}" placeholder="How much?"></td>
            </tr>
          </c:forEach>
        </table>
      </form>
    </c:if>
  </jsp:body>
</t:genericpage>