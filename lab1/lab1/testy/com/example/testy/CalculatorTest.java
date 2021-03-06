package com.example.testy;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CalculatorTest {

	Calculator calc = new Calculator();
	
	
//	@BeforeClass
//	public static void globalSetub(){
//		System.out.println("Before - global");
//	}
//	@Ignore
//	@AfterClass
//	public static void globalTearDown(){
//		System.out.println("After - global");
//	}
//	
//	@Before
//	public void setUp(){
//		System.out.println("BEFORE ");
//	}
//	
//	@After
//	public void terDown(){
//		System.out.println("After ");
//	}
	
	@Test
	public void checkAdding(){
		assertEquals(5,calc.add(2, 3));
		System.out.println("ADDING "+calc);
	}
	@Test
	public void checkSub(){
		assertEquals(2,calc.sub(5, 3));
		System.out.println("SUB "+calc);
	}

	@Test
	public void checkMulti(){
		assertEquals(4,calc.multi(2, 2));
		System.out.println("multi "+calc);
	}
	@Test
	public void checkDiv(){
		assertEquals(5,calc.div(5, 1));
		System.out.println("div "+calc);
	}
	
	@Test(expected = ArithmeticException.class)
	public void checkSubExpection(){
		calc.div(5, 0);
	}
	
	@Test
	public void checkGreaterTrue(){
		assertTrue("failure - should be true",calc.greater(5, 3));
		System.out.println("greater true "+calc);
	}
	@Test
	public void checkGreaterFalse(){
		assertFalse("failure - should be true",calc.greater(5, 7));
		System.out.println("greater false "+calc);
	}
	
	@Ignore
	@Test
	public void checkIgnore(){
		assertEquals(5,calc.add(2, 3));
		System.out.println("IGNORED "+calc);
	}
}
