<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="resources" value="${pageContext.request.contextPath}/resources"/>

<%-- Bootstrap validation style could be has-warning, has-success, ... --%>
<c:set var="validationStyle" value="has-error"/>

<%--
It's not assumed that every customer has Js enabled,
thus server-side validation will be performed if it's disabled
 --%>

<%-- Server-side validation results (if Js is disabled) --%>
<c:if test="${error ne null}">
  <c:choose>
    <c:when test="${error eq 'email_dup'}">
      <c:set var="emailWarn" value="${validationStyle}"/>
      <c:set var="emailErrMsg" value="Acount with such an email already exists"/>
    </c:when>
    <c:when test="${error eq 'email_empty'}">
      <c:set var="emailWarn" value="${validationStyle}"/>
      <c:set var="emailErrMsg" value="Enter email"/>
    </c:when>
    <c:when test="${error eq 'email_wrong'}">
      <c:set var="emailWarn" value="${validationStyle}"/>
      <c:set var="emailErrMsg" value="'${_customer.email}' is not a valid email address"/>
    </c:when>
    <c:when test="${error eq 'psswd_empty'}">
      <c:set var="psswdWarn" value="${validationStyle}"/>
      <c:set var="psswdErrMsg" value="Enter password"/>
    </c:when>
    <c:when test="${error eq 'psswd_weak'}">
      <c:set var="psswdWarn" value="${validationStyle}"/>
      <c:set var="psswdErrMsg" value="Password is weak, should be at least 8 characters"/>
    </c:when>
    <c:when test="${error eq 'psswd2_wrong'}">
      <c:set var="psswd2Warn" value="${validationStyle}"/>
      <c:set var="psswd2ErrMsg" value="Passwords do not match"/>
    </c:when>
  </c:choose>
</c:if>

<t:genericpage>
  <jsp:attribute name="title">Registration</jsp:attribute>

  <jsp:attribute name="js">
    <script type="text/javascript" src="${resources}/js/register-form-validator.js"></script>
  </jsp:attribute>

  <jsp:body>
    <c:url var="controller" value="/controller"/>
    <form action="${controller}" method="post" id="register-form" class="form-horizontal" role="form" onsubmit="return validateForm('${validationStyle}');">
      <input type="hidden" name="validated" value="false">
      <input type="hidden" name="action" value="register">
      <div id="email" class="form-group col-sm-12 ${emailWarn}">
        <label for="email-inp" class="control-label col-sm-2">Email:</label>
        <div class="controls col-sm-4">
          <input id="email-inp" type="text" name="email" value="${_customer.email}" placeholder="Email?" class="form-control" aria-describedby="email-help">
          <span id="email-help" class="help-block">${emailErrMsg}</span>
        </div>
      </div>
      <div id="psswd" class="form-group col-sm-12 ${psswdWarn}">
        <label for="psswd-inp" class="control-label col-sm-2">Password:</label>
        <div class="col-sm-4">
          <input id="psswd-inp" type="password" name="psswd" placeholder="Password?" class="form-control" aria-describedby="password-help">
          <span id="psswd-help" class="help-block">${psswdErrMsg}</span>
        </div>
      </div>
      <div id="psswd2" class="form-group col-sm-12 ${psswd2Warn}">
        <label for="psswd2-inp" class="control-label col-sm-2">Confirm password:</label>
        <div class="col-sm-4">
          <input id="psswd2-inp" type="password" name="psswd2" placeholder="Password?" class="form-control" aria-describedby="password-help">
          <span id="psswd2-help" class="help-block">${psswd2ErrMsg}</span>
        </div>
      </div>
      <div class="form-group col-sm-12">
        <div class="col-sm-offset-2 col-sm-4">
          <button type="submit" class="btn btn-default">Create account</button>
        </div>
      </div>
    </form>
  </jsp:body>
</t:genericpage>