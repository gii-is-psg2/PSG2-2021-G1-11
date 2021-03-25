<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="owners">
	<h2>
		<c:if test="${owner['new']}">
			<fmt:message key="new" />
		</c:if>
		<a> </a>
		<fmt:message key="owner" />
	</h2>
	<form:form modelAttribute="owner" class="form-horizontal"
		id="add-owner-form">
		<div class="form-group has-feedback">
			<fmt:message key="fName" var="fName"/>
			<petclinic:inputField label="${fName}" name="firstName" />
			<fmt:message key="lastName" var="lastName"/>
			<petclinic:inputField label="${lastName}" name="lastName" />
			<fmt:message key="address" var="address"/>
			<petclinic:inputField label="${address}" name="address" />
			<fmt:message key="city" var="city"/>
			<petclinic:inputField label="${city}" name="city" />
			<fmt:message key="phone" var="phone"/>
			<petclinic:inputField label="${phone}" name="telephone" />
			<fmt:message key="username" var="username"/>
			<petclinic:inputField label="${username}" name="user.username" />
			<fmt:message key="password" var="password"/>
			<petclinic:inputField label="${password}" name="user.password" />
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<c:choose>
					<c:when test="${owner['new']}">
						<button class="btn btn-default" type="submit">
							<fmt:message key="add"/>
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-default" type="submit"><fmt:message key="updOwner" /></button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form:form>
</petclinic:layout>
