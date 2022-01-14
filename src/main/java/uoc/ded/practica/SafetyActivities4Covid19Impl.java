package uoc.ded.practica;

import java.time.LocalDate;
import java.util.Date;

import uoc.ded.practica.exceptions.ActivityNotFoundException;
import uoc.ded.practica.exceptions.GroupNotFoundException;
import uoc.ded.practica.exceptions.LimitExceededException;
import uoc.ded.practica.exceptions.NoActivitiesException;
import uoc.ded.practica.exceptions.NoOrganizationException;
import uoc.ded.practica.exceptions.NoRatingsException;
import uoc.ded.practica.exceptions.NoRecordsException;
import uoc.ded.practica.exceptions.NoUserException;
import uoc.ded.practica.exceptions.NoWorkersException;
import uoc.ded.practica.exceptions.OrderNotFoundException;
import uoc.ded.practica.exceptions.OrganizationNotFoundException;
import uoc.ded.practica.exceptions.UserNotFoundException;
import uoc.ded.practica.exceptions.UserNotInActivityException;
import uoc.ded.practica.model.Activity;
import uoc.ded.practica.model.Group;
import uoc.ded.practica.model.Order;
import uoc.ded.practica.model.OrderGroup;
import uoc.ded.practica.model.OrderUser;
import uoc.ded.practica.model.Organization;
import uoc.ded.practica.model.Record;
import uoc.ded.practica.model.Role;
import uoc.ded.practica.model.User;
import uoc.ded.practica.model.Worker;
import uoc.ded.practica.util.OrderedVector;
import uoc.ei.tads.Cua;
import uoc.ei.tads.CuaAmbPrioritat;
import uoc.ei.tads.Diccionari;
import uoc.ei.tads.DiccionariAVLImpl;
import uoc.ei.tads.Iterador;
import uoc.ei.tads.TaulaDispersio;

public class SafetyActivities4Covid19Impl implements SafetyActivities4Covid19 {
	// Users as a hash table
	private TaulaDispersio<String, User> users;
	// Counter for the workers inside of users
	private int numWorkers;
	
	// Organizations as a hash table
	private TaulaDispersio<String, Organization> organizations;
	
	// Records as a priority queue
	private Cua<Record> records;
	// Counter for the rejected records
	private int rejectedRecords;
	// Counter for the total records
	private int totalRecords;
	
	// Activities as an AVL tree
	private Diccionari<String, Activity> activities;
	
	// Roles as a simple vector
	private Role roles[];
	private int numRoles;
	
	// Groups as an AVL tree
	private Diccionari<String, Group> groups;
	
	// Orders as an AVL tree
	private Diccionari<String, Order> orders;
	
	// Most active user as a pointer
	private User mostActiveUser;
	
	// Best activities as an ordered vector (ordered by its rating)
	private OrderedVector<Activity> bestActivities;
	
	// Best organizations as an ordered vector (ordered by its rating)
	private OrderedVector<Organization> bestOrganizations;
	
	public SafetyActivities4Covid19Impl() {
		this.users = new TaulaDispersio<String, User>();
		this.numWorkers = 0;
		
		this.organizations = new TaulaDispersio<String, Organization>();
		
		this.records = new CuaAmbPrioritat<Record>(Record.CMP_Q);
		this.rejectedRecords = 0;
		this.totalRecords = 0;
		
		this.activities = new DiccionariAVLImpl<String, Activity>(Activity.CMP_K);
		
		this.roles = new Role[R];
		this.numRoles = 0;
		
		this.groups = new DiccionariAVLImpl<String, Group>(Group.CMP_K);
		
		this.orders = new DiccionariAVLImpl<String, Order>(Order.CMP_K);
		
		this.mostActiveUser = null;
		
		this.bestActivities = new OrderedVector<Activity>(BEST_10_ACTIVITIES, Activity.CMP_V);
		
		this.bestOrganizations = new OrderedVector<Organization>(BEST_ORGANIZATIONS, Organization.CMP_V);
	}

