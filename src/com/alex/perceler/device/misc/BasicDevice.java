package com.alex.perceler.device.misc;

import org.apache.commons.codec.digest.DigestUtils;

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
	newip,
	newgateway,
	newmask;

	public BasicDevice(itmType type, String name, String ip, String mask, String gateway, String officeid, String newip, String newgateway,
			String newmask)
		{
		super();
		this.name = name;
		this.type = type;
		this.ip = ip;
		this.mask = mask;
		this.gateway = gateway;
		this.officeid = officeid;
		this.newip = newip;
		this.newgateway = newgateway;
		this.newmask = newmask;
		id = DigestUtils.md5Hex(name+ip+officeid);
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
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
