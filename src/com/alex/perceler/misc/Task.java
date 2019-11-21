package com.alex.perceler.misc;

import java.util.ArrayList;

import com.alex.perceler.cli.CliManager;
import com.alex.perceler.device.misc.Device;
import com.alex.perceler.misc.ItemToMigrate.itmStatus;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itmType;
import com.alex.perceler.utils.Variables.statusType;

/**********************************
 * Class used to store a list of todo
 * 
 * It also allowed to launch the task
 * 
 * @author RATEL Alexandre
 **********************************/
public class Task extends Thread
	{
	/**
	 * Variables
	 */
	public enum taskActionType
		{
		start,
		stop,
		pause
		};
	
	private ArrayList<ItemToMigrate> todoList;
	private itmStatus status;
	private boolean pause, stop, started, end;
	private String taskID, ownerID;
	private actionType action;
	private CliManager cliManager;
	
	/***************
	 * Constructor
	 ***************/
	public Task(ArrayList<ItemToMigrate> todoList, String id, String ownerID, actionType action)
		{
		this.todoList = todoList;
		this.status = itmStatus.init;
		stop = false;
		pause = true;
		started = false;
		end = false;
		this.taskID = id;
		this.ownerID = ownerID;
		this.action = action;
		}
	
	/******
	 * Used to start the build process
	 * @throws Exception 
	 */
	private void startBuildProcess() throws Exception
		{
		//Build
		Variables.getLogger().info("Beginning of the build process");
		for(ItemToMigrate todo : todoList)
			{
			todo.build();
			}
		Variables.getLogger().info("End of the build process");
		}
	
	/**
	 * Start survey process
	 */
	private void startSurvey() throws Exception
		{
		Variables.getLogger().info("Beginning of the survey process");
		for(ItemToMigrate myToDo : todoList)
			{
			if(!myToDo.getStatus().equals(itmStatus.disabled))
				{
				myToDo.startSurvey();
				}
			else Variables.getLogger().debug("The following item has been disabled so we do not process it : "+myToDo.getInfo());
			}
		Variables.getLogger().info("End of the survey process");
		}
	
	/**
	 * Start the real process
	 */
	private void startUpdate()
		{
		Variables.getLogger().info("Beginning of the update process");
		
		/**
		 * First we take all the devices and start the cli update process
		 * Because each cliInjector is a different thread we can start them all
		 * simultaneously to save time
		 */
		cliManager = new CliManager();
		
		for(ItemToMigrate myToDo : todoList)
			{
			if(!myToDo.getStatus().equals(itmStatus.disabled))
				{
				if(myToDo instanceof Device)
					{
					cliManager.getCliIList().add(((Device)myToDo).getCliInjector());
					}
				}
			else Variables.getLogger().debug("The following item has been disabled so we do not process it : "+myToDo.getInfo());
			}
		if(cliManager.getCliIList().size() != 0)cliManager.start();
		
		/**
		 * Then we start the axl item updates wich is not multithreaded
		 * so it has to be item by item
		 */
		for(ItemToMigrate myToDo : todoList)
			{
			try
				{
				while(pause)
					{
					this.sleep(500);
					}
				
				if(!myToDo.getStatus().equals(itmStatus.disabled))
					{
					myToDo.update();
					}
				else Variables.getLogger().debug("The following item has been disabled so we do not process it : "+myToDo.getInfo());
				}
			catch (Exception e)
				{
				Variables.getLogger().error("An error occured with the item \""+myToDo.getName()+"\" : "+e.getMessage(), e);
				myToDo.setStatus(itmStatus.error);
				}
			}
		Variables.getLogger().info("End of the update process");
		}
	
	/**
	 * Start reset process
	 */
	private void startReset()
		{
		Variables.getLogger().info("Beginning of the reset process");
		for(ItemToMigrate myToDo : todoList)
			{
			try
				{
				while(pause)
					{
					this.sleep(500);
					}
				if(!myToDo.getStatus().equals(itmStatus.disabled))myToDo.reset();
				else Variables.getLogger().debug("The following item has been disabled so we do not process it : "+myToDo.getInfo());
				}
			catch (InterruptedException e)
				{
				Variables.getLogger().error("An error occured with the item \""+myToDo.getName()+"\" : "+e.getMessage(), e);
				myToDo.setStatus(itmStatus.error);
				}
			}
		Variables.getLogger().info("End of the reset process");
		}
	
	private void setItemStatus(itmStatus status)
		{
		for(ItemToMigrate myToDo : todoList)
			{
			myToDo.setStatus(status);
			}
		}
	
	public void run()
		{
		try
			{
			Variables.getLogger().info(action+" task "+taskID+" begins");
			started = true;
			
			if(action.equals(actionType.reset))
				{
				pause = false;
				status = itmStatus.reset;
				setItemStatus(itmStatus.reset);
				startReset();
				}
			else
				{
				status = itmStatus.preaudit;
				setItemStatus(itmStatus.preaudit);
				startBuildProcess();
				startSurvey();
				
				//We then wait for the user to accept the migration
				while(pause)
					{
					this.sleep(500);
					}
				status = itmStatus.update;
				setItemStatus(itmStatus.update);
				startUpdate();
				startReset();
				
				status = itmStatus.postaudit;
				setItemStatus(itmStatus.postaudit);
				while(!stop)
					{
					/**
					 * We proceed with the survey as long as the user want
					 */
					startSurvey();
					this.sleep(5000);
					}
				}
			
			setItemStatus(itmStatus.done);
			status = itmStatus.done;
			end = true;
			Variables.getLogger().info(action+" task "+taskID+" ends");
			Variables.setUuidList(new ArrayList<storedUUID>());//We clean the UUID list
			Variables.getLogger().info("UUID list cleared");
			}
		catch (Exception e)
			{
			Variables.getLogger().debug("ERROR : "+e.getMessage(),e);
			status = itmStatus.error;
			}
		}
	
	public void act(taskActionType action)
		{
		switch(action)
			{
			case pause:setPause(true);break;
			case start:setPause(false);break;
			case stop:setStop(true);break;
			default:break;
			}
		}

	public ArrayList<ItemToMigrate> getTodoList()
		{
		return todoList;
		}

	public void setTodoList(ArrayList<ItemToMigrate> todoList)
		{
		this.todoList = todoList;
		}

	public itmStatus getStatus()
		{
		return status;
		}

	public void Stop()
		{
		this.stop = true;
		}

	public boolean isPause()
		{
		return pause;
		}

	public void setPause(boolean pause)
		{
		this.pause = pause;
		
		if(this.pause)
			{
			Variables.getLogger().debug("The user asked to pause the task : "+taskID);
			cliManager.setPause(pause);
			}
		else
			{
			Variables.getLogger().debug("The user asked to resume the task : "+taskID);
			cliManager.setPause(pause);
			}
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop)
		{
		Variables.getLogger().debug("The user asked to stop the task : "+taskID);
		this.stop = stop;
		cliManager.setStop(stop);
		}

	public boolean isStarted()
		{
		return started;
		}

	public boolean isEnd()
		{
		return end;
		}

	public void setEnd(boolean end)
		{
		this.end = end;
		}

	public String getTaskId()
		{
		return taskID;
		}

	public void setTaskId(String id)
		{
		this.taskID = id;
		}

	public String getOwnerID()
		{
		return ownerID;
		}

	public actionType getAction()
		{
		return action;
		}
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}

