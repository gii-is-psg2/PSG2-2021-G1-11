<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  
<petclinic:layout pageName="Pets in adoption">
	<h2>
		<fmt:message key="petsInAdoption" />
	</h2>
	
	<table id="inAdoptionTable" class="table table-striped">
		<thead>
			<tr>
				<th><fmt:message key="name" /></th>
				<th><fmt:message key="birthDate" /></th>
				<th><fmt:message key="type"/></th>
				<th><fmt:message key="owner"/></th>
				<sec:authorize access="hasAuthority('owner')">
				<th><fmt:message key="adoptionApplication"/></th>
				</sec:authorize>
			

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pets}" var="pet">
				<tr>
					<td><c:out value="${pet.name}"/></td>
				
					<td><c:out value="${pet.birthDate}"/></td>
						
					<td><c:out value="${pet.type}"/></td>
						
					<td><c:out value="${pet.owner.user.username}"/></td>
					
					<sec:authorize access="hasAuthority('owner')">
					<td><spring:url value="/adoptions/pets/{petId}/apply" var="petUrl">
                              <spring:param name="petId" value="${pet.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(petUrl)}" class="btn btn-default" ><fmt:message key="Apply"/></a></td>
					</sec:authorize>
					
				</tr>
				
		</c:forEach>
		</tbody>
	</table>
	
</petclinic:layout>