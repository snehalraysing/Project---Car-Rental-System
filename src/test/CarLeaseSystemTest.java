package test;

import entity.Car;
import entity.Customer;
import entity.Lease;
import dao.ICarLeaseRepositoryImpl;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.LeaseNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;


import static org.junit.Assert.*;

public class CarLeaseSystemTest {
    private ICarLeaseRepositoryImpl carLeaseRepo;

    @Before
    public void setUp() {
        // Initialize repository and any required dependencies.
        carLeaseRepo = new ICarLeaseRepositoryImpl();
    }

    // Test case to verify that a car is created successfully.
    @Test
    public void testCarCreation() {
        // Create a car object with all attributes
        Car car = new Car(11, "Sedan", "Camry", 2024, 45.0, "available", 5, 2.5);

        // Mock the database interaction or use an in-memory database
        boolean isCreated = carLeaseRepo.addCar(car);

        // Assert that the car was created successfully
        assertTrue("Car should be created successfully", isCreated);
    }


    // Test case to verify that a lease is created successfully.
    @Test
    public void testLeaseCreation() {
        // Create a car object (ensure this car exists in the database)
        Car car = new Car(1, "Toyota", "Camry", 2020, 45.0, "available", 5, 2.5);

        // Create a customer object (ensure this customer exists in the database)
        Customer customer = new Customer(4, "Liam", "Garcia", "liam.garcia@example.com", "6543210987");

        // Create a lease
        // Set lease dates
        Calendar calendar = Calendar.getInstance();
        Date startDate = new Date(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 7);  // Lease for 7 days
        Date endDate = new Date(calendar.getTimeInMillis());

        Lease lease = carLeaseRepo.createLease(customer.getCustomerID(), car.getVehicleID(), startDate,endDate, "Daily");

        // Assert that the lease was created successfully
        assertNotNull("Lease should be created successfully", lease);
        assertEquals("Lease type should be Daily", "Daily", lease.getType());
        assertEquals("Lease should be for the correct car", car.getVehicleID(), lease.getCar().getVehicleID());
        assertEquals("Lease should be for the correct customer", customer.getCustomerID(), lease.getCustomer().getCustomerID());
    }

    // Test case to verify that a lease is retrieved successfully.
    @Test
    public void testLeaseRetrieval() {
        // Assuming a lease was previously created with ID 1
        Lease lease = carLeaseRepo.listActiveLeases().getLast();
        assertNotNull("Lease should be retrieved successfully", lease);
    }


    public static void main(String[] args) {
        CarLeaseSystemTest test = new CarLeaseSystemTest();
        test.testLeaseRetrieval();
    }
}
