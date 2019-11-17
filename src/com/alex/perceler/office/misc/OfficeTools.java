package com.alex.perceler.office.misc;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alex.perceler.misc.SimpleRequest;
import com.alex.perceler.office.items.MobilityInfo;
import com.alex.perceler.office.items.SRSTReference;
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
		String request = "select name,pkid from devicemobilityinfo where subnet='"+range.getIpRange()+"' and subnetmasksz='"+range.getMask()+"'";
		try
			{
			List<Object> reply = SimpleRequest.doSQLQuery(request);
			MobilityInfo mi = new MobilityInfo("TBD");
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				mi.setSubnet(range.getIpRange());
				mi.setSubnetMask(range.getMask());
				
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
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
