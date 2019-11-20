package com.alex.perceler.office.misc;

import java.util.ArrayList;

import com.alex.perceler.device.misc.BasicPhone;
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
			String dataIPRange, String newVoiceIPRange, String newDataIPRange, actionType action)
		{
		super(itmType.office, name, id, action);
		this.idcomu = idcomu;
		this.idCAF = idCAF;
		this.shortname = shortname;
		this.newName = newName;
		this.officeType = officeType;
		
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
			name;
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
		dataMI = OfficeTools.getMobilityInfo(dataIPRange);
		
		if(voiceMI != null)
			{
			Variables.getLogger().debug(name+" mobilityInfo found for range "+voiceIPRange.getInfo()+" : "+voiceMI.getName());
			axlList.add(voiceMI);
			}
		else
			{
			Variables.getLogger().debug(name+" no MobilityInfo found for the following ip range :"+voiceIPRange.getInfo());
			}
		if(dataMI != null)
			{
			Variables.getLogger().debug(name+" mobilityInfo found for range "+dataIPRange.getInfo()+" : "+dataMI.getName());
			axlList.add(dataMI);
			}
		else
			{
			Variables.getLogger().debug(name+" no MobilityInfo found for the following ip range :"+dataIPRange.getInfo());
			}
		
		/**
		 * We build the associated device pool
		 */
		dp = new DevicePool(UsefulMethod.getTargetOption("devicepoolprefix")+idcomu);
		
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
		if(!dp.isExisting())errorList.add(new ErrorTemplate(name+" warning the associated device pool has not been found : "+dp.getName()));
		
		//We get the associated phones status
		phoneList = RisportTools.doPhoneSurvey(phoneList);
		for()
		}

	@Override
	public void doUpdate() throws Exception
		{
		/**
		 * here we first used the current values to check that
		 * the items exists. Now we are about to proceed with the update so
		 * we change the values with the new ones 
		 */
		voiceMI.setSubnet(this.newVoiceIPRange.getIpRange());
		voiceMI.setSubnetMask(this.newVoiceIPRange.getMask());
		dataMI.setSubnet(this.newDataIPRange.getIpRange());
		dataMI.setSubnetMask(this.newDataIPRange.getMask());
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
			errorList.add(new ErrorTemplate("Failed to reset the device pool for "+type+" "+name));
			}
		}

	public String getIdcomu()
		{
		return idcomu;
		}

	public void setIdcomu(String idcomu)
		{
		this.idcomu = idcomu;
		}

	public String getId()
		{
		return idCAF;
		}

	public void setId(String id)
		{
		this.idCAF = id;
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
