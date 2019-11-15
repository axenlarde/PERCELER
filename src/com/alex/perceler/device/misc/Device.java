package com.alex.perceler.device.misc;

import java.util.ArrayList;

import com.alex.perceler.cli.OneLine;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.office.items.SRSTReference;
import com.alex.perceler.office.items.TrunkSip;
import com.alex.perceler.utils.Variables;

/**
 * Represent a device
 *
 * @author Alexandre RATEL
 */
public class Device extends ItemToMigrate
	{
	/**
	 * Variables
	 */
	private String ip,
	mask,
	gateway,
	officeid,
	newip,
	newgateway,
	newmask;
	
	private boolean reachable;
	
	private ArrayList<OneLine> cliList;

	public Device(itmType type, String name, String ip, String mask, String gateway, String officeid, String newip,
			String newgateway, String newmask) throws Exception
		{
		super(type, name);
		this.ip = ip;
		this.mask = mask;
		this.gateway = gateway;
		this.officeid = officeid;
		this.newip = newip;
		this.newgateway = newgateway;
		this.newmask = newmask;
		}
	
	@Override
	public String getInfo()
		{
		return name+" "+
		ip+" "+
		type;
		}
	
	@Override
	public void init() throws Exception
		{
		// TODO Auto-generated method stub
		
		}
	
	//To init the item
	@Override
	public void build() throws Exception
		{
		/**
		 * First we find the related CUCM items
		 * Only for ISR !
		 */
		if(type.equals(itmType.isr))
			{
			/**
			 * We need to find 2 items : SRST reference and related sip trunk
			 * To be sure to find the right ones we have to send SQL request to the CUCM
			 * Just using the database is unreliable
			 */
			//Trunk SIP
			ArrayList<TrunkSip> stList = DeviceTools.getSIPTrunk(ip);
			if(stList != null)
				{
				axlList.addAll(stList);
				}
			else
				{
				Variables.getLogger().debug("No sip trunk found for the following ip :"+ip);
				}
			
			//SRST reference
			ArrayList<SRSTReference> srstRefList = DeviceTools.getSRSTReference(ip);
			if(srstRefList != null)
				{
				axlList.addAll(srstRefList);
				}
			else
				{
				Variables.getLogger().debug("No SRST reference found for the following ip :"+ip);
				}
			}
		
		/**
		 * Then we initialize the CLI list
		 */
		//cliList = DeviceTools.
		}

	@Override
	public void startSurvey() throws Exception
		{
		Variables.getLogger().debug("Starting survey for "+type+" "+name);
		
		reachable = DeviceTools.ping(ip);
		
		for(ItemToInject iti : axlList)
			{
			//Here we just want to know if the object exists in the CUCM
			//So this will just try to get the UUID of the item
			if(!iti.isExisting())
				{
				//If the item doesn't exist we set the item as unreachable
				//Doing so, we discourage the user to migrate it
				//Maybe we will change that later
				Variables.getLogger().debug("The item "+iti.getType().name()+" "+iti.getName()+" doesn't exist in the CUCM, so we declare the whole item as unreachable");
				reachable = false;
				break;
				}
			}
		}

	@Override
	public void migrate() throws Exception
		{
		Variables.getLogger().debug("Starting migration for "+type+" "+name);
		
		//To be written
		
		for(ItemToInject iti : axlList)
			{
			iti.update();
			}
		}
	
	@Override
	public void rollback() throws Exception
		{
		Variables.getLogger().debug("Starting rollback for "+type+" "+name);
		
		
		}
	
	
	
	
	
	public String getIp()
		{
		return ip;
		}

	public void setIp(String ip)
		{
		this.ip = ip;
		}

	public String getMask()
		{
		return mask;
		}

	public void setMask(String mask)
		{
		this.mask = mask;
		}

	public String getGateway()
		{
		return gateway;
		}

	public void setGateway(String gateway)
		{
		this.gateway = gateway;
		}

	public String getOfficeid()
		{
		return officeid;
		}

	public void setOfficeid(String officeid)
		{
		this.officeid = officeid;
		}

	public String getNewip()
		{
		return newip;
		}

	public void setNewip(String newip)
		{
		this.newip = newip;
		}

	public String getNewgateway()
		{
		return newgateway;
		}

	public void setNewgateway(String newgateway)
		{
		this.newgateway = newgateway;
		}

	public String getNewmask()
		{
		return newmask;
		}

	public void setNewmask(String newmask)
		{
		this.newmask = newmask;
		}

	public ArrayList<OneLine> getCliList()
		{
		return cliList;
		}

	public void setCliList(ArrayList<OneLine> cliList)
		{
		this.cliList = cliList;
		}

	public boolean isReachable()
		{
		return reachable;
		}

	public void setReachable(boolean reachable)
		{
		this.reachable = reachable;
		}

		
	/*2019*//*RATEL Alexandre 8)*/
	}
