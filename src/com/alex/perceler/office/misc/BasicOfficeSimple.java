package com.alex.perceler.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.perceler.device.misc.BasicDevice;
import com.alex.perceler.misc.SimpleItem;
import com.alex.perceler.utils.Variables.officeType;

/**
 * Used to store office data
 *
 * @author Alexandre RATEL
 */
public class BasicOfficeSimple extends SimpleItem
	{
	/**
	 * Variables
	 */
	private String idcomu, name, devicePoolName;
	
	private ArrayList<BasicDevice> deviceList;
	
	public BasicOfficeSimple(String name, String idcomu, String devicePoolName) throws Exception
		{
		super(name+idcomu);
		try
			{
			this.name = name;
			this.idcomu = idcomu;
			this.devicePoolName = devicePoolName;
			deviceList = new ArrayList<BasicDevice>();
			}
		catch (Exception e)
			{
			throw new Exception(getInfo()+" : "+e.getMessage());
			}
		}
	
	public String getInfo()
		{
		return idcomu+" "+
			name;
		}
	
	/******
	 * Used to return a value based on the string provided
	 * @throws Exception 
	 */
	public String getString(String s) throws Exception
		{
		String tab[] = s.split("\\.");
		
		if(tab.length == 2)
			{
			for(Field f : this.getClass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					return (String) f.get(this);
					}
				}
			}
		else if(tab.length == 3)
			{
			for(Field f : this.getClass().getDeclaredFields())
				{
				if(f.getName().toLowerCase().equals(tab[1].toLowerCase()))
					{
					if(f.get(this) instanceof IPRange)
						{
						return ((IPRange) f.get(this)).getString(tab[2]);
						}
					}
				}
			}
		
		throw new Exception("String not found");
		}

	public String getIdcomu()
		{
		return idcomu;
		}

	public void setIdcomu(String idcomu)
		{
		this.idcomu = idcomu;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public ArrayList<BasicDevice> getDeviceList()
		{
		return deviceList;
		}

	public void setDeviceList(ArrayList<BasicDevice> deviceList)
		{
		this.deviceList = deviceList;
		}

	public String getDevicePoolName()
		{
		return devicePoolName;
		}

	public void setDevicePoolName(String devicePoolName)
		{
		this.devicePoolName = devicePoolName;
		}

	/*2019*//*RATEL Alexandre 8)*/
	}
