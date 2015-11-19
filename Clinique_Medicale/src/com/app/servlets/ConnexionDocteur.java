package com.app.servlets;

import java.io.IOException;
import java.io.PrintWriter;

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

import com.app.beans.Docteur;
import com.app.dao.DAOFactory;
import com.app.dao.DocteurDao;
import com.app.forms.ConnexionDocteurForm;

public class ConnexionDocteur extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String ATT_USER = "docteur";
	public static final String ATT_FORM = "form";
	public static final String ATT_INTERVALLE_CONNEXIONS = "intervalleConnexions";
	public static final String ATT_SESSION_USER = "sessionDocteur";
	public static final String COOKIE_DERNIERE_CONNEXION = "derniereConnexion";
	public static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
	public static final String VUE = "/WEB-INF/connexionDocteur.jsp";
	private static final String GROUPE_MOT_DE_PASSE = "motDePasseDocteur";
	private static final String CHAMP_EMAIL = "emailDocteur";
	public static final String VUE_SUCCES = "/WEB-INF/afficherDocteur.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String CHAMP_MEMOIRE = "memoire";
	public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 365; // 1 an
	private DocteurDao docteurDao;

	public void init() throws ServletException {
		/* RÃ©cupÃ©ration d'une instance de notre DAO Utilisateur */
		this.docteurDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getDocteurDao();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Tentative de rï¿½cupï¿½ration du cookie depuis la requï¿½te */
		String derniereConnexion = getCookieValue(request, COOKIE_DERNIERE_CONNEXION);

		/* Si le cookie existe, alors calcul de la durï¿½e */
		if (derniereConnexion != null) {
			/* Rï¿½cupï¿½ration de la date courante */
			DateTime dtCourante = new DateTime();
			/* Rï¿½cupï¿½ration de la date prï¿½sente dans le cookie */
			org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
			DateTime dtDerniereConnexion = formatter.parseDateTime(derniereConnexion);
			/* Calcul de la durï¿½e de l'intervalle */
			Period periode = new Period(dtDerniereConnexion, dtCourante);
			/* Formatage de la durï¿½e de l'intervalle */
			PeriodFormatter periodFormatter = new PeriodFormatterBuilder().appendYears().appendSuffix(" an ", " ans ")
					.appendMonths().appendSuffix(" mois ").appendDays().appendSuffix(" jour ", " jours ").appendHours()
					.appendSuffix(" heure ", " heures").appendMinutes().appendSuffix(" minute ", "minutes ")
					.appendSeparator("et ").appendSeconds().appendSuffix(" seconde", "secondes").toFormatter();
			String intervalleConnexions = periodFormatter.print(periode);
			/*
			 * Ajout de l'intervalle en tant qu'attribut de la requï¿½te
			 */
			request.setAttribute(ATT_INTERVALLE_CONNEXIONS, intervalleConnexions);
		}
		ConnexionDocteurForm form = new ConnexionDocteurForm(docteurDao);
		/*
		 * Traitement de la requï¿½te et rï¿½cupï¿½ration du bean en rï¿½sultant
		 */
		Docteur docteur = null;
		/* Rï¿½cupï¿½ration de la session depuis la requï¿½te */
		HttpSession session = request.getSession();
		/*
		 * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
		 * Docteur à la session, sinon suppression du bean de la session.
		 */
		/* Affichage de la page de connexion */
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			String nomCookie = cookie.getName();
			if (nomCookie.equals(CHAMP_EMAIL)) {
				for (int j = 0; j < cookies.length; j++) {
					Cookie cookie1 = cookies[j];
					String nomCookie1 = cookie1.getName();					
					if (nomCookie1.equals(GROUPE_MOT_DE_PASSE)) {
						request.setAttribute(GROUPE_MOT_DE_PASSE,cookie1.getValue() );
						request.setAttribute(CHAMP_EMAIL, cookie.getValue());
						docteur = docteurDao.trouverEmail(cookie.getValue());
						if (form.getErreurs().isEmpty() && docteur != null) {
							session.setAttribute(ATT_SESSION_USER, docteur);
							request.setAttribute(ATT_FORM, form);
							request.setAttribute(ATT_USER, docteur);
							this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(request, response);
						} else {
							session.setAttribute(ATT_SESSION_USER, null);
						}
					}
				}
			}
		}

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Prï¿½paration de l'objet formulaire */
		ConnexionDocteurForm form = new ConnexionDocteurForm(docteurDao);
		/*
		 * Traitement de la requï¿½te et rï¿½cupï¿½ration du bean en rï¿½sultant
		 */
		Docteur docteur = form.connecterDocteur(request);
		/* Rï¿½cupï¿½ration de la session depuis la requï¿½te */
		HttpSession session = request.getSession();
		/*
		 * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
		 * Docteur à la session, sinon suppression du bean de la session.
		 */
		if (form.getErreurs().isEmpty()) {
			session.setAttribute(ATT_SESSION_USER, docteur);
		} else {
			session.setAttribute(ATT_SESSION_USER, null);
		}
		/* Si et seulement si la case du formulaire est cochï¿½e */
		if (request.getParameter(CHAMP_MEMOIRE) != null) {
			/* Rï¿½cupï¿½ration de la date courante */
			DateTime dt = new DateTime();
			/* Formatage de la date et conversion en texte */
			org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
			String dateDerniereConnexion = dt.toString(formatter);
			/* Crï¿½ation du cookie, et ajout ï¿½ la rï¿½ponse HTTP */
			setCookie(response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion, COOKIE_MAX_AGE);
		} else {
			/* Demande de suppression du cookie du navigateur */
			setCookie(response, COOKIE_DERNIERE_CONNEXION, "", 0);
		}

		/* Stockage du formulaire et du bean dans l'objet request */
		request.setAttribute(ATT_FORM, form);
		request.setAttribute(ATT_USER, docteur);

		if (form.getErreurs().isEmpty()) {
			Cookie cookie = new Cookie(CHAMP_EMAIL, docteur.getEmail());
			Cookie cookie1 = new Cookie(GROUPE_MOT_DE_PASSE, docteur.getMotDePasse());
			response.addCookie(cookie);
			response.addCookie(cookie1);
			this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(request, response);
		} else {
			this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
		}
	}

	/*
	 * Mï¿½thode utilitaire gï¿½rant la crï¿½ation d'un cookie et son ajout ï¿½
	 * la rï¿½ponse HTTP.
	 */
	private static void setCookie(HttpServletResponse response, String nom, String valeur, int maxAge) {
		Cookie cookie = new Cookie(nom, valeur);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * Mï¿½thode utilitaire gï¿½rant la rï¿½cupï¿½ration de la valeur d'un
	 * cookie donnï¿½ depuis la requï¿½te HTTP.
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

//
// public void doGet(HttpServletRequest request, HttpServletResponse response)
// throws ServletException, IOException {
// ConnexionDocteurForm form = new ConnexionDocteurForm(docteurDao);
// /*
// * Traitement de la requï¿½te et rï¿½cupï¿½ration du bean en rï¿½sultant
// */
// Docteur docteur = form.connecterDocteur(request);
// /* Tentative de rï¿½cupï¿½ration du cookie depuis la requï¿½te */
// String derniereConnexion = getCookieValue(request,
// COOKIE_DERNIERE_CONNEXION);
//
// /* Prï¿½paration de l'objet formulaire */
// docteur = docteurDao.trouverEmailPasse(getCookieValue(request, CHAMP_EMAIL),
// getCookieValue(request, GROUPE_MOT_DE_PASSE));
// /* Rï¿½cupï¿½ration de la session depuis la requï¿½te */
// HttpSession session = request.getSession();
// /*
// * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
// * Docteur à la session, sinon suppression du bean de la session.
// */
// if (form.getErreurs().isEmpty()) {
// session.setAttribute(ATT_SESSION_USER, docteur);
// } else {
// session.setAttribute(ATT_SESSION_USER, null);
// }
// /* Si et seulement si la case du formulaire est cochï¿½e */
// if (request.getParameter(CHAMP_MEMOIRE) != null) {
// /* Rï¿½cupï¿½ration de la date courante */
// DateTime dt = new DateTime();
// /* Formatage de la date et conversion en texte */
// org.joda.time.format.DateTimeFormatter formatter =
// DateTimeFormat.forPattern(FORMAT_DATE);
// String dateDerniereConnexion = dt.toString(formatter);
// /* Crï¿½ation du cookie, et ajout ï¿½ la rï¿½ponse HTTP */
// setCookie(response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion,
// COOKIE_MAX_AGE);
// } else {
// /* Demande de suppression du cookie du navigateur */
// setCookie(response, COOKIE_DERNIERE_CONNEXION, "", 0);
// }
// if(docteur!=null){
// Cookie cookie=new Cookie(CHAMP_EMAIL,docteur.getEmail());
// Cookie cookie1=new Cookie(GROUPE_MOT_DE_PASSE,docteur.getMotDePasse());
// response.addCookie(cookie);
// response.addCookie(cookie1);
// }
// /* Stockage du formulaire et du bean dans l'objet request */
// request.setAttribute(ATT_FORM, form);
// request.setAttribute(ATT_USER, docteur);
// if (form.getErreurs().isEmpty()) {
// this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(request,
// response);
// } else {
// this.getServletContext().getRequestDispatcher(VUE).forward(request,
// response);
// }
// /* Si le cookie existe, alors calcul de la durï¿½e */
// if (derniereConnexion != null) {
// /* Rï¿½cupï¿½ration de la date courante */
// DateTime dtCourante = new DateTime();
// /* Rï¿½cupï¿½ration de la date prï¿½sente dans le cookie */
// org.joda.time.format.DateTimeFormatter formatter =
// DateTimeFormat.forPattern(FORMAT_DATE);
// DateTime dtDerniereConnexion = formatter.parseDateTime(derniereConnexion);
// /* Calcul de la durï¿½e de l'intervalle */
// Period periode = new Period(dtDerniereConnexion, dtCourante);
// /* Formatage de la durï¿½e de l'intervalle */
// PeriodFormatter periodFormatter = new
// PeriodFormatterBuilder().appendYears().appendSuffix(" an ", " ans ")
// .appendMonths().appendSuffix(" mois ").appendDays().appendSuffix(" jour ", "
// jours ").appendHours()
// .appendSuffix(" heure ", " heures").appendMinutes().appendSuffix(" minute ",
// "minutes ")
// .appendSeparator("et ").appendSeconds().appendSuffix(" seconde",
// "secondes").toFormatter();
// String intervalleConnexions = periodFormatter.print(periode);
// /*
// * Ajout de l'intervalle en tant qu'attribut de la requï¿½te
// */
// request.setAttribute(ATT_INTERVALLE_CONNEXIONS, intervalleConnexions);
// }
// /* Affichage de la page de connexion */
// this.getServletContext().getRequestDispatcher(VUE).forward(request,
// response);
// }
