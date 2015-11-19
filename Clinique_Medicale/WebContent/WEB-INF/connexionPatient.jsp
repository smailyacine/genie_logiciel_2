<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Connexion</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/inc/style.css"/>" />
</head>
<body>
	<c:if test="${empty sessionScope.sessionPatient }">
		<form method="post" action="<c:url value="/connexionPatient" />">
			<fieldset>
				<legend>Connexion Patient</legend>
				<c:choose>
				<c:when test="${!empty form.erreurs['erreur_pass_email']}"><p class="requis">${form.erreurs['erreur_pass_email']}<p></c:when>
				<c:otherwise><p>Vous pouvez vous connecter via ce formulaire.</p></c:otherwise>
				</c:choose>
				<c:if test="${!empty requestScope.intervalleConnexions}">
					<p class="info">(Vous ne vous êtes pas connecté(e) depuis ce
						navigateur depuis ${requestScope.intervalleConnexions})</p>
				</c:if>
				<label for="nom">Adresse email <span class="requis">*</span></label>
				<input type="email" id="email" name="email"
					value="<c:out value="${patient.email}"/>" size="20" maxlength="60" />
				<span class="erreur">${form.erreurs['email']}</span> <br /> <label
					for="motdepasse">Mot de passe <span class="requis">*</span></label>
				<input type="password" id="motdepasse" name="motdepasse" value=""
					size="20" maxlength="20" /> <span class="erreur">${form.erreurs['motdepasse']}</span>
				<br /> <br /> <label for="memoire">Se souvenir de moi</label> <input
					type="checkbox" id="memoire" name="memoire" /> <br /> <input
					type="submit" value="Connexion" class="sansLabel" /> <br />
			</fieldset>
		</form>
	</c:if>
	<c:if test="${ !empty sessionScope.sessionPatient}">
		<c:import url="/inc/inc_patient_menu.jsp" />
		<p class="${empty form.erreurs ? 'succes' :'erreur'}">${form.resultat}</p>
		<%-- Vérification de la présence d'un objet docteur en session --%>
		<%-- Si l'docteur existe en session, alorson affiche son adresse email. --%>
		<p class="succes">Vous êtes connecté(e) avec l'adresse :
			${sessionScope.sessionPatient.email}</p>
	</c:if>
</html>