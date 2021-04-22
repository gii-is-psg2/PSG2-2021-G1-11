<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<petclinic:layout pageName="adoptionApplication">
	<c:choose>
		<c:when test="${adoptionApplicationsNumber > 0}">
			<h2>
				<fmt:message key="pendingAdoptionApplication" />
			</h2>
			<table class="table table-striped">
				<thead>
					<tr>
						<th><fmt:message key="requestedPetName" /></th>
						<th><fmt:message key="applicantName" /></th>
						<th><fmt:message key="descriptionAdoption" /></th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<c:forEach var="adoptionApplication" items="${adoptionApplications}">
					<tr>
						<td><c:out value="${adoptionApplication.requestedPet}" /></td>
						<td><c:out
								value="${adoptionApplication.applicant.firstName} ${adoptionApplication.applicant.lastName} " /></td>
						<td><c:out value="${adoptionApplication.description}" /></td>
						<td><a href="/adoptions/${adoptionApplication.id}/accept"><span
								class="glyphicon glyphicon-ok"></span></a></td>
						<td><a href="/adoptions/${adoptionApplication.id}/decline"><span
								class="glyphicon glyphicon-remove"></span></a></td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<h3>
				<fmt:message key="noMoreAdoptionApplication" />
			</h3>
		</c:otherwise>
	</c:choose>


</petclinic:layout>
