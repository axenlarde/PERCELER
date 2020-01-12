package com.alex.perceler.device.misc;

import java.lang.reflect.Field;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.misc.SimpleItem;
import com.alex.perceler.utils.Variables.itmType;

/**
 * Used to store a device data
 *
 * @author Alexandre RATEL
 */
public class BasicDevice extends SimpleItem
	{
	/**
	 * Variables
	 */
	protected itmType type;
	protected String id,
	name,
	ip,
	mask,
	gateway,
	officeid,
	officename,
	newip,
	newgateway,
	newmask,
	user,
	password;
	protected CliProfile cliProfile;
	protected cliProtocol connexionProtocol;
	protected basicItemStatus status;
	
	
	public BasicDevice(itmType type, String name, String ip, String mask, String gateway, String officeid, String newip,
			String newgateway, String newmask, String user, String password, CliProfile cliProfile,
			cliProtocol connexionProtocol) throws Exception
		{
		super(name+ip+officeid);
		this.type = type;
		this.name = name;
		this.officeid = officeid;
		this.user = user;
		this.password = password;
		this.cliProfile = cliProfile;
		this.connexionProtocol = connexionProtocol;
		
		this.ip = (InetAddressValidator.getInstance().isValidInet4Address(ip))?ip:"";
		this.mask = (InetAddressValidator.getInstance().isValidInet4Address(mask))?mask:"";
		this.gateway = (InetAddressValidator.getInstance().isValidInet4Address(gateway))?gateway:"";
		this.newip = (InetAddressValidator.getInstance().isValidInet4Address(newip))?newip:"";
		this.newgateway = (InetAddressValidator.getInstance().isValidInet4Address(newgateway))?newgateway:"";
		this.newmask = (InetAddressValidator.getInstance().isValidInet4Address(newmask))?newmask:"";
		
		if(this.ip.isEmpty() || this.mask.isEmpty() || this.gateway.isEmpty() || this.newip.isEmpty() || this.newmask.isEmpty() || this.newgateway.isEmpty())
			{
			throw new Exception(getInfo()+" : A mandatory field was either incorrect or empty");
			}
		}

	public String getInfo()
		{
		return type+" "+
		ip+" "+
		name;
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
			}
		
		return null;
		}

	public itmType getType()
		{
		return type;
		}

	public void setType(itmType type)
		{
		this.type = type;
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

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public CliProfile getCliProfile()
		{
		return cliProfile;
		}

	public void setCliProfile(CliProfile cliProfile)
		{
		this.cliProfile = cliProfile;
		}

	public cliProtocol getConnexionProtocol()
		{
		return connexionProtocol;
		}

	public void setConnexionProtocol(cliProtocol connexionProtocol)
		{
		this.connexionProtocol = connexionProtocol;
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

	public String getOfficename()
		{
		return officename;
		}

	public void setOfficename(String officename)
		{
		this.officename = officename;
		}

	
	/*2019*//*RATEL Alexandre 8)*/
	}
