package mainpackage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;
import mainpackage.company.CompanyConfig;
import mainpackage.customer.PermCustomer;

@Component
public class NamedParamJdbcDaoImpl extends NamedParameterJdbcDaoSupport {

	@Autowired
	private CompanyConfig companyConfig;
	@Autowired
	private DataSource dataSource;
    
    public void displayPermanentCustomer() {
        try (Connection connection = dataSource.getConnection()) {
            String selectQuery = "SELECT * FROM PERMCUSTOMER";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = statement.executeQuery();
            int i = 1;
            while (resultSet.next()) {
                String nameAndSurname = resultSet.getString("NAMEANDSURNAME");
                PermCustomer permCustomer = createPermCustomerFromResultSet(resultSet, dataSource);
                displayCustomerTasks(nameAndSurname, permCustomer, resultSet, i);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PermCustomer createPermCustomerFromResultSet(ResultSet resultSet, DataSource dataSource) {
        PermCustomer permCustomer = new PermCustomer();
        try {
			permCustomer.setNameAndSurname(resultSet.getString("NAMEANDSURNAME"));
			permCustomer.setTask1(resultSet.getString("TASK1"));
	        permCustomer.setTask2(resultSet.getString("TASK2"));
	        permCustomer.setTask3(resultSet.getString("TASK3"));
	        permCustomer.setTask4(resultSet.getString("TASK4"));
	        permCustomer.setTask5(resultSet.getString("TASK5"));
	        permCustomer.setTask6(resultSet.getString("TASK6"));
	        permCustomer.setTask7(resultSet.getString("TASK7"));
	        permCustomer.setTask8(resultSet.getString("TASK8"));
	        permCustomer.setTask9(resultSet.getString("TASK9"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return permCustomer;
    }
    
    private PermCustomer createPermCustomerFromDatabase(int i) {
    	PermCustomer permCustomer = new PermCustomer();
    	try (Connection connection = dataSource.getConnection()) {
            String selectQuery = "SELECT * FROM PERMCUSTOMER WHERE NAMEANDSURNAME = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, "NameAndSurname"+String.valueOf(i));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
            	permCustomer.setNameAndSurname(resultSet.getString("NAMEANDSURNAME"));
    			permCustomer.setTask1(resultSet.getString("TASK1"));
    	        permCustomer.setTask2(resultSet.getString("TASK2"));
    	        permCustomer.setTask3(resultSet.getString("TASK3"));
    	        permCustomer.setTask4(resultSet.getString("TASK4"));
    	        permCustomer.setTask5(resultSet.getString("TASK5"));
    	        permCustomer.setTask6(resultSet.getString("TASK6"));
    	        permCustomer.setTask7(resultSet.getString("TASK7"));
    	        permCustomer.setTask8(resultSet.getString("TASK8"));
    	        permCustomer.setTask9(resultSet.getString("TASK9"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return permCustomer;
    }

    private void displayCustomerTasks(String nameAndSurname, PermCustomer permCustomer, ResultSet resultSet, int i) {
    	companyConfig.initializeDAYNUMFromDatabase(dataSource);
        System.out.println("Permanent customer " + nameAndSurname + " at day number " + companyConfig.getDAYNUM() + " has tasks " + fetchedTasks(permCustomer, resultSet, i));
        companyConfig.incrementDAYNUM();
        companyConfig.updateDAYNUMInDatabase(dataSource);
    }
	
    public ArrayList<String> fetchedTasks(PermCustomer permCustomer, ResultSet resultSet, int i) {
        int remhour = 10;
        String temp = permCustomer.getTask1();
        ArrayList<String> tasks = new ArrayList<>();
        if (temp.equals(permCustomer.getTask2())) {
            if (temp.equals("Task1")) {
                remhour -= CompanyConfig.getHourOfTask1() - 1;
            } else if (temp.equals("Task2")) {
                remhour -= CompanyConfig.getHourOfTask2() - 1;
            } else if (temp.equals("Task3")) {
                remhour -= CompanyConfig.getHourOfTask3() - 1;
            } else {
                System.out.println("Invalid task");
                return tasks;
            }
        } else {
            if (temp.equals("Task1")) {
                remhour -= CompanyConfig.getHourOfTask1();
            } else if (temp.equals("Task2")) {
                remhour -= CompanyConfig.getHourOfTask2();
            } else if (temp.equals("Task3")) {
                remhour -= CompanyConfig.getHourOfTask3();
            } else {
                System.out.println("Invalid task");
                return tasks;
            }
        }
        while (remhour >= 0) {
            tasks.add(temp);
            permCustomer.updateTasksInDatabase(dataSource, i);
            permCustomer = createPermCustomerFromDatabase(i);
            temp = permCustomer.getTask1();
            if (temp != null && temp.equals(permCustomer.getTask2())) {
                if (temp.equals("Task1")) {
                    remhour -= CompanyConfig.getHourOfTask1() - 1;
                } else if (temp.equals("Task2")) {
                    remhour -= CompanyConfig.getHourOfTask2() - 1;
                } else if (temp.equals("Task3")) {
                    remhour -= CompanyConfig.getHourOfTask3() - 1;
                } else {
                    System.out.println("Invalid task");
                    break;
                }
            } else if (temp != null) {
                if (temp.equals("Task1")) {
                    remhour -= CompanyConfig.getHourOfTask1();
                } else if (temp.equals("Task2")) {
                    remhour -= CompanyConfig.getHourOfTask2();
                } else if (temp.equals("Task3")) {
                    remhour -= CompanyConfig.getHourOfTask3();
                } else {
                    System.out.println("Invalid task");
                    break;
                }
            } else {
                break;
            }
        }
        return tasks;
    }
    
}