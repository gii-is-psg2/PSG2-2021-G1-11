<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="vets">
    <h2><fmt:message key="vet"/></h2>
	<spring:url value="/vets/new" var="vetUrl"></spring:url>
    <a class="btn btn-default" href="${fn:escapeXml(vetUrl)}"><fmt:message key="addVet"/></a>
    <br>
    <br>
    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="specialties"/></th>
            <th><fmt:message key="actions"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vets.vetList}" var="vet">
            <tr>
                <td>
                    <c:out value="${vet.firstName} ${vet.lastName}"/>
                </td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}">
                        <c:out value="${specialty.name} "/>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}">ninguna</c:if>
                </td>
                <td>
	                <div style="display: flex;">
	                	<spring:url value="/vets/{vetId}/edit" var="vetUrl">
	        				<spring:param name="vetId" value="${vet.id}"/>
	    				</spring:url>
	    				<a href="${fn:escapeXml(vetUrl)}" class="btn btn-default"><fmt:message key="editVet"/></a>
	                	<form:form method="POST" action="vets/${vet.id}/remove">
	                		<button class="btn btn-default" type="submit" style="margin-left: 20px;"><fmt:message key="remVet"/></button>
	                	</form:form>
                	</div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>

