<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="resources" value="${pageContext.request.contextPath}/resources"/>

<%-- Bootstrap validation style could be has-warning, has-success, ... --%>
<c:set var="validationStyle" value="has-error"/>

<%-- Server-side validation results --%>
<c:if test="${error ne null}">
  <c:choose>
    <c:when test="${error eq 'email_wrong'}">
      <c:set var="emailWarn" value="${validationStyle}"/>
      <c:set var="emailErrMsg" value="Account with such email not found"/>
    </c:when>
    <c:when test="${error eq 'psswd_wrong'}">
      <c:set var="psswdWarn" value="${validationStyle}"/>
      <c:set var="psswdErrMsg" value="Account with such combination of email and password not found"/>
    </c:when>
    <c:when test="${error eq 'acc_closed'}">
      <c:set var="closedWarn" value="${validationStyle}"/>
      <c:set var="closedErrMsg" value="Your account was closed"/>
    </c:when>
  </c:choose>
</c:if>

<t:genericpage>
  <jsp:attribute name="title">Login</jsp:attribute>

  <jsp:attribute name="js">
    <script type="text/javascript" src="${resources}/js/process-login-error.js"></script>
  </jsp:attribute>

  <jsp:body>
    <c:url var="controller" value="/controller"/>
    <form action="${controller}" method="post" class="form-horizontal" role="form">
      <input type="hidden" name="action" value="login">
      <div id="email" class="form-group col-sm-12 ${emailWarn}">
        <label for="email-inp" class="control-label col-sm-2">Email:</label>
        <div class="col-sm-4">
          <input id="email-inp" type="text" name="email" placeholder="Email?" class="form-control" aria-describedby="email-help">
          <span id="email-help" class="help-block">${emailErrMsg}</span>
        </div>
      </div>
      <div id="psswd" class="form-group col-sm-12 ${psswdWarn} ${closedWarn}">
        <label for="psswd-inp" class="control-label col-sm-2">Password:</label>
        <div class="col-sm-4">
          <input id="psswd-inp" type="password" name="psswd" placeholder="Password?" class="form-control" aria-describedby="password-help">
          <span id="psswd-help" class="help-block">${psswdErrMsg}${closedErrMsg}</span>
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