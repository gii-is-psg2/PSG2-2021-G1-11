<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'yy/mm/dd'});
                $("#finishDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>New Booking</h2>

        <form:form modelAttribute="booking" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Check-in" name="startDate"/>
                <petclinic:inputField label="Check-out" name="finishDate"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                	<input type="hidden" name="petId" value="${booking.pet.id}"/>
                    <button class="btn btn-default" type="submit">Add Booking</button>
                </div>
            </div>
        </form:form>

		<b>Previous Bookings</b>
        <table class="table table-striped">
            <tr>
                <th>Check-in</th>
                <th>Check-out</th>
            </tr>
            <c:forEach var="booking" items="${booking.pet.bookings}">
				<tr>
				    <td><petclinic:localDate date="${booking.startDate}" pattern="yyyy/MM/dd"/></td>
				    <td><petclinic:localDate date="${booking.finishDate}" pattern="yyyy/MM/dd"/></td>
				</tr>                
			</c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>
