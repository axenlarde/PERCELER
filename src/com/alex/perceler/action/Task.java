package com.alex.perceler.action;

import java.util.ArrayList;
import java.util.Arrays;

import com.alex.perceler.cli.CliInjector;
import com.alex.perceler.cli.CliManager;
import com.alex.perceler.device.misc.Ascom;
import com.alex.perceler.device.misc.Device;
import com.alex.perceler.device.misc.PingManager;
import com.alex.perceler.device.misc.PingProcess;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.misc.ItemToMigrate.itmStatus;
import com.alex.perceler.misc.storedUUID;
import com.alex.perceler.remoteclient.MultipleClientManager;
import com.alex.perceler.remoteclient.RequestBuilder;
import com.alex.perceler.utils.LanguageManagement;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.ascomType;

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
	private PingManager pingManager;
	
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
			if(stop)break;
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
		/**
		 * First we take all the devices and start the ping manager process
		 * Because each ping process is a different thread we can start them all
		 * simultaneously to save time
		 */
		PingManager pm = new PingManager();
		
		for(ItemToMigrate myToDo : todoList)
			{
			if(!myToDo.getStatus().equals(itmStatus.disabled))
				{
				if(myToDo instanceof Device)
					{
					pm.getPingList().add(new PingProcess((Device)myToDo));
					}
				}
			else Variables.getLogger().debug("The following item has been disabled so we do not process it : "+myToDo.getInfo());
			}
		if((pm.getPingList().size() != 0) && (!stop))pm.start();
		
		/**
		 * It is better to wait for the ping manager to end before continue
		 */
		Variables.getLogger().debug("We wait for the ping manager to end");
		while(pm.isAlive() && (!stop))
			{
			this.sleep(500);
			}
		Variables.getLogger().debug("Ping manager ends");
		
		for(ItemToMigrate myToDo : todoList)
			{
			if(stop)break;
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
	 * @throws Exception 
	 */
	private void startUpdate() throws Exception
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
					CliInjector clii = ((Device)myToDo).getCliInjector();
					if(clii != null)cliManager.getCliIList().add(clii);
					}
				}
			else Variables.getLogger().debug("The following item has been disabled so we do not process it : "+myToDo.getInfo());
			}
		if((cliManager.getCliIList().size() != 0) && (!stop))cliManager.start();
		
		/**
		 * It is better to wait for the cli task to end before starting the AXL ones
		 * For instance, it is pointless to reset a sip trunk before changing the ISR ip
		 */
		Variables.getLogger().debug("We wait for the cli tasks to end");
		while(cliManager.isAlive() && (!stop))
			{
			this.sleep(500);
			}
		Variables.getLogger().debug("Cli tasks end so we can start the AXL ones");
		
		/**
		 * Then we start the axl item updates wich is not multithreaded
		 * so it has to be item by item
		 */
		for(ItemToMigrate myToDo : todoList)
			{
			try
				{
				if(stop)break;
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
				if(stop)break;
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
			if((myToDo.getStatus().equals(itmStatus.error)) ||
					(myToDo.getStatus().equals(itmStatus.disabled)))
				{
				Variables.getLogger().debug(myToDo.getInfo()+" : In this status '"+myToDo.getStatus()+"' we do not modify the item status");
				}
			else
				{
				myToDo.setStatus(status);
				}
					
			}
		}
	
	/**
	 * Used to send 
	 */
	private void updateServicePilot()
		{
		try
			{
			String[] s = UsefulMethod.getTargetOption("remoteclientip").split(",");
			ArrayList<String> ipList = new ArrayList<String>(); 
			ipList.addAll(Arrays.asList(s));
			s = UsefulMethod.getTargetOption("remoteclientport").split(",");
			ArrayList<String> portList = new ArrayList<String>(); 
			portList.addAll(Arrays.asList(s));
			MultipleClientManager mcm = new MultipleClientManager(ipList, portList);
			
			boolean updateAnyWay = Boolean.parseBoolean(UsefulMethod.getTargetOption("updateanyway"));
			
			for(ItemToMigrate myToDo : todoList)
				{
				if(!myToDo.getStatus().equals(itmStatus.disabled))
					{
					if(myToDo instanceof Device)
						{
						Device d = (Device)myToDo;
						if(updateAnyWay || d.isReachable())
							{
							Variables.getLogger().debug(d.getInfo()+" : Sending service pilot replace ip request");
							if(myToDo.getAction().equals(actionType.update))mcm.sendRequest(RequestBuilder.buildReplaceIP(d.getIp(), d.getNewip()));
							else if(myToDo.getAction().equals(actionType.rollback))mcm.sendRequest(RequestBuilder.buildReplaceIP(d.getNewip(), d.getIp()));
							}
						else
							{
							Variables.getLogger().debug(d.getName()+" is not reachable so we do not update it in service pilot");
							}
						}
					}
				else
					{
					Variables.getLogger().debug("The following item has been disabled so we do not process it : "+myToDo.getInfo());
					}
				}
			
			Variables.getLogger().debug("Sending service pilot restart service request");
			mcm.sendRequest(RequestBuilder.buildRestartService());
			mcm.close();
			Variables.getLogger().debug("Service pilot update done !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while updating service pilot : "+e.getMessage(),e);
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
				if(!stop)startReset();
				}
			else
				{
				status = itmStatus.preaudit;
				setItemStatus(itmStatus.preaudit);
				if(!stop)startBuildProcess();
				if(!stop)startSurvey();
				
				//We then wait for the user to accept the migration
				int counter = 0;
				while(pause && (!stop))
					{
					this.sleep(500);
					counter++;
					if(counter > 240)//240 = 2 minutes
						{
						Variables.getLogger().debug("Max time reached, we end the task");
						stop = true;
						}
					}
				if((pause == false) && (stop == false))Variables.getLogger().info(action+" task "+taskID+" starts");
				
				if(!stop)status = itmStatus.update;
				if(!stop)setItemStatus(itmStatus.update);
				if(!stop)startUpdate();
				if(!stop)startReset();
				
				if(!stop)status = itmStatus.postaudit;
				if(!stop)setItemStatus(itmStatus.postaudit);
				
				counter = 0;
				while((!stop) && (counter<12))
					{
					/**
					 * We proceed with the survey as long as the user want or max 2 minutes
					 * 
					 * This is necessary to allow phone or device to register after a reset
					 * Indeed, there is no point at raising a warning about a phone which is just taking a little time to register
					 * Instead it is better to try several times to reach it 
					 */
					startSurvey();
					counter++;
					this.sleep(10000);
					}
				
				/**
				 * To finish we update service pilot but only if the devices really change there ip
				 */
				if(status.equals(itmStatus.postaudit))updateServicePilot();
				}
			
			setItemStatus(itmStatus.done);
			status = itmStatus.done;
			end = true;
			Variables.getLogger().info(action+" task "+taskID+" ends");
			
			if(status.equals(itmStatus.postaudit))sendReportEmail();
			
			Variables.setUuidList(new ArrayList<storedUUID>());//We clean the UUID list
			Variables.getLogger().info("UUID list cleared");
			}
		catch (Exception e)
			{
			Variables.getLogger().debug("ERROR : "+e.getMessage(),e);
			status = itmStatus.error;
			}
		}
	
	public void act(taskActionType action) throws Exception
		{
		switch(action)
			{
			case pause:setPause(true);break;
			case start:setPause(false);break;
			case stop:setStop(true);break;
			default:break;
			}
		}
	
	private void sendReportEmail()
		{
		try
			{
			StringBuffer content = new StringBuffer("");
			content.append(LanguageManagement.getString("emailreportcontent"));
			for(ItemToMigrate itm : todoList)
				{
				content.append(itm.getInfo()+" : "+itm.getStatus()+"\r\n");
				}
			content.append(LanguageManagement.getString("emailfooter"));

			UsefulMethod.sendEmailToTheAdminList(
					LanguageManagement.getString("emailreportsubject"),
					content.toString());
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while sending email : "+e.getMessage());
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

	public void setPause(boolean pause) throws Exception
		{
		this.pause = pause;
		
		if(this.isAlive())
			{
			if(this.pause)
				{
				Variables.getLogger().debug("The user asked to pause the task : "+taskID);
				if(cliManager != null)cliManager.setPause(pause);
				}
			else
				{
				Variables.getLogger().debug("The user asked to resume the task : "+taskID);
				if(cliManager != null)cliManager.setPause(pause);
				}
			}
		else
			{
			throw new Exception("The task is finished and therefore cannot be paused");
			}
		}

	public boolean isStop()
		{
		return stop;
		}

	public void setStop(boolean stop) throws Exception
		{
		if(this.isAlive())
			{
			Variables.getLogger().debug("The user asked to stop the task : "+taskID);
			this.stop = stop;
			if(cliManager != null)cliManager.setStop(stop);
			}
		else
			{
			throw new Exception("The task is finished and therefore cannot be stopped again");
			}
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

