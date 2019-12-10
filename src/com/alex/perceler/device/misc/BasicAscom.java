package com.alex.perceler.device.misc;

import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.utils.Variables.ascomType;
import com.alex.perceler.utils.Variables.itmType;

/**
 * Dedicated to ascom devices
 *
 * @author Alexandre RATEL
 */
public class BasicAscom extends BasicDevice
	{
	/**
	 * Variables
	 */
	private ascomType deviceType;

	public BasicAscom(itmType type, String name, String ip, String mask, String gateway, String officeid, String newip,
			String newgateway, String newmask, String user, String password, CliProfile cliProfile,
			cliProtocol connexionProtocol, ascomType deviceType) throws Exception
		{
		super(type, name, ip, mask, gateway, officeid, newip, newgateway, newmask, user, password, cliProfile,
				connexionProtocol);
		this.deviceType = deviceType;
		}

	public ascomType getAscomType()
		{
		return deviceType;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
