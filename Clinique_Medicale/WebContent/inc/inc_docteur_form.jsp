<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<label for="identifiantDocteur">Identifiant <span class="requis">*</span></label>
<input type="text" id="identifiantDocteur" name="identifiantDocteur" value="<c:out value="${docteur.identifiant}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['identifiantDocteur']}</span>
<br />

<label for="nomDocteur">Nom <span class="requis">*</span></label>
<input type="text" id="nomDocteur" name="nomDocteur" value="<c:out value="${docteur.nom}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['nomDocteur']}</span>
<br />

<label for="prenomDocteur">Prénom </label>
<input type="text" id="prenomDocteur" name="prenomDocteur" value="<c:out value="${docteur.prenom}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['prenomDocteur']}</span>
<br />

<label for="adresseDocteur">Adresse <span class="requis">*</span></label>
<input type="text" id="adresseDocteur" name="adresseDocteur" value="<c:out value="${docteur.adresse}"/>" size="30" maxlength="60" />
<span class="erreur">${form.erreurs['adresseDocteur']}</span>
<br />

<label for="telephoneDocteur">Numéro de téléphone <span class="requis">*</span></label>
<input type="text" id="telephoneDocteur" name="telephoneDocteur" value="<c:out value="${docteur.telephone}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['telephoneDocteur']}</span>
<br />

<label for="emailDocteur">Adresse email</label>
<input type="email" id="emailDocteur" name="emailDocteur" value="<c:out value="${docteur.email}"/>" size="30" maxlength="60" />
<span class="erreur">${form.erreurs['emailDocteur']}</span>
<br />

<label for="motDePasseDocteur">Le mot de passe</label>
<input type="password" id="motDePasseDocteur" name="motDePasseDocteur" value="<c:out value="${docteur.motDePasse}"/>" size="30" maxlength="60" />
<span class="erreur">${form.erreurs['motDePasseDocteur']}</span>
<br />

<label for="imageDocteur">Upploader une image <span class="requis">*</span></label>
<input type="file" id="imageDocteur" name="imageDocteur"  />
<span class="erreur">${form.erreurs['imageDocteur']}</span>
<br />





