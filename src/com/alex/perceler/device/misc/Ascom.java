package com.alex.perceler.device.misc;

import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
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

	public Ascom(BasicAscom ba, actionType action)
		{
		super(ba, action);
		this.deviceType = ba.getAscomType();
		}
	
	public ascomType getAscomType()
		{
		return deviceType;
		}
	
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
