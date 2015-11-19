package com.app.dao;

import java.util.List;

import com.app.beans.Docteur;
import com.app.beans.Patient;
public interface PatientDao {
void creer( Patient patient ) throws DAOException;
Patient trouverSecurite( String numero_securite ) throws DAOException;
Patient trouverNom( String nom ) throws DAOException;
Patient trouverEmail( String email ) throws DAOException;
Patient trouverID( Long ID ) throws DAOException;
List<Patient> lister_patients() throws DAOException;
void supprimer(Patient patient)throws DAOException;
Patient trouverEmailPasse(String email, String motDePasse)throws DAOException;
}