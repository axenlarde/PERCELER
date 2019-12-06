package com.alex.perceler.office.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alex.perceler.axlitems.linkers.MobilityInfoLinker;
import com.alex.perceler.device.misc.BasicPhone;
import com.alex.perceler.misc.CollectionTools;
import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.office.items.DevicePool;
import com.alex.perceler.office.items.MobilityInfo;
import com.alex.perceler.risport.RisportTools;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itmType;
import com.alex.perceler.utils.Variables.officeType;

/**
 * Represent an office
 *
 * @author Alexandre RATEL
 */
public class Office extends ItemToMigrate  
	{
	/**
	 * Variables
	 */
	private String idcomu,
	idCAF,
	shortname,
	newName;
	
	private MobilityInfo voiceMI, dataMI;
	private DevicePool dp;
	private ArrayList<BasicPhone> phoneList;
	
	private officeType officeType;
	private IPRange voiceIPRange, dataIPRange, newVoiceIPRange, newDataIPRange;

	public Office(String name, String id, String idcomu, String idCAF, String shortname,
			String newName, officeType officeType, String voiceIPRange,
			String dataIPRange, String newVoiceIPRange, String newDataIPRange, actionType action) throws Exception
		{
		super(itmType.office, name, id, action);
		this.idcomu = idcomu;
		this.idCAF = idCAF;
		this.shortname = shortname;
		this.newName = newName;
		this.officeType = officeType;
		phoneList = new ArrayList<BasicPhone>();
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.voiceIPRange = new IPRange(newVoiceIPRange);
			this.dataIPRange = new IPRange(newDataIPRange);
			this.newVoiceIPRange = new IPRange(voiceIPRange);
			this.newDataIPRange = new IPRange(dataIPRange);
			}
		else
			{
			this.voiceIPRange = new IPRange(voiceIPRange);
			this.dataIPRange = new IPRange(dataIPRange);
			this.newVoiceIPRange = new IPRange(newVoiceIPRange);
			this.newDataIPRange = new IPRange(newDataIPRange);
			}
		}
	
	public Office(BasicOffice bo, actionType action)
		{
		super(itmType.office, bo.getFullname(), bo.getId(), action);
		this.idcomu = bo.getIdcomu();
		this.idCAF = bo.getIdCAF();
		this.shortname = bo.getShortname();
		this.newName = bo.getNewName();
		this.officeType = bo.getOfficeType();
		phoneList = new ArrayList<BasicPhone>();
		
		/**
		 * In case of rollback we reverse the following values
		 */
		if(action.equals(actionType.rollback))
			{
			this.voiceIPRange = bo.getNewVoiceIPRange();
			this.dataIPRange = bo.getNewDataIPRange();
			this.newVoiceIPRange = bo.getVoiceIPRange();
			this.newDataIPRange = bo.getDataIPRange();
			}
		else
			{
			this.voiceIPRange = bo.getVoiceIPRange();
			this.dataIPRange = bo.getDataIPRange();
			this.newVoiceIPRange = bo.getNewVoiceIPRange();
			this.newDataIPRange = bo.getNewDataIPRange();
			}
		}

	@Override
	public String getInfo()
		{
		return idcomu+" "+
			name+" "+
			type;
		}
	
	@Override
	public void doInit() throws Exception
		{
		//Write something if needed
		}

	@Override
	public void doBuild() throws Exception
		{
		/**
		 * We need to find one AXL items : mobilityInfo
		 * To be sure to find the right one we have to send SQL request to the CUCM
		 * Just using the database is unreliable
		 */
		voiceMI = OfficeTools.getMobilityInfo(voiceIPRange);
		//dataMI = OfficeTools.getMobilityInfo(dataIPRange);
		
		if(voiceMI != null)
			{
			Variables.getLogger().debug(name+" mobilityInfo found for range "+voiceIPRange.getInfo()+" : "+voiceMI.getName());
			axlList.add(voiceMI);
			}
		else
			{
			Variables.getLogger().debug(name+" no MobilityInfo found for the following ip range :"+voiceIPRange.getInfo());
			}
		/*
		if(dataMI != null)
			{
			Variables.getLogger().debug(name+" mobilityInfo found for range "+dataIPRange.getInfo()+" : "+dataMI.getName());
			axlList.add(dataMI);
			}
		else
			{
			Variables.getLogger().debug(name+" no MobilityInfo found for the following ip range :"+dataIPRange.getInfo());
			}*/
		
		/**
		 * We build the associated device pool
		 */
		dp = new DevicePool(UsefulMethod.getTargetOption("devicepoolprefix")+idcomu);
		
		/**
		 * For the data mobility info, we inject a new one or delete it in case of rollback
		 */
		ArrayList<String> dpList = new ArrayList<String>();
		dpList.add(dp.getName());
		dataMI = new MobilityInfo(CollectionTools.resolveOfficeValue(this, UsefulMethod.getTargetOption("datamobilityinfopattern")),
				newDataIPRange.getSubnet(),
				newDataIPRange.getShortmask(),
				dpList);
		
		if(action.equals(actionType.update))
			{
			dataMI.setAction(actionType.inject);
			}
		else if(action.equals(actionType.rollback))
			{
			dataMI.setAction(actionType.delete);
			}
		
		dataMI.getReady();
		dataMI.build();
		
		//axlList.add(dataMI);//here we do not update but inject, so the item will have to be treated apart
		
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
		//We check for the office device pool
		if(!dp.isExisting())addError(new ErrorTemplate(name+" warning the associated device pool has not been found : "+dp.getName()));
		
		//We get the associated phones status
		ArrayList<BasicPhone> pl = RisportTools.doPhoneSurvey(phoneList);
		try
			{
			for(BasicPhone bp : pl)
				{
				for(BasicPhone p : phoneList)
					{
					if(p.getName().equals(bp.getName()))
						{
						p.newStatus(bp.getStatus());
						p.newIP(bp.getIp());
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage());
			}
		}

	@Override
	public void doUpdate() throws Exception
		{
		/**
		 * here we first used the current values to check that
		 * the items exists. Now we are about to proceed with the update so
		 * we change the values with the new ones 
		 */
		if(voiceMI != null)
			{
			voiceMI.setSubnet(this.newVoiceIPRange.getSubnet());
			voiceMI.setSubnetMask(this.newVoiceIPRange.getShortmask());
			}
		/*
		We inject a new one already with the new ip
		dataMI.setSubnet(this.newDataIPRange.getIpRange());
		dataMI.setSubnetMask(this.newDataIPRange.getMask());
		*/
		if(action.equals(actionType.update))
			{
			dataMI.inject();
			}
		else if(action.equals(actionType.rollback))
			{
			dataMI.delete();
			}
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
			dp.reset();
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
		int lost = 0;
		for(BasicPhone p : phoneList)
			{
			if(!p.isOK())
				{
				lost++;
				temp.append("+ "+p.getName()+" "+p.getModel()+" "+p.getDescription()+" : "+p.getNewStatus()+"\r\n");
				}
			}
		
		result.append("Phones : Found "+phoneList.size()+", Lost "+lost);
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

	public void setOfficeType(officeType type)
		{
		this.officeType = type;
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

	public String getIdCAF()
		{
		return idCAF;
		}

	public void setIdCAF(String idCAF)
		{
		this.idCAF = idCAF;
		}

	public ArrayList<ItemToInject> getAxlList()
		{
		return axlList;
		}

	public void setAxlList(ArrayList<ItemToInject> axlList)
		{
		this.axlList = axlList;
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

	public ArrayList<BasicPhone> getPhoneList()
		{
		return phoneList;
		}

	public void setPhoneList(ArrayList<BasicPhone> phoneList)
		{
		this.phoneList = phoneList;
		}

	
	/*2019*//*RATEL Alexandre 8)*/
	}
