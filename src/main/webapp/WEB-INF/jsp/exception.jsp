<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">
	<div class="row">
		<div class="col-md-12">
			<spring:url value="/resources/images/gatotriste.jpg" var="petsImage"/>
    		<img src="${petsImage}" class="center-block" />
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-md-12">
			<h1 class="text-center"><fmt:message key="error"/></h1>
			<h2 class="text-center">${exception.message}</h2>
		</div>
	</div>
</petclinic:layout>
