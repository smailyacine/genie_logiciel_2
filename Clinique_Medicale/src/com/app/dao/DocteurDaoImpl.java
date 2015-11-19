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

public class DocteurDaoImpl implements DocteurDao {
	/*
	 * Implémentation de la méthode trouver() définie dans l'interface
	 * UtilisateurDao
	 */
	private static final String SQL_INSERT = "INSERT INTO p_cliniuqe.docteur(identifiant, nom,prenom, adresse,telephone,email,motDePasse) VALUES (?, ?, ?, ?, ?,?,?)";
	private static final String SQL_DELETE = "delete from docteur where id = ?";
	private static final String SQL_SELECT_ID = "select identifiant, nom,prenom, adresse,telephone,email from docteur where identifiant = ?";
	private static final String SQL_SELECT_EMAIL = "select identifiant, nom,prenom, adresse,telephone,email from docteur where email = ?";
	private static final String SQL_SELECT_PASSE_EMAIL = "select identifiant, nom,prenom, adresse,telephone,email from docteur where email = ? and motDePasse = ?";
	private static final String SQL_SELECT = "select * from docteur";
	private DAOFactory daoFactory;

	DocteurDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public Docteur trouverID(String identifiant) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Docteur docteur = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_ID, false, identifiant);
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			if (resultSet.next()) {
				docteur = map_docteur(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return docteur;
	}

	public Docteur trouverEmail(String email) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Docteur docteur = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_SELECT_EMAIL, false, email);
			resultSet = preparedStatement.executeQuery();
			/*
			 * Parcours de la ligne de données de l'éventuel ResulSet
			 * retourné
			 */
			if (resultSet.next()) {
				docteur = map_docteur(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return docteur;
	}

	/*
	 * Implémentation de la méthode définie dans l'interface UtilisateurDao
	 */
	@Override
	public void creer(Docteur docteur) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true, docteur.getIdentifiant(),
					docteur.getNom(), docteur.getPrenom(), docteur.getAdresse(), docteur.getTelephone(),
					docteur.getEmail(), docteur.getMotDePasse());
			int statut = preparedStatement.executeUpdate();
			/* Analyse du statut retourné par la requête d'insertion */
			if (statut == 0) {
				throw new DAOException("Échec de la création de Docteur, aucune ligne ajoutée dans la table.");
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
				docteur.setId(valeursAutoGenerees.getLong(1));
			} else {
				throw new DAOException("Échec de la création de docteur en base, aucun ID auto-généré retourné.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
		}
	}

	@Override
	public void supprimer(Docteur docteur) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion, SQL_DELETE, false, docteur.getId());
			int statut = preparedStatement.executeUpdate();
			if (statut != 1) {
				throw new DAOException("Échec de la suppression de de docteur, aucune ligne supprimer dans la table.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(preparedStatement, connexion);
		}

	}

	@Override
	public List<Docteur> lister_docteurs() throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Docteur> docteurs = new ArrayList<Docteur>();
		try {
			connection = daoFactory.getConnection();
			preparedStatement = connection.prepareStatement(SQL_SELECT);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				docteurs.add(map_docteur(resultSet));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connection);
		}
		return docteurs;
	}

	@Override
	public Docteur trouverEmailPasse(String email, String motDePasse) throws DAOException {

		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Docteur docteur = null;
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
				docteur = map_docteur(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}
		return docteur;
	}

}