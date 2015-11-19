package com.app.dao;

import java.util.ArrayList;

import com.app.beans.Consultation;
import com.app.beans.Docteur;
import com.app.beans.Patient;
public interface ConsultationDao {
void creer( Consultation consutation ) throws DAOException;
ArrayList<Consultation> trouverPatient(Patient patient ) throws DAOException;
ArrayList<Consultation> trouverDocteur(Docteur docteur) throws DAOException;
void supprimer(Consultation consultation)throws DAOException;
}