	@Override
	public void addUser(String userId, String name, String surname, LocalDate birthday, boolean covidCertificate) {
		// Get the user from the user hash table this.users
		User user = this.getUser(userId);
		
		// The user has to be added, as long as he/she doesn't exist yet
		if (user == null)
			this.users.afegir(userId, new User(userId, name, surname, birthday, covidCertificate));
		// Update its information if he/she exists
		else {
			user.setName(name);
			user.setSurname(surname);
			user.setBirthday(birthday);
			user.setCovidCertificate(covidCertificate);
		}
	}

	@Override
	public void addOrganization(String organizationId, String name, String description) {
		// Get the organization from the organization hash table this.organizations
		Organization organization = this.getOrganization(organizationId);
		
		// The organization has to be added, as long as it doesn't exist yet
		if (organization == null)
			this.organizations.afegir(organizationId, new Organization(organizationId, name, description));
		// Update its information if it exists
		else {
			organization.setName(name);
			organization.setDescription(description);
		}
	}

	@Override
	public void addRecord(String recordId, String actId, String description, Date date, LocalDate dateRecord, Mode mode, int num, String organizationId) throws OrganizationNotFoundException {
		// Get the organization from the organization hash table this.organizations
		Organization org = this.getOrganization(organizationId);
		
		// Throw an exception if the organization doesn't exists in the hash table
		if (org == null)
			throw new OrganizationNotFoundException();
		
		// Initialize the record with its values received
		Record record = new Record(recordId, actId, description, date, dateRecord, mode, num, org);
		
		// Add the record to the organization, enqueue to the records of the TAD and increment the total amount of records
		org.addRecord(record);
		this.records.encuar(record);
		this.totalRecords++;
	}
	
	@Override
	public Record updateRecord(Status status, Date date, String description) throws NoRecordsException {
		// No records to be updated in the priority queue
		if (this.records.estaBuit())
			throw new NoRecordsException();
		
		// Get the record from the priority queue this.records and remove it from there
		Record record = this.records.desencuar();
		
		// In this case, if the record doesn't exist, the method desempilar will throw ExcepcioContenidorBuit
		// so it's not necessary to check if record is null or not
		
		// Update the information of the record
		record.update(status, date, description);
		
		// If the record is ENABLED create the the activity and add to the AVL tree of activities
		if (record.isEnabled()) {
			Activity activity = record.newActivity();
			this.activities.afegir(activity.getActId(), activity);
		}
		// Otherwise, increment the rejected records
		else
			this.rejectedRecords++;
		
		return record;
	}

	@Override
	public Order createTicket(String userId, String actId, LocalDate date) throws UserNotFoundException, ActivityNotFoundException, LimitExceededException {
		// Get the user from the user array this.users
		User user = this.getUser(userId);
		
		// Check if the user is in the array throw an exception if not
		if (user == null)
			throw new UserNotFoundException();
	
		// Get the activity from the ordered vector dictionary this.activities
		Activity activity = this.getActivity(actId);
		
		// Check if activity is in the ordered vector dictionary and throw an exception if not
		if (activity == null)
			throw new ActivityNotFoundException();
		
		// Check if the activity already has reach the maximium capacity
		// and throw an exception in that case
		if (!activity.hasAvailabilityOfTickets())
			throw new LimitExceededException();
		
		// Initialize the order for this user
		Order order = new OrderUser(user, activity, date);
		// Create the tickets without the seat number
		order.createTickets(activity);
		
		// Add the order to AVL tree of orders
		this.orders.afegir(order.getId(), order);
		// Create a new order for that activity
		activity.addOrder(order);
		// Add this activity to user linked list of activities
		user.addActivity(activity);
		// Add the user to the linked list of users in this activity
		activity.addUser(user);
		// Update the most active user according to its number of activities
		this.updateMostActiveUser(user);
		
		return order;
	}

	@Override
	public Order assignSeat(String actId) throws ActivityNotFoundException {
		// Get the activity from the ordered vector dictionary this.activities
		Activity activity = this.getActivity(actId);
		
		// Check if activity is in the ordered vector dictionary and throw an exception if not
		if (activity == null)
			throw new ActivityNotFoundException();
		
		// Get a ticket from the priority queue of tickets of this activity
		return activity.dequeueOrder();
	}

