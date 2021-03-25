<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="owners">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#startDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
										$("#finishDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
        <h2>
			<fmt:message key="newBooking" />
		</h2>
        
        <b><fmt:message key="pet" /></b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="name" /></th>
                <th><fmt:message key="bDate" /></th>
                <th><fmt:message key="type" /></th>
                <th><fmt:message key="owner" /></th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${booking.pet.name}" /></td>
                <td><petclinic:localDate
						date="${booking.pet.birthDate}" pattern="yyyy/MM/dd" /></td>
                <td><c:out value="${booking.pet.type.name}" /></td>
                <td><c:out
						value="${booking.pet.owner.firstName} ${booking.pet.owner.lastName}" /></td>
            </tr>
        </table>

        <form:form modelAttribute="booking" class="form-horizontal">
            <div class="form-group has-feedback">

            <fmt:message key="checkIn" var="checkIn" />
			
                <petclinic:inputField label="${checkIn}"
					name="startDate" />
                <fmt:message key="checkOut" var="checkOut" />
                <petclinic:inputField label="${checkOut}"
					name="finishDate" />
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                	<input type="hidden" name="petId"
						value="${booking.pet.id}" />
                    <button class="btn btn-default" type="submit">
						<fmt:message key="addBooking" />
					</button>
                </div>
            </div>
        </form:form>
		<b><fmt:message key="prBookings" /></b>
        <table class="table table-striped">
            <tr>
                <th><fmt:message key="checkIn" /></th>
                <th><fmt:message key="checkOut" /></th>
            </tr>
            <c:forEach var="booking" items="${booking.pet.bookings}">
				<tr>
				    <td><petclinic:localDate date="${booking.startDate}"
							pattern="yyyy/MM/dd" /></td>
				    <td><petclinic:localDate date="${booking.finishDate}"
							pattern="yyyy/MM/dd" /></td>
				</tr>                
			</c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>
