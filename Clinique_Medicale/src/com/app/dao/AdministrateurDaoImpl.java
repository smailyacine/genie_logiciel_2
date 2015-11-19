package com.app.dao;
import static com.app.dao.DAOUtilitaire.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.app.dao.DAOException;
import com.app.beans.Administrateur;

public class AdministrateurDaoImpl implements AdministrateurDao {
/* Implémentation de la méthode trouver() définie dans
l'interface UtilisateurDao */
	private static final String SQL_INSERT = "INSERT INTO Utilisateur (email, mot_de_passe, nom, date_inscription) VALUES (?, ?, ?, NOW())";
	private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, 	email, nom, mot_de_passe, date_inscription FROM Utilisateur WHERE email = ?";
private DAOFactory daoFactory;
AdministrateurDaoImpl( DAOFactory daoFactory ) {
this.daoFactory = daoFactory;
}

public Administrateur trouver( Long ID ) throws DAOException {
Connection connexion = null;
PreparedStatement preparedStatement = null;
ResultSet resultSet = null;
Administrateur administrateur = null;
try {
/* Récupération d'une connexion depuis la Factory */
connexion = daoFactory.getConnection();
preparedStatement = initialisationRequetePreparee( connexion,SQL_SELECT_PAR_EMAIL, false, ID );
resultSet = preparedStatement.executeQuery();
/* Parcours de la ligne de données de l'éventuel ResulSet
retourné */
if ( resultSet.next() ) {
administrateur = map_administrateur( resultSet );
}
} catch ( SQLException e ) {
throw new DAOException( e );
} finally {
fermeturesSilencieuses( resultSet, preparedStatement, connexion );
}
return administrateur;
}

}