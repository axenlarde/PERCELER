package com.alex.perceler.cli;

import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.device.misc.Device;

/**
 * This class will connect to the given device and send the given command
 *
 * @author Alexandre RATEL
 */
public class CliLinker
	{
	/**
	 * Variables
	 */
	private Device device;
	private cliProtocol protocol;
	private boolean connected = false;
	
	public CliLinker(Device device, cliProtocol protocol)
		{
		super();
		this.device = device;
		this.protocol = protocol;
		}
	
	/**
	 * here we connect using the given ip
	 * @throws ConnectionException 
	 */
	public void connect(String ip) throws ConnectionException
		{
		
		throw new ConnectionException(device.getInfo()+" : CLI : Failed to connect");
		}
	
	/**
	 * Here we connect using the default device ip
	 * @throws ConnectionException 
	 */
	public void connect() throws ConnectionException
		{
		connect(device.getIp());
		}
	
	
	
	public void write(String s)
		{
		if(!connected)connect();
		
		
		}
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
