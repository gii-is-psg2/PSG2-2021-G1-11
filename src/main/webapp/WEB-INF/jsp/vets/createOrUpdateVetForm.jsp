<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script>
    window.addEventListener("load", onLoad, false);

    let domList;
    let addBtn;
    let clearBtn;
    let specialtyInput;
    let specialtiesMap;
    let specialties = [];
    
    function onLoad() {
        domList = $("#specialtyList");
        specialtyInput = $("#specialtyInput");
        addBtn = $("#specialtyAddBtn");
        clearBtn = $("#specialtiesClearBtn");
        specialtiesMap = $("#specialtiesMap");
        
        specialties = [
            <c:forEach items="${vet.specialtiesLS}" var="specialty">
                "${specialty}",
            </c:forEach>
        ];
        updateList();
        
        addBtn.on("click", addSpecialty);
        clearBtn.on("click", clearSpecialties);
    }

    function updateList() {
    	domList.html("");
    	for (let i = 0; i < specialties.length; i++) {
    		domList.append("<tr><td>" + specialties[i] + "</td><td><button onclick='removeSpecialty(" + i + ")' class='btn btn-default' type='button'>X</button></td></tr>");
    	}
    	mapSpecialtiesToDOM();
    }
    
    function addSpecialty() {
    	let specialty = specialtyInput.val();
    	if (specialty.length < 3 || specialty.length > 50) {
    		return;
    	}
    	
    	for (let i = 0; i < specialties.length; i++) {
    		if (specialties[i] === specialty) {
    			return;
    		}
    	}
    	
    	specialtyInput.val("");
    	specialties.push(specialty);
    	updateList();
    }
    
    function removeSpecialty(idx) {
    	specialties.splice(idx, 1);
    	updateList();
    }
    
    function clearSpecialties() {
    	specialties = [];
    	updateList();
    }
    
    function mapSpecialtiesToDOM() {
    	specialtiesMap.html("");
    	for (let i = 0; i < specialties.length; i++) {
    		specialtiesMap.append("<input type='hidden' name='specialtiesLS[" + i + "]' value='" + specialties[i] + "'>");
    	}
    }
</script>
<petclinic:layout pageName="vets">
	<jsp:body>
        <h2>Veterinarians</h2>
        <c:choose>
            <c:when test="${vet['new']}">
                <c:set var="action" value="/vets/new" />
            </c:when>
            <c:otherwise>
                <c:set var="action" value="/vets/${vet.id}/edit" />
            </c:otherwise>
        </c:choose>

        <form:form modelAttribute="vet" class="form-horizontal"
			action="${action}">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Nombre" name="firstName" />
                <petclinic:inputField label="Apellido" name="lastName" />
                <div class="form-group">
                	<div class="col-sm-2"></div>
                	<div class="col-sm-10">
                		<label>Especialidades</label>
	                	<table id="specialtyList" class="table table-striped"></table>
	                	<div class="col-sm-6">
	                	<input id="specialtyInput" class="form-control" type="text"
								list="specialtiesDatalist" maxlength="50">
		                <datalist id="specialtiesDatalist">
		                	<c:forEach items="${specialties}" var="specialty">
		                		<option value="${specialty}">
							</c:forEach>
		                </datalist>
		                </div>
		                <div class="col-sm-6">
		                <button type="button" class="btn btn-default" id="specialtyAddBtn">Añadir especialidad</button>
		                <button type="button" class="btn btn-default" id="specialtiesClearBtn">Borrar todas las especialidades</button>
		                </div>
		                <div id="specialtiesMap"></div>
					</div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${vet.id}" />

                    <c:choose>
                        <c:when test="${vet['new']}">
                            <button class="btn btn-default"
								type="submit">Añadir veterinario</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default"
								type="submit">Actualizar veterinario</button>
                        </c:otherwise>
                    </c:choose>

                    <spring:url value="/vets" var="vetUrl"> </spring:url>
                    <a class="btn btn-default"
						href="${fn:escapeXml(vetUrl)}">Volver atrás</a>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
