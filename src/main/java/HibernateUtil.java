import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
 
import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    final static String hibernateProp = "hibernate.properties";

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Properties prop = new Properties();
                prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(hibernateProp));
                sessionFactory = new Configuration()
                        .addProperties(prop)
                        .addAnnotatedClass(User.class)
                        .buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
