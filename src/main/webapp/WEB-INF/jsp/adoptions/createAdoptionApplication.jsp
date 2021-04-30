<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="adoptionApplication">
	<jsp:body>
		<h2><fmt:message key="sendAdoptionApplication" /></h2>
		<br>
        <form:form modelAttribute="adoptionApplication" class="form-horizontal">
           	<div class="form-group has-feedback">
				<fmt:message key="description" var="description" />
				<petclinic:inputField label="${description}" name="description" />
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button class="btn btn-default" type="submit">
						<fmt:message key="sendApplication" />
					</button>
					<a class="btn btn-default">
						<fmt:message key="goBack" />
					</a>
				</div>
			</div>
        </form:form>
    </jsp:body>
</petclinic:layout>
