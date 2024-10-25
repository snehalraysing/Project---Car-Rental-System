package dao;

import entity.Car;
import entity.Customer;
import entity.Lease;
import exception.CarNotFoundException;
import exception.CustomerNotFoundException;
import util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ICarLeaseRepositoryImpl implements ICarLeaseRepository {
    private Connection connection;

    public ICarLeaseRepositoryImpl() {
        this.connection = DBConnUtil.getConnection();
    }

    // Car Management
    @Override
    public boolean addCar(Car car) {
        String query = "INSERT INTO Vehicle (make, model, year, dailyRate, status, passengerCapacity, engineCapacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, car.getMake());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getDailyRate());
            stmt.setString(5, car.getStatus());
            stmt.setInt(6, car.getPassengerCapacity());
            stmt.setDouble(7, car.getEngineCapacity());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeCar(int carID) {
        String query = "DELETE FROM Vehicle WHERE vehicleID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> listAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE status = 'available'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                availableCars.add(new Car(
                        rs.getInt("vehicleID"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("dailyRate"),
                        rs.getString("status"),
                        rs.getInt("passengerCapacity"),
                        rs.getDouble("engineCapacity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableCars;
    }

    @Override
    public List<Car> listRentedCars() {
        List<Car> rentedCars = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE status = 'notAvailable'";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rentedCars.add(new Car(
                        rs.getInt("vehicleID"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("dailyRate"),
                        rs.getString("status"),
                        rs.getInt("passengerCapacity"),
                        rs.getDouble("engineCapacity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedCars;
    }

    @Override
    public Car findCarById(int carID) throws CarNotFoundException {
        String query = "SELECT * FROM Vehicle WHERE vehicleID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, carID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Car(
                        rs.getInt("vehicleID"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("dailyRate"),
                        rs.getString("status"),
                        rs.getInt("passengerCapacity"),
                        rs.getDouble("engineCapacity")
                );
            } else {
                throw new CarNotFoundException("Car with ID " + carID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Customer Management
    @Override
    public void addCustomer(Customer customer) {
        String query = "INSERT INTO Customer (firstName, lastName, email, phoneNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCustomer(int customerID) {
        String query = "DELETE FROM Customer WHERE customerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> listCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("customerID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Customer findCustomerById(int customerID) throws CustomerNotFoundException {
        String query = "SELECT * FROM Customer WHERE customerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customerID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")
                );
            } else {
                throw new CustomerNotFoundException("Customer with ID " + customerID + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Lease Management
    @Override
    public Lease createLease(int customerID, int vehicleID, Date startDate, Date endDate, String leaseType) {
        // Validate lease type
        if (!leaseType.equalsIgnoreCase("Daily") && !leaseType.equalsIgnoreCase("Monthly")) {
            throw new IllegalArgumentException("Invalid lease type. Please enter either 'Daily' or 'Monthly'.");
        }

        Lease lease = null;
        String insertLeaseQuery = "INSERT INTO Lease (vehicleID, customerID, startDate, endDate, type) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertLeaseQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, vehicleID);
            stmt.setInt(2, customerID);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            stmt.setString(5, leaseType);  // Use the validated lease type

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int leaseID = generatedKeys.getInt(1);

                        // Retrieve the inserted lease to return
                        lease = new Lease();
                        lease.setLeaseID(leaseID);
                        lease.setCar(findCarById(vehicleID));  // Fetch Vehicle object
                        lease.setCustomer(findCustomerById(customerID));  // Fetch Customer object
                        lease.setStartDate(startDate);
                        lease.setEndDate(endDate);
                        lease.setType(leaseType);  // Set type as either 'Daily' or 'Monthly'
                    }
                } catch (CarNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (CustomerNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lease;
    }



    @Override
    public void returnCar(int leaseID) {

        String deletePaymentQuery = "DELETE FROM Payment WHERE leaseID = ?";
        String deleteLeaseQuery = "DELETE FROM Lease WHERE leaseID = ?";
        try (PreparedStatement paymentStmt = connection.prepareStatement(deletePaymentQuery);
             PreparedStatement leaseStmt = connection.prepareStatement(deleteLeaseQuery)) {

            // Delete related payments first
            paymentStmt.setInt(1, leaseID);
            paymentStmt.executeUpdate();

            // Delete the lease record
            leaseStmt.setInt(1, leaseID);
            leaseStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Lease> listActiveLeases() {
        List<Lease> activeLeases = new ArrayList<>();
        String query = "SELECT l.leaseID, l.vehicleID, l.customerID, l.startDate, l.endDate, l.type, "
                + "v.make, v.model, c.firstName, c.lastName "
                + "FROM Lease l "
                + "JOIN Customer c ON l.customerID = c.customerID "
                + "JOIN Vehicle v ON l.vehicleID = v.vehicleID "
                + "WHERE l.endDate > CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Car c = new Car(); // Assuming a Vehicle constructor
                c.setVehicleID(rs.getInt("vehicleID"));
                c.setMake(rs.getString("make"));
                c.setModel(rs.getString("model"));
                // Populate other vehicle fields as necessary

                Customer customer = new Customer();
                customer.setCustomerID(rs.getInt("customerID"));
                customer.setFirstName(rs.getString("firstName"));
                customer.setLastName(rs.getString("lastName"));

                Lease lease = new Lease();
                lease.setLeaseID(rs.getInt("leaseID"));
                lease.setCar(c);
                lease.setCustomer(customer);
                lease.setStartDate(rs.getDate("startDate"));
                lease.setEndDate(rs.getDate("endDate"));
                lease.setType(rs.getString("type"));

                activeLeases.add(lease);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeLeases;
    }


    @Override
    public List<Lease> listLeaseHistory() {
            List<Lease> leaseHistory = new ArrayList<>();
            String query = "SELECT l.leaseID, l.vehicleID, l.customerID, l.startDate, l.endDate, l.type, "
                    + "v.make, v.model, c.firstName, c.lastName "
                    + "FROM Lease l "
                    + "JOIN Customer c ON l.customerID = c.customerID "
                    + "JOIN Vehicle v ON l.vehicleID = v.vehicleID";

            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Car vehicle = new Car(); // Assuming Vehicle has a constructor
                    vehicle.setVehicleID(rs.getInt("vehicleID"));
                    vehicle.setMake(rs.getString("make"));
                    vehicle.setModel(rs.getString("model"));
                    // Set other vehicle details as necessary

                    Customer customer = new Customer();
                    customer.setCustomerID(rs.getInt("customerID"));
                    customer.setFirstName(rs.getString("firstName"));
                    customer.setLastName(rs.getString("lastName"));

                    Lease lease = new Lease();
                    lease.setLeaseID(rs.getInt("leaseID"));
                    lease.setCar(vehicle);
                    lease.setCustomer(customer);
                    lease.setStartDate(rs.getDate("startDate"));
                    lease.setEndDate(rs.getDate("endDate"));
                    lease.setType(rs.getString("type"));

                    leaseHistory.add(lease);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return leaseHistory;
        }



    @Override
    public void recordPayment(Lease lease, double amount) {
        String insertPaymentQuery = "INSERT INTO Payment (leaseID, paymentDate, amount) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertPaymentQuery)) {
            stmt.setInt(1, lease.getLeaseID()); // Set lease ID from the Lease object
            stmt.setDate(2, new java.sql.Date(System.currentTimeMillis())); // Set current date as payment date
            stmt.setDouble(3, amount); // Set the amount

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Payment recorded successfully for Lease ID: " + lease.getLeaseID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}