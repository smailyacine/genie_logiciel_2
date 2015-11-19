package com.app.forms;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.app.beans.Consultation;
import com.app.beans.Patient;
import com.app.dao.ConsultationDao;
import com.app.dao.DAOException;
import com.app.dao.PatientDao;
public final class ConnexionPatientForm {
	private static final String CHAMP_EMAIL = "email";
	private static final String CHAMP_PASS = "motdepasse";
	private static final String CHAMP_PASS_ERROR = "erreur_pass_email";
	private String resultat;
	private Map<String, String> erreurs = new HashMap<String,String>();
	private PatientDao patientDao;
	private ConsultationDao consultationtDao;
	public ConnexionPatientForm(PatientDao patientDao, ConsultationDao consultationDao) {
		this.consultationtDao = consultationDao;
		this.patientDao = patientDao;
	}
	
	public String getResultat() {
		return resultat;
	}
	public Map<String, String> getErreurs() {
		return erreurs;
	}
	public Patient connecterPatient( HttpServletRequest
			request ) {
		/* Récupération des champs du formulaire */
		String email = getValeurChamp( request, CHAMP_EMAIL );
		String motDePasse = getValeurChamp( request, CHAMP_PASS );
		Patient patient = new Patient();
		/* Validation du champ email. */
		
		traiterEmail( email, patient );
		traiterMotDePasse( motDePasse, patient );
		try {
			if ( erreurs.isEmpty() ) {
				patient = patientDao.trouverEmailPasse(email, motDePasse);
				if(patient == null){
					setErreur( CHAMP_PASS_ERROR, "La combinaison entrée n'est pas trouvée dans notre base de données !");
					resultat = "Echec de la connexion de médecin.";
				}
				else{
					resultat = "Succes de la connexion de médecin.";
				}
			
			} else {
				resultat = "Échec de la connexion du patient.";
			}
		} catch ( DAOException e ) {
			setErreur( "imprévu", "Erreur imprévue lors de la connexion." );
			resultat = "Échec de la connexion du médecin : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
			e.printStackTrace();
		}
		return patient;
	}
	/*---------- La mèthode pour recuprer les consultations d'un patient --------*/
	public ArrayList<Consultation> consultationPatient(Patient patient){
		ArrayList<Consultation> consultation = new ArrayList<Consultation>();
		try {
			consultation = consultationtDao.trouverPatient(patient);
			if(consultation == null){
				
				resultat = "Pas de consultation pour effectué pour Vous.";
			}
		}catch ( DAOException e ) {
			resultat = "Échec de la connexion du table de consultation : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
			e.printStackTrace();
		}
		return consultation;
		
	}
	/*---------------------------------------------------------------------------------------*/
	
	private void traiterEmail( String email, Patient patient ) {
		try {
			validationEmail( email );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_EMAIL, e.getMessage() );
		}
		patient.setEmail( email );
	}
	/* Initialisation du r�sultat global de la validation. */
	
	private void traiterMotDePasse( String motDePasse,  Patient patient ) {
		try {
			validationMotDePasse(motDePasse );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_PASS, e.getMessage() );
		}
		patient.setMotDePasse(motDePasse);
	}
	/**
	 * Valide l'adresse email saisie.
	 */
	private void validationEmail( String email ) throws FormValidationException {
		if ( email != null && !email.matches(
				"([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
			throw new FormValidationException( "Merci de saisir une adresse mail valide." );
		}
		if (email == null){
			throw new FormValidationException( "Ce champ est obligatoire ,Merci de saisir une adresse mail." );
		}
	}
	/**
	 * Valide le mot de passe saisi.
	 */
	private void validationMotDePasse( String motDePasse ) throws
	FormValidationException {
		if ( motDePasse != null ) {
			if ( motDePasse.length() < 3 ) {
				throw new FormValidationException( "Le mot de passe doit contenir au moins 3 caractères." );
			}
		} else {
			throw new FormValidationException( "Merci de saisir votre mot de passe." );
		}
	}
	/*
	 * Ajoute un message correspondant au champ spécifié à la map des
erreurs.
	 */
	private void setErreur( String champ, String message ) {
		erreurs.put( champ, message );
	}
	/* Méthode utilitaire qui retourne null si un champ est vide, et son
contenu
	 * sinon.
	 */
	private static String getValeurChamp( HttpServletRequest
			request, String nomChamp ) {
		String valeur = request.getParameter( nomChamp );
		if ( valeur == null || valeur.trim().length() == 0 ) {
			return null;
		} else {
			return valeur;
		}
	}
}