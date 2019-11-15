package com.alex.perceler.device.misc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alex.perceler.misc.SimpleRequest;
import com.alex.perceler.office.items.SRSTReference;
import com.alex.perceler.office.items.TrunkSip;
import com.alex.perceler.soap.items.SipTrunkDestination;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;

/**
 * Device toolbox
 *
 * @author Alexandre RATEL
 */
public class DeviceTools
	{
	
	/**
	 * Method used to retrieve the sip trunk destination related to an ISR using the IP address
	 * Indeed, using the sip trunk name is not a reliable method
	 * We just return the UUID of the sip trunk destination to update
	 */
	public static ArrayList<String> getSIPTrunkDestination(String IP)
		{
		String request = "select pkid from siptrunkdestination where address='"+IP+"'";
		ArrayList<String> stdList = new ArrayList<String>();
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request);
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("pkid"))
						{
						String UUID = list.item(i).getTextContent();
						Variables.getLogger().debug("siptrunkdestination UUID found for ip "+IP+" : "+UUID);
						stdList.add(UUID);
						}
					}
				}
			if(stdList.size() != 0)return stdList;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get sip trunk destination for "+IP+" "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Method used to retrieve the sip trunk related to an ISR using the IP address
	 * Indeed, using the sip trunk name is not a reliable method
	 */
	public static ArrayList<TrunkSip> getSIPTrunk(String IP)
		{
		String request = "select d.name as siptrunkname, std.pkid as stdpkid, std.port as port from siptrunkdestination std, sipdevice sd, device d where std.fksipdevice = sd.pkid and sd.fkdevice=d.pkid and std.address='"+IP+"'";
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request);
			ArrayList<TrunkSip> stList = new ArrayList<TrunkSip>();
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				TrunkSip myST = new TrunkSip("TBD");
				SipTrunkDestination std = new SipTrunkDestination(IP, "5060");
				ArrayList<SipTrunkDestination> mydests = new ArrayList<SipTrunkDestination>();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("siptrunkname"))
						{
						myST.setName(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("stdpkid"))
						{
						std.setUUID(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("port"))
						{
						std.setPort(list.item(i).getTextContent());
						}
					}
				myST.setMyDestinations(mydests);
				stList.add(myST);
				}
			if(stList.size() != 0)return stList;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get sip trunk destination for "+IP+" "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Method used to retrieve the sip trunk related to an ISR using the IP address
	 * Indeed, using the sip trunk name is not a reliable method
	 */
	public static ArrayList<SRSTReference> getSRSTReference(String IP)
		{
		String request = "select name,pkid from srst where ipaddr1='"+IP+"' or sipipaddr1='"+IP+"'";
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request);
			ArrayList<SRSTReference> srstRefList = new ArrayList<SRSTReference>();
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				SRSTReference srstRef = new SRSTReference("TBD", IP);
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						srstRef.setName(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("pkid"))
						{
						srstRef.setUUID(list.item(i).getTextContent());
						}
					}
				srstRefList.add(srstRef);
				}
			if(srstRefList.size() != 0)return srstRefList;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get SRST reference for "+IP+" "+e.getMessage(),e);
			}
		
		return null;
		}
	
	
	/**
	 * Method used to "ping" a host to see if it is reachable
	 * Warning : Requires root privilege to work
	 */
	public static boolean ping(String ip)
		{
		try
			{
			InetAddress inet = InetAddress.getByName(ip);
			return inet.isReachable(Integer.parseInt(UsefulMethod.getTargetOption("pingtimeout")));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to ping host : "+e.getMessage(),e);
			}
		
		return false;
		}
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
