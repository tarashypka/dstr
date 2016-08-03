<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: deoxys
  Date: 29.05.16
  Time: 7:48
  To change this template use File | Settings | File Templates.
--%>

<c:set var="resources" value="${pageContext.request.contextPath}/resources"/>

<t:genericpage>
  <jsp:attribute name="title">Login</jsp:attribute>

  <jsp:attribute name="js">
    <script type="text/javascript" src="${resources}/js/process-login-error.js"></script>
  </jsp:attribute>

  <jsp:body>
    <c:url var="controller" value="/controller"/>
    <form action="${controller}" method="post" class="form-horizontal" role="form">
      <input type="hidden" name="action" value="login">
      <div id="email" class="form-group col-sm-12">
        <label for="email-inp" class="control-label col-sm-2">Email:</label>
        <div class="col-sm-4">
          <input id="email-inp" type="text" name="email" placeholder="Email?" class="form-control" aria-describedby="email-help">
          <span id="email-help" class="help-block"></span>
        </div>
      </div>
      <div id="password" class="form-group col-sm-12">
        <label for="password-inp" class="control-label col-sm-2">Password:</label>
        <div class="col-sm-4">
          <input id="password-inp" type="password" name="password" placeholder="Password?" class="form-control" aria-describedby="password-help">
          <span id="password-help" class="help-block"></span>
        </div>
      </div>
      <div class="form-group col-sm-12">
        <div class="col-sm-offset-2 col-sm-4">
          <button type="submit" class="btn btn-default">Sign in</button>
        </div>
      </div>
    </form>
    <script>embedErrorMessage("${error}");</script>
  </jsp:body>
</t:genericpage>