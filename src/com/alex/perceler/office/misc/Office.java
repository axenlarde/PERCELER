package com.alex.perceler.office.misc;

import java.util.ArrayList;

import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.ItemToMigrate;
import com.alex.perceler.utils.Variables;

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
	public enum officeType
		{
		CAF,
		ANT,
		CER,
		CNA,
		CNE,
		PRM
		};
	
	private String idcomu,
	idCAF,
	shortname,
	fullname,
	newName;
	
	private officeType officeType;
	private IPRange voiceIPRange, dataIPRange;
	private ArrayList<ItemToMigrate> itemList;

	public Office(String idcomu, String idCAF, String shortname, String fullname, String newName,
			officeType officeType, IPRange voiceIPRange, IPRange dataIPRange, ArrayList<ItemToMigrate> itemList)
		{
		super(itmType.office,fullname);
		this.idcomu = idcomu;
		this.idCAF = idCAF;
		this.shortname = shortname;
		this.fullname = fullname;
		this.newName = newName;
		this.officeType = officeType;
		this.voiceIPRange = voiceIPRange;
		this.dataIPRange = dataIPRange;
		this.itemList = itemList;
		
		//Do not forget to initialize ItemList and AxlList
		}
	
	@Override
	public void startSurvey() throws Exception
		{
		Variables.getLogger().debug("Starting survey for "+type+" "+name);
		for(ItemToMigrate itm : itemList)
			{
			itm.startSurvey();
			}
		for(ItemToInject iti : axlList)
			{
			iti.getStatus();
			}
		}

	@Override
	public void migrate() throws Exception
		{
		Variables.getLogger().debug("Starting migration for "+type+" "+name);
		for(ItemToMigrate itm : itemList)
			{
			itm.migrate();
			}
		for(ItemToInject iti : axlList)
			{
			iti.update();
			}
		}
	
	@Override
	public void rollback() throws Exception
		{
		Variables.getLogger().debug("Starting rollback for "+type+" "+name);
		for(ItemToMigrate itm : itemList)
			{
			itm.rollback();
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

	public String getFullname()
		{
		return fullname;
		}

	public void setFullname(String fullname)
		{
		this.fullname = fullname;
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

	public ArrayList<ItemToMigrate> getItemList()
		{
		return itemList;
		}

	public void setItemList(ArrayList<ItemToMigrate> itemList)
		{
		this.itemList = itemList;
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

	/*2019*//*RATEL Alexandre 8)*/
	}
