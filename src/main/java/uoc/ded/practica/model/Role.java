package uoc.ded.practica.model;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.Llista;
import uoc.ei.tads.LlistaEncadenada;
import uoc.ei.tads.Posicio;
import uoc.ei.tads.Recorregut;

public class Role {
	// Role ID
	private String roleId;
	// Role name
	private String name;
	// Role workers as a linked list
	private Llista<Worker> workers;
	
	public Role(String roleId, String name) {
		this.setRoleId(roleId);
		this.setName(name);
		this.workers = new LlistaEncadenada<Worker>();
	}
	
	// Role ID setter
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	// Role ID getter
	public String getRoleId() {
		return this.roleId;
	}
	
	// Role name setter
	public void setName(String name) {
		this.name = name;
	}

	// Role name getter
	public String getName() {
		return this.name;
	}
	
	// Add a worker to the linked list
	public void addWorker(Worker worker) {
		this.workers.afegirAlFinal(worker);
	}
	
	// Remove a worker from the linked list
	public void removeWorker(Worker worker) {
		Recorregut<Worker> recorregut = this.workers.posicions();
		Posicio<Worker> position;
		
		while (recorregut.hiHaSeguent())
		{
			position = recorregut.seguent();
			
			if (position.getElem().is(worker.getId()))
			{
				this.workers.esborrar(position);
				return;
			}
		}
	}
	
	// Check if the role has workers
	public boolean hasWorkers() {
		return this.workers.nombreElems() > 0;
	}

	// Return the amount of workers
	public int numWorkers() {
		return this.workers.nombreElems();
	}

	// Return an iterator with all the workers of this role
	public Iterador<Worker> workers() {
		return this.workers.elements();
	}
	
	// Check if the role it's the same by its id
	public boolean is(String roleId) {
		return this.getRoleId().equals(roleId);
	}
}
