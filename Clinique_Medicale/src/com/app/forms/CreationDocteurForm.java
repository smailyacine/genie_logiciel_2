package com.app.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.app.beans.Docteur;
import com.app.beans.Patient;
import com.app.dao.DAOException;
import com.app.dao.DocteurDao;

public class CreationDocteurForm {

	private static final String CHAMP_CHOIX_DOCTEUR     = "choixNouveauDocteur";
	private static final String CHAMP_NOM               = "nomDocteur";
	private static final String CHAMP_PRENOM            = "prenomDocteur";
	private static final String CHAMP_ADRESSE           = "adresseDocteur";
	private static final String CHAMP_TELEPHONE         = "telephoneDocteur";
	private static final String CHAMP_EMAIL             = "emailDocteur";
	private static final String ANCIEN_DOCTEUR         = "ancienDocteur";
	private static final String CHAMP_LISTE_DOCTEURS    = "listeDocteurs";
	private static final String SESSION_DOCTEURS        = "docteurs";
	private static final String CHAMP_IDENTIFIANT      = "identifiantDocteur";
	private static final String GROUPE_MOT_DE_PASSE     = "motDePasseDocteur";


	private String              resultat;
	private Map<String, String> erreurs        = new HashMap<String, String>();
	private DocteurDao docteurDao;

	public CreationDocteurForm(DocteurDao docteurDao) {
		this.docteurDao = docteurDao;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public String getResultat() {
		return resultat;
	}



	public Docteur creerDocteur ( HttpServletRequest request ) {


		String choixNouveauDocteur = getValeurChamp( request, CHAMP_CHOIX_DOCTEUR );

		if ( ANCIEN_DOCTEUR.equals( choixNouveauDocteur ) ) {
			/* Récupération du nom du client choisi */
			String nomAncienDocteur = getValeurChamp( request, CHAMP_LISTE_DOCTEURS );
			/* Récupération de l'objet client correspondant dans la session */
			HttpSession session = request.getSession();
			Docteur docteur = ( (Map<String, Docteur>) session.getAttribute( SESSION_DOCTEURS ) ).get( nomAncienDocteur ); 

			return docteur;
		}

		else {

			String nom          = getValeurChamp( request, CHAMP_NOM );
			String prenom       = getValeurChamp( request, CHAMP_PRENOM );
			String adresse      = getValeurChamp( request, CHAMP_ADRESSE );
			String telephone    = getValeurChamp( request, CHAMP_TELEPHONE );
			String email        = getValeurChamp( request, CHAMP_EMAIL );
			String identifiant  = getValeurChamp( request, CHAMP_IDENTIFIANT );
			String motDePasse   = getValeurChamp( request, GROUPE_MOT_DE_PASSE );


			Docteur docteur = new Docteur();

			traiterNom( nom, docteur );
			traiterPrenom( prenom, docteur );
			traiterAdresse( adresse, docteur );
			traiterTelephone( telephone, docteur );
			traiterEmail( email, docteur );
			traiterIdentifiant( identifiant, docteur );
			traiterMotDePasse( motDePasse, docteur );

			try {
				if ( erreurs.isEmpty() ) {
					docteurDao.creer(docteur);
					resultat = "Succès de la création de patient.";
				} else {
					resultat = "Échec de la création du patient.";
				}
			} catch ( DAOException e ) {
				setErreur( "imprévu", "Erreur imprévue lors de la création." );
				resultat = "Échec de la création du client : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
				e.printStackTrace();
			}
			return docteur;
		}
	}
	private void traiterNom( String nom, Docteur docteur ) {
		try {
			validationNom( nom );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_NOM, e.getMessage() );
		}
		docteur.setNom( nom );
	}

	private void traiterPrenom( String prenom, Docteur docteur ) {
		try {
			validationPrenom( prenom );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_PRENOM, e.getMessage() );
		}
		docteur.setPrenom( prenom );
	}

	private void traiterAdresse( String adresse, Docteur docteur ) {
		try {
			validationAdresse( adresse );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_ADRESSE, e.getMessage() );
		}
		docteur.setAdresse( adresse );
	}


	private void traiterTelephone( String telephone, Docteur docteur )
	{
		try {
			validationTelephone( telephone );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_TELEPHONE, e.getMessage() );
		}
		docteur.setTelephone( telephone );
	}
	
	private void traiterEmail( String email, Docteur docteur ) {
		try {
			validationEmail( email );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_EMAIL, e.getMessage() );
		}
		docteur.setEmail( email );
	}
	
	private void traiterIdentifiant( String identifiant,Docteur docteur ) {
		try {
			validationIdentifiant(identifiant );
		} catch ( FormValidationException e ) {
			setErreur( CHAMP_IDENTIFIANT, e.getMessage() );
		}
		docteur.setIdentifiant(identifiant);
	}

	private void traiterMotDePasse( String motDePasse, Docteur docteur ) {
		try {
			validationMotDePasse(motDePasse );
		} catch ( FormValidationException e ) {
			setErreur( GROUPE_MOT_DE_PASSE, e.getMessage() );
		}
		docteur.setMotDePasse(motDePasse);
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

	private void validationAdresse( String adresse ) throws FormValidationException {
		if ( adresse != null ) {
			if ( adresse.length() < 10 ) {
				throw new FormValidationException( "L'adresse de livraison doit contenir au moins 10 caractères." );
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
		if(email != null){	
			if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
			throw new FormValidationException( "Merci de saisir une adresse mail valide." );
		}
			if(docteurDao.trouverEmail(email) != null){
			throw new FormValidationException( "Cette mail exise déja, merci d'entrer une autre." );
			}
		}
		 if (email == null){
			throw new FormValidationException( "Merci de saisir une mail." );
		}
	}


	private void validationIdentifiant( String identifiant ) throws FormValidationException {
		if ( identifiant != null ) {
			if ( !identifiant.matches( "^\\d+$" ) ) {
				throw new FormValidationException( "Le numéro RPPS doit uniquement contenir des chiffres." );
			} else if ( identifiant.length() != 11 ) {
				throw new FormValidationException( "Le numéro RPPS doit contenir 11 chiffres." );
			} else if ( docteurDao.trouverID(identifiant) != null ) {
				throw new FormValidationException( "Ce numéro existe déja merci d'entrer un autre." );
			}
		} else {
			throw new FormValidationException( "Merci d'entrer un identifiant." );
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



