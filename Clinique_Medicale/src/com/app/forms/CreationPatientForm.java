package com.app.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.app.beans.Patient;
import com.app.dao.*;
import com.app.forms.FormValidationException;

public class CreationPatientForm {

	private static final String CHAMP_CHOIX_PATIENT     = "choixNouveauPatient";
	private static final String CHAMP_NOM               = "nomPatient";
	private static final String CHAMP_PRENOM            = "prenomPatient";
	private static final String CHAMP_ADRESSE           = "adressePatient";
	private static final String CHAMP_TELEPHONE         = "telephonePatient";
	private static final String CHAMP_EMAIL             = "emailPatient";
	private static final String GROUPE_SANGUIN          = "groupeSanguinPatient";
	private static final String GROUPE_SEXE             = "sexePatient";
	private static final String GROUPE_ASSURANCE        = "numAssurancePatient";
	private static final String GROUPE_MOT_DE_PASSE     = "motDePassePatient";
	private static final String ANCIEN_PATIENT          = "ancienPatient";
	private static final String CHAMP_LISTE_PATIENTS    = "listePatients";
	private static final String SESSION_PATIENTS        = "patients";


	private String              resultat;
	private Map<String, String> erreurs        = new HashMap<String, String>();
	private PatientDao patientDao;

	public CreationPatientForm(PatientDao patientDao) {
		this.patientDao = patientDao;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public String getResultat() {
		return resultat;
	}
	public Patient creerPatient ( HttpServletRequest request ) {
		String choixNouveauPatient = getValeurChamp( request, CHAMP_CHOIX_PATIENT );
		if ( ANCIEN_PATIENT.equals( choixNouveauPatient ) ) {
			/* Récupération du nom du client choisi */
			String nomAncienPatient = getValeurChamp( request, CHAMP_LISTE_PATIENTS );
			/* Récupération de l'objet client correspondant dans la session */
			HttpSession session = request.getSession();
			Patient patient = ( (Map<String, Patient>) session.getAttribute( SESSION_PATIENTS ) ).get( nomAncienPatient ); 
			return patient;
		}
		else {

			String nom = getValeurChamp( request, CHAMP_NOM );
			String prenom = getValeurChamp( request, CHAMP_PRENOM );
			String adresse = getValeurChamp( request, CHAMP_ADRESSE );
			String telephone = getValeurChamp( request, CHAMP_TELEPHONE );
			String email = getValeurChamp( request, CHAMP_EMAIL );
			String Gsanguin = getValeurChamp( request, GROUPE_SANGUIN );
			String sexe = getValeurChamp( request, GROUPE_SEXE );
			String assurance = getValeurChamp( request, GROUPE_ASSURANCE );
			String motDePasse = getValeurChamp( request, GROUPE_MOT_DE_PASSE );


			Patient patient = new Patient();

			traiterNom( nom, patient );
			traiterPrenom( prenom, patient );
			traiterAdresse( adresse, patient );
			traiterTelephone( telephone, patient );
			traiterEmail( email, patient );
			traiterGsnguin( Gsanguin, patient );
			traiterSexe( sexe, patient );
			traiterAssurance( assurance, patient );
			traiterMotDePasse( motDePasse, patient );

			try {
				if ( erreurs.isEmpty() ) {
					patientDao.creer(patient);
					resultat = "Succès de la création de patient.";
				} else {
					resultat = "Échec de la création du patient.";
				}
			} catch ( DAOException e ) {
				setErreur( "imprévu", "Erreur imprévue lors de la création." );
				resultat = "Échec de la création du client : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
				e.printStackTrace();
			}
			return patient;
		}

	}
	private void traiterNom( String nom, Patient patient ) {
		try {
			validationNom( nom );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_NOM, e.getMessage() );
		}
		patient.setNom( nom );
	}

