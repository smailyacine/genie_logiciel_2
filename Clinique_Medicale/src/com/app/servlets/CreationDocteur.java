package com.app.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.beans.Docteur;
import com.app.dao.DAOFactory;
import com.app.dao.DocteurDao;
import com.app.forms.CreationDocteurForm;

public class CreationDocteur extends HttpServlet {

    public static final String ATT_DOCTEUR      = "docteur";
    public static final String ATT_FORM        = "form";
    public static final String SESSION_DOCTEURS = "docteurs";
    public static final String VUE_SUCCES      = "/WEB-INF/afficherDocteur.jsp";
    public static final String VUE_FORM        = "/WEB-INF/creerDocteur.jsp";
    public static final String CONF_DAO_FACTORY= "daofactory";
    public static final String COOKIE_DERNIERE_CONNEXION = "derniereConnexion";
    
	private DocteurDao docteurDao;
	public void init() throws ServletException {
    	/* Récupération d'une instance de notre DAO Utilisateur */
    	this.docteurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getDocteurDao();
    	}

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* À la réception d'une requête GET, simple affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }
    
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        CreationDocteurForm form = new CreationDocteurForm(docteurDao);
        /* Traitement de la requête et récupération du bean en résultant */
        Docteur docteur = form.creerDocteur( request );
        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_DOCTEUR, docteur );
        request.setAttribute( ATT_FORM, form );
        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /* Alors récupération de la map des clients dans la session */
            HttpSession session = request.getSession();
            Map<String, Docteur> docteurs = (HashMap<String, Docteur>) session.getAttribute( SESSION_DOCTEURS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( docteurs == null ) {
                docteurs = new HashMap<String, Docteur>();
            }
            /* Puis ajout du client courant dans la map */
            docteurs.put( docteur.getNom(), docteur );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_DOCTEURS, docteurs );
            // garder les cookies 
            
            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
    private static void setCookie( HttpServletResponse response, String	nom, String valeur, int maxAge ) {
    		Cookie cookie = new Cookie( nom, valeur );
    		cookie.setMaxAge( maxAge );
    		response.addCookie( cookie );
    		}
    }
    

