package uoc.ded.practica.model;

import java.time.LocalDate;

public class Worker extends User {
	// Role ID
	private String idRole;
	// Role organization ID
	private String idOrganization;
	
	public Worker(String idUser, String name, String surname, LocalDate birthday, boolean covidCertificate, String idRole, String idOrganization) {
		super(idUser, name, surname, birthday, covidCertificate);
		
		this.setRoleId(idRole);
		this.setOrganizationId(idOrganization);
	}
	
	// Role ID setter
	public void setRoleId(String roleId) {
		this.idRole = roleId;
	}
	
	// Role ID getter
	public String getRoleId() {
		return this.idRole;
	}
	
	// Role organization ID setter
	public void setOrganizationId(String organizationId) {
		this.idOrganization = organizationId;
	}
	
	// Role organization ID getter
	public String getOrganizationId() {
		return this.idOrganization;
	}
	
	// Application of a patter to know if the instance is a User or a Worker rather than use the keyword instanceof
	@Override
	public boolean isWorker() {
		return true;
	}
}
