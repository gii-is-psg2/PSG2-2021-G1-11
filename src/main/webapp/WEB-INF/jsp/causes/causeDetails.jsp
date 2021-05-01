<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<petclinic:layout pageName="cause">

    <h2>
        <fmt:message key="causesInfo"/>
    </h2>

    <table id="causesDetails" class="table table-striped">
        <tr>
            <th><fmt:message key="name"/></th>
            <td><b><c:out value="${cause.name}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="description"/></th>
            <td><c:out value="${cause.description}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="target"/></th>
            <td><c:out value="${cause.target}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="organization"/></th>
            <td><c:out value="${cause.organization}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="isClosed"/></th>
            <td>
                <c:if test="${cause.isClosed }">
                    <fmt:message key="True"/>
                </c:if>
                <c:if test="${!cause.isClosed }">
                    <fmt:message key="False"/>
                </c:if>
            </td>
        </tr>
    </table>
    
    <div style="display: flex;">
    	<spring:url value="/causes/{causeId}/edit" var="editCausesUrl">
					<spring:param name="causeId" value="${cause.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(editCausesUrl)}" class="btn btn-default"><fmt:message key="editCauseMessage"/></a>	
		
		 <form:form method="POST" action="${cause.id}/remove">
            <button type="submit" class="btn btn-default"><fmt:message key="removeCause" /></button>
        </form:form>	
    </div>

    <br/>
    <h2>
        <fmt:message key="donations"/>
    </h2>

    <table id="donationsTable" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="date"/></th>
            <th><fmt:message key="amount"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${donations}" var="donation">
            <tr>
                <td><c:out value="${donation.ownerName}"/></td>
                <td><c:out value="${donation.donationDate}"/></td>
                <td><c:out value="${donation.amount}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${not empty status}">
        <div class="alert alert-info">
            <p><fmt:message key="${status}"/>
                <c:if test="${not empty returnAmount}">
                    <c:out value="${returnAmount}"></c:out>
                </c:if>
            </p>
        </div>
    </c:if>

    <spring:url value="/causes" var="causesUrl"> </spring:url>
    <a class="btn btn-default"
       href="${fn:escapeXml(causesUrl)}"><fmt:message key="goBack"/></a>
</petclinic:layout>
