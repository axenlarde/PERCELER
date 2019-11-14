package com.alex.perceler.device.misc;

import java.util.ArrayList;

import com.alex.perceler.cli.OneLine;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
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
			String newgateway, String newmask)
		{
		super(type, name);
		this.ip = ip;
		this.mask = mask;
		this.gateway = gateway;
		this.officeid = officeid;
		this.newip = newip;
		this.newgateway = newgateway;
		this.newmask = newmask;
		
		//Do not forget to initialize the cliList
		}

	@Override
	public void startSurvey() throws Exception
		{
		Variables.getLogger().debug("Starting survey for "+type+" "+name);
		
		//Ping
		//To be written
		//reachable = true/false;
		
		for(ItemToInject iti : axlList)
			{
			iti.getStatus();
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
		
		//To be written
		
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
