package com.app.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.beans.Patient;
import com.app.forms.CreationPatientForm;
import com.app.dao.DAOFactory;
import com.app.dao.PatientDao;


public class CreationPatient extends HttpServlet {

    public static final String ATT_PATIENT      = "patient";
    public static final String ATT_FORM        = "form";
    public static final String SESSION_PATIENTS = "patients";
    public static final String SESSION_PATIENT = "patient";
    public static final String VUE_SUCCES      = "/WEB-INF/afficherPatient.jsp";
    public static final String VUE_FORM        = "/WEB-INF/creerPatient.jsp";
    public static final String CONF_DAO_FACTORY= "daofactory";
	private PatientDao patientDao;
    
    public void init() throws ServletException {
    	/* Récupération d'une instance de notre DAO Utilisateur */
    	this.patientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getPatientDao();
    	}
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* À la réception d'une requête GET, simple affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }
    
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        CreationPatientForm form = new CreationPatientForm(patientDao);

        /* Traitement de la requête et récupération du bean en résultant */
        Patient patient = form.creerPatient( request );
        
        
        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_PATIENT, patient );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /* Alors récupération de la map des clients dans la session */
            HttpSession session = request.getSession();
            session.setAttribute( SESSION_PATIENT, patient );
            Map<String, Patient> patients = (HashMap<String, Patient>) session.getAttribute( SESSION_PATIENTS );
            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */
            if ( patients == null ) {
                patients = new HashMap<String, Patient>();
            }
            /* Puis ajout du client courant dans la map */
            patients.put( patient.getNom(), patient );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_PATIENTS, patients );

            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
    }
    

