package com.example.restservicedemo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.restservicedemo.domain.Car;
import com.example.restservicedemo.domain.Person;

public class PersonManager {

	private Connection connection;

	private static final String URL = "jdbc:hsqldb:hsql://localhost/workdb";
	private static final String CREATE_TABLE_PERSON = "CREATE TABLE Person(p_id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY, name varchar(20), yob integer)";
	private static final String CREATE_TABLE_CAR = "CREATE TABLE Car(c_id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY, model varchar(20), yop integer, owner_id bigint FOREIGN KEY references Person(p_id))";

	private PreparedStatement addPersonStmt;
	private PreparedStatement addCarStmt;
	private PreparedStatement sellCarStmt;
	private PreparedStatement deleteAllPersonsStmt;
	private PreparedStatement getAllPersonsStmt;
	private PreparedStatement getPersonByIdStmt;
	private PreparedStatement getAllPersonsWithCarsStmt;
	private PreparedStatement getCarWithOwnerStmt;
	private PreparedStatement getAllCarsStmt;
	private PreparedStatement addPersonWithIdStmt;
	private PreparedStatement addCarWithIdStmt;
	private PreparedStatement getCarByIdStmt;
	private PreparedStatement deleteAllCarsStmt;
	private Statement statement;

	public PersonManager() {
		try {
			connection = DriverManager.getConnection(URL);
			statement = connection.createStatement();

			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			boolean tableExists = false;
			while (rs.next()) {
				if ("Person".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableExists = true;
					break;
				}
			}

			if (!tableExists)
				statement.executeUpdate(CREATE_TABLE_PERSON);

			rs = connection.getMetaData().getTables(null, null, null, null);
			tableExists = false;
			while (rs.next()) {
				if ("Car".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableExists = true;
					break;
				}
			}

			if (!tableExists)
				statement.executeUpdate(CREATE_TABLE_CAR);

			addPersonStmt = connection.prepareStatement("INSERT INTO Person (name, yob) VALUES (?, ?)");
			addPersonWithIdStmt = connection.prepareStatement("INSERT INTO Person (p_id, name, yob) VALUES (?, ?, ?)");
			addCarStmt = connection.prepareStatement("INSERT INTO Car (model, yop) VALUES (?, ?)");
			addCarWithIdStmt = connection.prepareStatement("INSERT INTO Car (c_id, model, yop) VALUES (?, ?, ?)");
			deleteAllPersonsStmt = connection.prepareStatement("DELETE FROM Person");
			deleteAllCarsStmt = connection.prepareStatement("DELETE FROM Car");
			
			
			getAllPersonsStmt = connection.prepareStatement("SELECT p_id, name, yob FROM Person");
			getPersonByIdStmt = connection.prepareStatement("SELECT p_id, name, yob FROM Person where p_id = ?");
			
			getCarByIdStmt = connection.prepareStatement("SELECT c_id, model, yop FROM Car where c_id = ?");
			sellCarStmt = connection.prepareStatement("UPDATE Car SET owner_id = ? WHERE c_id = ?");
			getAllPersonsWithCarsStmt = connection.prepareStatement("SELECT p_id, name, yob, c_id, model, yop, owner_id FROM Person JOIN Car ON owner_id = p_id");
			getCarWithOwnerStmt = connection.prepareStatement("SELECT p_id, name, yob, c_id, model, yop, owner_id FROM Person JOIN Car ON c_id = ?");
			getAllCarsStmt = connection.prepareStatement("SELECT c_id, model, yop FROM Car");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	Connection getConnection() {
		return connection;
	}

	public void clearPersons() {
		try {
			deleteAllPersonsStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int addPerson(Person person) {
		int count = 0;
		try {
			addPersonStmt.setString(1, person.getFirstName());
			addPersonStmt.setInt(2, person.getYob());

			count = addPersonStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int addPersonWithId(Person person) {
		int count = 0;
		try {
			addPersonWithIdStmt.setLong(1, person.getId());
			addPersonWithIdStmt.setString(2, person.getFirstName());
			addPersonWithIdStmt.setInt(3, person.getYob());
			count = addPersonWithIdStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<Person> getAllPersons() {
		List<Person> persons = new ArrayList<Person>();

		try {
			ResultSet rs = getAllPersonsStmt.executeQuery();

			while (rs.next()) {
				Person p = new Person();
				p.setId(rs.getInt("p_id"));
				p.setFirstName(rs.getString("name"));
				p.setYob(rs.getInt("yob"));
				persons.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return persons;
	}

	public Person getPerson(Long id) {
		Person p = new Person();
		try {
			getPersonByIdStmt.setLong(1, id);
			ResultSet rs = getPersonByIdStmt.executeQuery();

			while (rs.next()) {
				p.setId(rs.getInt("p_id"));
				p.setFirstName(rs.getString("name"));
				p.setYob(rs.getInt("yob"));
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return p;
	}

	public int addCar(Car car) {
		int count = 0;
		try {
			addCarStmt.setString(1, car.getModel());
			addCarStmt.setInt(2, car.getYop());

			count = addCarStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int addCarWithId(Car car) {
		int count = 0;
		try {
			addCarWithIdStmt.setLong(1, car.getId());
			addCarWithIdStmt.setString(2, car.getModel());
			addCarWithIdStmt.setInt(3, car.getYop());

			count = addCarWithIdStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public Map<Person, List<Car>> getPersonWithCar() {

		List<Car> cars = new ArrayList<Car>();

		Map<Person, List<Car>> result = new HashMap<>();

		try {
			ResultSet rs = getAllPersonsWithCarsStmt.executeQuery();

			while (rs.next()) {

				Person p = new Person();

				p.setId(rs.getInt("p_id"));
				p.setFirstName(rs.getString("name"));
				p.setYob(rs.getInt("yob"));

				Car c = new Car();
				c.setId(rs.getInt("c_id"));
				c.setModel(rs.getString("model"));
				c.setYop(rs.getInt("yop"));

				c.setOwner(p);

				if (result.containsKey(p)) {
					cars = result.get(p);
					cars.add(c);
				} else {
					cars = new ArrayList<>();
					cars.add(c);
					result.put(p, cars);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Car getCarWithOwner(Car car) {

		Car c = new Car();
		try {
			
			getCarWithOwnerStmt.setLong(1, car.getId());
			ResultSet rs = getCarWithOwnerStmt.executeQuery();

			while (rs.next()) {

				Person p = new Person();

				p.setId(rs.getInt("p_id"));
				p.setFirstName(rs.getString("name"));
				p.setYob(rs.getInt("yob"));

				c.setId(rs.getInt("c_id"));
				c.setModel(rs.getString("model"));
				c.setYop(rs.getInt("yop"));

				c.setOwner(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public int sellCar(Car car, Person person) {
		int count = 0;
		try {
			
			sellCarStmt.setLong(1, person.getId());
			sellCarStmt.setLong(2, car.getId());
			

			count = sellCarStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public List<Car> getAllCars() {
		
		List<Car> cars = new ArrayList<Car>();

		try {
			ResultSet rs = getAllCarsStmt.executeQuery();

			while (rs.next()) {
				Car c = new Car();
				c.setId(rs.getInt("c_id"));
				c.setModel(rs.getString("model"));
				c.setYop(rs.getInt("yop"));
				cars.add(c);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cars;
	}

	public Car getCar(Long id) {
		Car c = new Car();
		try {
			getCarByIdStmt.setLong(1, id);
			ResultSet rs = getCarByIdStmt.executeQuery();

			while (rs.next()) {
				c.setId(rs.getInt("c_id"));
				c.setModel(rs.getString("model"));
				c.setYop(rs.getInt("yob"));
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return c;
	}

	public void clearCars() {
		try {
			deleteAllCarsStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

}