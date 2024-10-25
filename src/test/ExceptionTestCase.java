package test;
import dao.ICarLeaseRepository;
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

import static org.junit.Assert.fail;

public class ExceptionTestCase {
    private ICarLeaseRepositoryImpl carLeaseRepo;

    @Before
    public void setUp() {
        // Initialize repository and any required dependencies.
        carLeaseRepo = new ICarLeaseRepositoryImpl();
    }

    // Test case to verify that exception is thrown when customer ID is not found.
    @Test(expected = CustomerNotFoundException.class)
    public void testCustomerNotFoundException() throws CustomerNotFoundException {
        // Attempt to find a customer with a non-existing ID
        carLeaseRepo.findCustomerById(99);
    }

    // Test case to verify that exception is thrown when car ID is not found.
    @Test(expected = CarNotFoundException.class)
    public void testCarNotFoundException() throws CarNotFoundException {
        // Attempt to find a car with a non-existing ID
        carLeaseRepo.findCarById(666);
    }



}
