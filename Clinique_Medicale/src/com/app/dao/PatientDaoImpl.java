package com.app.dao;

import static com.app.dao.DAOUtilitaire.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.app.dao.DAOException;
import com.app.beans.Docteur;
import com.app.beans.Patient;

public class PatientDaoImpl implements PatientDao {
	/*
	 * Implémentation de la méthode trouver() définie dans l'interface
	 * UtilisateurDao
	 */
	private static final String SQL_INSERT = "INSERT INTO p_cliniuqe.patient(nom,prenom,adresse,telephone,email,groupesanguin, sex,num_assurance, motDePasse) VALUES (?, ?, ?, ?,?, ?,?,?,?)";
	private static final String SQL_SELECT_PAR_ID = "select nom,prenom,adresse,telephone,email,groupesanguin, sex,num_assurance from p_cliniuqe.patient where id = ?";
	private static final String SQL_SELECT_PAR_NAME = "select nom,prenom,adresse,telephone,email,groupesanguin, sex,num_assurance from p_cliniuqe.patient where nom = ?";
	private static final String SQL_SELECT_PAR_EMAIL = "select nom,prenom,adresse,telephone,email,groupesanguin, sex,num_assurance from p_cliniuqe.patient where email = ?";
	private static final String SQL_SELECT_PAR_NUM_SECURITE = "select nom,prenom,adresse,telephone,email,groupesanguin, sex,num_assurance from p_cliniuqe.patient where num_assurance = ?";
	private static final String SQL_SELECT_PASSE_EMAIL = "select nom,prenom,adresse,telephone,email,groupesanguin, sex,num_assurance from p_cliniuqe.patient where email = ? and motDePasse = ?";
	private static final String SQL_DELETE_PATIENT = "delete from p_cliniuqe.patient where id = ?";
	private static final String SQL_LISTER_PATIENT = "SELECT *from p_cliniuqe.patient ";
	private DAOFactory daoFactory;

	PatientDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public Patient trouverID(Long ID) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Patient patient = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, ID);
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			if (resultSet.next()) {
				patient = map_patient(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return patient;
	}

	public Patient trouverNom(String nom) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Patient patient = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_NAME, false, nom);
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			if (resultSet.next()) {
				patient = map_patient(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return patient;
	}

	public Patient trouverEmail(String email) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Patient patient = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_EMAIL, false, email);
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			if (resultSet.next()) {
				patient = map_patient(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return patient;
	}

	public Patient trouverSecurite(String numero_securite) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Patient patient = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PAR_NUM_SECURITE, false,
					numero_securite);
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			if (resultSet.next()) {
				patient = map_patient(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return patient;
	}

	/*
	 * Implémentation de la méthode définie dans l'interface UtilisateurDao
	 */
	@Override
	public void creer(Patient patient) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true, patient.getNom(),
					patient.getPrenom(), patient.getAdresse(), patient.getTelephone(), patient.getEmail(),
					patient.getGroupeSanguin(), patient.getSexe(), patient.getNum_assurance(), patient.getMotDePasse());
			int statut = preparedStatement.executeUpdate();
			/* Analyse du statut retourné par la requête d'insertion */
			if (statut == 0) {
				throw new DAOException("Échec de la création de patient, aucune ligne ajoutée dans la table.");
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
				patient.setID(valeursAutoGenerees.getLong(1));
			} else {
				throw new DAOException("Échec de la création de client en base, aucun ID auto-généré retourné.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
		}
	}

	@Override
	public void supprimer(Patient patient) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_DELETE_PATIENT, false, patient.getID());
			int statut = preparedStatement.executeUpdate();
			if (statut != 1) {
				throw new DAOException("Échec de la suppression de patient, aucune ligne supprimer dans la table.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(preparedStatement, connexion);
		}

	}

	public List<Patient> lister_patients() throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Patient> patients = new ArrayList<Patient>();
		try {
			connection = daoFactory.getConnection();
			preparedStatement = connection.prepareStatement(SQL_LISTER_PATIENT);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				patients.add(map_patient(resultSet));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connection);
		}
		return patients;

	}

	@Override
	public Patient trouverEmailPasse(String email, String motDePasse) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Patient patient = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_PASSE_EMAIL, false, email,
					motDePasse);
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			if (resultSet.next()) {
				patient = map_patient(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return patient;
	}

}