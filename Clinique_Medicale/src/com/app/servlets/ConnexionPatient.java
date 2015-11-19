package com.app.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.app.beans.Consultation;
import com.app.beans.Patient;
import com.app.dao.ConsultationDao;
import com.app.dao.DAOFactory;
import com.app.dao.PatientDao;
import com.app.forms.ConnexionPatientForm;

public class ConnexionPatient extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String ATT_USER = "patient";
	public static final String ATT_FORM = "form";
	public static final String ATT_INTERVALLE_CONNEXIONS = "intervalleConnexions";
	public static final String ATT_SESSION_USER = "sessionPatient";
	public static final String ATT_CONSULTATAION_USER = "consultationPatient";
	public static final String COOKIE_DERNIERE_CONNEXION = "derniereConnexion";
	public static final String COOKIE_ID_USER = "idpatient";
	public static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
	public static final String VUE = "/WEB-INF/connexionPatient.jsp";
	public static final String CHAMP_MEMOIRE = "memoire";
	 public static final String CONF_DAO_FACTORY= "daofactory";
	public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 365; // 1 an
	private static final String ATT_CONSULTATION_USER = null;
	private PatientDao patientDao;
	private ConsultationDao consultationDao;
	
	 public void init() throws ServletException {
	    	/* Récupération d'une instance de notre DAO Utilisateur */
	    	this.patientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getPatientDao();
	    	this.consultationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY )).getConsultationDao();
	    	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Tentative de r�cup�ration du cookie depuis la requ�te */
		String derniereConnexion = getCookieValue(request, COOKIE_DERNIERE_CONNEXION);
		/* Si le cookie existe, alors calcul de la dur�e */
		if (derniereConnexion != null) {
			/* R�cup�ration de la date courante */
			DateTime dtCourante = new DateTime();
			/* R�cup�ration de la date pr�sente dans le cookie */
			org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
			DateTime dtDerniereConnexion = formatter.parseDateTime(derniereConnexion);
			/* Calcul de la dur�e de l'intervalle */
			Period periode = new Period(dtDerniereConnexion, dtCourante);
			/* Formatage de la dur�e de l'intervalle */
			PeriodFormatter periodFormatter = new PeriodFormatterBuilder().appendYears().appendSuffix(" an ", " ans ")
					.appendMonths().appendSuffix(" mois ").appendDays().appendSuffix(" jour ", " jours ").appendHours()
					.appendSuffix(" heure ", " heures").appendMinutes().appendSuffix(" minute ", "minutes ")
					.appendSeparator("et ").appendSeconds().appendSuffix(" seconde", "secondes").toFormatter();
			String intervalleConnexions = periodFormatter.print(periode);
			/*
			 * Ajout de l'intervalle en tant qu'attribut de la requ�te
			 */
			request.setAttribute(ATT_INTERVALLE_CONNEXIONS, intervalleConnexions);
		}
		/* Affichage de la page de connexion */
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Pr�paration de l'objet formulaire */
		ConnexionPatientForm form = new ConnexionPatientForm(patientDao,consultationDao);
		/*
		 * Traitement de la requ�te et r�cup�ration du bean en r�sultant
		 */
		Patient patient = form.connecterPatient(request);
		ArrayList<Consultation> consultation = form.consultationPatient(patient);
		/* R�cup�ration de la session depuis la requ�te */
		HttpSession session = request.getSession();
		/*
		 * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
		 * Docteur � la session, sinon suppression du bean de la session.
		 */
		if (form.getErreurs().isEmpty()) {
			session.setAttribute(ATT_SESSION_USER, patient);
			if(consultation != null){
				session.setAttribute(ATT_CONSULTATAION_USER, consultation);
			}else{
				session.setAttribute(ATT_CONSULTATAION_USER, null);
			}
		} else {
			session.setAttribute(ATT_SESSION_USER, null);
		}
		/* Si et seulement si la case du formulaire est coch�e */
		if (request.getParameter(CHAMP_MEMOIRE) != null) {
			/* R�cup�ration de la date courante */
			DateTime dt = new DateTime();
			/* Formatage de la date et conversion en texte */
			org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
			String dateDerniereConnexion = dt.toString(formatter);
			/* Cr�ation du cookie, et ajout � la r�ponse HTTP */
			setCookie(response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion, COOKIE_MAX_AGE);
			setCookie(response, COOKIE_ID_USER, patient.getNum_assurance(), COOKIE_MAX_AGE);
		} else {
			/* Demande de suppression du cookie du navigateur */
			setCookie(response, COOKIE_DERNIERE_CONNEXION, "", 0);
			setCookie(response, COOKIE_ID_USER, "", 0);
		}
		/* Stockage du formulaire et du bean dans l'objet request */
		request.setAttribute(ATT_FORM, form);
		request.setAttribute(ATT_USER, patient);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	/*
	 * M�thode utilitaire g�rant la cr�ation d'un cookie et son ajout � la
	 * r�ponse HTTP.
	 */
	private static void setCookie(HttpServletResponse response, String nom, String valeur, int maxAge) {
		Cookie cookie = new Cookie(nom, valeur);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * M�thode utilitaire g�rant la r�cup�ration de la valeur d'un cookie donn�
	 * depuis la requ�te HTTP.
	 */
	private static String getCookieValue(HttpServletRequest request, String nom) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null && nom.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
