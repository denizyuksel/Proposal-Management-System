/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.net.deniz;

import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author yukseldeniz
 */
@Api("CyberSuggest services")
@Path("cybersuggest")
public class SuggestionService {

    Member member;
    Suggestion suggestion;
    FileInstance fileObj;

    static Random rand = new Random();
    public static Map<String, Member> sessionMap = new HashMap<String, Member>();

    @Resource
    UserTransaction transaction;
    @PersistenceContext(unitName = "com.mycompany_suggestion_system_v2_war_1.0-SNAPSHOTPU")
    EntityManager em;

    // Signup service
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    //@Produces(MediaType.MULTIPART_FORM_DATA)
    //@Consumes(MediaType.MULTIPART_FORM_DATA)

    @ApiOperation("Signup")
    @Path("/member/signup")
    public boolean SignUp(
            @FormParam("memberObj") Member member) {

        String userName = member.getUserName();
        String email = member.getEmail();
        String password = member.getPassword();
        String name = member.getName();
        String surname = member.getSurname();       
        //String userType = member.getUserType();

        if (("".equals(userName) || userName == null) || ("".equals(password) || password == null) || ("".equals(email) || email == null)
                || ("".equals(surname) || surname == null) || (name == null || "".equals(name))) {
            return false;
        } else {

            try {
                //Member member = new Member(userName, email, password, name, surname, userType);
                Member compared = em.find(Member.class, userName);
                transaction.begin();

                if (compared == null) {

                    try {
                        member.setUserType("standart");
                        em.persist(member);
                        transaction.commit();

                        return true;
                    } catch (RollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicMixedException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicRollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalStateException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SystemException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String memUserName = member.getUserName();
                    String comparedUserName = compared.getUserName();
                    if (!(memUserName.equals(comparedUserName))) {

                        try {
                            em.persist(member);
                            transaction.commit();

                            return true;
                        } catch (RollbackException ex) {
                            Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (HeuristicMixedException ex) {
                            Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (HeuristicRollbackException ex) {
                            Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SecurityException ex) {
                            Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalStateException ex) {
                            Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SystemException ex) {
                            Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        return false;
                    }
                }
            } catch (javax.transaction.NotSupportedException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    // Login service
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Login")
    @Path("/member/login")
    public String login(
            @FormParam("memberObj") @ApiParam(example = "{ \"userName\": \"1\", \"email\": \"1\", \"password\": \"1\", \"name\": \"1\", \"surname\": \"1\", \"userType\": \"admin\" }") Member member
    ) {
        String userName = member.getUserName();
        String password = member.getPassword();
        String allFieldsError = "Please fill all the required fields!";

        if ("".equals(userName) || "".equals(password)) {
            return allFieldsError;
        } else if (userName == null || password == null) {
            // Please fill all the fields.
            return null;
        } else {
            Member memberFound = em.find(Member.class, userName);

            if (memberFound != null) {
                if ((userName.equalsIgnoreCase(memberFound.getUserName())) && (password.equals(memberFound.getPassword()))) {
                    byte[] randomToken = new byte[27];
                    rand.nextBytes(randomToken);
                    String token = Base64.getEncoder().encodeToString(randomToken);
                    sessionMap.put(token, memberFound);
                    return token;

                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    // Logout service
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Logout")
    @Path("/member/logout")
    public boolean logout(
            @FormParam("token") String token
    ) {
        if (sessionMap.containsKey(token)) {
            sessionMap.remove(token);
            return true;
        }
        return false;
    }

    // Add suggestion service
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    //@Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Add Suggestion")
    @Path("/suggestion/new")
    public Long addSuggestion(
            @FormParam("token") String token,
            @FormParam("suggestion") @ApiParam(example = "{ \"suggestionId\": \"2\", \"title\": \"suggestion test\", \"byUserName\": \"1\", \"suggestionText\": \"test text\", \"hasFile\": \"false\", \"fileIdRef\": \"100\", \"isLocked\": \"false\", \"lockedById\": \"1\" } ") Suggestion suggestion) {

        if (sessionMap.containsKey(token)) {
            Member member = sessionMap.get(token);
            String byUserName = member.getUserName();
            suggestion.setByUserName(byUserName);

            String sugTitle = suggestion.getTitle();

            if (sugTitle.equals("")) {
                return null;
            } else {

                try {
                    //this adds the suggestion texts without body.

                    transaction.begin();

                    try {
                        Suggestion merged = em.merge(suggestion);
                        em.persist(merged);
                        Long suggId = merged.getSuggestionId();
                        transaction.commit();
                        return suggId;
                    } catch (RollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicMixedException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicRollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalStateException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SystemException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (javax.transaction.NotSupportedException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SystemException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    // Suggestions fetch service    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch Suggestions")
    @Path("/suggestion/fetchSuggestions")
    public Suggestion[] fetchSuggestions(
            @QueryParam("token") String token
    ) {
        if (sessionMap.containsKey(token)) {
            try {
                Member memberFound = sessionMap.get(token);
                String userType = memberFound.getUserType();
                boolean viewsAll = userType.equals("manager") || userType.equals("admin");

                transaction.begin();

                if (viewsAll == false) {
                    try {
                        // suggestors cannot use lockable.
                        CriteriaBuilder cb = em.getCriteriaBuilder();

                        AbstractQuery<Suggestion> cq1 = cb.createQuery(Suggestion.class);
                        AbstractQuery<Suggestion> cq2 = cb.createQuery(Suggestion.class);
                        Root<Suggestion> sug = cq1.from(Suggestion.class);
                        cq1.where(cb.equal(sug.get("byUserName"), memberFound.getUserName()));

                        CriteriaQuery<Suggestion> selectSpecific = ((CriteriaQuery<Suggestion>) cq1).select(sug);
                        TypedQuery<Suggestion> tq1 = em.createQuery(selectSpecific);

                        Suggestion[] fetchedSuggestions = tq1.getResultList().toArray(new Suggestion[0]);

                        //insert if !isEmpty?
                        transaction.commit();
                        return fetchedSuggestions;
                    } catch (RollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicMixedException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicRollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalStateException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SystemException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    try {
                        //managers can use lockable.
                        CriteriaBuilder cb = em.getCriteriaBuilder();
                        CriteriaQuery<Suggestion> cq = cb.createQuery(Suggestion.class);
                        Root<Suggestion> sug = cq.from(Suggestion.class);

                        CriteriaQuery<Suggestion> selectAll = cq.select(sug);
                        selectAll.orderBy(cb.asc(sug.get("suggestionId")));
                        TypedQuery<Suggestion> q = em.createQuery(selectAll);

                        Suggestion[] fetchedSuggestions = q.getResultList().toArray(new Suggestion[0]);

                        transaction.commit();
                        return fetchedSuggestions;

                    } catch (RollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicMixedException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicRollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalStateException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SystemException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (javax.transaction.NotSupportedException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Filter suggestions")
    @Path("/suggestion/filterSuggestions")
    public Suggestion[] filterSuggestions(
            @QueryParam("token") String token,
            @QueryParam("filter") String filter) {

        if (sessionMap.containsKey(token)) {

            try {
                Member memberFound = sessionMap.get(token);
                String userType = memberFound.getUserType();
                boolean viewsAll = userType.equals("manager") || userType.equals("admin");

                transaction.begin();
                if (viewsAll == false) {
                    try {
                        // standart user

                        CriteriaBuilder cb = em.getCriteriaBuilder();
                        AbstractQuery<Suggestion> cq = cb.createQuery(Suggestion.class);
                        Root<Suggestion> sug = cq.from(Suggestion.class);
                        List<Predicate> predicates = new ArrayList<Predicate>();

                        predicates.add(cb.equal(sug.get("byUserName"), memberFound.getUserName()));
                        predicates.add(cb.like(sug.<String>get("title"), "%" + filter + "%"));

                        cq.where(predicates.toArray(new Predicate[]{}));

                        CriteriaQuery<Suggestion> selectSpecific = ((CriteriaQuery<Suggestion>) cq).select(sug);
                        TypedQuery<Suggestion> tq1 = em.createQuery(selectSpecific);

                        Suggestion[] filteredSuggestions = tq1.getResultList().toArray(new Suggestion[0]);
                        transaction.commit();
                        return filteredSuggestions;

                        //cq.where(predicates.toArray(new Predicate[]{}));
                        /*
                        TypedQuery<Suggestion> query
                        = em.createQuery("SELECT s FROM Suggestion s WHERE UPPER(s.title) LIKE CONCAT('%',UPPER(:title),'%')", Suggestion.class);
                        List<Suggestion> results = query.getResultList();
                         */
                    } catch (RollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicMixedException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicRollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalStateException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SystemException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        // manager user

                        CriteriaBuilder cb = em.getCriteriaBuilder();
                        CriteriaQuery<Suggestion> cq = cb.createQuery(Suggestion.class);
                        Root<Suggestion> sug = cq.from(Suggestion.class);

                        List<Predicate> predicates = new ArrayList<Predicate>();
                        predicates.add(cb.like(sug.<String>get("title"), "%" + filter + "%"));
                        cq.where(predicates.toArray(new Predicate[]{}));

                        CriteriaQuery<Suggestion> selectAll = cq.select(sug);
                        selectAll.orderBy(cb.asc(sug.get("suggestionId")));
                        TypedQuery<Suggestion> q = em.createQuery(selectAll);

                        Suggestion[] filteredSuggestions = q.getResultList().toArray(new Suggestion[0]);

                        transaction.commit();
                        return filteredSuggestions;
                    } catch (RollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicMixedException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (HeuristicRollbackException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalStateException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SystemException ex) {
                        Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (javax.transaction.NotSupportedException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    //@Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Lock suggestion")
    @Path("/suggestion/changeLockStatus")
    public boolean changeLockStatus(
            @FormParam("token") String token,
            @FormParam("suggestionId") Long suggestionId) {

        if (sessionMap.containsKey(token)) {
            try {
                transaction.begin();
                Suggestion suggestionFound = em.find(Suggestion.class, suggestionId);
                boolean isLocked = suggestionFound.getIsLocked();
                Member memberFound = sessionMap.get(token);
                boolean isAdmin = memberFound.getUserType().equals("admin");

                if (!isLocked) { // if it's unlocked.

                    if (!isAdmin) {
                        suggestionFound.setLockedById(memberFound.getUserName());

                        suggestionFound.setIsLocked(true);
                        em.persist(suggestionFound);

                        /* ORDER BY ID IN DB?
                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<Suggestion> cq = cb.createQuery(Suggestion.class);
                    Root<Suggestion> sug = cq.from(Suggestion.class);

                    CriteriaQuery<Suggestion> selectAll = cq.select(sug);
                    selectAll.orderBy(cb.asc(sug.get("suggestionId")));
                    Query q = em.createQuery(selectAll);
                    List<Suggestion> fetchedSuggestions = q.getResultList();
                         */
                        transaction.commit();
                        return true;
                    } else {
                        return false;
                    }

                } else { //if it's locked.
                    String lockedByUser = suggestionFound.getLockedById();
                    String memberFoundUserName = memberFound.getUserName();

                    if (lockedByUser.equals(memberFoundUserName) || isAdmin) {
                        //allow unclocking.
                        suggestionFound.setLockedById("");
                        suggestionFound.setIsLocked(false);
                        em.persist(suggestionFound);

                        /* ORDER BY ID IN DB?
                        CriteriaBuilder cb = em.getCriteriaBuilder();
                        CriteriaQuery<Suggestion> cq = cb.createQuery(Suggestion.class);
                        Root<Suggestion> sug = cq.from(Suggestion.class);

                        CriteriaQuery<Suggestion> selectAll = cq.select(sug);
                        selectAll.orderBy(cb.asc(sug.get("suggestionId")));
                        Query q = em.createQuery(selectAll);
                        List<Suggestion> fetchedSuggestions = q.getResultList();
                         */
                        transaction.commit();
                        return true;
                    } else { //don't allow unlocking.
                        return false;
                    }

                }

            } catch (RollbackException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.transaction.NotSupportedException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return false;

    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Delete Suggestion")
    @Path("/suggestion/delete")
    public boolean deleteSuggestion(
            @FormParam("token") String token,
            @FormParam("suggestionId") Long suggestionId) {

        if (sessionMap.containsKey(token)) {
            Member memberFound = sessionMap.get(token);
            boolean canDelete = memberFound.getUserType().equals("admin");
            Suggestion suggestionFound = em.find(Suggestion.class, suggestionId);
            boolean hasFile = suggestionFound.isHasFile();
            Long fileId = suggestionFound.getFileIdRef();

            if (canDelete) {
                try {
                    // DELETE HERE!
                    transaction.begin();
                    if (hasFile) {
                        FileInstance fileObject = em.find(FileInstance.class, fileId);
                        em.remove(fileObject);
                        Suggestion merged = em.merge(suggestionFound);
                        em.remove(merged);
                    } else {
                        Suggestion merged = em.merge(suggestionFound);
                        em.remove(merged);
                    }
                    transaction.commit();
                    return true;
                } catch (RollbackException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (HeuristicMixedException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (HeuristicRollbackException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalStateException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SystemException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (javax.transaction.NotSupportedException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                return false;
            }
        }
        return false;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.TEXT_PLAIN)
    @ApiOperation("Get Authorization Array")
    @Path("/member/authorization")
    public String[] getAuthorization(
            @QueryParam("token") String token) {

        List<String> auth = new ArrayList<String>();

        if (sessionMap.containsKey(token)) {
            Member memberFound = sessionMap.get(token);
            String userType = memberFound.getUserType();
            if (userType.equals("admin")) {
                auth.add("lock");
                auth.add("delete");
                auth.add("console");
            } else if (userType.equals("manager")) {
                auth.add("lock");
            }
        }
        return auth.toArray(new String[0]);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch member data")
    @Path("/member/admin/fetch")
    public Member[] fetchMembers(
            @QueryParam("token") String token) {
        if (sessionMap.containsKey(token)) {

            Member memberInfo = sessionMap.get(token);
            String userType = memberInfo.getUserType();
            String memUserName = memberInfo.getUserName();
            if (userType.equals("admin")) {

                try {
                    transaction.begin();
                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<Member> cq = cb.createQuery(Member.class);
                    Root<Member> mem = cq.from(Member.class);

                    List<Predicate> predicates = new ArrayList<Predicate>();

                    //predicates.add(cb.notEqual(mem.get("userName"), memUserName));                   

                    predicates.add(cb.notEqual(mem.get("userType"), userType)); 
                    
                    cq.where(predicates.toArray(new Predicate[]{}));

                    CriteriaQuery<Member> selectAll = cq.select(mem);
                    //selectAll.orderBy(cb.asc(mem.get("userName")));
                    TypedQuery<Member> q = em.createQuery(selectAll);

                    Member[] fetchedMembers = q.getResultList().toArray(new Member[0]);
                    transaction.commit();
                    return fetchedMembers;
                } catch (RollbackException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (HeuristicMixedException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (HeuristicRollbackException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalStateException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SystemException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (javax.transaction.NotSupportedException ex) {
                    Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return null;
        }
        return null;
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Change user type")
    @Path("/member/admin/changeType")
    public boolean changeMemberType(
        @FormParam("token") String token,
        @FormParam("userName") String userName){
        
        if(sessionMap.containsKey(token)){
            
            try {
                transaction.begin();
                Member memberFound = em.find(Member.class, userName);
                String userType = memberFound.getUserType();
                
                if( userType.equals("standart")){
                    memberFound.setUserType("manager");
                }
                else{
                    memberFound.setUserType("standart");
                }
                em.persist(memberFound);
                transaction.commit();
                return true;
            } catch (RollbackException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.transaction.NotSupportedException ex) {
                Logger.getLogger(SuggestionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Get File")
    @Path("file")
    public FileInstance getFileInstance() {
        return new FileInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Get Member")
    @Path("/member/getMember")
    public Member getMember() {
        return new Member();
    }

    // Upload file service
    // Download file service
}
