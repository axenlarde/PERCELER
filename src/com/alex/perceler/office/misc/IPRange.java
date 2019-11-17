package com.alex.perceler.office.misc;

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
		String[] temp = ipmask.split("/");
		ipRange = temp[0];
		mask = temp[1];
		}
	
	public String getInfo()
		{
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
