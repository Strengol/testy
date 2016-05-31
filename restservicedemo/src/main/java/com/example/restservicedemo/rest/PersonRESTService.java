package com.example.restservicedemo.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.restservicedemo.domain.Car;
import com.example.restservicedemo.domain.Person;
import com.example.restservicedemo.service.PersonManager;

@Path("person")
public class PersonRESTService {	
	
	private PersonManager pm = new PersonManager();
	
	@GET
	@Path("/{personId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Person getPerson(@PathParam("personId") Long id){
		Person p = pm.getPerson(id);
		return p;
	}
	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Person> getAll(){
		return pm.getAllPersons();
	}
	
	@GET
	@Path("/getCar/{carId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Car getCar(@PathParam("carId") Long id){
		Car c = pm.getCar(id);
		return c;
	}
	
	@GET
	@Path("/car/{carId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Car getCars(@PathParam("carId") Long id){
		Car c = new Car();
		c.setId(id);
		Car car = pm.getCarWithOwner(c);
		return car;
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPerson(Person person){
		pm.addPerson(person);
		return Response.status(201).entity("Person").build(); 
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPersonWithId(Person person){
		pm.addPersonWithId(person);
		return Response.status(201).entity("Person").build(); 
	}
	
	@POST
	@Path("/addCarWithId")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCarWithId(Car car){
		pm.addCarWithId(car);
		return Response.status(201).entity("Car").build(); 
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test(){
		return "REST API /person is running";
	}
	
	@DELETE
	@Path("/deletePersons")
	public Response clearPersons(){
		pm.clearPersons();
		return Response.status(200).build();
	}
	
	@DELETE
	@Path("/deleteCars")
	public Response clearCars(){
		pm.clearCars();
		return Response.status(200).build();
	}
	

}