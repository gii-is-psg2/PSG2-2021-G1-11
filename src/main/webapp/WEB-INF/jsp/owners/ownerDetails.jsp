<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <h2><fmt:message key="ownerInf" /></h2>

    <table id ="ownersDetails" class="table table-striped">
        <tr>
            <th><fmt:message key="name" /></th>
            <td>
                <b><c:out value="${owner.firstName} ${owner.lastName}" /></b>
            </td>
        </tr>
        <tr>
            <th><fmt:message key="address" /></th>
            <td><c:out value="${owner.address}" /></td>
        </tr>
        <tr>
            <th><fmt:message key="city" /></th>
            <td><c:out value="${owner.city}" /></td>
        </tr>
        <tr>
            <th><fmt:message key="phone" /></th>
            <td><c:out value="${owner.telephone}" /></td>
        </tr>
    </table>

    <div style="display: flex;">
        <spring:url value="{ownerId}/edit" var="editUrl">
            <spring:param name="ownerId" value="${owner.id}" />
        </spring:url>
        <a href="${fn:escapeXml(editUrl)}" class="btn btn-default" style="margin-right: 10px;"><fmt:message key="editOwner" /></a>

        <spring:url value="{ownerId}/pets/new" var="addUrl">
            <spring:param name="ownerId" value="${owner.id}" />
        </spring:url>
        <a href="${fn:escapeXml(addUrl)}" class="btn btn-default" style="margin-right: 10px;"><fmt:message key="addPet" /></a>
        <form:form method="POST" action="${owner.id}/remove">
            <button type="submit" class="btn btn-default"><fmt:message key="remOwner" /></button>
        </form:form>
    </div>

    <br />
    <br />
    <br />
    <h2><fmt:message key="petsAndVisits" /></h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">
            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt><fmt:message key="name" /></dt>
                        <dd><c:out value="${pet.name}" /></dd>
                        <dt><fmt:message key="bDate" /></dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd" /></dd>
                        <dt><fmt:message key="type" /></dt>
                        <dd><c:out value="${pet.type.name}" /></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                            <tr>
                                <th><fmt:message key="vDate" /></th>
                                <th><fmt:message key="description" /></th>
                                <th><fmt:message key="actions" /></th>
                            </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd" /></td>
                                <td><c:out value="${visit.description}" /></td>
                                <td>
                                    <form:form method="POST" action="/owners/${owner.id}/pets/${pet.id}/visits/${visit.id}/remove">
                                        <button class="btn btn-default" type="submit"><fmt:message key="remVisit" /></button>
                                    </form:form>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}" />
                                    <spring:param name="petId" value="${pet.id}" />
                                </spring:url>
                                <a class="btn btn-default" href="${fn:escapeXml(petUrl)}"><fmt:message key="editPet" /></a>
                            </td>
                            <td>
                                <form:form method="POST" action="/owners/${owner.id}/pets/${pet.id}/remove">
                                    <button type="submit" class="btn btn-default"><fmt:message key="remPet" /></button>
                                </form:form>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}" />
                                    <spring:param name="petId" value="${pet.id}" />
                                </spring:url>
                                <a class="btn btn-default" href="${fn:escapeXml(visitUrl)}"><fmt:message key="addVisit" /></a>
                            </td>
                        </tr>
                    </table>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                            <tr>
                                <th><fmt:message key="checkIn" /></th>
                                <th><fmt:message key="checkOut" /></th>
                            </tr>
                        </thead>
                        <c:forEach var="booking" items="${pet.bookings}">
                            <tr>
                                <td><c:out value="${booking.startDate}" /></td>
                                <td><c:out value="${booking.finishDate}" /></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/booking/new" var="bookingUrl">
                                    <spring:param name="ownerId" value="${owner.id}" />
                                    <spring:param name="petId" value="${pet.id}" />
                                </spring:url>
                                <a class="btn btn-default" href="${fn:escapeXml(bookingUrl)}"><fmt:message key="addBooking" /></a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>

</petclinic:layout>
