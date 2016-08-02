<%@ tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" fragment="true" %>
<%@ attribute name="css" fragment="true" %>
<%@ attribute name="js" fragment="true" %>
<%@ include file="/WEB-INF/jsp/genericurls.jsp"%>

<c:set var="resources" value="${pageContext.request.contextPath}/resources"/>

<html>
<head>
  <title><jsp:invoke fragment="title"/></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="shortcut icon" href="${resources}/img/distributed24.png"/>
  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="${resources}/bootstrap/css/bootstrap.min.css">
  <!-- Custom CSS -->
  <link rel="stylesheet" href="${resources}/bootstrap/css/bootstrap-tagsinput.css">
  <link rel="stylesheet" href="${resources}/css/generic.css">
  <jsp:invoke fragment="css"/>
  <link rel="stylesheet" href="${resources}/">
  <!-- jQuery -->
  <script src="${resources}/jquery/jquery.min.js"></script>
  <!-- Bootstrap JS -->
  <script src="${resources}/bootstrap/js/bootstrap.min.js"></script>
  <script src="${resources}/bootstrap/js/bootstrap-tagsinput.js"></script>
  <!-- Custom JS -->
  <jsp:invoke fragment="js"/>
</head>

<body>

    <!-- header -->
    <c:url var="controller" value="/controller"/>

    <div class="container-fluid no-padding">
      <nav class="navbar navbar-default navbar-static-top">
        <div class="container-fluid">
          <div class="navbar-header">
            <a href="${homeGET}" class="btn btn-default">
              <img alt="Brand" src="${resources}/img/distributed24.png">
            </a>
            <a href="${showItemsGET}" class="btn btn-default navbar-btn">All items</a>
            <c:set var="customer" value="${sessionScope.customer}"/>
            <c:choose><c:when test="${customer eq null}">
              <a href="${loginGET}" class="btn btn-default navbar-btn pull-right">Sign in</a>
              <a href="${registerGET}" class="btn btn-default navbar-btn pull-right">New account</a>
            </c:when>
              <c:otherwise>
                <c:choose><c:when test="${customer.role eq 'admin'}">
                  <a href="${showOrdersGET}" class="btn btn-default navbar-btn">All orders</a>
                  <a href="${showCustomersGET}" class="btn btn-default navbar-btn">All customers</a>
                  <a href="${newItemGET}" class="btn btn-default navbar-btn">Add item</a>
                </c:when>
                  <c:otherwise>
                    <a href="${myAccountGET}" class="btn btn-default navbar-btn">My account</a>
                    <a href="${myItemsGET}" class="btn btn-default navbar-btn">My items</a>
                    <a href="${myOrdersGET}" class="btn btn-default navbar-btn">My orders</a>
                    <a href="${newOrderGET}" class="btn btn-default navbar-btn">New order</a>
                  </c:otherwise>
                </c:choose>
                <form action="${logoutPOST}" method="post" class="navbar-form navbar-right">
                  <button type="submit" class="btn btn-default">Sign out</button>
                </form>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </nav>

      <!-- page -->
      <jsp:doBody/>
    </div>

    <!-- footer -->
    <div id="footer">
      <div class="container text-center">
        Contact <a href="mailto:tarashypka@gmail.com">me</a> at anytime<br>May-August 2016
      </div>
    </div>
</body>
</html>
