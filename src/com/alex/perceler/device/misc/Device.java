package com.alex.perceler.device.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.perceler.cli.CliInjector;
import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.office.items.SRSTReference;
import com.alex.perceler.office.items.TrunkSip;
import com.alex.perceler.soap.items.SipTrunkDestination;
import com.alex.perceler.utils.LanguageManagement;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itemType;
import com.alex.perceler.utils.Variables.itmType;
import com.alex.perceler.utils.Variables.reachableStatus;

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
	protected String ip,
	mask,
	shortmask,
	gateway,
	officeid,
	newip,
	newgateway,
	newmask,
	newshortmask,
	user,
	password;
	
	protected reachableStatus reachable;
	protected CliInjector cliInjector;
	protected cliProtocol connexionProtocol;

	public Device(itmType type, String id, String name, String ip, String mask, String gateway, String officeid, String newip,
			String newgateway, String newmask, actionType action, String user, String password,
			CliProfile cliProfile, cliProtocol connexionProtocol) throws Exception
		{
		super(type, name, id, action);
		this.officeid = officeid;
		this.user = user;
		this.password = password;
		this.connexionProtocol = connexionProtocol;
		if(cliProfile != null)this.cliInjector = new CliInjector(this, cliProfile);
		
		this.reachable = reachableStatus.unknown;
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(newip))?newip:"";
			this.mask = (InetAddressValidator.getInstance().isValidInet4Address(newmask))?newmask:"";
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(newgateway))?newgateway:"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(gateway))?gateway:"";
			this.newmask = (InetAddressValidator.getInstance().isValidInet4Address(mask))?mask:"";
			}
		else
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
			this.mask = (InetAddressValidator.getInstance().isValidInet4Address(mask))?mask:"";
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(gateway))?gateway:"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(newip))?newip:"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(newgateway))?newgateway:"";
			this.newmask = (InetAddressValidator.getInstance().isValidInet4Address(newmask))?newmask:"";
			}
		
		if(ip.isEmpty() || mask.isEmpty() || gateway.isEmpty() || newip.isEmpty() || newmask.isEmpty() || newgateway.isEmpty())
			{
			throw new Exception("A mandatory field was empty");
			}
		
		shortmask = UsefulMethod.convertlongMaskToShortOne(this.mask);
		newshortmask = UsefulMethod.convertlongMaskToShortOne(this.newmask);
		}
	
	public Device(BasicDevice bd, actionType action) throws Exception
		{
		super(bd.getType(), bd.getName(), bd.getId(), action);
		this.officeid = bd.getOfficeid();
		this.user = bd.getUser();
		this.password = bd.getPassword();
		this.connexionProtocol = bd.getConnexionProtocol();
		if(bd.getCliProfile() != null)this.cliInjector = new CliInjector(this, bd.getCliProfile());
		
		this.reachable = reachableStatus.unknown;
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewip()))?bd.getNewip():"";
			this.mask = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewmask()))?bd.getNewmask():"";
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewgateway()))?bd.getNewgateway():"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getIp()))?bd.getIp():"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getGateway()))?bd.getGateway():"";
			this.newmask = (InetAddressValidator.getInstance().isValidInet4Address(bd.getMask()))?bd.getMask():"";
			}
		else
			{
			this.ip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getIp()))?bd.getIp():"";
			this.mask = (InetAddressValidator.getInstance().isValidInet4Address(bd.getMask()))?bd.getMask():"";
			this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getGateway()))?bd.getGateway():"";
			this.newip = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewip()))?bd.getNewip():"";
			this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewgateway()))?bd.getNewgateway():"";
			this.newmask = (InetAddressValidator.getInstance().isValidInet4Address(bd.getNewmask()))?bd.getNewmask():"";
			}
		
		if(ip.isEmpty() || mask.isEmpty() || gateway.isEmpty() || newip.isEmpty() || newmask.isEmpty() || newgateway.isEmpty())
			{
			throw new Exception("A mandatory field was empty");
			}
		
		shortmask = UsefulMethod.convertlongMaskToShortOne(this.mask);
		newshortmask = UsefulMethod.convertlongMaskToShortOne(this.newmask);
		}
	
	@Override
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		
		if(action.equals(actionType.reset))
			{
			s.append(LanguageManagement.getString(type.name())+" ");
			s.append(ip+" ");
			s.append(name);
			}
		else
			{
			s.append(LanguageManagement.getString(type.name())+" ");
			s.append(ip+" > ");
			s.append(newip+" ");
			s.append(name);
			}
		
		int maxchar = 60;
		
		try
			{
			maxchar = Integer.parseInt(UsefulMethod.getTargetOption("maxinfochar"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("Unable to retrieve maxinfochar");
			}
		
		if(s.length()>maxchar)
			{
			String t = s.substring(0, maxchar);
			t = t+"...";
			return t;
			}
		else return s.toString();
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
		if(cliInjector != null)cliInjector.build();
		}

	@Override
	public void doStartSurvey() throws Exception
		{
		if(reachable.equals(reachableStatus.reachable))Variables.getLogger().debug(name+" "+type+" : The device is reachable (ping)");
		else if(reachable.equals(reachableStatus.unknown))Variables.getLogger().debug(name+" "+type+" : The device reachability is currently unknown (ping)");
		else Variables.getLogger().debug(name+" "+type+" : The device could not been reach (ping failed)");
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
		
		switch(reachable)
			{
			case reachable:
				{
				s.append("Reachable : true");
				break;
				}
			case unreachable:
				{
				s.append("Reachable : false");
				break;
				}
			default:
				{
				s.append("Reachable : unknown");
				break;
				}
			}
		
		/*
		if(cliInjector.getErrorList().size() > 0)
			{
			s.append("\r\n");
			s.append("Cli error list : \r\n");
			
			for(ErrorTemplate e : cliInjector.getErrorList())
				{
				s.append(e.getErrorDesc()+"\r\n");
				}
			}*/
		
		if((cliInjector != null) && (cliInjector.getErrorList().size() > 0))s.append(", Error found");
		else if((errorList != null) && (errorList.size() > 0))s.append(", Error found");
		
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
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			//We try also in the super class
			for(Field f : this.getClass().getSuperclass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
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
		shortmask = UsefulMethod.convertlongMaskToShortOne(mask);
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
		newshortmask = UsefulMethod.convertlongMaskToShortOne(newmask);
		}

	public boolean isReachable()
		{
		if(this.reachable.equals(reachableStatus.reachable))return true;
		else return false;
		}

	public void setReachable(reachableStatus reachable)
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

	public String getShortmask()
		{
		return shortmask;
		}

	public String getNewshortmask()
		{
		return newshortmask;
		}

		
	/*2019*//*RATEL Alexandre 8)*/
	}
