package com.chess.login;

import com.chess.gui.LoginFrame;
import com.chess.gui.PopupMessage;
import entity.AttemptEntity;
import entity.PasswordEntity;
import entity.ResultEntity;
import entity.UserEntity;
import jakarta.persistence.*;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBOperations {
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
    private static EntityManager manager = factory.createEntityManager();
    private static EntityTransaction transaction = manager.getTransaction();

    public static UserEntity createUser(String userName, String email){
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secretKey = key.getKey();

        UserEntity u = new UserEntity();
        u.setUserName(userName);
        u.setLastlyBlocked(null);
        u.setSecretKey(secretKey);
        u.setGmailAdress(email);

        return u;
    }

    public static PasswordEntity createPassword(String userName, String passwordText){
        PasswordEntity p = new PasswordEntity();
        p.setUserId(getUserByName(userName).getUserId());
        p.setPasswordText(passwordText);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        p.setCreationDate(timestamp);

        return p;
    }

    public static String persistUserPasswordPair(String userName, String email, String passwordText){
        UserEntity u = createUser(userName, email);
        persist(u);
        PasswordEntity p = createPassword(userName, passwordText);
        persist(p);

        return u.getSecretKey();
    }

    public static void persist(UserEntity entity){
        try{
            transaction.begin();
            manager.persist(entity);
            transaction.commit();
        }
        catch (PersistenceException e){
            PopupMessage.showErrorMessage("Email or user name are already used", "Error");
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static void persist(PasswordEntity entity){
        try{
            transaction.begin();
            manager.persist(entity);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static void persist(AttemptEntity entity){
        try{
            transaction.begin();
            manager.persist(entity);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static void persist(ResultEntity entity){
        try{
            transaction.begin();
            manager.persist(entity);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static UserEntity getUserByName(String userName){
        try{
            transaction.begin();
            TypedQuery<UserEntity> getUser = manager.createNamedQuery("User.findByUserName",UserEntity.class);
            getUser.setParameter(1,userName);
            List<UserEntity> set = getUser.getResultList();
            transaction.commit();
            if(set.isEmpty()){
                PopupMessage.showErrorMessage("Something went wrong", "Hold on...");
                return null;
            }
            return set.get(0);

        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static boolean isUserBlocked(UserEntity u){
        Timestamp current = new Timestamp(new Date().getTime());
        Timestamp usersLastBlock = u.getLastlyBlocked();
        if(usersLastBlock == null || current.getTime() - usersLastBlock.getTime() >= 15*60*1000){
            return false;
        }
        return true;
    }

    public static void blockUser(int id){
        try{
            transaction.begin();
            UserEntity u = manager.find(UserEntity.class, id);
            u.setLastlyBlocked(new Timestamp(new Date().getTime()));
            manager.merge(u);
            transaction.commit();
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    private static PasswordEntity getRecentPasswordOfUser(int id) {
        try{
            transaction.begin();
            TypedQuery<PasswordEntity> recent = manager.createNamedQuery("Password.mostRecent",PasswordEntity.class);
            recent.setParameter(1,id);
            PasswordEntity password = recent.getResultList().get(0);
            transaction.commit();
            return password;
        }
        finally {
            if(transaction.isActive()){
                transaction.rollback();
            }
        }
    }

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static boolean signIn(String userName, String identifier){
        UserEntity user = getUserByName(userName);
        if(user == null){
            return false;
        }
        if(isUserBlocked(user)){
            PopupMessage.showErrorMessage("User is blocked! try again later!", "Hold on...");
            return false;
        }

        AttemptEntity attempt = new AttemptEntity();
        attempt.setDate(new Timestamp(new Date().getTime()));
        attempt.setUserId(user.getUserId());

        boolean isValid = identifier.equals(getTOTPCode(user.getSecretKey()));
        if(isValid){
            attempt.setAllowed(true);
            persist(attempt);
            return true;
        }

        else if(getRecentPasswordOfUser(user.getUserId()).getPasswordText().equals(identifier)){
            attempt.setAllowed(true);
            persist(attempt);
            return true;
        }
        else{
            attempt.setAllowed(false);
            persist(attempt);
            TypedQuery<AttemptEntity> attemptsEntityTypedQuery =
                    manager.createNamedQuery("Attempt.threeLastEntries",AttemptEntity.class);
            attemptsEntityTypedQuery.setParameter(1,user.getUserId());
            attemptsEntityTypedQuery.setMaxResults(3);
            List<AttemptEntity> attemptsEntityList = attemptsEntityTypedQuery.getResultList();
            if(attemptsEntityList.size()==3){
                Timestamp past = attemptsEntityList.get(2).getDate();
                Timestamp recent = attemptsEntityList.get(0).getDate();
                if(recent.getTime()-past.getTime() < 15*60*1000){
                    blockUser(user.getUserId());
                    PopupMessage.showErrorMessage("User Blocked for 15 minutes", "A bit sus...");
                    return false;
                }
            }
            PopupMessage.showErrorMessage("Entrance was not allowed!", "Hold on...");
            return false;
        }
    }


    public static Object[][] getTopThreeLeaders(){
        TypedQuery<Object[]> query = manager.createNamedQuery("Result.getLeaders", Object[].class);
        query.setMaxResults(3);
        List<Object[]> topThreeCandidates = query.getResultList();

        List<String> userNames = new ArrayList<>();
        String userName;
        for(Object[] o: topThreeCandidates){
            userName = getUsernameByID((int)(o[0]));
            userNames.add(userName);
        }

        Object[][] result = new Object[topThreeCandidates.size()][2];
        int index = 0;
        for(String name: userNames){
            result[index][0] = name;
            index++;
        }

        index = 0;
        for(Object[] o: topThreeCandidates){
            result[index][1] = o[1];
            index++;
        }

        return result;
    }

    private static String getUsernameByID(int id) {
        TypedQuery<String> query = manager.createNamedQuery("User.findNameByID", String.class);
        query.setParameter(1, id);
        String name = query.getSingleResult();
        return name;
    }

    public static long getTotalPointsById(int id){
        TypedQuery<Long> query = manager.createNamedQuery("Result.getTotalPointsById",Long.class);
        query.setParameter(1, id);
        List<Long> result = query.getResultList();
        return result.isEmpty()? 0: result.get(0);
    }
}
