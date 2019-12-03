package com.alex.perceler.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.perceler.device.misc.BasicDevice;
import com.alex.perceler.misc.SimpleItem;
import com.alex.perceler.utils.Variables.officeType;

/**
 * Used to store office data
 *
 * @author Alexandre RATEL
 */
public class BasicOffice extends SimpleItem
	{
	/**
	 * Variables
	 */
	private String id,
	idcomu,
	idCAF,
	fullname,
	shortname,
	newName;
	
	private officeType officeType;
	private IPRange voiceIPRange, dataIPRange, newVoiceIPRange, newDataIPRange;
	private ArrayList<BasicDevice> deviceList;
	
	public BasicOffice(String fullname, String idcomu, String idCAF, String shortname, String newName,
			com.alex.perceler.utils.Variables.officeType officeType, String voiceIPRange, String dataIPRange,
			String newVoiceIPRange, String newDataIPRange)
		{
		super(fullname+idcomu);
		this.fullname = fullname;
		this.idcomu = idcomu;
		this.idCAF = idCAF;
		this.shortname = shortname;
		this.newName = newName;
		this.officeType = officeType;
		this.voiceIPRange = new IPRange(voiceIPRange);
		this.dataIPRange = new IPRange(dataIPRange);
		this.newVoiceIPRange = new IPRange(newVoiceIPRange);
		this.newDataIPRange = new IPRange(newDataIPRange);
		id = DigestUtils.md5Hex(fullname+idcomu);
		deviceList = new ArrayList<BasicDevice>();
		}
	
	public String getInfo()
		{
		return idcomu+" "+
			fullname;
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
		
		return null;
		}

	public String getIdcomu()
		{
		return idcomu;
		}

	public void setIdcomu(String idcomu)
		{
		this.idcomu = idcomu;
		}

	public String getIdCAF()
		{
		return idCAF;
		}

	public void setIdCAF(String idCAF)
		{
		this.idCAF = idCAF;
		}

	public String getShortname()
		{
		return shortname;
		}

	public void setShortname(String shortname)
		{
		this.shortname = shortname;
		}

	public String getNewName()
		{
		return newName;
		}

	public void setNewName(String newName)
		{
		this.newName = newName;
		}

	public officeType getOfficeType()
		{
		return officeType;
		}

	public void setOfficeType(officeType officeType)
		{
		this.officeType = officeType;
		}

	public IPRange getVoiceIPRange()
		{
		return voiceIPRange;
		}

	public void setVoiceIPRange(IPRange voiceIPRange)
		{
		this.voiceIPRange = voiceIPRange;
		}

	public IPRange getDataIPRange()
		{
		return dataIPRange;
		}

	public void setDataIPRange(IPRange dataIPRange)
		{
		this.dataIPRange = dataIPRange;
		}

	public IPRange getNewVoiceIPRange()
		{
		return newVoiceIPRange;
		}

	public void setNewVoiceIPRange(IPRange newVoiceIPRange)
		{
		this.newVoiceIPRange = newVoiceIPRange;
		}

	public IPRange getNewDataIPRange()
		{
		return newDataIPRange;
		}

	public void setNewDataIPRange(IPRange newDataIPRange)
		{
		this.newDataIPRange = newDataIPRange;
		}

	public String getFullname()
		{
		return fullname;
		}

	public void setFullname(String fullname)
		{
		this.fullname = fullname;
		}

	public ArrayList<BasicDevice> getDeviceList()
		{
		return deviceList;
		}

	public void setDeviceList(ArrayList<BasicDevice> deviceList)
		{
		this.deviceList = deviceList;
		}

	/*2019*//*RATEL Alexandre 8)*/
	}
