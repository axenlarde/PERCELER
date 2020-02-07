package com.alex.perceler.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.perceler.device.misc.BasicPhone;
import com.alex.perceler.device.misc.BasicPhone.phoneStatus;
import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.office.items.DevicePool;
import com.alex.perceler.risport.RisportTools;
import com.alex.perceler.utils.LanguageManagement;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itmType;

/**
 * Represent a simple office
 * 
 * This kind of office is used just to reset phones
 * When the user looks for phones using an IP address and the office is not in the database
 * we create a simple office just to allow for phone reset
 *
 * @author Alexandre RATEL
 */
public class OfficeSimple extends ItemToMigrate  
	{
	/**
	 * Variables
	 */
	private String idcomu;
	
	private boolean exists;
	
	private DevicePool dp;
	private ArrayList<BasicPhone> phoneList;

	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public OfficeSimple(String name, String id, actionType action,
			String idcomu, String devicePoolName) throws Exception
		{
		super(itmType.office, name, id, action);
		this.idcomu = idcomu;
		this.dp = new DevicePool(devicePoolName);
		}

	public OfficeSimple(BasicOffice bo, actionType action)
		{
		super(itmType.office, bo.getName(), bo.getId(), action);
		this.idcomu = bo.getIdcomu();
		phoneList = new ArrayList<BasicPhone>();
		exists = false;
		}

	@Override
	public String getInfo()
		{
		StringBuffer s = new StringBuffer("");
		s.append(LanguageManagement.getString("office")+" ");
		s.append(idcomu+" ");
		s.append(LanguageManagement.getString("unknownoffice"));
		
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
	
	@Override
	public void doInit() throws Exception
		{
		//Write something if needed
		}

	@Override
	public void doBuild() throws Exception
		{
		//We check for the office device pool
		try
			{
			exists = dp.isExisting();
			}
		catch(Exception e)
			{
			Variables.getLogger().error(name+" warning : The associated device pool was not found : "+dp.getName());
			addError(new ErrorTemplate(name+" warning the associated device pool was not found : "+dp.getName()));
			}
		
		/**
		 * We now build the associated phone list
		 */
		phoneList = OfficeTools.getDevicePoolPhoneList(dp.getName());
		}
	
	/**
	 * Will check items and return the error list
	 */
	@Override
	public void doStartSurvey() throws Exception
		{
		/**
		 * We get the associated phones status only if the device pool exists
		 * otherwise the result would have been empty anyway
		 */
		if(exists)
			{
			ArrayList<BasicPhone> pl = RisportTools.doPhoneSurvey(phoneList);
			try
				{
				for(BasicPhone p : phoneList)
					{
					boolean found = false;
					for(BasicPhone bp : pl)
						{
						if(p.getName().equals(bp.getName()))
							{
							found = true;
							break;
							}
						}
					p.newStatus(found?phoneStatus.registered:phoneStatus.unregistered);
					}
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR : "+e.getMessage());
				}
			}
		}

	@Override
	public void doUpdate() throws Exception
		{
		//Write something if needed
		}
	
	@Override
	public void doResolve() throws Exception
		{
		//Write something if needed
		}
	
	@Override
	public void doReset()
		{
		try
			{
			if(exists)dp.reset();
			else Variables.getLogger().debug(getIndex()+" Reset could not be performed because the device pool was not found");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while reseting devices for "+type+" "+name+" "+e.getMessage(), e);
			addError(new ErrorTemplate("Failed to reset the device pool for "+type+" "+name));
			}
		}
	
	/**
	 * Will return a detailed status of the item
	 * For instance will return phone status
	 */
	public String doGetDetailedStatus()
		{
		StringBuffer result = new StringBuffer("");
		
		StringBuffer temp = new StringBuffer("");
		int lostPhone = 0;
		for(BasicPhone p : phoneList)
			{
			if(!p.isOK())
				{
				lostPhone++;
				temp.append("+ "+p.getName()+" "+p.getModel()+" "+p.getDescription()+" : "+p.getNewStatus()+"\r\n");
				}
			}
		
		String phone = LanguageManagement.getString("phone");
		String found = LanguageManagement.getString("found");
		String lost = LanguageManagement.getString("lost");
		
		result.append(phone+" : "+found+" "+phoneList.size()+", "+lost+" "+lostPhone);
		Variables.getLogger().debug(this.name+" : Lost phone found : "+temp);
		//result.append(temp);
		
		return result.toString();
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
			//We try also in the super class
			for(Field f : this.getClass().getSuperclass().getDeclaredFields())
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

	public ArrayList<ItemToInject> getAxlList()
		{
		return axlList;
		}

	public void setAxlList(ArrayList<ItemToInject> axlList)
		{
		this.axlList = axlList;
		}

	public ArrayList<BasicPhone> getPhoneList()
		{
		return phoneList;
		}

	public void setPhoneList(ArrayList<BasicPhone> phoneList)
		{
		this.phoneList = phoneList;
		}

	public boolean isExists()
		{
		return exists;
		}

	public DevicePool getDp()
		{
		return dp;
		}

	public void setDp(DevicePool dp)
		{
		this.dp = dp;
		}

	
	/*2019*//*RATEL Alexandre 8)*/
	}
