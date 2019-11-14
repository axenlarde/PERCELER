package com.alex.perceler.webserver;

import java.util.ArrayList;

import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
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
	/**
	 * web request
	 */
	public enum webRequestType
		{
		getCUCMUsers,
		getSalesforceUsers,
		getUserList,
		getUser,
		addUser,
		updateUser,
		deleteUser,
		getSettings,
		updateSettings,
		success,
		error
		}
	
	/**
	 * Will decode web request
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>webRequestType<type>
	 * 		<content>
	 * 			<...>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
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
	 * Get all the CUCM users
	 */
	/*
	public synchronized static WebRequest getCUCMUsers(String content)
		{
		try
			{
			com.cisco.axl.api._10.ListUserReq req = new com.cisco.axl.api._10.ListUserReq();
			com.cisco.axl.api._10.ListUserReq.SearchCriteria sc = new com.cisco.axl.api._10.ListUserReq.SearchCriteria();
			com.cisco.axl.api._10.LUser rt = new com.cisco.axl.api._10.LUser();
			
			sc.setUserid("");
			rt.setUuid("");
			rt.setLastName("");
			rt.setFirstName("");
			rt.setUserid("");
			rt.setMailid("");
			rt.setTelephoneNumber("");//Should be improved, we should instead retrieve the associated line
			
			req.setSearchCriteria(sc);
			req.setReturnedTags(rt);
			
			com.cisco.axl.api._10.ListUserRes resp = Variables.getAXLConnectionToCUCMV105().listUser(req);
			
			ArrayList<CUCMUser> ul = new ArrayList<CUCMUser>();
			
			for(LUser u : resp.getReturn().getUser())
				{
				ul.add(new CUCMUser(u.getFirstName(),
						u.getLastName(),
						u.getUserid(),
						u.getMailid(),
						u.getTelephoneNumber(),
						u.getUuid()));
				}
			
			Variables.getLogger().debug(ul.size()+" user retrieved form CUCM");
			
			return WebRequestBuilder.buildGetCUCMUsersReply(ul);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while fetching CUCM users : "+e.getMessage(),e);
			}
		
		return null;
		}*/
	
	/**
	 * Get the internal user list
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>getUserList<type>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static WebRequest getUserList()
		{
		return WebRequestBuilder.buildGetUserList();
		}
	
	/**
	 * Add a new user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>addUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<...>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static WebRequest addUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			/**
			 * At this point we just receive the salesforce user id and the extension
			 * from the web portal. We do not check if the extension is correct and we do not
			 * try to find the matching cucm user. In addition, after adding the user
			 * we still have to manually add the ECCP to the line and add the device to the
			 * application user. So could be a lot improved
			 */
			
			//using the id received we get the user info from salesforce
			String salesforceID = UsefulMethod.getItemByName("salesforceid", t);
			SalesForceUser su = SalesForceManager.getUser(salesforceID);
			
			ManageUserFile.addUser(su.getFirstName(),
					su.getLastName(),
					UsefulMethod.getItemByName("extension", t),
					su.getEmail(),
					"forlateruse",
					salesforceID,
					true,
					true,
					true,
					UsefulMethod.getTargetOption("defaultbrowser"),
					"");
			
			return WebRequestBuilder.buildSuccess();
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to add a new user : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Update a given user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>updateUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<id>
	 * 				<extension>
	 * 				<defaultbrowser>
	 * 				<browseroptions>
	 * 				<incomingCallPopup>
	 * 				<reverseLookup>
	 * 				<emailReminder>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static WebRequest updateUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			ManageUserFile.updateUser(UsefulMethod.getItemByName("id", t),
					UsefulMethod.getItemByName("extension", t),
					UsefulMethod.getItemByName("defaultbrowser", t),
					UsefulMethod.getItemByName("browseroptions", t),
					Boolean.parseBoolean(UsefulMethod.getItemByName("incomingcallpopup", t)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("reverselookup", t)),
					Boolean.parseBoolean(UsefulMethod.getItemByName("emailreminder", t)));
			
			return WebRequestBuilder.buildSuccess();
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to add a new user : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Delete a given user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>updateUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<id>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static WebRequest deleteUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			ManageUserFile.deleteUser(UsefulMethod.getItemByName("id", t));
			
			return WebRequestBuilder.buildSuccess();
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to delete user : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Get a given user
	 * 
	 * Web request format :
	 * <xml>
	 * 	<request>
	 * 		<type>getUser<type>
	 * 		<content>
	 * 			<user>
	 * 				<id>
	 * 			</user>
	 * 		</content>
	 * 	</request>
 	 * </xml> 
	 */
	public synchronized static WebRequest getUser(String content)
		{
		try
			{
			ArrayList<String> params = new ArrayList<String>();
			params.add("request");
			params.add("content");
			params.add("user");
			
			ArrayList<String[][]> parsed = xMLGear.getResultListTab(content, params);
			String[][] t = parsed.get(0);
			
			for(User u : Variables.getUserList())
				{
				if(u.getID().equals(UsefulMethod.getItemByName("id", t)))
					{
					return WebRequestBuilder.buildGetUserReply(u);
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Failed to retreive the given user : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * get settings
	 */
	public synchronized static WebRequest getSettings()
		{
		//To be written
		
		return null;
		}
	
	/**
	 * update settings
	 */
	public synchronized static WebRequest updateSettings(String content)
		{
		//To be written
		return null;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
