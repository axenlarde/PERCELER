package com.alex.perceler.misc;

import java.util.ArrayList;

import com.alex.perceler.misc.ItemToMigrate.itmStatus;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
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
	private ArrayList<ItemToMigrate> todoList;
	private itmStatus status;
	private boolean pause, stop, started, end;
	private String taskID, ownerID;
	private actionType action;
	
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
			todo.build(action);
			
			/*
			if(todo.getErrorList().size() != 0)
				{
				for(ErrorTemplate e : todo.getErrorList())
					{
					Variables.getLogger().debug("- "+e.getTargetName()+" "+e.getIssueName()+" "+e.getErrorDesc()+" "+e.getError().name()+" "+e.getIssueType().name());
					}
				todo.setStatus(itmStatus.disabled);
				}*/
			}
		Variables.getLogger().info("End of the build process");
		}
	
	/**
	 * Start the real process
	 */
	private void startProcess()
		{
		for(ItemToMigrate myToDo : todoList)
			{
			try
				{
				myToDo.migrate();
				}
			catch (Exception e)
				{
				Variables.getLogger().error("An error occured with the item \""+myToDo.getName()+"\" : "+e.getMessage(), e);
				myToDo.setStatus(itmStatus.error);
				}
			}
		}
	
	private void startSurvey() throws Exception
		{
		for(ItemToMigrate myToDo : todoList)
			{
			myToDo.startSurvey();
			}
		}
	
	public void run()
		{
		try
			{
			Variables.getLogger().info("Task "+taskID+" begins");
			started = true;
			status = itmStatus.preaudit;
			startBuildProcess();
			startSurvey();
			status = itmStatus.migration;
			while(pause)
				{
				this.sleep(500);
				}
			startProcess();
			status = itmStatus.postaudit;
			startSurvey();//Once finished we proceed with another survey
			status = itmStatus.done;
			
			end = true;
			Variables.getLogger().info("Task ends");
			Variables.setUuidList(new ArrayList<storedUUID>());//We clean the UUID list
			Variables.getLogger().info("UUID list cleared");
			}
		catch (Exception e)
			{
			Variables.getLogger().debug("ERROR : "+e.getMessage(),e);
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
			Variables.getLogger().debug("The user asked to pause the task");
			}
		else
			{
			Variables.getLogger().debug("The user asked to resume the task");
			}
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop)
		{
		this.stop = stop;
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

