package com.alex.perceler.office.misc;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alex.perceler.device.misc.BasicPhone;
import com.alex.perceler.misc.SimpleRequest;
import com.alex.perceler.office.items.MobilityInfo;
import com.alex.perceler.utils.Variables;

/**
 * Toolbox of static method about offices
 *
 * @author Alexandre RATEL
 */
public class OfficeTools
	{
	
	/**
	 * Method used to retrieve the sip trunk related to an ISR using the IP address
	 * Indeed, using the sip trunk name is not a reliable method
	 */
	public static MobilityInfo getMobilityInfo(IPRange range)
		{
		String request = "select name,pkid from devicemobilityinfo where subnet='"+range.getSubnet()+"' and subnetmasksz='"+range.getShortmask()+"'";
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request);
			MobilityInfo mi = new MobilityInfo("TBD");
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				mi.setSubnet(range.getSubnet());
				mi.setSubnetMask(range.getShortmask());
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						mi.setName(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("pkid"))
						{
						mi.setUUID(list.item(i).getTextContent());
						}
					}
				//Only one MobilityInfo can be found so we return right now
				return mi;
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get MobilityInfo for "+range.getInfo()+" "+e.getMessage(),e);
			}
		
		return null;
		}
	
	/**
	 * Used to get a devicepool associated phones
	 */
	public static ArrayList<BasicPhone> getDevicePoolPhoneList(String DevicePoolName)
		{
		Variables.getLogger().debug("Looking for devicepool's phone "+DevicePoolName);
		
		ArrayList<BasicPhone> l = new ArrayList<BasicPhone>();
		
		String request = "select d.name, d.description, tm.name as model from device d, devicepool dp, typemodel tm where dp.pkid=d.fkdevicepool and tm.enum=d.tkmodel and d.tkClass='1' and dp.name='"+DevicePoolName+"'";
		
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request);
			
			for(Object o : reply)
				{
				BasicPhone bp = new BasicPhone("TBD", "", "");
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						bp.setName(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("description"))
						{
						bp.setDescription(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("model"))
						{
						bp.setModel(list.item(i).getTextContent());
						}
					}
				Variables.getLogger().debug("Phone found : "+bp.getName());
				l.add(bp);
				}
			Variables.getLogger().debug("Found "+l.size()+" phones for "+DevicePoolName);
			return l;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get the devicepool's phones for "+DevicePoolName+" "+e.getMessage(),e);
			}
		
		return new ArrayList<BasicPhone>();
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
