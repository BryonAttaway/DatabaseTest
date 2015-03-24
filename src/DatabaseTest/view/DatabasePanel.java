package DatabaseTest.view;

import java.awt.Color;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.*;

import DatabaseTest.controller.DatabaseAppController;

public class DatabasePanel extends JPanel
{
	private DatabaseAppController baseController;
	private SpringLayout baseLayout;
	private JButton queryButton;
	private JScrollPane displayPane;
	private JTextArea displayArea;
	private JTable resultsTable;

	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		}
	}


	private void setupTable()
	{
		//1D Array for column titles
		//2D array for contents
		resultsTable = new JTable(new DefaultTableModel(baseController.getDatabase().realInfo(), baseController.getDatabase().getMetaData()));
		
		displayPane = new JScrollPane(resultsTable);
		for(int spot = 0; spot < resultsTable.getColumnCount(); spot++)
		{
//			resultsTable.getColumnModel().getColumn(spot).setCellRenederer(new TableCellWrapRenderer());
		}
	}
	

	public DatabasePanel(DatabaseAppController baseController)
	{
		this.baseController = baseController;
		baseLayout = new SpringLayout();
		queryButton = new JButton("Click here to test the query");
		displayArea = new JTextArea(10, 30);
		displayPane = new JScrollPane(displayArea);

		setupDisplayPane();
		setupPanel();
		setupLayout();
		setupListeners();
	}

	private void displayErrors(Exception currentException)
	{
		JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL State: " + ((SQLException) currentException).getSQLState());
		JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL Error Code: " + ((SQLException) currentException).getErrorCode());
	}

	public void closeConnection()
	{

	}

	private void setupConnection()
	{

	}

	public String displayTables()
	{
		return null;

	}

	private void setupDisplayPane()
	{
		displayArea.setWrapStyleWord(true);
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
		displayArea.setBackground(Color.RED);
	}

	private void setupPanel()
	{
		this.setBackground(Color.GREEN);
		this.setSize(1000, 1000);
		this.setLayout(baseLayout);
		this.add(displayPane);
		this.add(queryButton);
	}

	private void setupLayout()
	{

	}

	private void setupListeners()
	{
		queryButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent click)
			{
				String[] temp = baseController.getDatabase().getMetaDataTitles();
				for (String current : temp)
				{
					displayArea.setText(displayArea.getText() + "Column : " + current + "\n");
				}
			}
		});
	}
}
