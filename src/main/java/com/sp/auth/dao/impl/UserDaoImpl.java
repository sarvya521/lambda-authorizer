package com.sp.auth.dao.impl;

import com.sp.auth.util.HibernateUtil;
import com.sp.auth.dao.UserDao;
import com.sp.auth.dto.GhAuthenticatedUser;
import com.sp.auth.entity.User;
import com.sp.auth.util.AuthUtil;
import com.sp.auth.util.JwtTokenUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
public class UserDaoImpl implements UserDao {
    @Override
    public GhAuthenticatedUser findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Query<User> query = session.createQuery("FROM User WHERE email =:email", User.class);
            query.setParameter("email", email);
            final User user = query.getSingleResult();
            return AuthUtil.prepareUserDto(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//        UserDao userDao = new UserDaoImpl();
//        final SpAuthenticatedUser user = userDao.findByEmail("sarvya521@github.oktaidp");
//        System.out.println(GSON.toJson(user));
//
//        String xAuthToken = JwtTokenUtil.generateXAuthToken(user);
//        System.out.println(xAuthToken);
//        xAuthToken = xAuthToken.replace("Bearer ", "");
//        System.out.println(JwtTokenUtil.getUsername(xAuthToken));
//        System.out.println(GSON.toJson(JwtTokenUtil.getUserDetails(xAuthToken)));
//    }
}
