<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="menu">
	<p>
		<a href="<c:url value="/creationPatient"/>">Créer un nouveau
			patient</a>
	</p>
	<p>
		<a href="<c:url value="/creationDocteur"/>">Créer un nouveau
			docteur</a>
	</p>
	<p>
		<a href="<c:url value="/listePatients"/>">lister patients</a>
	</p>
	<p>
		<a href="<c:url value="/listeDocteurs"/>">lister docteurs</a>
	</p>

</div>