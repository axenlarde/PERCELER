package com.alex.perceler.webserver;

import java.util.ArrayList;

import com.alex.perceler.action.TaskManager;
import com.alex.perceler.misc.Task;
import com.alex.perceler.misc.Task.taskActionType;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.xMLGear;
import com.cisco.axl.api._10.LUser;


/**
 * Used to Manage web requests
 *
 * @author Alexandre RATEL
 */
public class ManageWebRequest
	{
	/**
	 * Variables
	 */
	public enum webRequestType
		{
		doAuthenticate,
		getOfficeList,
		getDeviceList,
		getTaskList,
		getDevice,
		getOffice,
		getTask,
		newTask,
		setTask,
		success,
		error
		}
	
	/**
	 * Parse a web request
	 * @throws Exception 
	 */
	public synchronized static WebRequest parseWebRequest(String content) throws Exception
		{
		ArrayList<String> params = new ArrayList<String>();
		
		//We parse the request type
		params.add("request");
		ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
		webRequestType type = webRequestType.valueOf(UsefulMethod.getItemByName("type", parsed.get(0)));
		Variables.getLogger().debug("Web request type found : "+type.name());
		
		return new WebRequest(content, type);
		}
	
	/**
	 * doAuthenticate
	 */
	public synchronized static WebRequest doAuthenticate(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String userID = UsefulMethod.getItemByName("userid", t);
			String password = UsefulMethod.getItemByName("userpassword", t);
			
			if(UsefulMethod.doAuthenticate(userID, password))return WebRequestBuilder.buildWebRequest(webRequestType.success, null);
			else return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing doAuthenticate web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getOfficeList
	 */
	public synchronized static WebRequest getOfficeList()
		{
		try
			{
			return WebRequestBuilder.buildWebRequest(webRequestType.getOfficeList, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getOfficeList web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getDeviceList
	 */
	public synchronized static WebRequest getDeviceList()	
		{
		try
			{
			return WebRequestBuilder.buildWebRequest(webRequestType.getDeviceList, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getDeviceList web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getTaskList
	 */
	public synchronized static WebRequest getTaskList()	
		{
		try
			{
			return WebRequestBuilder.buildWebRequest(webRequestType.getTaskList, null);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getTaskList web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getOffice
	 */
	public synchronized static WebRequest getOffice(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String officeID = UsefulMethod.getItemByName("officeid", t);
			return WebRequestBuilder.buildWebRequest(webRequestType.getOffice, officeID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getOffice web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getDevice
	 */
	public synchronized static WebRequest getDevice(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String deviceID = UsefulMethod.getItemByName("deviceid", t);
			return WebRequestBuilder.buildWebRequest(webRequestType.getDevice, deviceID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getDevice web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * getTask
	 */
	public synchronized static WebRequest getTask(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			String taskID = UsefulMethod.getItemByName("taskid", t);
			return WebRequestBuilder.buildWebRequest(webRequestType.getTask, taskID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing getTask web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * newTask
	 */
	public synchronized static WebRequest newTask(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] tab = parsed.get(0);
			
			actionType action = actionType.valueOf(UsefulMethod.getItemByName("action", tab));
			String ownerid = UsefulMethod.getItemByName("ownerid", tab);
			
			params.add("itemlist");
			parsed = xMLGear.getResultListTab(content, params);
			tab = parsed.get(0);
			ArrayList<String> idList = new ArrayList<String>();
			
			for(String[] t : tab)
				{
				idList.add(t[1]);
				}
			
			String taskID = TaskManager.addNewTask(idList, action, ownerid);
			
			return WebRequestBuilder.buildWebRequest(webRequestType.getTask, taskID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing newTask web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/**
	 * setTask
	 */
	public synchronized static WebRequest setTask(String content)	
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("task");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] tab = parsed.get(0);
			
			String taskID = UsefulMethod.getItemByName("taskid", tab);
			taskActionType action = taskActionType.valueOf(UsefulMethod.getItemByName("action", tab));
			
			for(Task t : Variables.getTaskList())
				{
				if(t.getTaskId().equals(taskID))
					{
					t.act(action);
					break;
					}
				}
			
			return WebRequestBuilder.buildWebRequest(webRequestType.success, taskID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while processing newTask web request : "+e.getMessage(),e);
			}
		
		return WebRequestBuilder.buildWebRequest(webRequestType.error, null);
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
