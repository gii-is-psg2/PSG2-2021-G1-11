<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<petclinic:layout pageName="adoptions">
	<c:choose> 
		<c:when test="${requestsNumber > 0}">
			<h2><fmt:message key="pendingRequest"/></h2>
			<table class="table table-striped">
		    	<thead>
		    		<tr>
		    			<th><fmt:message key="requestedPetName"/></th>
		    			<th><fmt:message key="applicantName"/></th>
		    			<th><fmt:message key="descriptionAdoption"/></th>
		    			<th></th>
		    			<th></th>
		    		</tr>
		    	</thead>
		    	<c:forEach var="request" items="${requests}">
		  			<tr>
		  				<td><c:out value="${request.requestedPet}" /></td>
		  				<td><c:out value="${request.applicant.firstName} ${request.applicant.lastName} " /></td>
		  				<td><c:out value="${request.description}" /></td>
		  				<td><a href="/adoptions/${request.id}/accept"><span class="glyphicon glyphicon-ok"></span></a></td>
		  				<td><a href="/adoptions/${request.id}/decline"><span class="glyphicon glyphicon-remove"></span></a></td>
		  			</tr>
		    	</c:forEach>
		    </table>
		</c:when>
		<c:otherwise>
			<h3><fmt:message key="noMoreRequest"/></h3>
		</c:otherwise>
	</c:choose>
    
	
</petclinic:layout>
