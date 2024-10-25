package main;

import dao.ICarLeaseRepository;
import dao.ICarLeaseRepositoryImpl;
import entity.Car;
import entity.Customer;
import entity.Lease;
import entity.Payment;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import exception.LeaseNotFoundException;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    public static void main(String[] args) throws CarNotFoundException, CustomerNotFoundException, LeaseNotFoundException {
        ICarLeaseRepository carLeaseRepo = new ICarLeaseRepositoryImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Car Lease Management System");
            System.out.println("1. Add Car");
            System.out.println("2. Remove Car");
            System.out.println("3. List Available Cars");
            System.out.println("4. List Rented Cars");
            System.out.println("5. Find Car by ID");
            System.out.println("6. Add Customer");
            System.out.println("7. Remove Customer");
            System.out.println("8. List Customers");
            System.out.println("9. Find Customer by ID");
            System.out.println("10. Create Lease");
            System.out.println("11. Return Car");
            System.out.println("12. List Active Leases");
            System.out.println("13. List Lease History");
            System.out.println("14. Payment Record");
            System.out.println("15. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: // Add Car
                    System.out.print("Enter Make: ");
                    String make = scanner.nextLine();
                    System.out.print("Enter Model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter Year: ");
                    int year = scanner.nextInt();
                    System.out.print("Enter Daily Rate: ");
                    double dailyRate = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    System.out.print("Enter Status (available/notAvailable): ");
                    String status = scanner.nextLine();
                    System.out.print("Enter Passenger Capacity: ");
                    int passengerCapacity = scanner.nextInt();
                    System.out.print("Enter Engine Capacity: ");
                    double engineCapacity = scanner.nextDouble();
                    Car newCar = new Car(0, make, model, year, dailyRate, status, passengerCapacity, engineCapacity);
                    carLeaseRepo.addCar(newCar);
                    System.out.println("Car added successfully.");
                    break;

                case 2: // Remove Car
                    System.out.print("Enter Car ID to remove: ");
                    int carID = scanner.nextInt();
                    carLeaseRepo.removeCar(carID);
                    System.out.println("Car removed successfully.");
                    break;

                case 3: // List Available Cars
                    List<Car> availableCars = carLeaseRepo.listAvailableCars();
                    System.out.println("Available Cars:");
                    for (Car car : availableCars) {
                        System.out.println(car.getMake() + " " + car.getModel() + " - " + car.getYear());
                    }
                    break;

                case 4: // List Rented Cars
                    List<Car> rentedCars = carLeaseRepo.listRentedCars();
                    System.out.println("Rented Cars:");
                    for (Car car : rentedCars) {
                        System.out.println(car.getMake() + " " + car.getModel() + " - " + car.getYear());
                    }
                    break;

                case 5: // Find Car by ID
                    System.out.print("Enter Car ID to find: ");
                    try {
                        carID = scanner.nextInt();
                        Car foundCar = carLeaseRepo.findCarById(carID);
                        System.out.println("Car Found: " + foundCar.getMake() + " " + foundCar.getModel());
                    } catch (CarNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6: // Add Customer
                    System.out.print("Enter First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String phoneNumber = scanner.nextLine();
                    Customer newCustomer = new Customer(0, firstName, lastName, email, phoneNumber);
                    carLeaseRepo.addCustomer(newCustomer);
                    System.out.println("Customer added successfully.");
                    break;

                case 7: // Remove Customer
                    System.out.print("Enter Customer ID to remove: ");
                    int customerID = scanner.nextInt();
                    carLeaseRepo.removeCustomer(customerID);
                    System.out.println("Customer removed successfully.");
                    break;

                case 8: // List Customers
                    List<Customer> customers = carLeaseRepo.listCustomers();
                    System.out.println("Customers:");
                    for (Customer customer : customers) {
                        System.out.println(customer.getFirstName() + " " + customer.getLastName());
                    }
                    break;

                case 9: // Find Customer by ID
                    System.out.print("Enter Customer ID to find: ");
                    try {
                        customerID = scanner.nextInt();
                        Customer foundCustomer = carLeaseRepo.findCustomerById(customerID);
                        System.out.println("Customer Found: " + foundCustomer.getFirstName() + " " + foundCustomer.getLastName());
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 10: // Create Lease
                    System.out.print("Enter Customer ID: ");
                    int custID = scanner.nextInt();
                    System.out.print("Enter Car ID: ");
                    int vID = scanner.nextInt();
                    System.out.print("Enter Start Date (yyyy-mm-dd): ");
                    String startDateStr = scanner.next();
                    System.out.print("Enter End Date (yyyy-mm-dd): ");
                    String endDateStr = scanner.next();
                    System.out.print("Enter Lease Type (Daily/Monthly): ");
                    String leaseType = scanner.next();
                    Date startDate = Date.valueOf(startDateStr);
                    Date endDate = Date.valueOf(endDateStr);
                    Lease lease = carLeaseRepo.createLease(custID, vID, startDate, endDate, leaseType);
                    System.out.println("Lease created successfully. Lease ID: " + lease.getLeaseID());
                    break;

                case 11: // Return Car //leaseID was not directly deleted bcaz of foreign key relation with payment table
                    System.out.print("Enter Lease ID to return: ");
                    int leaseID = scanner.nextInt();
                    carLeaseRepo.returnCar(leaseID);
                    System.out.println("Car returned successfully.");
                    break;

                case 12: // List Active Leases
                    List<Lease> activeLeases = carLeaseRepo.listActiveLeases();
                    System.out.println("Active Leases:");
                    for (Lease activeLease : activeLeases) {
                        System.out.println("Lease ID: " + activeLease.getLeaseID() +
                                ", Customer ID: " + activeLease.getCustomer().getCustomerID() +
                                ", Vehicle ID: " + activeLease.getCar().getVehicleID());
                    }
                    break;


                case 13: //List lease history
                    List<Lease> leaseHistory = carLeaseRepo.listLeaseHistory();

                    if (!leaseHistory.isEmpty()) {
                        System.out.println("Lease History:");
                        for (Lease leasehist : leaseHistory) {
                            System.out.println("Lease ID: " + leasehist.getLeaseID());
                            System.out.println("Car: " + leasehist.getCar().getMake() + " " + leasehist.getCar().getModel());
                            System.out.println("Customer: " + leasehist.getCustomer().getFirstName() + " " + leasehist.getCustomer().getLastName());
                            System.out.println("Start Date: " + leasehist.getStartDate());
                            System.out.println("End Date: " + leasehist.getEndDate());
                            System.out.println("Lease Type: " + leasehist.getType());
                            System.out.println("---------------");
                        }
                    } else {
                        System.out.println("No lease history available.");
                    }
                    break;

               case 14:  //record payment

                   // Get Lease ID from the user
                   System.out.print("Enter Lease ID: ");
                   int lease_ID = scanner.nextInt();

                   // Get payment amount from the user
                   System.out.print("Enter payment amount: ");
                   double paymentAmount = scanner.nextDouble();

                   // Simulate fetching a Lease object (in practice, you would fetch this from the database)
                   Lease lease1 = new Lease();
                   lease1.setLeaseID(lease_ID); // Set the user-input Lease ID

                   // Record the payment
                   carLeaseRepo.recordPayment(lease1, paymentAmount);
                   break;

                case 15:  //Exit
                    System.out.println("Exiting the application.");
                    scanner.close();
                    return;


                    default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
