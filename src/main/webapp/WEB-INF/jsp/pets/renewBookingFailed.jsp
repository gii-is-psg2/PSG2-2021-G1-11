<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<petclinic:layout pageName="home">
	<jsp:body>
	<div class="row">
		<div class="col-md-12">
			<h3><fmt:message key="couldntRenewBooking" /></h3>
			<a href="/owners/${ownerId}" class="btn btn-default"><fmt:message key="goBack" /></a>
		</div>
	</div>
    </jsp:body>
</petclinic:layout>
