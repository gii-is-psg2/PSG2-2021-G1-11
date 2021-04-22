<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">
    <h2>
        <c:if test="${cause['new']}"><fmt:message key="newCause"/> </c:if>
    </h2>
    <form:form modelAttribute="cause" class="form-horizontal" id="add-cause-form">
        <div class="form-group has-feedback">
        	<form:hidden path="isClosed"/>
      		<fmt:message var="name" key="name"/>
      		<fmt:message var="description" key="description"/>
      		<fmt:message var="budgetTarget" key="budgetTarget"/>
      		<fmt:message var="organization" key="organization"/>
            <petclinic:inputField label="${name}" name="name"/>
            <petclinic:inputField label="${description}" name="description"/>
            <petclinic:inputField label="${budgetTarget}" name="budgetTarget"/>
            <petclinic:inputField label="${organization}" name="organization"/>            
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${cause['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addCause"/></button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>