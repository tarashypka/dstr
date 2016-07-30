<%@ page contentType="text/html" pageEncoding="UTF-8" %>
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
  <jsp:attribute name="title">Registration</jsp:attribute>

  <jsp:attribute name="js">
    <script type="text/javascript" src="${resources}/js/process-register-error.js"></script>
  </jsp:attribute>

  <jsp:body>

    <c:url var="controller" value="/controller"/>
    <form action="${controller}" method="post" class="form-horizontal" role="form">
      <input type="hidden" name="action" value="register">
      <div id="email" class="form-group col-sm-12">
        <label for="email-inp" class="control-label col-sm-2">Email:</label>
        <div class="controls col-sm-4">
          <input id="email-inp" type="text" name="email" value="${_customer.email}" placeholder="Email?" class="form-control" aria-describedby="email-help">
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
      <div id="password2" class="form-group col-sm-12">
        <label for="password2-inp" class="control-label col-sm-2">Repeat password:</label>
        <div class="col-sm-4">
          <input id="password2-inp" type="password" name="password2" placeholder="Password?" class="form-control" aria-describedby="password-help">
          <span id="password2-help" class="help-block"></span>
        </div>
      </div>
      <div id="name" class="form-group col-sm-12">
        <label for="name-inp" class="control-label col-sm-2">Name:</label>
        <div class="col-sm-4">
          <input id="name-inp" type="text" name="name" value="${_customer.name}" placeholder="Name?" class="form-control" aria-describedby="name-help">
          <span id="name-help" class="help-block"></span>
        </div>
      </div>
      <div id="surname" class="form-group col-sm-12">
        <label for="surname-inp" class="control-label col-sm-2">Surname:</label>
        <div class="col-sm-4">
          <input id="surname-inp" type="text" name="surname" value="${_customer.surname}" placeholder="Surname?" class="form-control" aria-describedby="surname-help">
          <span id="surname-help" class="help-block"></span>
        </div>
      </div>
      <div class="form-group col-sm-12">
        <div class="col-sm-offset-2 col-sm-4">
          <button type="submit" class="btn btn-default">Create account</button>
        </div>
      </div>
    </form>
    <script>process("${error}");</script>
  </jsp:body>
</t:genericpage>