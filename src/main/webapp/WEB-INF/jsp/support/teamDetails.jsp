<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="contact">
	<h2>
		<fmt:message key="contact" />
	</h2>
	<table class="table table-striped">
        <thead>
	        <tr>
	            <th><fmt:message key="name"/></th>
				<th><fmt:message key="email"/></th>
				<th><fmt:message key="phone"/></th>
	        </tr>
        </thead>
        <tbody id="contactsBody">
        </tbody>
    </table>
</petclinic:layout>

<script>
window.addEventListener("load", setup, false);

var contactsBody;

var oJSON = {
    "operation": "core/get",
    "class": "Contact",
    "key": "SELECT Person FROM Person JOIN lnkPersonToTeam ON lnkPersonToTeam.person_id = Person.id JOIN Team ON lnkPersonToTeam.team_id = Team.id WHERE Team.name = 'G1-11 Team'",
    "output_fields": "friendlyname, email, phone"
};

function setup() {
    contactsBody = $("#contactsBody");

    $.ajax({
        type: "POST",
        url: "http://psg2-g1-11.indevspace.xyz/webservices/rest.php?version=1.0",
        dataType: "json",
        data: {
            auth_user: "apirest",
            auth_pwd: "@Apirest1234",
            json_data: JSON.stringify(oJSON)
        },
        crossDomain: "true"
    }).then(
        function(data, textStatus, jqXHR) {
            if (data.code === 0) {
                onLoadedData(Object.values(data.objects))
            }
        },
        function(jqXHR, textStatus, errorThrown) {
            putContactRow("Error", "Couldn't fetch data from server", "");
            console.debug(jqXHR);
        }
    );
}

function putContactRow(name, email, phone) {
	let tr = $("<tr>");
	tr.append($("<td>").text(name));
	tr.append($("<td>").text(email));
	tr.append($("<td>").text(phone));
    contactsBody.append(tr);
}

function onLoadedData(objects) {
    for (let contact of objects) {
        putContactRow(contact.fields.friendlyname, contact.fields.email, contact.fields.phone);
    }
}
</script>