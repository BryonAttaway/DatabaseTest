package DatabaseTest.view;

import javax.swing.JFrame;

import DatabaseTest.controller.DatabaseAppController;
import DatabaseTest.controller.DatabaseController;

public class DatabaseFrame extends JFrame
{
	private DatabasePanel basePane;
	
	public DatabaseFrame(DatabaseAppController databaseAppController)
	{
		basePane = new DatabasePanel(databaseAppController);
		setupFrame();
	}
	
	private void setupFrame()
	{
		this.setContentPane(basePane);
		this.setSize(1000,1000);
		this.setVisible(true);
	}

}
