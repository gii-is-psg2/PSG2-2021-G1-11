<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="cause">

	<h2>
		<fmt:message key="causesInfo" />
	</h2>
	
	<table id="causesDetails" class="table table-striped">
		<tr>
			<th><fmt:message key="name" /></th>
			<td><b><c:out value="${cause.name}" /></b></td>
		</tr>
		<tr>
			<th><fmt:message key="description" /></th>
			<td><c:out value="${cause.description}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="target" /></th>
			<td><c:out value="${cause.target}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="organization" /></th>
			<td><c:out value="${cause.organization}" /></td>
		</tr>
		<tr>
			<th><fmt:message key="isClosed" /></th>
			<td>
				<c:if test="${cause.isClosed }">
					<fmt:message key="True" />
				</c:if>
				<c:if test="${!cause.isClosed }">
					<fmt:message key="False" />
				</c:if>
			</td>
		</tr>
	</table>
	
	<br />
	<h2>
		<fmt:message key="donations" />
	</h2>

	<table id="donationsTable" class="table table-striped">
		<thead>
			<tr>
				<th><fmt:message key="name" /></th>
				<th><fmt:message key="date" /></th>
				<th><fmt:message key="amount" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${donations}" var="donation">
				<tr>
					<td><c:out value="${donation.client}" /></td>
					<td><c:out value="${donation.date}" /></td>
					<td><c:out value="${donation.amount}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
