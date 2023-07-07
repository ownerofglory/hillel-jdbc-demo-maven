package ua.ithillel;

import ua.ithillel.dao.CustomerDao;
import ua.ithillel.dao.CustomerMySqlJdbcDao;
import ua.ithillel.db.JdbcUtil;
import ua.ithillel.exception.CustomerCreateException;
import ua.ithillel.model.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = JdbcUtil.getConnection()) {

            CustomerDao customerDao = new CustomerMySqlJdbcDao(connection);

            Customer byId = customerDao.findById(1);
            Customer byEmail = customerDao.findByEmail("jd@mail.com");
            System.out.println(byId);
            System.out.println(byEmail);

            Customer byIdWithOrders = customerDao.findByIdWithOrders(1);
            Customer byIdWithOrders1 = customerDao.findByIdWithOrders(3);

            System.out.println(byIdWithOrders);
            System.out.println(byIdWithOrders1);


            Customer customer = new Customer();
            customer.setName("Petro Vasylchenko");
            customer.setEmail("Petro.Vasylchenko@mail.com");
            customer.setPassword("wpfhpishfhsuir");
            customer.setBirthDate(Date.valueOf(LocalDate.now()));

            customerDao.create(customer);

            Customer addedCustomer = customerDao.findByEmail("Petro.Vasylchenko@mail.com");
            System.out.println(addedCustomer);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CustomerCreateException e) {
            throw new RuntimeException(e);
        }

        // load JDBC driver implementation
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            // java.sql.Driver <-- com.mysql.cj.jdbc.Driver
//
//            // host address
//            // port: MYSQL - 3306
//            // database name (optional)
//            // username
//            // password
//            String host = System.getenv("JDBC_HOST");
//            String dbName = System.getenv("JDBC_DB_NAME");
//            String username = System.getenv("JDBC_USER");
//            String password = System.getenv("JDBC_PASSWORD");
//
//            String url = String.format("%s/%s", host, dbName);
//
//            try (Connection connection = DriverManager.getConnection(url, username, password)) {
//                List<Customer> allCustomers = getAllCustomers(connection);
//                System.out.println(allCustomers);
//
//                printCustomerById(connection, 1);
//                printCustomerByEmail(connection, "jd@mail.com");
//
//
//
//
//
//            } catch (SQLException e) {
//                System.out.println("Unable to create connection. " + e.getMessage());
//            }
//
//
//        } catch (ClassNotFoundException e) {
//            System.out.println("Driver not found");
//        }


    }

    private static List<Customer> getAllCustomers(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        boolean dataReturned = statement.execute("SELECT * FROM customer");

        List<Customer> customers = new ArrayList<>();

        if (dataReturned) {
            ResultSet resultSet = statement.getResultSet(); // like iterator

            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPassword(resultSet.getString("password"));
                customer.setBirthDate(resultSet.getDate("birth_date"));

                customers.add(customer);
            }

        }

        return customers;
    }

    private static void printCustomerById(Connection connection, int id) throws SQLException {
        // SELECT * FROM customer WHERE id = 1;
        Statement statement = connection.createStatement();
        boolean dataReturned = statement.execute("SELECT * FROM customer WHERE id = " + id);

        if (dataReturned) {
            ResultSet resultSet = statement.getResultSet(); // like iterator

            while (resultSet.next()) {
                int customerId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String customerPassword = resultSet.getString("password");
                Date birthDate = resultSet.getDate("birth_date");

                System.out.printf("Customer{id=%d; name=%s; email=%s; password=%s; birthDate=%s}%n", customerId, name, email, customerPassword, birthDate);
            }

        }

    }

    // someMail@mail.com'; drop table customer
    private static void printCustomerByEmail(Connection connection, String email) throws SQLException {
        // SELECT * FROM customer WHERE id = 1;
//        Statement statement = connection.createStatement();
//        boolean dataReturned = statement.execute("SELECT * FROM customer WHERE email = \"" + email + "\"") ;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE email = ?");
        statement.setString(1, email);
        boolean dataReturned = statement.execute();

        if (dataReturned) {
            ResultSet resultSet = statement.getResultSet(); // like iterator

            while (resultSet.next()) {
                int customerId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String customerEmail = resultSet.getString("email");
                String customerPassword = resultSet.getString("password");
                Date birthDate = resultSet.getDate("birth_date");

                System.out.printf("Customer{id=%d; name=%s; email=%s; password=%s; birthDate=%s}%n", customerId, name, email, customerPassword, birthDate);
            }

        }

    }
}