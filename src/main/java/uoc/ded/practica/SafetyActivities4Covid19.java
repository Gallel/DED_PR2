package uoc.ded.practica;

import uoc.ded.practica.exceptions.*;
import uoc.ded.practica.model.*;
import uoc.ded.practica.model.Record;
import uoc.ei.tads.Iterador;

import java.time.LocalDate;
import java.util.Date;


/**
 * Definició del TAD de gestió de la plataforma de gestió d'activitats culturals
 */
public interface SafetyActivities4Covid19 {

    /**
     * dimensió per al contenidor de les 10 millors activitats
     */
    public static final int BEST_10_ACTIVITIES = 10;

    /**
     * dimensió del contenidor de rols
     */
    public static final int R = 15;

    /**
     * dimensió de les millors organitzacions
     */
    public static final int BEST_ORGANIZATIONS = 5;

    enum Mode {
        ON_LINE,
        FACE2FACE
    }

    enum Status {
        PENDING,
        ENABLED,
        DISABLED
    }

    enum Rating {

        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        private final int value;

        private Rating(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum Badge {
        SENIOR_PLUS(100),
        SENIOR(85),
        MASTER_PLUS(75),
        YOUTH_PLUS(65),
        MASTER(50),
        YOUTH(40),
        JUNIOR_PLUS(35),
        JUNIOR(20),
        DARK(0);

        private final int value;

        private Badge(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * Mètode que permet afegir un usuari en el sistema
     *
     * @param userId           identificador de l'usuari
     * @param name             nom de l'usuari
     * @param surname          cognoms de l'usuari
     * @param birthday         data de naixement
     * @param covidCertificate certificat de covid
     * @pre cert.
     * @post si el codi d'usuari és nou, els usuaris seran els mateixos més
     * un nou usuari amb les dades indicades. Si no, les dades del
     * usuari s'hauran actualitzat amb els nous.
     */
    public void addUser(String userId, String name, String surname, LocalDate birthday,
                        boolean covidCertificate);


    /**
     * Mètode que afegeix una organització en el sistema
     *
     * @param organizationId
     * @param name
     * @param description
     * @pre cert.
     * @post Si el codi d'organització no existeix les organitzacions seran les
     * mateixes més una nova amb les dades indicades. Si no, les dades de l'organització
     * s'hauran actualitzat amb els nous.
     */
    public void addOrganization(String organizationId, String name, String description);


    /**
     * Mètode que afegeix un nou expedient en el sistema
     *
     * @param recordId       identificador de l'expedient
     * @param actId          identificador de l'activitat cultural
     * @param description    la descripció de l'activitat cultural
     * @param date           data en la qual es realitzarà l'activitat cultural
     * @param dateRecord     data en la qual es registra l'expedient
     * @param mode           tipus d'activitat cultural (ON-LINE o PRESENCIAL)
     * @param num            nombre màxim d'assistents
     * @param organizationId identificador de l'organització
     * @pre l'activitat i el codi d'expedient no existeixen.
     * @post els expedients seran els mateixos
     * més un de nou amb les dades indicades. El nou expedient és el
     * element del cap d'expedients.
     * En cas que l'organització, identificada per organizacionId, no existeixi,
     * s'haurà d'informar de l'error
     */
    public void addRecord(String recordId, String actId, String description, Date date, LocalDate dateRecord,
                          Mode mode, int num, String organizationId) throws OrganizationNotFoundException;

    /**
     * Mètode que actualitza l'estat de l'expedient pendent de validar i retorna
     * l'expedient
     *
     * @param status      estat de l'espediente
     * @param date        data en la qual es realitza la valoració de l'expedient
     * @param description descripció de la valoració de l'expedient
     * @return Retorna l'expedient modificat
     * @throws NoRecordsException llança una excepció en cas que no existeixin expedients pendents de valorar
     * @pre cert.
     * @post L'estat de l'element que està en el cim de la pila d'expedients
     * es modifica, el nombre
     * d'expedients seran els mateixos menys un (el cim) i el nombre d'activitats
     * seran els mateixos més un, en cas que l'expedient sigui favorable. En cas que
     * no hi hagi expedients en la pila, s'haurà d'informar de l'error.
     */
    public Record updateRecord(Status status, Date date, String description) throws NoRecordsException;

    /**
     * Mètode que permet comprar entrades en una activitat cultural
     *
     * @param userId identificador de l'usuari
     * @param actId  identificador de l'activitat cultural
     * @param date   data en la qual és fa la compra d'entrades
     * @throws UserNotFoundException     llança l'excepció en cas que l'usuari no existeixi
     * @throws ActivityNotFoundException llança l'excepció en cas que l'activitat no existeixi
     * @throws LimitExceededException    llança l'excepció en cas que es demanin més entrades que les disponibles
     * @pre cert.
     * @post El nombre d'ordres de compra d'una activitat cultural seran els mateixos més una unitat.
     * En cas que l'usuari o l'activitat cultural no existeixi, s'haurà d'informar de
     * un error. En cas que ja s'hagi superat el màxim de places, s'haurà d'indicar un error.
     */
    public Order createTicket(String userId, String actId, LocalDate date) throws UserNotFoundException,
            ActivityNotFoundException, LimitExceededException;


    /**
     * Mètode que permet l'assignació d'un seient en un acte cultural
     *
     * @param actId identificador de l'activitat
     * @return retorna l'ordre de compra amb les entrades assignades
     * @throws ActivityNotFoundException llança l'activitat en cas que no existeixi
     * @pre cert.
     * @post El cap de la cua indica el seient a assignar i el nombre de tiquets pendents
     * d'assignar d'una activitat cultural seran els mateixos menys una unitat.  En cas que
     * l'activitat cultural no existeixi, s'haurà d'informar d'un error.
     */
    public Order assignSeat(String actId) throws ActivityNotFoundException;


    /**
     * Mètode que afegeix una valoració sobre una activitat cultural per part d'un usuari
     *
     * @param actId   identificador de l'activitat
     * @param rating  valoració de l'activitat
     * @param message missatge associat a l'activitat
     * @param userId  identificador de l'activitat
     * @throws ActivityNotFoundException  es llança l'excepció en cas que l'actiivdad no existeixi
     * @throws UserNotFoundException      es llança l'excepció en cas que l'usuari no existeixi
     * @throws UserNotInActivityException es llança l'excepció en cas que l'usuari no
     *                                    hagi participat en l'activitat cultural
     * @pre cert.
     * @post les valoracions seran les mateixes més una nova amb les dades indicades. En cas que
     * l'activitat o l'usuari no existeixi, s'haurà d'informar del
     * error. Si l'usuari no ha participat en l'activitat cultural també es
     * haurà d'informar de l'error.
     */
    public void addRating(String actId, Rating rating, String message, String userId)
            throws ActivityNotFoundException, UserNotFoundException, UserNotInActivityException;


    /**
     * Mètode que proporciona les valoracions d'una activitat cultural
     *
     * @param actId identificador de l'activitat
     * @return retorna un iterador per recórrer les activitats culturals
     * @throws ActivityNotFoundException es llança l'excepció en cas que no existeixi l'activitat
     * @throws NoRatingsException        es llança l'excepció en cas que no existeixin valoracions sobre l'activitat
     * @pre cert.
     * @post retorna un iterador per recorer les valoracions d'una activitat. En
     * cas * que no existeixi l'activitat o no hi hagi valoracions s'indicarà un error
     */
    public Iterador<uoc.ded.practica.model.Rating> getRatings(String actId) throws ActivityNotFoundException, NoRatingsException;


    /**
     * Mètodo que proporciona l'actividat millor valorada
     *
     * @return retorna l'actividad millorr valorada
     * @throws NoActivitiesException  llença l'excepció en cas que no existeixi cap activitat
     * @pre cert.
     * @post retorna l'actividat millor valorada. En cass que no existeixi
     * cap activitat s'indicarà un error
     */
    public Activity bestActivity() throws ActivityNotFoundException;

    /**
     * Mètode que proporciona les 10 millor activitats segons la seva valoració
     *
     * @return retorna  un iterador amb les 10 millors activitats
     * @throws NoActivitiesException es llança l'excepció en cas que no existeixi cap activitat
     * @pre cert.
     * @post retorna un iterador amb les 10 millors activitats. En cas que no existeixi
     * cap activitat s'haurà d'indicar un error
     */
    public Iterador<Activity> best10Activities() throws ActivityNotFoundException;


    /**
     * Mètode que proporcina l'usuari més participatiu
     *
     * @return retorna l'usuari més participatiu
     * @throws UserNotFoundException es llança l'excepció en cas que no existeixi cap usuari com activitat
     * @pre cert.
     * @post retorna a l'usuari més actiu (major participació en activitats culturals).
     * En cas que existeixi més d'un usuari amb el mateix nombre de participacions es proporciona aquell que va participar amb anterioritat. En cas que no existeixi cap usuari s'haurà d'indicar un error
     * mostActiveUser(): User
     */
    public User mostActiveUser() throws UserNotFoundException;


    /**
     * Mètode que proporciona el % d'expedients rebutjats
     *
     * @return retorna el % d'expedients rebutjats
     * @pre cert.
     * @post retorna un enter amb el valor d'expedients que no han estat validats
     */
    public double getInfoRejectedRecords();


    /**
     * Mètode que proporciona totes les activitats del sistema
     *
     * @return retorna un iterador per recorrerr totes les activitats
     * @throws NoActivitiesException llança una excepció en cas que no hagin activitats
     * @pre cert.
     * @post retorna un iterador per recórrer totes les activitats. En
     * cas * que no existeixin activitats s'haurà d'indicar un error
     */
    public Iterador<Activity> getAllActivities() throws NoActivitiesException;


    /**
     * Mètode que proporciona les activitats a les quals ha assistido un usuari
     *
     * @param userId identificador de l'usuari
     * @return retorna un iterador per recórrer les activitats d'un usuari
     * @throws NoActivitiesException llança una excepció en cas que no hi hagi activitats associades a l'usuari
     * @pre l'usuari existeix.
     * @post retorna un iterador per recórrer les activitats d'un usuari. En cas que no existeixin activitats s'indicarà un error
     */
    public Iterador<Activity> getActivitiesByUser(String userId) throws NoActivitiesException;


    ///////////////////////////////////////////////////////////////////////
    ///
    ///////////////////////////////////////////////////////////////////////


    /**
     * Mètode que proporciona l'usuari identificat
     *
     * @param userId identificador de l'usuari
     * @return retorna l'usuari o null en cas que no existeixi
     */
    public User getUser(String userId);


    /**
     * Mètode que proporciona una organització
     *
     * @param organizationId identificador de l'organització
     * @return retorna l'organització o null en cas que no existeixi.
     */
    public Organization getOrganization(String organizationId);

    /**
     * Mètode que proporciona l'expedient actual a valorar
     *
     * @return retorna l'expedient a valorar o null si no hi ha cap
     */
    public Record currentRecord();

    /**
     * Mètode que proporciona el nombre d'usuaris
     *
     * @return retona el nombre d'usuaris
     */
    public int numUsers();

    /**
     * Mètode que proporciona el nombre d'organitzacions
     *
     * @return retona el nombre d'organitzacions
     */
    public int numOrganizations();


    /**
     * Mètode que retorna el nombre d'expedients pendents de validar
     *
     * @return retorna el nombre d'expedients
     */
    public int numPendingRecords();

    /**
     * Mètode que proporciona el total d'expedients que hi ha en el sistema
     *
     * @return retorna el nombre total d'expedients
     */
    public int numRecords();

    /**
     * Mètode que proporciona el nombre d'expedients que han estat rebutjats
     *
     * @return retorna el nombre d'expedients rebutjats
     */
    public int numRejectedRecords();

    /**
     * Mètode que proporciona el nombre d'activitats
     *
     * @return retorna el nombre d'activitats
     */
    public int numActivities();


    /**
     * Mètode que proporciona el nombre d'activitats d'una organització
     *
     * @param organizationId identificador de l'activitat
     * @return retorna el nombre d'activitats de l'organització
     * @PRE l'organització existeix
     */
    public int numActivitiesByOrganization(String organizationId);

    /**
     * Mètode que proporciona el nombre total d'expedients d'una organització
     *
     * @param organizationId identificador de l'activitat
     * @return retorna el nombre d'expedients de l'organització
     * @PRE l'organització existeix
     */
    public int numRecordsByOrganization(String organizationId);

    /**
     * Mètode que proporciona una activitat
     *
     * @param actId identificador de l'activitat
     * @return retorna l'activitat o null si no existeix
     */
    public Activity getActivity(String actId);


    /**
     * Mètode que proporciona el nombre d'entrades disponibles sobre una activitat cultural
     *
     * @param actId identificador de l'activitat cultural
     * @return retorna el nombre d'entrades disponibles d'una activitat cultural o zero en qualsevol altre cas
     */
    public int availabilityOfTickets(String actId);


    ///
    // PR2
    //


    /**
     * (1) Mètode que permet afegir o modificar rols en el sistema
     *
     * @param roleId identificador de rol
     * @param name   nom del rol
     * @pre cert.
     * @post si el codi de rol és nou, els rols seran els mateixos més un rol nou
     * amb les dades indicades. Si no, les dades del rol s'hauran actualitzat amb els nous.
     */
    public void addRole(String roleId, String name);


    /**
     * (2) Mètode que permet afegir un treballador en el sistema
     *
     * @param userId           identificador del treballador
     * @param name             nom del treballador
     * @param surname          cognoms del treballador
     * @param birthday         data de naixement del treballador
     * @param covidCertificate certificat covid
     * @param roleId           identificador del rol del treballador
     * @param organizationId   organització a la qual pertany el treballador
     * @pre cert.
     * @post si el codi d'usuari és nou, els usuaris seran els mateixos més un nou usuari
     * i el nombre usuaris d'una organització seran els mateixos més un de nou, amb les dades
     * indicades i el nombre d'usuari d'un rol seran els mateixos més un de nou. Si no, les dades
     * de l'usuari s'hauran actualitzat amb els nous;
     * <p>
     * addWorker(userID, name, surname, birthday, covidCertificate, role)
     */
    public void addWorker(String userId, String name, String surname, LocalDate birthday,
                          boolean covidCertificate, String roleId, String organizationId);


    /**
     * (3) Mètode que proporciona els treballadors d'una organització
     *
     * @param organizationId identificador de l'organització
     * @return retorna un iterador per recórrer els treballadors de l'organització
     * @throws OrganizationNotFoundException es llança l'excepció en cas que l'organització no existeixi
     * @throws NoWorkersException            es llança l'excepció en cas que no existeixin treballadors
     * @pre cert.
     * @post retorna un iterador per recórrer tots treballadors d'una orrganización. En
     * cas * que no existeixin treballadors o l'organització no existeixi, s'haurà d'indicar un error
     */
    public Iterador<Worker> getWorkersByOrganization(String organizationId) throws OrganizationNotFoundException, NoWorkersException;


    /**
     * (4) Mètode que proporciona els usuaris que han assistit a una activitat cultural
     *
     * @param activityId identificador de l'activitat cultural
     * @return retorna un iterador per recórrer els usuaris que han participat en una activitat cultural
     * @throws ActivityNotFoundException es llança en cas que l'activitat no existeixi
     * @throws NoUserException           es llança en cas que no hagin usuaris
     * @pre cert.
     * @post retorna un iterador per recórrer tots els usuaris que han participat
     * en una activitat cultural. En cas que no existeixin usuaris o l'activitat
     * no existeixi, s'haurà d'indicar un error
     */
    public Iterador<User> getUsersInActivity(String activityId) throws ActivityNotFoundException, NoUserException;


    /**
     * (5) Mètode que proporciona la insígnia associada a un usuari
     *
     * @param userId identificador de l'usuari
     * @param day    dia en el qual es consulta la insígnia
     * @return retorna la insígnia associada a l'usuari
     * @throws UserNotFoundException llança l'excepció en cas que l'usuari no existeixi
     * @pre cert.
     * @post retorna la insígnia associada a l'usuari. En cas que no existeixi l'usuari, s'haurà d'indicar un error
     */
    public Badge getBadge(String userId, LocalDate day) throws UserNotFoundException;

    /**
     * (6) Mètode que permet crear un grup i assignar un conjunt de membres
     *
     * @param groupId     identificador de grup
     * @param description descripció del grup
     * @param date      data de creació del grup
     * @param members     membres del grup
     * @pre tots els membres del grup existeixen
     * @post si el codi de grup és nou, els grups seran els mateixos més un grup nou amb les dades
     * indicades. Si no, les dades del grup s'hauran actualitzat amb els nous.
     */
    public void addGroup(String groupId, String description, LocalDate date, String... members );

    /**
     * (7) Mètode que proporciona un iterador per recórrer els membres d'un grup
     *
     * @pre cert.
     * @post retorna un iterador per recórrer tots els usuaris d'un grupol. En
     * cas que no existeixin el grup o no hi hagi usuaris, s'haurà d'indicar un error
     *
     * @param groupId identificador del grup
     * @return retorna un iterador per recórrer els membres d'un grup
     * @throws GroupNotFoundException llança l'excepció si el grup no existeix
     * @throws NoUserException llança l'excepció en cas que el grup no tingui membres
     */
    public Iterador<User>membersOf(String groupId) throws GroupNotFoundException, NoUserException;

    /**
     * (8) Mètode que calcula el valor mitjà d'un grup.
     *
     * @param groupId identificador del grup
     * @return retorna el valor mitjà d'un grup
     * @throws GroupNotFoundException es llança l'excepció en cas que el grup no existeixi
     * @pre cert.
     * @post retorna el valor mitjà d'un grup format pel valor dels membres del
     * grup (insígnies).  En
     * cas * que no existeixi el grup, s'haurà d'indicar un error
     */
    public double valueOf(String groupId) throws GroupNotFoundException;

    /**
     * (9) Mètode que permet comprar entrades a un grup creat prèviament
     *
     * @param groupId identificador del grup
     * @param actId   identificador de l'activitat
     * @param date  data en la qual es realitza la compra
     * @throws GroupNotFoundException    es llança l'excepció en cas que el grup no existeixi
     * @throws ActivityNotFoundException es llança l'excepció en cas que l'activitat no existeixi
     * @throws LimitExceededException    es llança l'excepció en cas que s'excedeixi el nombre d'entrades
     * @* return retorna l'órden de compra associada a la compra de les entrades
     */
    public Order createTicketByGroup(String groupId, String actId, LocalDate date) throws GroupNotFoundException,
            ActivityNotFoundException, LimitExceededException;


     /**
      * (10) Mètode que proporciona informació d'una ordre de compra
      *
      * @pre cert.
      * @post retorna l'ordre associada amb l'identificador. En cas que no existeixi, s'indicarà un error.
      *
      * @param orderId identificador de l'ordre de compra
      *
      * @return retorna l'ordre de compra
      * @throws OrderNotFoundException es llança l'excepció en cas que l'ordre de compra no existeixi
      */
     public Order getOrder(String orderId) throws OrderNotFoundException;


    /**
     * (11) Mètode que proporciona un iterador amb els treballadors d'un rol
     *
     * @param roleId identificador del rol
     * @return retorna un iterador amb els treballadors associats a un rol
     * @throws NoWorkersException es llança l'excepció en cas que no existeixin treballadors
     * @pre el rol existeix
     * @post retorna un iterador amb els treballadors que tinguin associat un rol. En cas que no existeixin
     * treballadors, s'indicarà un error.
     */
    public Iterador<Worker> getWorkersByRole(String roleId) throws NoWorkersException;

    /**
     * (12a) Mètode que proporciona un iterador amb les activitats creades per l'organització
     *
     * @param organizationId identificador de l'organització
     * @return retorna un iterador amb les activitats d'una organització
     * @throws NoActivitiesException llança una excepció en cas que no existeixin activitats
     *                               creades per l'organització
     * @pre l'organitzación existeix.
     * @post retorna un iterador per recórrer les activitats d'una organització. En cas que no existeixin
     * <p>
     * les activitats s'indicarà un error
     */
    public Iterador<Activity> getActivitiesByOrganization(String organizationId) throws NoActivitiesException;

    /**
     * (12b) Mètode que proporciona un iterador amb els expedients creadoss per l'organització
     *
     * @param organizationId identificador de l'organització
     * @return retorna un iterador amb els expedients d'una organització
     * @throws NoRecordsException llança una excepció en cas que no existeixin expedients
     *                            creades per l'organització
     * @pre l'organitzación existeix.
     * @post retorna un iterador per recórrer els expedients d'una organització. En cas que no existeixin
     * <p>
     * expedients s'indicarà un error
     */
    public Iterador<Record> getRecordsByOrganization(String organizationId) throws NoRecordsException;

    /**
     * (13) Mètode que proporciona les 5 (o menys) millors organitzacions
     *
     * @return retorna un iterador amb les 5 (o menys) millors organitzacions
     * @throws NoOrganizationException
     * @pre cert.
     * @post retorna un iterador per recórrer les 5 millors organitzacions
     */
    public Iterador<Organization> best5Organizations() throws NoOrganizationException;


    /**
     * Mètode que proporciona el treballador identificat
     *
     * @param workerId identificador del treballador
     * @return retorna el treballador requerit o null en cas que no existeixi
     */
    public Worker getWorker(String workerId);


    /**
     * Mètode que proporciona el rol
     *
     * @param roleId identificador del rol
     * @return retorna el rol requerit o null en cas que no existeixi
     */
    public Role getRole(String roleId);

    /**
     * Mètode que proporciona el grup
     *
     * @param groupId identificador del grup
     * @return retorna el grup requerit o null en cas que no existeixi
     */
    public Group getGroup(String groupId);


    /**
     * Mètode que proporciona el nombre de treballadors que hi ha en el sistema
     *
     * @return retorna el nombre de treballadors en el sistema
     */
    public int numWorkers();

    /**
     * Mètode que proporciona el nombre de treballadors d'una organització
     *
     * @param organizationId identificador de l'organització
     * @return retorna el nombre de treballadors d'una organització o zero en cas que no existeixi la
     * la organització
     */
    public int numWorkers(String organizationId);

    /**
     * Mètode que proporciona el nombre de rols
     *
     * @return retorna el nombre de rols
     */
    public int numRoles();


    /**
     * Mètode que proporciona el nombre de treballadors associats a un rol
     *
     * @param roleId identificador del rol
     * @return retorna el nombre de treballadors o zero en cas que no existeixi el rol
     */
    public int numWorkersByRole(String roleId);

    /**
     * Mètode que proporciona el nombre de grups que existeixen en el sistema
     *
     * @return retorna el nombre de grups
     */
    public int numGroups();


    /**
     * Mètode que proporciona el nombre d'ordres de compra
     *
     * @return retorna el nombre d'ordres de compra
     */
    public int numOrders();


}