	@Override
	public void addRating(String actId, Rating rating, String message, String userId) throws ActivityNotFoundException, UserNotFoundException, UserNotInActivityException {
		// Get the activity from the AVL tree this.activities
		Activity activity = this.getActivity(actId);
		
		// Check if the activity exists
		if (activity == null)
			throw new ActivityNotFoundException();
		
		// Get the user from the hash table this.users
		User user = this.getUser(userId);
		
		// Check if the user exists
		if (user == null)
			throw new UserNotFoundException();
		
		// Check if the user participate in that activity
		if (!user.isInActivity(actId))
			throw new UserNotInActivityException();
		
		// Add the rating to the linked list of ratings of this activity
		activity.addRating(rating, message, user);
		// Increment the number of ratings for this user according to calcule its badge with O(1)
		user.incrementRatings();
		// Increment the number of ratings for this organizations according to calcule its rating with O(1)
		activity.getOrganization().incrementRatings(rating.getValue());
		
		// Update the ordered vectors for the best activities and organizations
		this.updateBestActivities(activity);
		this.updateBestOrganizations(activity.getOrganization());
	}

	@Override
	public Iterador<uoc.ded.practica.model.Rating> getRatings(String actId) throws ActivityNotFoundException, NoRatingsException {
		// Get the activity from the AVL tree this.activities
		Activity activity = this.getActivity(actId);
		
		// Check if the activity exists
		if (activity == null)
			throw new ActivityNotFoundException();
		
		// Check if the activity has ratings
		if (!activity.hasRatings())
			throw new NoRatingsException();
		
		// Return the iterator
		return activity.ratings();
	}

	@Override
	public Activity bestActivity() throws ActivityNotFoundException {
		// Check if exists any activity rated
		if (this.bestActivities.estaBuit())
			throw new ActivityNotFoundException();
		
		// Return the first one
		return this.bestActivities.elementAt(0);
	}

	@Override
	public Iterador<Activity> best10Activities() throws ActivityNotFoundException {
		// Check if exists any activity rated
		if (this.bestActivities.estaBuit())
			throw new ActivityNotFoundException();
		
		// Return the iterator
		return this.bestActivities.elements();
	}

	@Override
	public User mostActiveUser() throws UserNotFoundException {
		// Check if exists any user participating in activities
		if (this.mostActiveUser == null)
			throw new UserNotFoundException();
		
		// Return the pointer
		return this.mostActiveUser;
	}

	@Override
	public double getInfoRejectedRecords() {
		// Return rejectedRecords/totalRecords
		return (double)this.numRejectedRecords()/(double)this.numRecords();
	}

	@Override
	public Iterador<Activity> getAllActivities() throws NoActivitiesException {
		// If there are no activities, throw an exception
		if (this.activities.estaBuit())
			throw new NoActivitiesException();
		
		// Return the iterator of activities
		return this.activities.elements();
	}

	@Override
	public Iterador<Activity> getActivitiesByUser(String userId) throws NoActivitiesException {
		// Get the user from the user hash table this.users
		// The user has to be in the hash table of users
		User user = this.getUser(userId);
		
		// Check if the users has activities
		if (!user.hasActivities())
			throw new NoActivitiesException();
		
		// Return the iterator of activities of the user searched
		return user.activities();
	}

	@Override
	public User getUser(String userId) {
		// Search an return the user in the hash table
		return this.users.consultar(userId);
	}

	@Override
	public Organization getOrganization(String organizationId) {
		// Search and return the organization in the hash table
		return this.organizations.consultar(organizationId);
	}
	
	@Override
	public Record currentRecord() {
		// Get the first position of the priority queue of records
		return this.records.primer();
	}

	@Override
	public int numUsers() {
		// Return the number of users in the hash table of users this.users
		return this.users.nombreElems();
	}

	@Override
	public int numOrganizations() {
		// Return the number of organizations in the hash table of organizations this.organizations
		return this.organizations.nombreElems();
	}