	private void traiterPrenom( String prenom, Patient patient ) {
		try {
			validationPrenom( prenom );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_PRENOM, e.getMessage() );
		}
		patient.setPrenom( prenom );
	}

	private void traiterAdresse( String adresse, Patient patient ) {
		try {
			validationAdresse( adresse );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_ADRESSE, e.getMessage() );
		}
		patient.setAdresse( adresse );
	}


	private void traiterTelephone( String telephone, Patient patient )
	{
		try {
			validationTelephone( telephone );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_TELEPHONE, e.getMessage() );
		}
		patient.setTelephone( telephone );
	}


	private void traiterGsnguin( String Gsanguin,Patient patient ) {
		try {
			validationGsanguin( Gsanguin  );
		} catch ( FormValidationException e ) {
			setErreur( GROUPE_SANGUIN, e.getMessage() );
		}
		patient.setGroupeSanguin(Gsanguin);
	}

	private void traiterEmail( String email, Patient patient ) {
		try {
			validationEmail( email );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_EMAIL, e.getMessage() );
		}
		patient.setEmail( email );
	}

	private void traiterSexe( String sexe, Patient patient ) {
		try {
			validationSexe( sexe  );
		} catch ( FormValidationException e ) {
			setErreur( GROUPE_SEXE, e.getMessage() );
		}
		patient.setSexe(sexe);
	}


	private void traiterAssurance( String assurance, Patient patient ) {
		try {
			validationAssurance(assurance );
		} catch ( FormValidationException e ) {
			setErreur( GROUPE_ASSURANCE, e.getMessage() );
		}
		patient.setNum_assurance(assurance);
	}

	private void traiterMotDePasse( String motDePasse, Patient patient ) {
		try {
			validationMotDePasse(motDePasse );
		} catch ( FormValidationException e ) {
			setErreur( GROUPE_MOT_DE_PASSE, e.getMessage() );
		}
		patient.setMotDePasse(motDePasse);
	}


	private void validationNom( String nom ) throws FormValidationException {
		if ( nom != null ) {
			if ( nom.length() < 2 ) {
				throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 2 caractères." );
			}
		} else {
			throw new FormValidationException( "Merci d'entrer un nom d'utilisateur." );
		}
	}

	private void validationPrenom( String prenom ) throws FormValidationException {
		if ( prenom != null && prenom.length() < 2 ) {
			throw new FormValidationException( "Le prénom d'utilisateur doit contenir au moins 2 caractères." );
		}
	}

	private void validationAdresse( String adresse ) throws FormValidationException{
		if ( adresse != null ) {
			if ( adresse.length() < 10 ) {
				throw new FormValidationException ( "L'adresse  doit contenir au moins 10 caractères." );
			}
		} else {
			throw new FormValidationException( "Merci d'entrer une adresse de livraison." );
		}
	}

	private void validationTelephone( String telephone ) throws FormValidationException {
		if ( telephone != null ) {
			if ( !telephone.matches( "^\\d+$" ) ) {
				throw new FormValidationException( "Le numéro de téléphone doit uniquement contenir des chiffres." );
			} else if ( telephone.length() < 4 ) {
				throw new FormValidationException( "Le numéro de téléphone doit contenir au moins 4 chiffres." );
			}
		} else {
			throw new FormValidationException( "Merci d'entrer un numéro de téléphone." );
		}
	}

	private void validationEmail( String email ) throws FormValidationException {
		if( email != null){
			if (!email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
				throw new FormValidationException( "Merci de saisir une adresse mail valide." );
			} else if (patientDao.trouverEmail(email) != null ) {
				throw new FormValidationException( "Cette mail existe déja, merci d'entrer une autre." );
			}
		}
		else {
			throw new FormValidationException( " Ce champ est obligatoiren, merci d'entrer une mail." );
		}
	}
	private void validationGsanguin( String Gsanguin  ) throws FormValidationException {
		if ( Gsanguin == null ) {
			throw new FormValidationException( "Merci de specifier le votre groupe sanguin." );
		}
	}

	private void validationSexe( String sexe  ) throws FormValidationException {
		if (sexe == null ) {
			throw new FormValidationException( "Merci de specifier Votre sexe." );
		}
	}

	private void  validationAssurance( String assurance  ) throws FormValidationException {
		String regex = "[0-9]+";
		if (assurance != null ) {
			if( !assurance.matches(regex)){
				throw new FormValidationException( "Le numéro de sécurité est composé seulement des chifres." );
			}
			else if(assurance.trim().length() != 11){

				throw new FormValidationException( "Le numéro de sécurité est composé de 11 chifres." );
			}
			else if(patientDao.trouverSecurite(assurance) != null){

				throw new FormValidationException( "Ce numéro de sécurité sociale existe dèja, merci d'entrer un autre" );
			}
		}
		else{
			throw new FormValidationException( "Merci d'introduire un numéro de sécurité ." );
		}
	}


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
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 */
	private void setErreur( String champ, String message ) {
		erreurs.put( champ, message );
	}

	/*
	 * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
	 * sinon.
	 */
	private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
		String valeur = request.getParameter( nomChamp );
		if ( valeur == null || valeur.trim().length() == 0 ) {
			return null;
		} else {
			return valeur;
		}
	}



}









