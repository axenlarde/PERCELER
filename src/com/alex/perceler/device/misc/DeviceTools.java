package com.alex.perceler.device.misc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.misc.SimpleRequest;
import com.alex.perceler.office.items.SRSTReference;
import com.alex.perceler.office.items.TrunkSip;
import com.alex.perceler.office.misc.BasicOffice;
import com.alex.perceler.office.misc.IPRange;
import com.alex.perceler.office.misc.Office;
import com.alex.perceler.soap.items.SipTrunkDestination;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;

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
				mydests.add(std);
				
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
	 * Will return Devices filtered by IP Range
	 * @throws Exception 
	 */
	public static ArrayList<ItemToMigrate> getDevicesByIpRange(IPRange range, actionType action) throws Exception
		{
		ArrayList<ItemToMigrate> l = new ArrayList<ItemToMigrate>();
		
		/**
		 * First we add the known items such as the ISR and offices
		 */
		for(BasicDevice d : Variables.getDeviceList())
			{
			if((UsefulMethod.isIPIncludedInThisSubnet(range,d.getIp())) ||
					(UsefulMethod.isIPIncludedInThisSubnet(range,d.getNewip())))l.add(new Device(d, action));
			}
		
		for(BasicOffice o : Variables.getOfficeList())
			{
			if((o.getVoiceIPRange().compareTo(range)) ||
					(o.getDataIPRange().compareTo(range)) ||
					(o.getNewVoiceIPRange().compareTo(range)) ||
					(o.getNewDataIPRange().compareTo(range)))l.add(new Office(o, action));
			}
		
		/**
		 * Then we look for phones
		 */
		//To be written
		
		return l;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
