package com.alex.perceler.device.misc;

import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.utils.LanguageManagement;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.ascomType;
import com.alex.perceler.utils.Variables.itmType;

/**
 * Dedicated to Ascom device
 *
 * @author Alexandre RATEL
 */
public class Ascom extends Device
	{
	/**
	 * Variables
	 */
	private ascomType deviceType;

	public Ascom(itmType type, String id, String name, String ip, String mask, String gateway, String officeid,
			String newip, String newgateway, String newmask, actionType action, String user, String password,
			CliProfile cliProfile, cliProtocol connexionProtocol, ascomType deviceType) throws Exception
		{
		super(type, id, name, ip, mask, gateway, officeid, newip, newgateway, newmask, action, user, password,
				cliProfile, connexionProtocol);
		this.deviceType = deviceType;
		}

	public Ascom(BasicAscom ba, actionType action) throws Exception
		{
		super(ba, action);
		this.deviceType = ba.getAscomType();
		}
	
	@Override
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		s.append(LanguageManagement.getString(type.name())+" ");
		s.append(ip+" ");
		s.append(deviceType+" ");
		s.append(name);
		
		int maxchar = 50;
		
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
	
	public ascomType getAscomType()
		{
		return deviceType;
		}
	
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
