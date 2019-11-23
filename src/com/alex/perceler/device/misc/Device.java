package com.alex.perceler.device.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.perceler.cli.CliInjector;
import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
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
	newmask,
	user,
	password;
	
	private boolean reachable;
	private CliInjector cliInjector;
	private cliProtocol connexionProtocol;

	public Device(itmType type, String id, String name, String ip, String mask, String gateway, String officeid, String newip,
			String newgateway, String newmask, actionType action, String user, String password,
			CliProfile cliProfile, cliProtocol connexionProtocol) throws Exception
		{
		super(type, name, id, action);
		this.officeid = officeid;
		this.user = user;
		this.password = password;
		this.connexionProtocol = connexionProtocol;
		this.cliInjector = new CliInjector(this, cliProfile);
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(newip))?newip:"";
			this.mask = newmask;
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(newgateway))?newgateway:"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(gateway))?gateway:"";
			this.newmask = mask;
			}
		else
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
			this.mask = mask;
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(gateway))?gateway:"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(newip))?newip:"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(newgateway))?newgateway:"";
			this.newmask = newmask;
			}
		}
	
	public Device(BasicDevice bd, actionType action)
		{
		super(bd.getType(), bd.getName(), bd.getId(), action);
		this.officeid = bd.getOfficeid();
		this.user = bd.getUser();
		this.password = bd.getPassword();
		this.connexionProtocol = bd.getConnexionProtocol();
		this.cliInjector = new CliInjector(this, bd.getCliProfile());
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewip()))?bd.getNewip():"";
			this.mask = bd.getNewmask();
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewgateway()))?bd.getNewgateway():"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getIp()))?bd.getIp():"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getGateway()))?bd.getGateway():"";
			this.newmask = bd.getMask();
			}
		else
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getIp()))?bd.getIp():"";
			this.mask = bd.getMask();
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getGateway()))?bd.getGateway():"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewip()))?bd.getNewip():"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewgateway()))?bd.getNewgateway():"";
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
				Variables.getLogger().debug(getInfo()+" : Sip trunk found");
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
				Variables.getLogger().debug(getInfo()+" : SRST reference found");
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
		cliInjector.build();
		}

	@Override
	public void doStartSurvey() throws Exception
		{
		if((status.equals(itmStatus.postaudit)) ||
				(status.equals(itmStatus.done)))
			{
			reachable = DeviceTools.ping(newip);
			}
		else
			{
			reachable = DeviceTools.ping(ip);
			}
		
		if(reachable)
			{
			Variables.getLogger().debug(getInfo()+" : The device is reachable (ping)");
			}
		else
			{
			Variables.getLogger().debug("The device could not been reach (ping failed)");
			}
		/*if(!reachable)
			{
			//If the device is not reachable, we should not update the CUCM data. So we disable the entire item
			status = itmStatus.disabled;
			addError(new ErrorTemplate(getInfo()+" : The device could not been reach (ping failed)"));
			}*/
		}

	@Override
	public void doUpdate() throws Exception
		{
		/**
		 * Before updating we change the values with the new ones
		 * Example : replace ip with newip 
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
			addError(new ErrorTemplate("Failed to reset the sip trunk for "+type+" "+name));
			}
		}
	
	/**
	 * Will return a detailed status of the item
	 * For instance will return phone status
	 */
	public String doGetDetailedStatus()
		{
		StringBuffer s = new StringBuffer("");
		s.append("Reachable : "+reachable+"\r\n");
		s.append("\r\n");
		s.append("Cli error list : \r\n");
		
		for(ErrorTemplate e : cliInjector.getErrorList())
			{
			s.append(e.getErrorDesc()+"\r\n");
			}
		
		return s.toString();
		}
	
	/******
	 * Used to return a value based on the string provided
	 * @throws Exception 
	 */
	public String getString(String s) throws Exception
		{
		String tab[] = s.split("\\.");
		
		if(tab.length == 2)
			{
			for(Field f : this.getClass().getDeclaredFields())
				{
				if(f.getName().equals(tab[1]))
					{
					String found = (String) f.get(this);
					return (String) f.get(this);
					}
				}
			}
		
		return null;
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

	public boolean isReachable()
		{
		return reachable;
		}

	public void setReachable(boolean reachable)
		{
		this.reachable = reachable;
		}

	public cliProtocol getConnexionProtocol()
		{
		return connexionProtocol;
		}

	public void setConnexionProtocol(cliProtocol connexionProtocol)
		{
		this.connexionProtocol = connexionProtocol;
		}
	
	public CliInjector getCliInjector()
		{
		return cliInjector;
		}

	public void setCliInjector(CliInjector cliInjector)
		{
		this.cliInjector = cliInjector;
		}

	public String getUser()
		{
		return user;
		}

	public void setUser(String user)
		{
		this.user = user;
		}

	public String getPassword()
		{
		return password;
		}

	public void setPassword(String password)
		{
		this.password = password;
		}

		
	/*2019*//*RATEL Alexandre 8)*/
	}
