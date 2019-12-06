package com.alex.perceler.webserver;

import java.util.ArrayList;

import com.alex.perceler.action.Task;
import com.alex.perceler.device.misc.BasicDevice;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.office.misc.BasicOffice;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.webserver.ManageWebRequest.webRequestType;

/**
 * Used to build web request
 *
 * @author Alexandre RATEL
 */
public class WebRequestBuilder
	{
	
	public static WebRequest buildWebRequest(webRequestType type, Object obj)
		{
		switch(type)
			{
			case search:return buildSearchReply((String)obj);
			case getOfficeList:return buildGetOfficeListReply();
			case getDeviceList:return buildGetDeviceListReply();
			case getTaskList:return buildGetTaskListReply();
			case getOffice:return buildGetOfficeReply((String)obj);
			case getDevice:return buildGetDeviceReply((String)obj);
			case getTask:return buildGetTaskReply((String)obj);
			case newTask:return buildNewTaskReply((String)obj);
			case success:return buildSuccess();
			case error:return buildError((String)obj);
			default:return null;
			}
		}
	
	/**
	 * To build the requested request
	 * getOfficeList
	 */
	private static WebRequest buildSearchReply(String search)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.search;
		
		/**
		 * First we look for offices
		 */
		ArrayList<BasicOffice> ol = new ArrayList<BasicOffice>();
		ArrayList<String> lookForDuplicate = new ArrayList<String>();
		ArrayList<BasicDevice> dl = new ArrayList<BasicDevice>();
		
		try
			{
			for(BasicOffice o : Variables.getOfficeList())
				{
				if((o.getFullname().toLowerCase().contains(search.toLowerCase())) ||
						(o.getNewName().toLowerCase().contains(search.toLowerCase())) ||
						(o.getIdcomu().toLowerCase().contains(search.toLowerCase())) ||
						(o.getVoiceIPRange().getSubnet().contains(search)) ||
						(o.getDataIPRange().getSubnet().contains(search)))
					{
					//Then we look for device associated to this office
					if(o.getDeviceList().size() == 0)
						{
						for(BasicDevice d : Variables.getDeviceList())
							{
							if(d.getOfficeid().equals(o.getIdcomu()))
								{
								o.getDeviceList().add(d);
								}
							}
						}
					
					ol.add(o);
					}
				}
			
			for(BasicOffice o : ol)
				{
				for(BasicDevice d : o.getDeviceList())
					{
					lookForDuplicate.add(d.getId());
					}
				}
			
			for(BasicDevice d : Variables.getDeviceList())
				{
				if(((d.getName().toLowerCase().contains(search.toLowerCase())) ||
						d.getIp().contains(search)) &&
						(!lookForDuplicate.contains(d.getId())))
					{
					for(BasicOffice o : Variables.getOfficeList())
						{
						if(d.getOfficeid().equals(o.getIdcomu()))
							{
							d.setOfficename(o.getFullname());
							break;
							}
						}
					dl.add(d);
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while building the search reply for '"+search+"' : "+e.getMessage());
			}
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<offices>\r\n");
		
		//offices
		if(ol.size() != 0)
			{
			for(BasicOffice o : ol)
				{
				content.append("				<office>\r\n");
				content.append("					<id>"+o.getId()+"</id>\r\n");
				content.append("					<idcomu>"+o.getIdcomu()+"</idcomu>\r\n");
				content.append("					<fullname>"+o.getFullname()+"</fullname>\r\n");
				content.append("					<newname>"+o.getNewName()+"</newname>\r\n");
				content.append("					<status>"+o.getStatus().name()+"</status>\r\n");
				content.append("					<devices>\r\n");
				
				if(o.getDeviceList().size()!=0)
					{
					for(BasicDevice d : o.getDeviceList())
						{
						content.append("						<device>\r\n");
						content.append("							<id>"+d.getId()+"</id>\r\n");
						content.append("							<name>"+d.getName()+"</name>\r\n");
						content.append("							<type>"+d.getType()+"</type>\r\n");
						content.append("							<ip>"+d.getIp()+"</ip>\r\n");
						content.append("							<status>"+d.getStatus().name()+"</status>\r\n");
						content.append("						</device>\r\n");
						}
					}
				else
					{
					//content.append("						<device></device>\r\n");
					}
				
				content.append("					</devices>\r\n");
				content.append("				</office>\r\n");
				}
			}
		else
			{
			//content.append("				<office></office>\r\n");
			}
		content.append("			</offices>\r\n");
		
		//Devices
		content.append("			<devices>\r\n");
		
		if(dl.size() != 0)
			{
			for(BasicDevice d : dl)
				{
				content.append("				<device>\r\n");
				content.append("					<id>"+d.getId()+"</id>\r\n");
				content.append("					<name>"+d.getName()+"</name>\r\n");
				content.append("					<type>"+d.getType()+"</type>\r\n");
				content.append("					<ip>"+d.getIp()+"</ip>\r\n");
				content.append("					<officeid>"+d.getOfficeid()+"</officeid>\r\n");
				content.append("					<officename>"+d.getOfficename()+"</officename>\r\n");
				content.append("					<status>"+d.getStatus().name()+"</status>\r\n");
				content.append("				</device>\r\n");
				}
			}
		else
			{
			//content.append("				<device></device>\r\n");
			}
		content.append("			</devices>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getOfficeList
	 */
	private static WebRequest buildGetOfficeListReply()
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getOfficeList;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<offices>\r\n");
		
		try
			{
			for(BasicOffice o : Variables.getOfficeList())
				{
				content.append("				<office>\r\n");
				content.append(getOffice("				", o));
				content.append("					<devices>\r\n");
				
				if(o.getDeviceList().size()!=0)
					{
					for(BasicDevice d : o.getDeviceList())
						{
						content.append("						<device>\r\n");
						content.append("							<id>"+d.getId()+"</id>\r\n");
						content.append("							<name>"+d.getName()+"</name>\r\n");
						content.append("							<type>"+d.getType()+"</type>\r\n");
						content.append("							<ip>"+d.getIp()+"</ip>\r\n");
						content.append("							<status>"+d.getStatus().name()+"</status>\r\n");
						content.append("						</device>\r\n");
						}
					}
				else
					{
					//content.append("						<device></device>\r\n");
					}
				
				content.append("					</devices>\r\n");
				content.append("				</office>\r\n");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the office list : "+e.getMessage());
			content.append("				<office></office>\r\n");
			}
		
		content.append("			</offices>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getDeviceList
	 */
	private static WebRequest buildGetDeviceListReply()
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getDeviceList;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<devices>\r\n");
		
		try
			{
			for(BasicDevice d : Variables.getDeviceList())
				{
				content.append("				<device>\r\n");
				content.append(getDevice("				", d));
				content.append("				</device>\r\n");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the device list : "+e.getMessage());
			content.append("				<device></device>\r\n");
			}
		
		content.append("			</devices>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getTaskList
	 */
	private static WebRequest buildGetTaskListReply()
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getTaskList;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			if(Variables.getTaskList().size()==0)throw new Exception("The tasklist is empty");
			
			for(Task t : Variables.getTaskList())
				{
				content.append("				<task>\r\n");
				content.append(getTask("				", t));
				content.append("				</task>\r\n");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the task status : "+e.getMessage());
			content.append("				<task></task>\r\n");
			}
		
		content.append("			</tasks>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getOffice
	 */
	private static WebRequest buildGetOfficeReply(String officeID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getOffice;
		boolean found = false;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			for(BasicOffice o : Variables.getOfficeList())
				{
				if(o.getId().equals(officeID))
					{
					content.append("			<office>\r\n");
					content.append(getOffice("			", o));
					content.append("				<devices>\r\n");
					
					//Then we look for device associated to this office
					if(o.getDeviceList().size() == 0)
						{
						for(BasicDevice d : Variables.getDeviceList())
							{
							if(d.getOfficeid().equals(o.getIdcomu()))
								{
								o.getDeviceList().add(d);
								}
							}
						}
					
					if(o.getDeviceList().size()!=0)
						{
						for(BasicDevice d : o.getDeviceList())
							{
							content.append("					<device>\r\n");
							content.append("						<id>"+d.getId()+"</id>\r\n");
							content.append("						<name>"+d.getName()+"</name>\r\n");
							content.append("						<type>"+d.getType()+"</type>\r\n");
							content.append("						<ip>"+d.getIp()+"</ip>\r\n");
							content.append("						<status>"+d.getStatus().name()+"</status>\r\n");
							content.append("					</device>\r\n");
							}
						}
					else
						{
						//content.append("						<device></device>\r\n");
						}
					
					content.append("				</devices>\r\n");
					content.append("			</office>\r\n");
					found = true;
					break;
					}
				}
			if(!found)throw new Exception("The following office was not found : "+officeID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the office list : "+e.getMessage());
			content.append("			<office></office>\r\n");
			}
		
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getDevice
	 */
	private static WebRequest buildGetDeviceReply(String deviceID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getDevice;
		boolean found = false;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			for(BasicDevice d : Variables.getDeviceList())
				{
				if(d.getId().equals(deviceID))
					{
					content.append("			<device>\r\n");
					content.append(getDevice("			", d));
					content.append("			</device>\r\n");
					found = true;
					break;
					}
				}
			
			if(!found)throw new Exception("The following office was not found : "+deviceID);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the device list : "+e.getMessage());
			content.append("			<device></device>\r\n");
			}
		
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * getTask
	 */
	private static WebRequest buildGetTaskReply(String taskID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.getTask;
		boolean found = false;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		
		try
			{
			if(taskID.equals(""))
				{
				//We return the current task
				if(Variables.getTaskList().size() != 0)
					{
					content.append("			<task>\r\n");
					content.append(getTask("			", Variables.getTaskList().get(0)));
					content.append("			</task>\r\n");
					}
				else
					{
					throw new Exception("No task in progress to return");
					}
				}
			else
				{
				for(Task t : Variables.getTaskList())
					{
					if(t.getTaskId().equals(taskID))
						{
						content.append("			<task>\r\n");
						content.append(getTask("			", t));
						content.append("			</task>\r\n");
						found = true;
						break;
						}
					}
				if(!found)throw new Exception("The following task was not found : "+taskID);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the task status : "+e.getMessage());
			content.append("			<task></task>\r\n");
			}
		
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To build the requested request
	 * newTask
	 */
	private static WebRequest buildNewTaskReply(String taskID)
		{
		StringBuffer content = new StringBuffer();
		webRequestType type = webRequestType.newTask;
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<taskid>"+taskID+"</taskid>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), type);
		}
	
	/**
	 * To create one office
	 */
	private static String getOffice(String tabs, BasicOffice o)
		{
		StringBuffer content = new StringBuffer();
		
		content.append(tabs+"	<id>"+o.getId()+"</id>\r\n");
		content.append(tabs+"	<idcomu>"+o.getIdcomu()+"</idcomu>\r\n");
		content.append(tabs+"	<idcaf>"+o.getIdCAF()+"</idcaf>\r\n");
		content.append(tabs+"	<fullname>"+o.getFullname()+"</fullname>\r\n");
		content.append(tabs+"	<shortname>"+o.getShortname()+"</shortname>\r\n");
		content.append(tabs+"	<newname>"+o.getNewName()+"</newname>\r\n");
		content.append(tabs+"	<officetype>"+o.getOfficeType()+"</officetype>\r\n");
		content.append(tabs+"	<voiceiprange>"+o.getVoiceIPRange().getCIDRFormat()+"</voiceiprange>\r\n");
		content.append(tabs+"	<dataiprange>"+o.getDataIPRange().getCIDRFormat()+"</dataiprange>\r\n");
		content.append(tabs+"	<newvoiceiprange>"+o.getNewVoiceIPRange().getCIDRFormat()+"</newvoiceiprange>\r\n");
		content.append(tabs+"	<newdataiprange>"+o.getNewDataIPRange().getCIDRFormat()+"</newdataiprange>\r\n");
		
		return content.toString();
		}
	
	
	/**
	 * To create one device
	 */
	private static String getDevice(String tabs, BasicDevice d)
		{
		StringBuffer content = new StringBuffer();
		
		content.append(tabs+"	<id>"+d.getId()+"</id>\r\n");
		content.append(tabs+"	<name>"+d.getName()+"</name>\r\n");
		content.append(tabs+"	<type>"+d.getType()+"</type>\r\n");
		content.append(tabs+"	<ip>"+d.getIp()+"</ip>\r\n");
		content.append(tabs+"	<mask>"+d.getMask()+"</mask>\r\n");
		content.append(tabs+"	<gateway>"+d.getGateway()+"</gateway>\r\n");
		content.append(tabs+"	<newip>"+d.getNewip()+"</newip>\r\n");
		content.append(tabs+"	<newmask>"+d.getNewmask()+"</newmask>\r\n");
		content.append(tabs+"	<newgateway>"+d.getNewgateway()+"</newgateway>\r\n");
		content.append(tabs+"	<officeid>"+d.getOfficeid()+"</officeid>\r\n");
		
		return content.toString();
		}
	
	/**
	 * To create one task
	 */
	private static String getTask(String tabs, Task t)
		{
		StringBuffer content = new StringBuffer();
		
		content.append(tabs+"	<id>"+t.getTaskId()+"</id>\r\n");
		content.append(tabs+"	<overallstatus>"+t.getStatus()+"</overallstatus>\r\n");
		content.append(tabs+"	<itemlist>\r\n");
		
		for(ItemToMigrate itm : t.getTodoList())
			{
			content.append(tabs+"	<item>\r\n");
			content.append(tabs+"		<id>"+itm.getId()+"</id>\r\n");
			content.append(tabs+"		<type>"+itm.getType()+"</type>\r\n");
			content.append(tabs+"		<info>"+itm.getInfo()+"</info>\r\n");
			content.append(tabs+"		<status>"+itm.getStatus().name()+"</status>\r\n");
			content.append(tabs+"		<desc>"+itm.getDetailedStatus()+"</desc>\r\n");
			content.append(tabs+"	</item>\r\n");
			}
		
		content.append(tabs+"	</itemlist>\r\n");
		
		return content.toString();
		}
	
	/**
	 * To build the requested request
	 * success
	 */
	private static WebRequest buildSuccess()
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>success</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<success></success>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.success);
		}
	
	/**
	 * To build the requested request
	 * error
	 */
	private static WebRequest buildError(String message)
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>error</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<error>"+message+"</error>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.error);
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
