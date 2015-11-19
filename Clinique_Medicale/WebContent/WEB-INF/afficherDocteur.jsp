<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'un docteur</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
    <body>
        <div id="corps">
            <p class="info">${ message }</p>
            <c:if test="${ !erreur }">
            <c:import url="/inc/inc_docteur_menu.jsp" />
                <p>Identifiant : <c:out value="${ docteur.identifiant }"/></p>
                <p>Nom : <c:out value="${ docteur.nom }"/></p>
                <p>Prénom : <c:out value="${ docteur.prenom }"/></p>
                <p>Adresse : <c:out value="${ docteur.adresse }"/></p>
                <p>Numéro de téléphone : <c:out value="${ docteur.telephone }"/></p>
                <p>Email : <c:out value="${ docteur.email }"/></p> 
                    <%-- Vérification de la présence d'un objet utilisateur en session --%> 
                <c:if test="${!empty sessionScope.docteurs}">
                
                    <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
                	<p class="succes">Vous êtes connecté(e) avec l'adresse : ${docteur.email}</p>
                </c:if>
            </c:if>
            
        </div>
    </body>
</html>