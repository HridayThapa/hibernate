package dao;

import java.util.Iterator;
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
	         factory = new Configuration().configure().buildSessionFactory();
	      } catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	      }
	      Display obj=new Display();
	      Integer ID1 = obj.addStudent("Alex",1);
	      Integer ID2 = obj.addStudent("Sam", 55);
	      Integer ID3 = obj.addStudent("Zara", 100);
	      Integer ID4 = obj.addStudent("John", 25);
	      
	      obj.listStudents();
	      
	      obj.deleteStudent(ID4);
	      
	      obj.listStudents();
	}
	public Integer addStudent(String name, int roll){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      Integer ID = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Student student = new Student(name, roll);
	         ID = (Integer) session.save(student); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	      return ID;
	   }
	 public void listStudents( ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         List students = session.createQuery("FROM Student").list(); 
	         for (Iterator iterator = students.iterator(); iterator.hasNext();){
	            Student student= (Student) iterator.next(); 
	            System.out.println("Name: " + student.getName()); 
	            System.out.println("  Roll no: " + student.getRoll()); 
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	   }
	 public void deleteStudent(Integer roll){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Student student = (Student)session.get(Student.class, roll); 
	         session.delete(student); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	   }
}
