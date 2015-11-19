package com.app.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.app.beans.Docteur;
import com.app.beans.Patient;
import com.app.dao.DAOException;
import com.app.dao.DocteurDao;

public final class ConnexionDocteurForm {
	private static final String CHAMP_EMAIL = "email";
	private static final String CHAMP_PASS = "motdepasse";
	private static final String CHAMP_PASS_ERROR = "erreur_pass_email";
	private static final String GROUPE_MOT_DE_PASSE     = "motDePasseDocteur";
	private String resultat;
	private Map<String, String> erreurs = new HashMap<String, String>();
	private DocteurDao docteurDao;

	public ConnexionDocteurForm(DocteurDao docteurDao) {
		this.docteurDao = docteurDao;
	}

	public String getResultat() {
		return resultat;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public Docteur connecterDocteur(HttpServletRequest request) {
		/* R�cup�ration des champs du formulaire */
		String email=(String) request.getAttribute(CHAMP_EMAIL);
		String motDePasse=(String) request.getAttribute(GROUPE_MOT_DE_PASSE);
		if(email==null ||motDePasse==null){
		email = getValeurChamp(request, CHAMP_EMAIL);
		motDePasse = getValeurChamp(request, CHAMP_PASS);
		}
		Docteur docteur = new Docteur();
		/* Validation du champ email. */

		traiterEmail(email, docteur);
		traiterMotDePasse(motDePasse, docteur);
		try {
			if (erreurs.isEmpty()) {
				docteur = docteurDao.trouverEmailPasse(email, motDePasse);
				if (docteur == null) {
					setErreur(CHAMP_PASS_ERROR, "La combinaison entr�e n'est trouv�e dans notre base de donn�es !");
					resultat = "Echec de la connexion de m�decin.";
				} else {
					resultat = "Succes de la connexion de m�decin.";
				}

			} else {
				resultat = "�chec de la connexion du patient.";
			}
		} catch (DAOException e) {
			setErreur("impr�vu", "Erreur impr�vue lors de la connexion.");
			resultat = "�chec de la connexion du m�decin : une erreur impr�vue est survenue, merci de r�essayer dans quelques instants.";
			e.printStackTrace();
		}
		return docteur;
	}

	private void traiterEmail(String email, Docteur docteur) {
		try {
			validationEmail(email);
		} catch (FormValidationException e) {
			setErreur(CHAMP_EMAIL, e.getMessage());
		}
		docteur.setEmail(email);
	}
	/* Initialisation du r�sultat global de la validation. */

	private void traiterMotDePasse(String motDePasse, Docteur docteur) {
		try {
			validationMotDePasse(motDePasse);
		} catch (FormValidationException e) {
			setErreur(CHAMP_PASS, e.getMessage());
		}
		docteur.setMotDePasse(motDePasse);
	}

	/**
	 * Valide l'adresse email saisie.
	 */
	private void validationEmail(String email) throws FormValidationException {
		if (email != null && !email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
			throw new FormValidationException("Merci de saisir une adresse mail valide.");
		}
		if (email == null) {
			throw new FormValidationException("Ce champ est obligatoire, Merci de saisir une adresse mail.");
		}
	}

	/**
	 * Valide le mot de passe saisi.
	 */
	private void validationMotDePasse(String motDePasse) throws FormValidationException {
		if (motDePasse != null) {
			if (motDePasse.length() < 3) {
				throw new FormValidationException("Le mot de passe doit contenir au moins 3 caract�res.");
			}
		} else {
			throw new FormValidationException("Merci de saisir votre mot de passe.");
		}
	}

	/*
	 * Ajoute un message correspondant au champ sp�cifi� � la map des
	 * erreurs.
	 */

	/* Verification de mot de passe a partir de base de données */

	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	/*
	 * M�thode utilitaire qui retourne null si un champ est vide, et son
	 * contenu sinon.
	 */
	private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
		String valeur = request.getParameter(nomChamp);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur;
		}
	}
/*---------- La mèthode pour recuprer les consultations d'un patient --------*/
	
}