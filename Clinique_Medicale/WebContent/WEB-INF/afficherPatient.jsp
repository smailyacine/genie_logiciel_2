<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'un patient</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
    <body>
        <div id="corps">
            <p class="info">${ message }</p>
                <p>Nom : <c:out value="${ patient.nom }"/></p>
                <p>Prénom : <c:out value="${ patient.prenom }"/></p>
                <p>Adresse : <c:out value="${ patient.adresse }"/></p>
                <p>Numéro de téléphone : <c:out value="${ patient.telephone }"/></p>
                <p>Email : <c:out value="${ patient.email }"/></p>
                    <%-- Vérification de la présence d'un objet utilisateur en session --%>
                <c:if test="${!empty sessionScope.patients}">
                    <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
                	<p class="succes">Vous êtes connecté(e) avec l'adresse : ${patient.email}</p>
                </c:if>
            
        </div>
    </body>
</html>