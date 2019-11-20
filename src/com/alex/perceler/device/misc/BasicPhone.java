package com.alex.perceler.device.misc;

/**
 * Used to represent a phone
 *
 * @author Alexandre RATEL
 */
public class BasicPhone
	{
	/**
	 * Variables
	 */
	public enum phoneStatus
		{
		Registered,
		UnRegistered,
		Rejected,
		PartiallyRegistered,
		Unknown
		};
	
	private String name,
	description,
	model,
	ip,
	newip;
	
	private phoneStatus status;
	private phoneStatus newStatus;
	
	
	
	public BasicPhone(String name, String description, String model, String ip, phoneStatus status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.status = status;
		}

	public BasicPhone(String name, String description, String model, String ip, String status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.status = phoneStatus.valueOf(status);
		}

	public BasicPhone(String name, String description, String model)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		}
	
	public void newStatus(phoneStatus status)
		{
		if(this.status == null)this.status = status;
		else this.newStatus = status;
		}
	
	public void newIP(String ip)
		{
		if(this.ip == null)this.ip = ip;
		else this.newip = ip;
		}
	
	public boolean isOK()
		{
		if(newStatus != null)
			{
			if((status.equals(phoneStatus.Registered)) && (!newStatus.equals(phoneStatus.Registered)))
				{
				return false;
				}
			}
		return true;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String getDescription()
		{
		return description;
		}

	public void setDescription(String description)
		{
		this.description = description;
		}

	public String getModel()
		{
		return model;
		}

	public void setModel(String model)
		{
		this.model = model;
		}

	public String getIp()
		{
		return ip;
		}

	public void setIp(String ip)
		{
		this.ip = ip;
		}

	public String getNewip()
		{
		return newip;
		}

	public void setNewip(String newip)
		{
		this.newip = newip;
		}

	public phoneStatus getStatus()
		{
		return status;
		}

	public void setStatus(phoneStatus status)
		{
		this.status = status;
		}

	public phoneStatus getNewStatus()
		{
		return newStatus;
		}

	public void setNewStatus(phoneStatus newStatus)
		{
		this.newStatus = newStatus;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
