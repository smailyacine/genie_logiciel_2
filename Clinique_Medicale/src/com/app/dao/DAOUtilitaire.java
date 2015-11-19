package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.app.beans.Administrateur;
import com.app.beans.Consultation;
import com.app.beans.Docteur;
import com.app.beans.Patient;

public class DAOUtilitaire {
	public static PreparedStatement initialisationRequetePreparee(
			Connection connexion, String sql, boolean returnGeneratedKeys,
			Object... objets ) throws SQLException {
		PreparedStatement preparedStatement =
				connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
		for ( int i = 0; i < objets.length; i++ ) {
			preparedStatement.setObject( i + 1, objets[i] );
		}
		return preparedStatement;
	}
	static Patient map_patient( ResultSet resultSet ) throws
	SQLException {
		Patient patient = new Patient();
		patient.setAdresse( resultSet.getString( "adresse" ) );
		patient.setNom(resultSet.getString( "nom" ));
		patient.setPrenom(resultSet.getString( "Prenom" ));
		patient.setTelephone(resultSet.getString( "telephone" ));
		patient.setEmail(resultSet.getString( "email" ));
		patient.setNum_assurance(resultSet.getString("num_assurance" ) );
		patient.setGroupeSanguin( resultSet.getString("groupesanguin" ) );
		patient.setSexe(resultSet.getString("sex"));
		patient.setMotDePasse("motDePasse");
		return patient;
	}


	static Administrateur map_administrateur( ResultSet resultSet ) throws
	SQLException {
		Administrateur administrateur = new Administrateur();
		administrateur.setID(resultSet.getLong("ID"));
		administrateur.setIdentifiant(resultSet.getInt("identifiant"));
		administrateur.setMotDePasse(resultSet.getString("motDePasse"));
		return administrateur;
	}

	static Docteur map_docteur( ResultSet resultSet ) throws
	SQLException {
		Docteur docteur = new Docteur();
		docteur.setNom(resultSet.getString("nom"));
		docteur.setPrenom(resultSet.getString("prenom"));
		docteur.setIdentifiant(resultSet.getString("identifiant"));
		docteur.setAdresse(resultSet.getString("adresse"));
		docteur.setEmail(resultSet.getString("email"));
		docteur.setTelephone(resultSet.getString("telephone"));

		return docteur;
	}
	
	static Consultation map_consultation( ResultSet resultSet ) throws
	SQLException {
		Consultation consultation = new Consultation();
		consultation.setID(resultSet.getLong("ID"));
		consultation.setID_Docteur(resultSet.getLong("ID_docteur"));
		consultation.setID_Patient(resultSet.getLong("ID_patient"));
		consultation.setDate(resultSet.getString("date"));
		consultation.setDiagnostique(resultSet.getString("diagnostique"));
		consultation.setTest(resultSet.getString("teste"));
		return consultation;
	}
	

	/* Fermeture silencieuse du resultset */
	public static void fermetureSilencieuse( ResultSet resultSet ) {
		if ( resultSet != null ) {
			try {
				resultSet.close();
			} catch ( SQLException e ) {
				System.out.println( "Échec de la fermeture du ResultSet : " + e.getMessage() );
			}
		}
	}
	/* Fermeture silencieuse du statement */
	public static void fermetureSilencieuse( Statement statement ) {
		if ( statement != null ) {
			try {
				statement.close();
			} catch ( SQLException e ) {
				System.out.println( "Échec de la fermeture du Statement : " + e.getMessage() );
			}
		}
	}
	/* Fermeture silencieuse de la connexion */
	public static void fermetureSilencieuse( Connection connexion ) {
		if ( connexion != null ) {
			try {
				connexion.close();
			} catch ( SQLException e ) {
				System.out.println( "Échec de la fermeture de la connexion : " + e.getMessage() );
			}
		}
	}
	/* Fermetures silencieuses du statement et de la connexion */
	public static void fermeturesSilencieuses( Statement statement,
			Connection connexion ) {
		fermetureSilencieuse( statement );
		fermetureSilencieuse( connexion );
	}
	/* Fermetures silencieuses du resultset, du statement et de la
	connexion */
	public static void fermeturesSilencieuses( ResultSet resultSet,
			Statement statement, Connection connexion ) {
		fermetureSilencieuse( resultSet );
		fermetureSilencieuse( statement );
		fermetureSilencieuse( connexion );
	}


}
