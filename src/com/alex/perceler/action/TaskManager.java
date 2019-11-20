package com.alex.perceler.action;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.perceler.device.misc.BasicDevice;
import com.alex.perceler.device.misc.Device;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.misc.Task;
import com.alex.perceler.office.misc.BasicOffice;
import com.alex.perceler.office.misc.Office;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;

public class TaskManager
	{
	
	/**
	 * To start new tasks
	 */
	public static String addNewTask(ArrayList<String> itemList, actionType action, String ownerID)
		{
		try
			{
			//First we clear finished tasks
			clearStaleTask();
			
			if(Variables.getTaskList().size() <= Integer.parseInt(UsefulMethod.getTargetOption("maxtaskthread")))
				{
				ArrayList<ItemToMigrate> todoList = new ArrayList<ItemToMigrate>();
				
				for(String s : itemList)
					{
					boolean found = false;
					//Devices
					for(BasicDevice d : Variables.getDeviceList())
						{
						if(d.getId().equals(s))
							{
							//In case of rollback we reverse the old and new values
							todoList.add(new Device(d, action));
							found = true;
							break;
							}
						}
					if(!found)
						{
						//Offices
						for(BasicOffice o : Variables.getOfficeList())
							{
							if(o.getId().equals(s))
								{
								//In case of rollback we reverse the old and new values
								todoList.add(new Office(o, action));
								found = true;
								break;
								}
							}
						}
					if(!found)Variables.getLogger().debug("Warning : The following item was not found in the database which is not normal : "+s);
					}
				
				//We generate a new unique ID
				String id = DigestUtils.md5Hex(System.currentTimeMillis()+"8)");
				Task t = new Task(todoList, id, ownerID, action);
				Variables.getTaskList().add(t);
				t.start();
				return id;
				}
			else
				{
				Variables.getLogger().debug("Max concurent task reached. You cannot start more task");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while adding a new migration task : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Will start a rollback task from a finished migration task
	 */
	public static String rollBackMigrationTask(String id, String ownerID)
		{
		for(Task t : Variables.getTaskList())
			{
			if(t.getTaskId().equals(id))
				{
				ArrayList<String> idList = new ArrayList<String>();
				for(ItemToMigrate itm : t.getTodoList())
					{
					idList.add(itm.getId());
					}
				return addNewTask(idList, t.getAction(), ownerID);
				}
			}
		Variables.getLogger().debug("No task found to rollback for id : "+id);
		
		return null;
		}
	
	/**
	 * To clear stale tasks
	 */
	private static void clearStaleTask()
		{
		Variables.getLogger().debug("Clearing task list");
		for(Task t : Variables.getTaskList())
			{
			if(t.isEnd())
				{
				Variables.getTaskList().remove(t);
				clearStaleTask();
				}
			}
		Variables.getLogger().debug("Task list cleared");
		}
	
	/**
	 * Give the current processing task ID
	 * useful only in mono thread cases
	 */
	public static String getCurrentTask()
		{
		return Variables.getTaskList().get(0).getTaskId();
		}
	
	/**
	 * To get a task using its ID
	 */
	public static Task getTask(String ID)
		{
		for(Task t : Variables.getTaskList())
			{
			if(t.getTaskId().equals(ID))return t;
			}
		return null;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
