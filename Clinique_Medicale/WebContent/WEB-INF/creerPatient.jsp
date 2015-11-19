<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Création d'un patient</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
    <body>
    
          
        <div>
            <form method="post" action="<c:url value="/creationPatient"/>">
                <fieldset>
                    <legend>Informations patient</legend>
                          <%-- Si et seulement si la Map des clients en session n'est pas vide, alors on propose un choix à l'utilisateur --%>
                    <c:if test="${ !empty sessionScope.patients }">
                        <label for="choixNouveauPatient">Nouveau patient ? <span class="requis">*</span></label>
                        <input type="radio" id="choixNouveauPatient" name="choixNouveauPatient" value="nouveauPatient" checked /> Oui
                        <input type="radio" id="choixNouveauPatient" name="choixNouveauPatient" value="ancienPatient" /> Non
                        <br/><br />
                    </c:if>
                
           
                    <c:set var="patient" value="${ patient }" scope="request" />
                    <div id="nouveauPatient">
                        <c:import url="/inc/inc_patient_form.jsp" />
                    </div>
       
         <%-- Si et seulement si la Map des clients en session n'est pas vide, alors on crée la liste déroulante --%>
                    <c:if test="${ !empty sessionScope.patients }">
                    <div id="ancienClient">
                        <select name="listePatients" id="listePatients">
                            <option value="">Choisissez un patient...</option>
                            <%-- Boucle sur la map des clients --%>
                            <c:forEach items="${ sessionScope.patients }" var="patients">
                            <%--  L'expression EL ${mapClients.value} permet de cibler l'objet Client stocké en tant que valeur dans la Map, 
                                  et on cible ensuite simplement ses propriétés nom et prenom comme on le ferait avec n'importe quel bean. --%>
                            <option value="${ patients.value.nom }">${ patients.value.prenom } ${ patients.value.nom }</option>
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