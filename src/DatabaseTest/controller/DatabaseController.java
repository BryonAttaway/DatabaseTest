package DatabaseTest.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import DatabaseTest.view.DatabaseFrame;

public class DatabaseController
{
	
	private String connectionString;
	private Connection databaseConnection;
	private DatabaseAppController baseController;
	private DatabaseFrame appFrame;
	private String query;
	private String currentQuery;
	private Exception currentSQLError;
	private Throwable currentException;
	private String SQLException;
	
	/**
	 * establish a connection
	 * @param baseController
	 */
	public DatabaseController(DatabaseAppController baseController)
	{
		this.baseController = baseController;
		this.connectionString = "jdbc:mysql://localhost/schools?user=root";
		checkDriver();
		setupConnection();
	}
	
	
	private void setupConnection()
	{
		// TODO Auto-generated method stub
		
	}
	

	/**
	 * This will check for the driver, if there isn't a driver is shuts down.
	 */
	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		} 
		
	}
	
	public void dropStatement(String query)
	{
		currentQuery = query;
		String results;
		try
		{
			if(checkForStructureViolation())
			{
				throw new SQLException("you is no allowed to dropping db's",
										"duh",
										Integer.MIN_VALUE);
			}
			if(currentQuery.toUpperCase().contains(" INDEX "))
			{
				results = "The index was ";
			}
			else
			{
				results = "The table was ";
			}
			
			Statement dropStatement = databaseConnection.createStatement();
			int affected = dropStatement.executeUpdate(currentQuery);
			
			dropStatement.close();
			
			if(affected == 0)
			{
				results += "dropped";
			}
			JOptionPane.showMessageDialog(baseController.getAppFrame(), results);
		}
		catch(SQLException dropError)
		{
			displayErrors(dropError);
		}
	}
	
	public String[] getMetaData()
	{
		String[] columnInformation;
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(currentQuery);
			ResultSetMetaData myMeta = answer.getMetaData();
			
			columnInformation = new String[myMeta.getColumnCount()];
			
			for (int spot = 0; spot < myMeta.getColumnCount(); spot++)
			{
				columnInformation[spot] = myMeta.getColumnName(spot + 1);
			}
			
			answer.close();
			firstStatement.close();
		}
		catch(SQLException curretnSQLError)
		{
			columnInformation = new String[] { "nada exists" };
			displayErrors(currentSQLError);
		}
		
		return columnInformation;
	}
	
	public int insertSample()
	{
		int rowsAffected = 0;
		String insertQuery = "INSERT INTO `movies`.`movies` " + "(`name`,`rating`,`stars`,`star_resource`)"//columns
				+ "VALUES (Insurgent, PG-13, 7, `IMDb`);";
		
		try
		{
			Statement insertStatement = databaseConnection.createStatement();
			rowsAffected = insertStatement.executeUpdate(insertQuery);
			insertStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		return rowsAffected;
	}
	
	public String[][] realInfo()
	{
		String[][] results;
		String query = "SELECT * FROM `INNODB_SYS_COLUMNS`";
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int columnCount = answer.getMetaData().getColumnCount();
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][columnCount];
			
			while (answer.next())
			{
				for(int col = 0; col < columnCount; col++)
				{
					results[answer.getRow() - 1][col] = answer.getString(col + 1);
				}
			}
			
			answer.close();
			firstStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			results = new String[][] { { "error processing" } };
			displayErrors(currentSQLError);
		}
		return results;
	}
	
	public String[][] selectQueryResults(String query)
	{
		this.currentQuery = query;
		String[][] results;
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int columnCount = answer.getMetaData().getColumnCount();
			
			answer.last();
			int rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][columnCount];
			
			while (answer.next())
			{
				for(int col = 0; col < columnCount; col++)
				{
					results[answer.getRow() - 1][col] = answer.getString(col + 1);
				}
			}
			
			answer.close();
			firstStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			results = new String[][] { { "error processing" }, { "ty sending a better query" }, { currentSQLError.getMessage() } };
			displayErrors(currentSQLError);
		}
		return results;
	}
	
	
	
	public void connectionStringBuilder(String pathToDBServer, String databaseName, String userName, String password)
	{
		connectionString = "jdbc:mysql://";
		connectionString += pathToDBServer;
		connectionString += "/" + databaseName;
		connectionString += "?user=" + userName;
		connectionString += "&password" + password;
	}

	
	public String displayTables()
	{
		String tableNames = "";
		String query = "SHOW TABLES";
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(query);
			
			while(answers.next())
			{
				tableNames += answers.getString(1) + "\n";
			}
		}
		
		catch(SQLException currentError)
		{
			displayErrors(currentError);
		}
		
		return tableNames;
	}
	

	public String[][] tableInfo()
	{
		String[][] results;
		String query = "SHOW TABLES";
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][1];
			
			while (answer.next())
			{
				results[answer.getRow() - 1][0] = answer.getString(1);
			}
			
			answer.close();
			firstStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			results = new String[][] { { "problem occured D:" } };
			displayErrors(currentSQLError);
		}
		return results;
	}
	
	private boolean checkForDataViolation()
	{
		if(currentQuery.toUpperCase().contains(" DROP ")
				|| currentQuery.toUpperCase().contains(" TRUNCATE ") 
				|| currentQuery.toUpperCase().contains(" SET ")
				|| currentQuery.toUpperCase().contains(" ALTER "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean checkForStructureViolation()
	{
		if(currentQuery.toUpperCase().contains(" DATABASE "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void displayErrors(Exception currentError)
	{
		JOptionPane.showMessageDialog(baseController.getAppFrame(), currentException.getMessage());
		
		if(currentException instanceof SQLException)
		{
			JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL State: " + ((SQLException)));
		}
		
	}
	
	private boolean checkQueryForDataViolation()
	{
		if(query.toUpperCase().contains(" DROP ")
				|| query.toUpperCase().contains(" TRUNCATE ")
				|| query.toUpperCase().contains(" SET ")
				|| query.toUpperCase().contains(" ALTER "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String[] getMetaDataTitles()
	{
		String [] columns;
		String query = "SHOW TABLES";
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(query);
			ResultSetMetaData answerData = answers.getMetaData();
			
			columns = new String[answerData.getColumnCount()];
			
			for(int column = 0; column < answerData.getColumnCount(); column++)
			{
				columns[column] = answerData.getColumnName(column+1);
			}
			
			answers.close();
			firstStatement.close();
		}
		catch(SQLException currentException)
		{
			columns = new String [] {"empty"};
			displayErrors(currentException);
		}
		
		return columns;
		
	}
	
	public DatabaseController()
	{
		baseController = new DatabaseAppController(test);
		appFrame = new DatabaseFrame(test);
	}
	public DatabaseFrame getAppFrame()
	{
		return appFrame;
	}
	
	
	
	public void start()
	{
		
	}

}
