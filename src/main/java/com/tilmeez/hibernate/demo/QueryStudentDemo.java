package com.tilmeez.hibernate.demo;

import com.tilmeez.hibernate.demo.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;


public class QueryStudentDemo {

    public static void main(String[] args) {

        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // query student
            List<Student> theStudent = session
                    .createQuery("from Student").getResultList();


            // display the student
            displayStudnets(theStudent);

            // query students: lastName='Doe'
            theStudent = session
                    .createQuery("from Student s where s.lastName='Doe'").list();

            // display student
            System.out.println("\nStudent who have last name of Doe");
            displayStudnets(theStudent);

            // query students: lastName 'Doe' OR firstName: 'Daffy'
            theStudent = session
                    .createQuery("from Student s where " +
                            "s.lastName='Doe' OR s.firstName='Daffy'").list();

            // display student
            System.out.println("\nStudent who have last name of Doe OR firstName Daffy");
            displayStudnets(theStudent);

            // query students: where email LIKE '%gmail.com
            theStudent = session
                    .createQuery("from Student s where " +
                            "s.email LIKE '%gmail.com'").list();

            // display student
            System.out.println("\nStudent who have gmail email");
            displayStudnets(theStudent);


            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");

        }finally {
            factory.close();
        }
    }

    private static void displayStudnets(List<Student> theStudent) {
        for (Student temStudent : theStudent) {
            System.out.println(temStudent);
        }
    }
}
