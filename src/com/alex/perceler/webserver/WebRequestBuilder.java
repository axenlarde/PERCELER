package com.alex.perceler.webserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.alex.perceler.utils.Variables;

/**
 * Used to build web request
 *
 * @author Alexandre RATEL
 */
public class WebRequestBuilder
	{
	
	/**
	 * To build the requested request
	 */
	public static WebRequest buildGetCUCMUsersReply(ArrayList<CUCMUser> ul)
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<cucmusers>\r\n");
		
		for(CUCMUser u : ul)
			{
			content.append("		<cucmuser>\r\n");
			content.append("			<firstname>"+u.getFirstName()+"</firstname>\r\n");
			content.append("			<lastname>"+u.getLastName()+"</lastname>\r\n");
			content.append("			<userid>"+u.getUserID()+"</userid>\r\n");
			content.append("			<email>"+u.getEmail()+"</email>\r\n");
			content.append("			<extension>"+u.getExtension()+"</extension>\r\n");
			content.append("			<uuid>"+u.getUUID()+"</uuid>\r\n");
			content.append("		</cucmuser>\r\n");
			}
		
		content.append("	</cucmusers>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.getCUCMUsers);
		}
	
	/**
	 * To build the requested request
	 */
	public static WebRequest buildGetUserList()
		{
		try
			{
			StringBuffer content = new StringBuffer();
			
			content.append("<xml>\r\n");
			content.append("	<users>\r\n");
			
			for(User u : Variables.getUserList())
				{
				content.append("		<user>\r\n");
				content.append("			<firstname>"+u.getFirstName()+"</firstname>\r\n");
				content.append("			<lastname>"+u.getLastName()+"</lastname>\r\n");
				content.append("			<extension>"+u.getExtension()+"</extension>\r\n");
				content.append("			<status>"+u.getStatus()+"</status>\r\n");
				content.append("			<id>"+u.getID()+"</id>\r\n");
				content.append("		</user>\r\n");
				}
			
			content.append("	</users>\r\n");
			content.append("</xml>\r\n");
			
			return new WebRequest(content.toString(), webRequestType.getUserList);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while building user list : "+e.getMessage(),e);
			}
		/*
		try
			{
			return new WebRequest(xMLReader.fileRead("./"+Variables.getUserFileName()), webRequestType.getUserList);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while building user list : "+e.getMessage(),e);
			}*/
		
		return null;
		}
	
	/**
	 * To build the requested request
	 */
	public static WebRequest buildGetUserReply(User u)
		{
		try
			{
			StringBuffer content = new StringBuffer();
			
			content.append("<xml>\r\n");
			content.append("	<user>\r\n");
			content.append("		<firstname>"+u.getFirstName()+"</firstname>\r\n");
			content.append("		<lastname>"+u.getLastName()+"</lastname>\r\n");
			content.append("		<extension>"+u.getExtension()+"</extension>\r\n");
			content.append("		<email>"+u.getEmail()+"</email>\r\n");
			content.append("		<defaultbrowser>"+u.getDefaultBrowser()+"</defaultbrowser>\r\n");
			content.append("		<browseroptions>"+u.getBrowserOptions()+"</browseroptions>\r\n");
			content.append("		<emailreminder>"+u.isEmailReminder()+"</emailreminder>\r\n");
			content.append("		<incomingcallpopup>"+u.isIncomingCallPopup()+"</incomingcallpopup>\r\n");
			content.append("		<reverselookup>"+u.isReverseLookup()+"</reverselookup>\r\n");
			content.append("		<status>"+u.getStatus()+"</status>\r\n");
			content.append("	</user>\r\n");
			content.append("</xml>\r\n");
			
			return new WebRequest(content.toString(), webRequestType.getUser);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Error while building user list : "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * To build the requested request
	 */
	public synchronized static WebRequest buildSuccess()
		{
		return new WebRequest("Done !", webRequestType.success);
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
