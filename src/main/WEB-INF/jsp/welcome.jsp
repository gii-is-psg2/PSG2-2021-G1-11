<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<petclinic:layout pageName="home">
	<div class="row">
		<div class="col-md-12">
			<h1 class="text-center"><fmt:message key="welcome"/></h1>
		</div>
	</div>
    <div class="row">
        <div class="col-md-12">
            <spring:url value="/resources/images/fotoInicio.jpg" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive center-block" src="${petsImage}"/>
        </div>
    </div>
</petclinic:layout>
