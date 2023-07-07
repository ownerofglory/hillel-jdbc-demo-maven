package ua.ithillel.dao;

import ua.ithillel.exception.CustomerCreateException;
import ua.ithillel.model.Customer;
import ua.ithillel.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerMySqlJdbcDao implements CustomerDao {
    private final Connection connection;

    public CustomerMySqlJdbcDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Customer findById(Integer id) {
        try {
            // SELECT * FROM customer WHERE id = 1;
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE id = ?");
            statement.setInt(1, id);
            boolean dataReturned = statement.execute();

            if (dataReturned) {
                ResultSet resultSet = statement.getResultSet(); // like iterator

                Customer customer = new Customer();

                while (resultSet.next()) {
                    customer.setId(resultSet.getInt("id"));
                    customer.setName(resultSet.getString("name"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setPassword(resultSet.getString("password"));
                    customer.setBirthDate(resultSet.getDate("birth_date"));
                }

                return customer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        try {
            // SELECT * FROM customer WHERE id = 1;
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE email = ?");
            statement.setString(1, email);
            boolean dataReturned = statement.execute();

            if (dataReturned) {
                ResultSet resultSet = statement.getResultSet(); // like iterator

                Customer customer = new Customer();

                while (resultSet.next()) {
                    customer.setId(resultSet.getInt("id"));
                    customer.setName(resultSet.getString("name"));
                    customer.setEmail(resultSet.getString("email"));
                    customer.setPassword(resultSet.getString("password"));
                    customer.setBirthDate(resultSet.getDate("birth_date"));
                }

                return customer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Customer findByIdWithOrders(Integer id) {
        try {
//        SELECT c.*, o.* FROM customer AS c
//        LEFT JOIN t_order AS o
//        ON c.id = o.customer_id
//        WHERE c.id = 1;
            PreparedStatement statement = connection.prepareStatement("SELECT c.*, o.* FROM customer AS c\n" +
                    "LEFT JOIN t_order AS o \n" +
                    "ON c.id = o.customer_id\n" +
                    "WHERE c.id = ?");

            statement.setInt(1, id);
            boolean dataReturned = statement.execute();
            if (dataReturned) {
                ResultSet resultSet = statement.getResultSet();

                Customer customer = null;

                List<Order> orders = new ArrayList<>();
                while (resultSet.next()) {

                    if (customer == null) {
                        customer = new Customer();
                        customer.setId(resultSet.getInt("c.id"));
                        customer.setName(resultSet.getString("c.name"));
                        customer.setEmail(resultSet.getString("c.email"));
                        customer.setPassword(resultSet.getString("c.password"));
                        customer.setBirthDate(resultSet.getDate("c.birth_date"));
                        customer.setOrders(orders);
                    }

                    Order order = new Order();
                    order.setId(resultSet.getInt("o.id"));
                    order.setOrderDate(resultSet.getDate("o.order_date"));
                    order.setTotalAmount((double) resultSet.getFloat("o.total_amount"));

                    order.setCustomer(customer);

                    orders.add(order);

                }

                return customer;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void create(Customer customer) throws CustomerCreateException {
        try {
//        INSERT INTO customer (name, email, password, birth_date)
//        VALUES ("Petro Vasylenko", "pv@mail.com", "234r3", "10-01-1985");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO customer (name, email, password, birth_date)\n" +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS) ;
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPassword());
            statement.setDate(4, customer.getBirthDate());

            int rows = statement.executeUpdate();


            if (rows == 0) {
                throw new CustomerCreateException("Unable to create customer");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                customer.setId(generatedKeys.getInt(1));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
