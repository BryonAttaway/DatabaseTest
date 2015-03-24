package DatabaseTest.controller;

import DatabaseTest.view.DatabaseFrame;

public class DatabaseAppController
{
	private DatabaseFrame appFrame;
	private DatabaseController database;
	
	public DatabaseAppController()
	{
		database = new DatabaseController(this);
		appFrame = new DatabaseFrame(this);
	}
	
	public void start()
	{
		//need to try the connectionStringBuolder and setupConnection methods
	}
	
	public DatabaseFrame getAppFrame()
	{
		return appFrame;
	}
	
	public DatabaseController getDatabase()
	{
		return database;
	}
}
