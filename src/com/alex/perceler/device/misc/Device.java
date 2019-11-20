package com.alex.perceler.device.misc;

import java.util.ArrayList;

import com.alex.perceler.cli.OneLine;
import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.office.items.SRSTReference;
import com.alex.perceler.office.items.TrunkSip;
import com.alex.perceler.soap.items.SipTrunkDestination;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itemType;
import com.alex.perceler.utils.Variables.itmType;
import com.alex.perceler.utils.Variables.statusType;

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

	public Device(itmType type, String id, String name, String ip, String mask, String gateway, String officeid, String newip,
			String newgateway, String newmask, actionType action) throws Exception
		{
		super(type, name, id, action);
		this.officeid = officeid;
		
		this.ip = ip;
		this.mask = mask;
		this.gateway = gateway;
		this.newip = newip;
		this.newgateway = newgateway;
		this.newmask = newmask;
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.ip = newip;
			this.mask = newmask;
			this.gateway = newgateway;
			this.newip = ip;
			this.newgateway = gateway;
			this.newmask = mask;
			}
		else
			{
			this.ip = ip;
			this.mask = mask;
			this.gateway = gateway;
			this.newip = newip;
			this.newgateway = newgateway;
			this.newmask = newmask;
			}
		}
	
	public Device(BasicDevice bd, actionType action)
		{
		super(bd.getType(), bd.getName(), bd.getId(), action);
		this.officeid = bd.getOfficeid();
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.ip = bd.getNewip();
			this.mask = bd.getNewmask();
			this.gateway = bd.getNewgateway();
			this.newip = bd.getIp();
			this.newgateway = bd.getGateway();
			this.newmask = bd.getMask();
			}
		else
			{
			this.ip = bd.getIp();
			this.mask = bd.getMask();
			this.gateway = bd.getGateway();
			this.newip = bd.getNewip();
			this.newgateway = bd.getNewgateway();
			this.newmask = bd.getNewmask();
			}
		}
	
	@Override
	public String getInfo()
		{
		return name+" "+
		ip+" "+
		type;
		}
	
	@Override
	public void doInit() throws Exception
		{
		//Write something if needed
		}
	
	//To init the item
	@Override
	public void doBuild() throws Exception
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
	public void doStartSurvey() throws Exception
		{
		if(type.equals(itmType.phone))
			{
			
			}
		else
			{
			reachable = DeviceTools.ping(ip);
			if(!reachable)
				{
				/**
				 * If the device is not reachable, we should not update the CUCM data. So we disable the entire item
				 */
				status = itmStatus.disabled;
				errorList.add(new ErrorTemplate(name+" : The device could not been reach (ping failed)"));
				}
			}
		}

	@Override
	public void doUpdate() throws Exception
		{
		/**
		 * here we first used the current values to check that
		 * the items exists. Now we are about to proceed with the update so
		 * we change the values with the new ones 
		 */
		for(ItemToInject iti : axlList)
			{
			if(iti.getType().equals(itemType.trunksip))
				{
				for(SipTrunkDestination std : ((TrunkSip)iti).getMyDestinations())
					{
					if(std.getAddressIpv4().equals(ip))std.setAddressIpv4(newip);
					}
				}
			else if(iti.getType().equals(itemType.srstreference))
				{
				if(((SRSTReference)iti).getIpAddress().equals(ip))((SRSTReference)iti).setIpAddress(newip);
				}
			}
		
		//Same for clid
		}
	
	@Override
	public void doResolve() throws Exception
		{
		//Write something if needed
		}
	
	@Override
	public void doReset()
		{
		try
			{
			for(ItemToInject iti : axlList)
				{
				if(iti.getType().equals(itemType.trunksip))
					{
					//In case of trunk SIP we must reset it to apply changes
					((TrunkSip) iti).reset();
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while reseting sip trunk for "+type+" "+name+" "+e.getMessage(), e);
			errorList.add(new ErrorTemplate("Failed to reset the sip trunk for "+type+" "+name));
			}
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
