package com.alex.perceler.cli;

import java.util.ArrayList;

import com.alex.perceler.device.misc.Device;
import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.utils.Variables;

/**
 * To inject the cli for one device
 *
 * @author Alexandre RATEL
 */
public class CliInjector extends Thread
	{
	/**
	 * Variables
	 */
	private Device device;
	private CliProfile cliProfile;
	private ArrayList<String> responses;
	private ArrayList<OneLine> todo;
	private ArrayList<OneLine> howToAuthenticate;
	private ArrayList<ErrorTemplate> errorList;
	
	public CliInjector(Device device, CliProfile cliProfile)
		{
		super();
		this.device = device;
		this.cliProfile = cliProfile;
		responses = new ArrayList<String>();
		todo = new ArrayList<OneLine>();
		howToAuthenticate = new ArrayList<OneLine>();
		errorList = new ArrayList<ErrorTemplate>();
		}
	
	public void build() throws Exception
		{
		/**
		 * First we get our own version of the cliprofile commands and resolve them to match device values
		 */
		for(OneLine ol : cliProfile.getHowToAuthenticate())
			{
			OneLine l = new OneLine(ol.getCommand(), ol.getType());
			l.resolve(device);
			howToAuthenticate.add(l);
			}
		for(OneLine ol : cliProfile.getCliList())
			{
			OneLine l = new OneLine(ol.getCommand(), ol.getType());
			l.resolve(device);
			todo.add(l);
			}
		}
	
	public void run()
		{
		try
			{
			
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage(), e);
			}
		}

	
	public Device getDevice()
		{
		return device;
		}

	public CliProfile getCliProfile()
		{
		return cliProfile;
		}

	public ArrayList<String> getResponses()
		{
		return responses;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
