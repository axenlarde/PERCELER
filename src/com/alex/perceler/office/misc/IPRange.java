package com.alex.perceler.office.misc;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.alex.perceler.utils.Tester;
import com.alex.perceler.utils.UsefulMethod;

/**
 * IPRange
 *
 * @author Alexandre RATEL
 */
public class IPRange
	{
	/**
	 * Variables
	 */
	private String ipRange,mask;

	public IPRange(String ipRange, String mask)
		{
		super();
		this.ipRange = ipRange;
		this.mask = mask;
		}
	
	public IPRange(String ipmask)
		{
		if(ipmask.contains("/"))
			{
			String[] temp = ipmask.split("/");
			if(InetAddressValidator.getInstance().isValidInet4Address(temp[0]))
				{
				ipRange = temp[0];
				mask = temp[1];
				}
			else
				{
				ipRange = "";
				mask = "";
				}
			}
		else
			{
			ipRange = "";
			mask = "";
			}
		}
	
	public String getInfo()
		{
		return ipRange+"/"+mask;
		}
	
	public boolean compareTo(IPRange range)
		{
		if((ipRange.equals(range.getIpRange())) && (mask.equals(range.getMask())))return true;
		return false;
		}
	
	public String getCIDRFormat()
		{
		if((ipRange.equals("")) || (mask.equals("")))
			{
			return "";
			}
		return ipRange+"/"+mask;
		}

	public String getIpRange()
		{
		return ipRange;
		}

	public void setIpRange(String ip)
		{
		this.ipRange = ip;
		}

	public String getMask()
		{
		return mask;
		}

	public void setMask(String mask)
		{
		this.mask = mask;
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
