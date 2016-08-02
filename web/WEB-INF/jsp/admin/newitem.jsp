<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 01.06.16
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>

<c:set var="resources" value="${pageContext.request.contextPath}/resources"/>
<c:choose>
  <c:when test="${param.action eq 'newItem'}">
    <c:set var="title" value="New item"/>
    <c:set var="btn_name" value="Add new item"/>
  </c:when>
  <c:otherwise>
    <c:set var="title" value="Edit item"/>
    <c:set var="btn_name" value="Edit item"/>
  </c:otherwise>
</c:choose>

<t:genericpage>
  <jsp:attribute name="title">${title}</jsp:attribute>
  <jsp:attribute name="js">
    <script type="text/javascript" src="${resources}/admin/js/item-extfield-builder.js"></script>
    <script type="text/javascript" src="${resources}/admin/js/item-form-validator.js"></script>
    <script>
      function retainSelected(selected) {
        document.querySelectorAll('#currency option[value="' + selected + '"]')[0].defaultSelected = true;
      }
    </script>
  </jsp:attribute>

  <jsp:body>
    <c:url var="customerController" value="/controller/admin"/>
    <c:set var="tags" value="${(item.tags ne '[]') ? item.tags : null}"/>
    <c:set var="status" value="${item.status}"/>
    <c:set var="price" value="${(item.price gt 0.0) ? item.price : null}"/>
    <c:set var="stocked" value="${(status.stocked gt 0) ? status.stocked : null}"/>
    <c:set var="reserved" value="${(status.reserved gt 0) ? status.reserved : null}"/>
    <c:set var="sold" value="${(status.sold gt 0) ? status.sold : null}"/>
    <form action="${customerController}" method="post" class="form-horizontal" role="form" onsubmit="validateForm()">
      <input type="hidden" name="action" value="${param.action}">
      <c:if test="${param.action eq 'editItem'}">
        <input type="hidden" name="id" value="${item.id}">
      </c:if>
      <div id="field-container">
        <div id="name" class="form-group col-sm-12">
          <label for="name-inp" class="control-label col-sm-2">Name:</label>
          <div class="col-sm-4">
            <input id="name-inp" type="text" name="name" value='${item.name}' placeholder="Name?" class="form-control" aria-describedby="name-help">
            <span id="name-help" class="help-block"></span>
          </div>
        </div>
        <div id="tags" class="form-group col-sm-12">
          <label for="tags-inp" class="control-label col-sm-2">Tags:</label>
          <div class="col-sm-4">
            <input id="tags-inp" type="text" name="tags" value="${tags}" data-role="tagsinput" class="form-control" aria-describedby="tags-help">
            <span id="tags-help" class="help-block"></span>
          </div>
        </div>
        <div id="price" class="form-group col-sm-12">
          <label for="price-inp" class="control-label col-sm-2">Price:</label>
          <div class="col-sm-4">
            <input id="price-inp" type="number" name="price" value="${price}" step="0.01" placeholder="Price?" class="form-control" aria-describedby="price-help">
            <span id="price-help" class="help-block"></span>
          </div>
        </div>
        <div id="currency" class="form-group col-sm-12">
          <label for="currency-inp" class="control-label col-sm-2">Currency:</label>
          <div class="col-sm-4">
            <select id="currency-inp" name="currency" class="form-control" aria-describedby="currency-help">
              <option selected disabled>-- select currency --</option>
              <option value="USD">USD ($)</option>
              <option value="EUR">EURO (€)</option>
              <option value="UAH">HRYVNIA (₴)</option>
              <option value="RUB">RUBLE (₽)</option>
            </select>
            <span id="currency-help" class="help-block"></span>
          </div>
        </div>
        <div id="stocked" class="form-group col-sm-12">
          <label for="stocked-inp" class="control-label col-sm-2">Left:</label>
          <div class="col-sm-4">
            <input id="stocked-inp" type="number" name="stocked" value="${stocked}" placeholder="How much in stock?" class="form-control" aria-describedby="stocked-help">
            <span id="stocked-help" class="help-block"></span>
          </div>
        </div>
        <div id="reserved" class="form-group col-sm-12">
          <label for="reserved-inp" class="control-label col-sm-2">Reserved:</label>
          <div class="col-sm-4">
            <input id="reserved-inp" type="number" name="reserved" value="${reserved}" placeholder="How much in reserve?" class="form-control">
          </div>
        </div>
        <div id="sold" class="form-group col-sm-12">
          <label for="sold-inp" class="control-label col-sm-2">Sold:</label>
          <div class="col-sm-4">
            <input id="sold-inp" type="number" name="sold" value="${sold}" placeholder="How much already sold?" class="form-control">
          </div>
        </div>
        <c:forEach var="field" items="${item.extendedFields}" varStatus="loop">
          <div id="field${loop.index}" class="row form-group col-sm-12">
            <div class="col-sm-offset-2 col-sm-4">
              <input id="field${loop.index}-name-inp" type="text" name="field${loop.index}-name" value="${field.key}" placeholder="Name?" class="form-control" aria-describedby="field${loop.index}-name-help">
              <span id="field${loop.index}-name-help" class="help-block"></span>
            </div>
            <div class="col-sm-4">
              <input id="field${loop.index}-val-inp" type="text" name="field${loop.index}-val" value="${field.value}" placeholder="Value?" class="form-control" aria-describedby="field${loop.index}-val-help">
              <span id="field${loop.index}-val-help" class="help-block"></span>
            </div>
            <div class="col-sm-1">
              <a href="#aa" onclick="removeField(${loop.index})" class="btn btn-default">x</a>
            </div>
          </div>
        </c:forEach>
      </div>
      <div class="form-group col-sm-12">
        <div class="col-sm-offset-2 col-sm-4">
          <input type="button" onclick="addField()" class="btn btn-default" value="Add new field">
          <button type="submit" class="btn btn-default">${btn_name}</button>
        </div>
      </div>
    </form>

    <c:if test="${item.currency ne null}">
      <script>retainSelected("${item.currency}");</script>
    </c:if>
    <c:if test="${param.currency ne null}">
      <script>retainSelected("${param.currency}");</script>
    </c:if>
  </jsp:body>
</t:genericpage>