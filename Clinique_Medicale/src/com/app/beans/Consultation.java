package com.app.beans;

public class Consultation {

	private Long ID;
	private String date;
	private Long ID_Docteur;
	private Long ID_Patient;
	private String diagnostique;
	private String test;

	public Consultation() {
		
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getID_Docteur() {
		return ID_Docteur;
	}

	public void setID_Docteur(long l) {
		ID_Docteur = l;
	}

	public Long getID_Patient() {
		return ID_Patient;
	}

	public void setID_Patient(Long iD_Patient) {
		ID_Patient = iD_Patient;
	}

	public String getDiagnostique() {
		return diagnostique;
	}

	public void setDiagnostique(String diagnostique) {
		this.diagnostique = diagnostique;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	


	

}
