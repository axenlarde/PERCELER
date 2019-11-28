package com.alex.perceler.device.misc;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.utils.Variables.basicItemStatus;
import com.alex.perceler.utils.Variables.itmType;

/**
 * Used to store a device data
 *
 * @author Alexandre RATEL
 */
public class BasicDevice
	{
	/**
	 * Variables
	 */
	private itmType type;
	private String id,
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
	private CliProfile cliProfile;
	private cliProtocol connexionProtocol;
	private basicItemStatus status;
	
	
	public BasicDevice(itmType type, String name, String ip, String mask, String gateway, String officeid, String newip,
			String newgateway, String newmask, String user, String password, CliProfile cliProfile,
			cliProtocol connexionProtocol)
		{
		super();
		this.type = type;
		this.name = name;
		this.ip = ip;
		this.mask = mask;
		this.gateway = gateway;
		this.officeid = officeid;
		this.newip = newip;
		this.newgateway = newgateway;
		this.newmask = newmask;
		this.user = user;
		this.password = password;
		this.cliProfile = cliProfile;
		this.connexionProtocol = connexionProtocol;
		this.id = DigestUtils.md5Hex(name+ip+officeid);
		status = basicItemStatus.tomigrate;
		}

	public String getInfo()
		{
		return name+" "+
		ip+" "+
		type;
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

	public String getId()
		{
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
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

	public basicItemStatus getStatus()
		{
		return status;
		}

	public void setStatus(basicItemStatus status)
		{
		this.status = status;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
