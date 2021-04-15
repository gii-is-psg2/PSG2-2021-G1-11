<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="donations">
    <h2><fmt:message key="donations"/></h2>
	<spring:url value="/doantionsList" var="donationUrl"></spring:url>
<table id="donationTable" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="amount"/></th>
            <th><fmt:message key="date"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${donations}" var="donation">
            <tr>
                <td>
                    <c:out value="${donation.owner.firstName} ${donation.owner.lastName}"/>
                </td>
                <td>
                    <c:out value="${donation.amount}"/>
                </td>
             	<td>
                    <c:out value="${donation.date}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <spring:url value="/causes/{causesId}" var="causesUrl"> </spring:url><spring:param name="causesId" value="${cause.id}"/>
    <a class="btn btn-default" href="${fn:escapeXml(vetUrl)}"><fmt:message key="goBack"/></a>
</petclinic:layout>