	@Override
	public int numPendingRecords() {
		// Return the number of pendings records, the number of the elements in the priority queue
		return this.records.nombreElems();
	}

	@Override
	public int numRecords() {
		// Return the number of records according its counter
		return this.totalRecords;
	}

	@Override
	public int numRejectedRecords() {
		// Return the number of records rejected according its counter
		return this.rejectedRecords;
	}

	@Override
	public int numActivities() {
		// Return the number of activies from the AVL tree this.activities
		return this.activities.nombreElems();
	}

	@Override
	public int numActivitiesByOrganization(String organizationId) {
		// Return the number of activities of an organization by its id,
		// the organization has to be in the hash table of organizations
		return this.getOrganization(organizationId).numActivities();
	}

	@Override
	public int numRecordsByOrganization(String organizationId) {
		// Return the number of records of an organization by its id,
		// the organization has to be in the hash table of organizations
		return this.getOrganization(organizationId).numRecords();
	}

	@Override
	public Activity getActivity(String actId) {
		// Search the activity in the AVL tree
		return this.activities.consultar(actId);
	}

	@Override
	public int availabilityOfTickets(String actId) {
		// Return the amount of tickets available,
		// the activity has to be in the AVL tree
		return this.getActivity(actId).availabilityOfTickets();
	}

	@Override
	public void addRole(String roleId, String name) {
		// Get the role from the role array this.roles
		Role role = this.getRole(roleId);
		
		// The role has to be added, as long as it doesn't exist yet, at the last available position of the array
		if (role == null) {
			this.roles[this.numRoles] = new Role(roleId, name);
			this.numRoles++;
		}
		// Update its information if it exists
		else
			role.setName(name);
	}

	@Override
	public void addWorker(String userId, String name, String surname, LocalDate birthday, boolean covidCertificate, String roleId, String organizationId) {
		// Get the worker from one of the roles
		Worker worker = this.getWorker(userId);
		
		// The worker has to be added, as long as he/she doesn't exist yet, at the last available position of the array
		if (worker == null)
		{
			worker = new Worker(userId, name, surname, birthday, covidCertificate, roleId, organizationId);
			this.users.afegir(userId, worker);
			
			// Add it to the role
			Role role = this.getRole(roleId);
			role.addWorker(worker);
			
			// Add it to the organization as a worker
			Organization organization = this.getOrganization(organizationId);
			organization.addWorker(worker);
			
			this.numWorkers++;
		}
		// Update its information if he/she exists
		else
		{
			this.addUser(userId, name, surname, birthday, covidCertificate);
			
			// Only replace its organization if it's not the same
			if (!worker.getOrganizationId().equals(organizationId))
			{
				// Remove the worker from the old organization
				Organization organizationOld = this.getOrganization(worker.getOrganizationId());
				organizationOld.removeWorker(worker);
				
				// Add the worker to the new organization
				Organization organizationNew = this.getOrganization(organizationId);
				organizationNew.addWorker(worker);
				
				// Update the organization ID
				worker.setOrganizationId(organizationId);
			}
			
			// Only replace the its role if it's not the same
			if (!worker.getRoleId().equals(roleId))
			{
				// Remove the worker from the old role
				Role roleOld = this.getRole(worker.getRoleId());
				roleOld.removeWorker(worker);
				
				// Add the worker to the new role
				Role roleNew = this.getRole(roleId);
				roleNew.addWorker(worker);
				
				// Update the role ID
				worker.setRoleId(roleId);
			}
		}
	}

	@Override
	public Iterador<Worker> getWorkersByOrganization(String organizationId) throws OrganizationNotFoundException, NoWorkersException {
		// Get the organization from the organization hash table this.organizations
		Organization organization = this.getOrganization(organizationId);
		
		// Throw an exception if the organization doesn't exist in the hash table
		if (organization == null)
			throw new OrganizationNotFoundException();
		
		// Throw an exception if the organizations doesn't have any worker
		if (!organization.hasWorkers())
			throw new NoWorkersException();
		
		// Return an iterator with all its workers
		return organization.workers();
	}

