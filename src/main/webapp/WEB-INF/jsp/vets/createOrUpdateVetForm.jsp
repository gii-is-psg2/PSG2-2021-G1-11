<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<petclinic:layout pageName="vets">
	<jsp:body>
    	<h2>Veterinarians</h2>
         <c:choose>
          	<c:when test="${vet['new']}">
          		<c:set var="action" value="/vets/new" />
          	</c:when>
          	<c:otherwise>
          		<c:set var ="action" value= "/vets/${vet.id}/edit"/>
          	</c:otherwise>
          </c:choose>
          
		<form:form modelAttribute="vet" class="form-horizontal" action= "${action}">
            <div class="form-group has-feedback">
            	<petclinic:inputField label="Nombre" name="firstName"/>
                <petclinic:inputField label="Apellido" name="lastName"/>			
                <petclinic:inputField label="especialidades" name="specialties"/>
            
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${vet.id}"/>
                  
                    <c:choose>
                    	<c:when test="${vet['new']}">
                    		<button class="btn btn-default" type="submit">Añadir veterinario</button>
                    	</c:when>
                    	<c:otherwise>
                    		<button class="btn btn-default" type="submit">Actualizar veterinario</button>
                    	</c:otherwise>
                    </c:choose>
                    
                    <spring:url value="/vets" var="vetUrl">
                    </spring:url>
                    <a class="btn btn-default" href="${fn:escapeXml(vetUrl)}">Volver atrás</a>
                </div>
            </div>                  
         </form:form>
          
    </jsp:body>
</petclinic:layout>
