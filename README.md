# hibernate-tutorial
## Introduction to Hibernate

**What is Hibernate ?**
+ A framework for persisting / saving Java objects in a database
    + www.hibernate.org

![image](https://user-images.githubusercontent.com/80107049/188279338-9fbc8719-c053-4d4f-ae70-890896a4665c.png)

**Benefits of Hibernate**
+ Hibernate handles all of the low-level SQL
+ Minimizes the amount of JDBC code you have to develop
+ Hibernate provides the Object-to-Relational Mapping (ORM)

**Object-To-Relational Mapping (ORM)**
+ The developer defines mapping between Java class and database table


![image](https://user-images.githubusercontent.com/80107049/188279346-b4b2815f-ee6f-4d4c-a7e4-4bd954ca1f18.png)

**Saving Java Object with Hibernate**

```JAVA
//create java object
Student theStudent = new Student("John", "Doe", "john@gmail.com");

// save it to database
int theId = (Integer) session.save(theStudent);
```
+ `session` is special Hibernate object
+ `session.save`, Hibernate will store the data into the database.
+ When `seeion.save` is done Hibernate will return the actual id `Return the primary key` that is assign to that entry

**Retrieving a Java Object with Hibernate**
```JAVA
//create java object
Student theStudent = new Student("John", "Doe", "john@gmail.com");

// save it to database
int theId = (Integer) session.save(theStudent);

// now retrieve from database using the primary key 
Student myStudent = session.get(Student.class, theId);
```

+ `session.get` Hibernate will query the table for given id

**Query for Java Objects**
```JAVA
Query query = session.createQuery("from Student");
List<Student> students = query.list();
```
+ `session.createQuery("from Student")` give the list of all student object
+ `query.list()` query the database and returns a list of Student objects from the database
+ `"from Student"` is Hibernate Query Language (HQL)

**Hibernate CRUD Apps**
+ **C**reate objects
+ **R**ead objects
+ **U**pdate objects
+ **D**elete objects

**Hibernate and JDBC**

How does Hibernate relate to JDBC?
+ Hibernate uses JDBC for all database communications

![image](https://user-images.githubusercontent.com/80107049/188279387-a254bd05-2697-4382-a8c2-d3a983b68027.png)

## Hibernate Configuration with Annotations

**Hibernate Development Process - To Do List**
1. Add Hibernate Configuration file
2. Annotate Java Class
3. Develop Java Code to perform database operations

**Configuration File**    

![image](https://user-images.githubusercontent.com/80107049/188279407-621b31d9-1505-42fa-a75d-79d1b9cf0807.png)

```XML
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        
        <!-- JDBC Database connection settings -->
        <property name="connection.driver_class">org.postgresql.Driver</property>
        <property name="connection.url">jdbc:postgresql://ec2-52-30-159-47.eu-west-1.compute.amazonaws.com:5432/d2j5mchnh8vql4</property>
        <property name="connection.username">ouxptxqkhymyap</property>
        <property name="connection.password">b597e7d85a71150fb92ecdeeba9195877f2367dfd9ed04c0011db39400eabd4e</property>

        <!-- JDBC connection pool settings ... using build-in test pool -->
        <property name="connection.pool_size">1</property>

        <!-- Select our SQL dialect -->
        <property name="dialect">org.hibernate.dialect.PostgreSQLDialect</property>

        <!-- Echo the SQL to stdout-->
        <property name="show_sql">true</property>

        <!-- Set the current session context-->
        <property name="current_session_context_class">thread</property>
        

    </session-factory>
</hibernate-configuration>
```

**Hibernate Annotations**

As of terminology Hibernate has its concept of **Entity Class**, Java class that is mapped to a database table.

_Two Options for Mapping_
+ Option 1: XML config file (legacy)
+ Option 2: Java Annotations (modern, preferred)

_Steps To Java Annotations_
+ Step 1: Map class to database table
+ Step 2: Map fields to database columns

_Step 1:Map class to database table_
```JAVA
@Entity
@Table(name="student")
public class Student {
  
  ...
}
```
+ `@Entity` annotation is a marker annotation, which is used to discover persistent entities.
+ By default, this entity will be mapped to the student table, as determined by the given class name. If you wanted to map this entity to another table (and, optionally, a specific schema) you could use the @Table annotation to do that.



_Step 2: Map fields to database columns_
```JAVA
@Entity
@Table(name="student")
public class Student {
  
  @Id
  @Column(name="id")
  private int id;
  
  @Column(name="first_name")
  private String firstName;
  
  ...
}
```
+ `id` is primary key 


## Hibernate CRUD Features: Create, Read, Update and Delete

**Two Key Players**

| Class             | Description                                                                                                                    |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| **SessonFactory** | Reads the hibernate config file <br>Creates Session objects <br> Heavy-weight object <br>Only create once in your app          |
| **Session**       | Wraps a JDBC connection <br>Main object used to save/retrieve objects <br>Short-lived object <br>Retrieved from SessionFactory |

**Java Code Setup**
```Java
public static void main(String[] args) {
  
  SessionFactory factory = new Configuration()
                           .configure("hibernate.cfg.xml")
                           .addAnnotatedClass(Student.class)
                           .buildSessionFactory();
  
  Session session = factory.getCurrentSession();
  
  try {
    
    // now use the session object to save/retrieve Java objects
    
  } finally{
    factory.close();
  }
  
}
```
+ `.configure("hibernate.cfg.xml")`
    + If file name not given Hibernate will look for default file name: hibernate.cfg.xml

### Create object

**Save a Java Object**
```Java
  try {
    
    // create a student object
    Student tempStudent = new Student("Paul", "Wall", "paul@gmail.com");
    
    // start transaction
    session.beginTransaction();
    
    // save the student
    session.save(tempStudent);
    
    // commit the transaction
    session.getTransaction().commit();
    
  } finally {
    factory.close();
  }
```

**Primary Key**

+ Uniquely identify each row in a table
+ Must be a unique value
+ Cannot contain NULL value

**PostgreSql - Auto Increment**
```SQL
CREATE TABLE student(
    id BIGSERIAL NOT NULL PRIMARY KEY  ,
    first_name VARCHAR(45) DEFAULT NULL,
    last_name VARCHAR(45) DEFAULT NULL,
    email VARCHAR(45) DEFAULT NULL
);
```
+ BIGSERIAL will handle auto-incrementing

**Hibernate Identity - Primary Key**

```JAVA
@Entity
@Table(name = "student")
public class Student {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id")
  private int id;
 
  ...
}
```
+ `@Id` tells hibernate that given field is Primary key for the class
+  if you want to be explicit, you can tell Hibernate how to actually perform the generation.
    + If we don't specify anything, by default it'll use the appropriate strategy for that given database implementation.
    + For to be explicit and write it out long hand we use  `@GeneratedValue(strategy=GenerationType.IDENTITY)`

**ID Generation Strategies**

| Name                        | Description                                                                 |
| --------------------------- | --------------------------------------------------------------------------- |
| **GenerationType.AUTO**     | Pick an appropriate strategy for the particular database                    |
| **GenerationType.IDENTITY** | Assign primary keys using database identity column                          |
| **GenerationType.SEQUENCE** | Assign primary keys using a database sequence                               |
| **GenerationType.TABLE**    | Assign primary keys using an underlying database table to ensure uniqueness |

+ We can define own CUSTOM generation strategy
+ Create implementation of **org.hibernate.id.IdentifierGenerator**
+ Override the method: **public Serializable generate(...)**
    + Add custom business logic

> WARNING
>> ALWAYS generate unique value
>>
>> Work in high-volume, multi-threaded environment
>>
>> If using server clusters, always generate unique Value
>>


### Read objects

**Retrieving a Java Object with Hibernate**
```JAVA
// create java object
Student thestudent = new Student("Daffy", "Duck", "daffy@gmail.com");

// save it to database 
session.save(theStudent);
...
  
// now retrieve/read from database using the primary key
Student theStudent = 
            session.get(Student.class, theStudent.getId());
```
+ If not found, it will return null

### Query Object

**Hibernate Query Language (HQL)**
+ Query language for retrieving objects
+ Similar in nature to SQL
    + where
    + like
    + oder by
    + join
    + in
    + etc ...

**Retrieving all Student**
```JAVA
List<Student> theStudents = session
                           .createQuery("from Student")
                           .getResultList();
```
+ In `.createQuery("from Student")`, "from Student" is the use of Java class name

**Retrieving Students: lastName = "Doe"**
```JAVA
List<Student> theStudents = session
                           .createQuery("from Student s where s.lastName='Doe'")
                           .getResultList();
```
+ `s`is alias and `s.lastName` is the Java property name (not column name)




**Retrieving Students using OR predicate:**
```JAVA
List<Student> theStudents = session
                           .createQuery("from Student s where s.lastName='Doe'"
                                        + "OR s.firstName='Daffy'")
                           .getResultList();
```


**Retrieving Students using LIKE predicate:**
```JAVA
List<Student> theStudent = session
                          .crateQuery("from Student s where"
                                      + "s.email LIKE %gmail.com")
                          .getResultList();
```
>Special Note about Deprecated Method in Hibernate 5.2
>> If you are using Hibernate 5.2 or higher, then the Query list() method has been deprecated.
>>
>> In your code you should make the following update:
>>
>> **Replace**
>>
>> `session.createQuery("from Student").list()`
>>
>> **With**
>>
>> `session.createQuery("from Student").getResultList()`


### Update objects

```JAVA
int studentId = 1;

Student myStudent = session.get(Student.class, studentId);

// update first name to "Scooby"
myStudent.setFirstName("Scooby");

// commit the transacion
session.getTransaction().commit();
```

**Update email for all students**
```JAVA
sesion
    .createQuery("update Student set email='foo@gmail.com'")
    .executeUpdate();
```

### Delete objects

```JAVA
int studentId = 2;

Student myStundent = session.get(Studnet.class, stundentId);

// delete the stundet
session.delete(myStudnet);

// commit the transction
session.getTransaction().commit();
```

**Another way of deleting**
```JAVA
session
    .createQuery("delete from Student where id=2")
    .executeUpdate();
```
+ `executeUpdate()` used for updates OR deletes

