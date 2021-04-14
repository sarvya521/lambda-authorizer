package com.sp.auth.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;
/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                //settings.put(Environment.URL, "jdbc:mysql://demo.c6u4wgk0kfk6.us-east-2.rds.amazonaws.com:3306/demo?useSSL=false");
                //settings.put(Environment.USER, "guardantuser");
                //settings.put(Environment.PASS, "guardant123");
		 settings.put(Environment.URL, System.getenv("datasource_url"));
                settings.put(Environment.USER, System.getenv("datasource_username"));
                settings.put(Environment.PASS, System.getenv("datasource_password"));
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                settings.put(Environment.SHOW_SQL, "false");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "none");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(com.sp.auth.entity.Claim.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.Module.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.ModuleFeatures.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.ModuleFeaturesClaim.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.Organization.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.Role.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.RoleFeatures.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.Tenant.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.User.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.UserRole.class);
                configuration.addAnnotatedClass(com.sp.auth.entity.UserTenant.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
