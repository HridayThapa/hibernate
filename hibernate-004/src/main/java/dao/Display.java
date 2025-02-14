package dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import entities.Student;

public class Display {
    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            // Load Hibernate using hibernate.properties (No XML)
        	factory = new Configuration().addAnnotatedClass(Student.class).buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object: " + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Display obj = new Display();

        // Add students
        Integer ID1 = obj.addStudent("Alex", 1);
        Integer ID2 = obj.addStudent("Sam", 55);
        Integer ID3 = obj.addStudent("Zara", 100);
        Integer ID4 = obj.addStudent("John", 25);

        // Display all students
        obj.listStudents();

        // Delete one student
        obj.deleteStudent(ID4);

        // Display students again after deletion
        obj.listStudents();
    }

    // Method to add a student
    public Integer addStudent(String name, int roll) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer ID = null;

        try {
            tx = session.beginTransaction();
            Student student = new Student(name, roll);
            ID = (Integer) session.save(student);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ID;
    }

    // Method to list all students
    public void listStudents() {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<Student> students = session.createQuery("FROM Student", Student.class).list();
            for (Student student : students) {
                System.out.println("ID: " + student.getId());
                System.out.println("Name: " + student.getName());
                System.out.println("Roll No: " + student.getRoll());
                System.out.println("-----------------------------");
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Method to delete a student by ID
    public void deleteStudent(Integer id) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.delete(student);
                System.out.println("Deleted student with ID: " + id);
            } else {
                System.out.println("Student not found with ID: " + id);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
