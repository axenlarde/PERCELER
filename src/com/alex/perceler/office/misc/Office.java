package com.alex.perceler.office.misc;

import java.util.ArrayList;

import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.office.items.MobilityInfo;
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
	
	private officeType officeType;
	private IPRange voiceIPRange, dataIPRange, newVoiceIPRange, newDataIPRange;

	public Office(String name, String id, String idcomu, String idCAF, String shortname,
			String newName, officeType officeType, String voiceIPRange,
			String dataIPRange, String newVoiceIPRange, String newDataIPRange)
		{
		super(itmType.office, name, id);
		this.idcomu = idcomu;
		this.idCAF = idCAF;
		this.shortname = shortname;
		this.newName = newName;
		this.officeType = officeType;
		this.voiceIPRange = new IPRange(voiceIPRange);
		this.dataIPRange = new IPRange(dataIPRange);
		this.newVoiceIPRange = new IPRange(newVoiceIPRange);
		this.newDataIPRange = new IPRange(newDataIPRange);
		}
	
	public Office(BasicOffice bo)
		{
		super(itmType.office, bo.getFullname(), bo.getId());
		this.idcomu = bo.getIdcomu();
		this.idCAF = bo.getIdCAF();
		this.shortname = bo.getShortname();
		this.newName = bo.getNewName();
		this.officeType = bo.getOfficeType();
		this.voiceIPRange = bo.getVoiceIPRange();
		this.dataIPRange = bo.getDataIPRange();
		this.newVoiceIPRange = bo.getNewVoiceIPRange();
		this.newDataIPRange = bo.getNewDataIPRange();
		}

	@Override
	public String getInfo()
		{
		return idcomu+" "+
			name;
		}
	
	@Override
	public void init() throws Exception
		{
		// TODO Auto-generated method stub
		
		}

	@Override
	public void build(actionType action) throws Exception
		{
		/**
		 * We need to find one AXL items : mobilityInfo
		 * To be sure to find the right one we have to send SQL request to the CUCM
		 * Just using the database is unreliable
		 */
		MobilityInfo voiceMI, dataMI;
		
		if(action.equals(actionType.rollback))
			{
			voiceMI = OfficeTools.getMobilityInfo(newVoiceIPRange);
			dataMI = OfficeTools.getMobilityInfo(newDataIPRange);
			}
		else
			{
			voiceMI = OfficeTools.getMobilityInfo(voiceIPRange);
			dataMI = OfficeTools.getMobilityInfo(dataIPRange);
			}
		
		if(voiceMI != null)
			{
			Variables.getLogger().debug(name+" mobilityInfo found for range "+voiceIPRange.getInfo()+" : "+voiceMI.getName());
			axlList.add(voiceMI);
			}
		else
			{
			Variables.getLogger().debug(name+" no MobilityInfo found for the following ip range :"+voiceIPRange.getInfo());
			errorList.add(new ErrorTemplate("No mobility info found for voice IP Range "+voiceIPRange.getInfo()));
			}
		if(dataMI != null)
			{
			Variables.getLogger().debug(name+" mobilityInfo found for range "+dataIPRange.getInfo()+" : "+dataMI.getName());
			axlList.add(dataMI);
			}
		else
			{
			Variables.getLogger().debug(name+" no MobilityInfo found for the following ip range :"+dataIPRange.getInfo());
			errorList.add(new ErrorTemplate("No mobility info found for data IP Range "+dataIPRange.getInfo()));
			}
		
		for(ItemToInject iti : axlList)
			{
			iti.build();
			}
		}
	
	@Override
	public void startSurvey() throws Exception
		{
		Variables.getLogger().debug("Starting survey for "+name);
		
		//We check for the device pool
		//To be written
		
		for(ItemToInject iti : axlList)
			{
			//To be written
			}
		}

	@Override
	public void migrate() throws Exception
		{
		Variables.getLogger().debug("Starting migration for "+type+" "+name);
		
		for(ItemToInject iti : axlList)
			{
			iti.update();
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

	
	/*2019*//*RATEL Alexandre 8)*/
	}
