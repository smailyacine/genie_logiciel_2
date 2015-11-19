<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Création d'un docteur</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
    <body>
    
          
        <div>
            <form method="post" action="<c:url value="/creationDocteur"/>">
                <fieldset>
                    <legend>Informations docteur</legend>
                          <%-- Si et seulement si la Map des clients en session n'est pas vide, alors on propose un choix à l'utilisateur --%>
                    <c:if test="${ !empty sessionScope.docteurs }">
                        <label for="choixNouveauDocteur">Nouveau docteur ? <span class="requis">*</span></label>
                        <input type="radio" id="choixNouveauDocteur" name="choixNouveauDocteur" value="nouveauDocteur" checked /> Oui
                        <input type="radio" id="choixNouveauDocteur" name="choixNouveauDocteur" value="ancienDocteur" /> Non
                        <br/><br />
                    </c:if>
                
           
                    <c:set var="docteur" value="${ docteur }" scope="request" />
                    <div id="nouveauDocteur">
                        <c:import url="/inc/inc_docteur_form.jsp" />
                    </div>
       
         <%-- Si et seulement si la Map des clients en session n'est pas vide, alors on crée la liste déroulante --%>
                    <c:if test="${ !empty sessionScope.docteurs }">
                    <div id="ancienDocteur">
                        <select name="listeDocteurs" id="listeDocteurs">
                            <option value="">Choisissez un docteur...</option>
                            <%-- Boucle sur la map des clients --%>
                            <c:forEach items="${ sessionScope.docteurs }" var="docteurs">
                            <%--  L'expression EL ${mapClients.value} permet de cibler l'objet Client stocké en tant que valeur dans la Map, 
                                  et on cible ensuite simplement ses propriétés nom et prenom comme on le ferait avec n'importe quel bean. --%>
                            <option value="${ docteurs.value.nom }">${ docteurs.value.prenom } ${ docteurs.value.nom }</option>
                            </c:forEach>
                        </select>
                    </div>
                    </c:if>
                    
                 <input type="submit" value="Valider"  />
                <input type="reset" value="Remettre à zéro" /> <br />
                
                </fieldset>
               </form>  
           </div>
    </body>
</html>