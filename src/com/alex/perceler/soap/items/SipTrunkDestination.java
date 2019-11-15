package com.alex.perceler.soap.items;

import com.alex.perceler.misc.BasicItem;
import com.alex.perceler.misc.CollectionTools;

/**********************************
 * Class used to store a Sip Trunk Destination
 * 
 * @author RATEL Alexandre
 **********************************/
public class SipTrunkDestination extends BasicItem
	{
	/**
	 * Variables
	 */
	private String addressIpv4,
	port;
	
	/***************
	 * Constructor
	 ***************/
	public SipTrunkDestination(String addressIpv4, String port)
		{
		super();
		this.addressIpv4 = addressIpv4;
		this.port = port;
		}
	
	public SipTrunkDestination(String addressIpv4, String port, String UUID)
		{
		super();
		this.addressIpv4 = addressIpv4;
		this.port = port;
		this.UUID = UUID;
		}
	
	@Override
	public void resolve() throws Exception
		{
		this.addressIpv4 = CollectionTools.getRawValue(this.addressIpv4, this, true);
		}

	public String getAddressIpv4()
		{
		return addressIpv4;
		}

	public void setAddressIpv4(String addressIpv4)
		{
		this.addressIpv4 = addressIpv4;
		}

	public String getPort()
		{
		return port;
		}

	public void setPort(String port)
		{
		this.port = port;
		}
	
	
	/*2018*//*RATEL Alexandre 8)*/
	}

