<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<label for="nomPatient">Nom <span class="requis">*</span></label>
<input type="text" id="nomPatient" name="nomPatient" value="<c:out value="${patient.nom}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['nomPatient']}</span>
<br />

<label for="prenomPatient">Prénom </label>
<input type="text" id="prenomPatient" name="prenomPatient" value="<c:out value="${patient.prenom}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['prenomPatient']}</span>
<br />

<label for="adressePatient">Adresse <span class="requis">*</span></label>
<input type="text" id="adressePatient" name="adressePatient" value="<c:out value="${patient.adresse}"/>" size="30" maxlength="60" />
<span class="erreur">${form.erreurs['adressePatient']}</span>
<br />

<label for="telephonePatient">Numéro de téléphone <span class="requis">*</span></label>
<input type="text" id="telephonePatient" name="telephonePatient" value="<c:out value="${patient.telephone}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['telephonePatient']}</span>
<br />

<label for="emailPatient">Adresse email</label>
<input type="email" id="emailPatient" name="emailPatient" value="<c:out value="${patient.email}"/>" size="30" maxlength="60" />
<span class="erreur">${form.erreurs['emailPatient']}</span>
<br />

<label for="groupeSanguinPatient">Le groupe Sanguin</label>
<input type="radio" id="groupeSanguinPatient" name="groupeSanguinPatient" value="A">A
<input type="radio" id="groupeSanguinPatient" name="groupeSanguinPatient" value="B"/>B
<input type="radio" id="groupeSanguinPatient" name="groupeSanguinPatient" value="AB"/>AB
<input type="radio" id="groupeSanguinPatient" name="groupeSanguinPatient" value="O"/>O
<span class="erreur">${form.erreurs['groupeSanguinPatient']}</span>
<br />

<label for="sexePAtient">Sexe</label>
<input type="radio" id="sexePatient" name="sexePatient" value="Homme">Homme
<input type="radio" id="sexePatient" name="sexePatient" value="Femme"/>Femme
<span class="erreur">${form.erreurs['sexePatient']}</span>
<br />

<label for="numAssurancePatient">Numéro De sécurité sociale <span class="requis">*</span></label>
<input type="text" id="numAssurancePatient" name="numAssurancePatient" value="<c:out value="${patient.num_assurance}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['numAssurancePatient']}</span>
<br />

<label for="motDePassePatient">Le mot de Passe <span class="requis">*</span></label>
<input type="password" id="motDePassePatient" name="motDePassePatient" value="<c:out value="${patient.motDePasse}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['motDePassePatient']}</span>
<br />

<label for="imagePatient">Upploader une image <span class="requis">*</span></label>
<input type="file" id="imagePatient" name="imagePatient"  />
<span class="erreur">${form.erreurs['imagePatient']}</span>
<br />