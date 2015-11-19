package com.app.dao;

import static com.app.dao.DAOUtilitaire.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.app.dao.DAOException;
import com.app.beans.Consultation;
import com.app.beans.Docteur;
import com.app.beans.Patient;

public class ConsultationDaoImpl implements ConsultationDao {
	/*
	 * Implémentation de la méthode trouver() définie dans l'interface
	 * UtilisateurDao
	 */
	private static final String SQL_INSERT = "INSERT INTO p_cliniuqe.consultation (id_patient, id_docteur, date, diagnostique,teste) VALUES (?, ?, ?,?,?)";
	private static final String SQL_SELECT_PAR_PATIENT = "SELECT id,id_patient, id_docteur, date, diagnostique,teste FROM p_cliniuqe.consultation WHERE id_patient = ?";
	private static final String SQL_SELECT_PAR_DOCTEUR = "SELECT id_patient, id_docteur, date, diagnostique,teste FROM p_cliniuqe.consultation WHERE id_docteur = ?";

	private DAOFactory daoFactory;

	ConsultationDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public ArrayList<Consultation> trouverPatient(Patient patient) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Consultation> consultation = new ArrayList<Consultation>();
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_PATIENT, false,
					patient.getNum_assurance());
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			while (resultSet.next()) {
				consultation.add(map_consultation(resultSet));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return consultation;
	}

	public ArrayList<Consultation> trouverDocteur(Docteur docteur) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Consultation> consultation = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_DOCTEUR, false,
					docteur.getIdentifiant());
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			while (resultSet.next()) {
				consultation.add(map_consultation(resultSet));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return consultation;
	}

	/*
	 * Implémentation de la méthode définie dans l'interface UtilisateurDao
	 */
	@Override
	public void creer(Consultation consultation) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true, consultation.getID_Docteur(),
					consultation.getID_Patient(), consultation.getDate(), consultation.getDiagnostique(),
					consultation.getTest());
			int statut = preparedStatement.executeUpdate();
			/* Analyse du statut retourné par la requête d'insertion */
			if (statut == 0) {
				throw new DAOException("Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table.");
			}
			/*
			 * Récupération de l'id auto-généré par la requête d'insertion
			 */
			valeursAutoGenerees = preparedStatement.getGeneratedKeys();
			if (valeursAutoGenerees.next()) {
				/*
				 * Puis initialisation de la propriété id du bean Utilisateur
				 * avec sa valeur
				 */
				consultation.setID(valeursAutoGenerees.getLong(1));
			} else {
				throw new DAOException(
						"Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
		}
	}

	@Override
	public void supprimer(Consultation consultation) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, false, consultation.getID());
			int statut = preparedStatement.executeUpdate();
			if (statut != 1) {
				throw new DAOException("Échec de la suppression de client, aucune ligne supprimer dans la table.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(preparedStatement, connexion);
		}

	}

}