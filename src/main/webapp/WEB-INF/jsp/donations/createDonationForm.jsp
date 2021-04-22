<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

</script>
<petclinic:layout pageName="donations">
    <jsp:body>
        <h2><fmt:message key="newDonation"/></h2>
        <form:form modelAttribute="donation" class="form-horizontal"
                   action="${action}">
            <div class="form-group has-feedback">
                <fmt:message key="amount" var="amount"/>
                <petclinic:inputField label="${amount}" name="amount"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default"
                            type="submit"><fmt:message key="addDonation"/></button>

                    <spring:url value="/causes" var="causesUrl"> </spring:url>
                    <a class="btn btn-default"
                       href="${fn:escapeXml(causesUrl)}"><fmt:message key="goBack"/></a>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