	@Override
	public Iterador<User> getUsersInActivity(String activityId) throws ActivityNotFoundException, NoUserException {
		// Get the activity from the AVL tree this.activities
		Activity activity = this.getActivity(activityId);
		
		// Throw an exception if the activity doesn't exist in the AVL tree
		if (activity == null)
			throw new ActivityNotFoundException();
		
		// Throw an exception if the activity doesn't have any user
		if (!activity.hasUsers())
			throw new NoUserException();
		
		// Return an iterator with all its users
		return activity.users();
	}

	@Override
	public Badge getBadge(String userId, LocalDate day) throws UserNotFoundException {
		// Get the user from the user hash table this.users
		User user = this.getUser(userId);
		
		// Throw an exception if the user doesn't exist in the hash table
		if (user == null)
			throw new UserNotFoundException();
		
		// Return the Badge according to the date received as a param
		return user.getBadge(day);
	}

	@Override
	public void addGroup(String groupId, String description, LocalDate date, String... members) {
		// Get the group from the group AVL tree this.groups
		Group group = this.getGroup(groupId);
		// Create an array with the needed positions
		User[] userMembers = new User[members.length];
		
		// Insert every member to the array
		for (int i = 0; i < members.length; i++)
			userMembers[i] = this.getUser(members[i]);
		
		// The group has to be added, as long as it doesn't exist yet
		if (group == null)
		{
			group = new Group(groupId, description, date);
			group.setMembers(userMembers);
			this.groups.afegir(groupId, group);
		}
		// Update its information if it exists
		else {
			group.setDescription(description);
			group.setDate(date);
			group.setMembers(userMembers);
		}
	}

	@Override
	public Iterador<User> membersOf(String groupId) throws GroupNotFoundException, NoUserException {
		// Get the group from the group AVL tree this.groups
		Group group = this.getGroup(groupId);
		
		// Throw an exception if the group doesn't exist in the AVL tree
		if (group == null)
			throw new GroupNotFoundException();
		
		// Throw an exception if the group doesn't have any member
		if (!group.hasMembers())
			throw new NoUserException();
		
		// Return an iterator with all its members
		return group.members();
	}

	@Override
	public double valueOf(String groupId) throws GroupNotFoundException {
		// Get the group from the group AVL tree this.groups
		Group group = this.getGroup(groupId);
		
		// Throw an exception if the group doesn't exist in the AVL tree
		if (group == null)
			throw new GroupNotFoundException();
		
		// Return the mean value of the ratings of all its users
		return group.valueOf();
	}

	@Override
	public Order createTicketByGroup(String groupId, String actId, LocalDate date) throws GroupNotFoundException, ActivityNotFoundException, LimitExceededException {
		// Get the group from the group AVL tree this.groups
		Group group = this.getGroup(groupId);
		
		// Throw an exception if the group doesn't exist in the AVL tree
		if (group == null)
			throw new GroupNotFoundException();
		
		// Get the activity from the AVL tree this.activities
		Activity activity = this.getActivity(actId);
		
		// Throw an exception if the activity doesn't exist in the AVL tree
		if (activity == null)
			throw new ActivityNotFoundException();
		
		// Throw an exception if the activity doesn't have more tickets available
		if (activity.availabilityOfTickets() < group.numMembers())
			throw new LimitExceededException();
		
		// Create the order
		Order order = new OrderGroup(group, activity, date);
		// Create the needed tickets according to the number of members of the group
		order.createTickets(activity);
		
		// Add the order to the AVL tree this.orders
		this.orders.afegir(order.getId(), order);
		
		// Add the order to the priority queue of orders of this activity
		activity.addOrder(order);
		
		// Add the activity to the user, add the user to this activity and update the most active user
		for (Iterador<User> it = group.members(); it.hiHaSeguent();)
		{
			User user = it.seguent();
			user.addActivity(activity);
			activity.addUser(user);
			
			this.updateMostActiveUser(user);
		}
		
		// Return the pointer to the order created
		return order;
	}

