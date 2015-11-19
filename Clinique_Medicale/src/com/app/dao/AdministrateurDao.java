package com.app.dao;

import com.app.beans.Administrateur;
public interface AdministrateurDao {
Administrateur trouver( Long ID ) throws DAOException;
}