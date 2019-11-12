package com.alex.perceler.office.misc;

import java.util.ArrayList;

import com.alex.perceler.misc.ItemToMigrate;

/**
 * Represent an office
 *
 * @author Alexandre RATEL
 */
public class Office
	{
	/**
	 * Variables
	 */
	public enum officeType
		{
		CAF,
		ANT
		};
	
	private String idcomu,
	id,
	shortname,
	fullname,
	newName;
	
	private officeType type;
	private IPRange voiceIPRange, dataIPRange;
	
	private ArrayList<ItemToMigrate> itemList;

	public Office(String idcomu, String id, String shortname, String fullname, String newName,
			officeType type, IPRange voiceIPRange, IPRange dataIPRange, ArrayList<ItemToMigrate> itemList)
		{
		super();
		this.idcomu = idcomu;
		this.id = id;
		this.shortname = shortname;
		this.fullname = fullname;
		this.newName = newName;
		this.type = type;
		this.voiceIPRange = voiceIPRange;
		this.dataIPRange = dataIPRange;
		this.itemList = itemList;
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
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
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

	public officeType getType()
		{
		return type;
		}

	public void setType(officeType type)
		{
		this.type = type;
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

	/*2019*//*RATEL Alexandre 8)*/
	}