	@Override
	public Order getOrder(String orderId) throws OrderNotFoundException {
		// Get the order from the AVL tree this.orders
		Order order = this.orders.consultar(orderId);
		
		// Throw an exception if the order doesn't exist
		if (order == null)
			throw new OrderNotFoundException();
		
		// Return the pointer to the order searched
		return order;
	}

	@Override
	public Iterador<Worker> getWorkersByRole(String roleId) throws NoWorkersException {
		// Get the role from the role array this.roles
		Role role = this.getRole(roleId);
		
		// Throw an exception if the role doesn't have any worker
		if (!role.hasWorkers())
			throw new NoWorkersException();
		
		// Return the iterator with all the workers of this role
		return role.workers();
	}

	@Override
	public Iterador<Activity> getActivitiesByOrganization(String organizationId) throws NoActivitiesException {
		// Get the organization from the organization hash table this.organizations
		Organization organization = this.getOrganization(organizationId);
		
		// Throw an exception if the organization doesn't have any activity
		if (!organization.hasActivities())
			throw new NoActivitiesException();
		
		// Return the iterator with all the activities of this organization
		return organization.activities();
	}
	
	@Override
	public Iterador<Record> getRecordsByOrganization(String organizationId) throws NoRecordsException {
		// Get the organization from the organization hash table this.organizations
		Organization organization = this.getOrganization(organizationId);
		
		// Throw an exception if the organization doesn't have any record
		if (!organization.hasRecords())
			throw new NoRecordsException();
		
		// Return an iterator with all the records of this organizations
		return organization.records();
	}

	@Override
	public Iterador<Organization> best5Organizations() throws NoOrganizationException {
		// Throw an exception if the ordered vector this.bestOrganizations is empty
		if (this.bestOrganizations.estaBuit())
			throw new NoOrganizationException();
		
		// Return an iterator with the best 5 organizations (can be 1-5)
		return this.bestOrganizations.elements();
	}

	@Override
	public Worker getWorker(String workerId) {
		// Search an return the worker in the hash table of users because the complexity is O(1)
		User user = this.users.consultar(workerId);
		
		// Check if the user has been found an he/she is a worker
		if (user != null && user.isWorker())
			return (Worker)user;
		
		// Return null if it's not a worker or the id doesn't exist
		return null;
	}

	@Override
	public Role getRole(String roleId) {
		Role role;
		
		// Search the role by its id an return it if it has been found
		for (int i = 0; i < this.numRoles; i++)
		{
			role = this.roles[i];
			if (role.is(roleId))
				return role;
		}
		
		// Return null if the for ends without returning a role
		return null;
	}

	@Override
	public Group getGroup(String groupId) {
		// Search an return the group in the AVL tree
		return this.groups.consultar(groupId);
	}

	@Override
	public int numWorkers() {
		// Return the number of users
		return this.numWorkers;
	}

	@Override
	public int numWorkers(String organizationId) {
		// Return the number of workers of an organization by its id
		return this.getOrganization(organizationId).numWorkers();
	}

	@Override
	public int numRoles() {
		// Return the number of roles
		return this.numRoles;
	}

	@Override
	public int numWorkersByRole(String roleId) {
		// Return the number of workers in a role by its id
		return this.getRole(roleId).numWorkers();
	}

	@Override
	public int numGroups() {
		// Return the number of groups
		return this.groups.nombreElems();
	}

	@Override
	public int numOrders() {
		// Return the number of orders
		return this.orders.nombreElems();
	}
	
	private void updateMostActiveUser(User user) {
		// If there are no most active user yet, update it with user from the param
		if (this.mostActiveUser == null)
			this.mostActiveUser = user;
		else {
			// Only update the user if the user from param has more activities than the old one
			if (this.mostActiveUser.numActivities() < user.numActivities())
				this.mostActiveUser = user;
		}
	}
	
	private void updateBestActivities(Activity activity) {
		// Remove and insert the activity to update the ordered vector
		this.bestActivities.delete(activity);
		this.bestActivities.update(activity);
	}
	
	private void updateBestOrganizations(Organization organization) {
		// Remove and insert the organization to update the ordered vector
		this.bestOrganizations.delete(organization);
		this.bestOrganizations.update(organization);
	}
}
