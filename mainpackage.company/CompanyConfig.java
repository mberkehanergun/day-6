package mainpackage.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class CompanyConfig {

    private int DAYNUM;
    private static int hourOfTask1 = 2;
    private static int hourOfTask2 = 3;
    private static int hourOfTask3 = 4;

	public int getDAYNUM() {
        return DAYNUM;
    }

    public void setDAYNUM(int DAYNUM) {
        this.DAYNUM = DAYNUM;
    }
    
    public void incrementDAYNUM() {
        this.DAYNUM++;
    }

    public void updateDAYNUMInDatabase(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            String updateQuery = "UPDATE COMPANYCONFIG SET DAYNUM = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, this.DAYNUM);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeDAYNUMFromDatabase(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            String selectQuery = "SELECT DAYNUM FROM COMPANYCONFIG";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int initialDAYNUM = resultSet.getInt("DAYNUM");
                this.DAYNUM = initialDAYNUM;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	public static int getHourOfTask1() {
		return hourOfTask1;
	}

	public static int getHourOfTask2() {
		return hourOfTask2;
	}

	public static int getHourOfTask3() {
		return hourOfTask3;
	}

    
    
}