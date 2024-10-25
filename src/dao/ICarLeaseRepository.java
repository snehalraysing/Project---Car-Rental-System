package dao;

import entity.*;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.LeaseNotFoundException;

import java.sql.Date;
import java.util.List;

public interface ICarLeaseRepository {

    // Car Management
    boolean addCar(Car car);
    void removeCar(int carID) throws CarNotFoundException;
    List<Car> listAvailableCars();
    List<Car> listRentedCars();
    Car findCarById(int carID) throws CarNotFoundException;



    // Customer Management
    void addCustomer(Customer customer);
    void removeCustomer(int customerID) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    Customer findCustomerById(int customerID) throws CustomerNotFoundException;


    //Lease Management
    Lease createLease(int customerID, int vehicleID, Date startDate, Date endDate, String leaseType);

    void returnCar(int leaseID) throws LeaseNotFoundException;
    List<Lease> listActiveLeases();
    List<Lease> listLeaseHistory();



    // Payment Handling
    void recordPayment(Lease lease, double amount);
}
