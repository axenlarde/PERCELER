package com.alex.perceler.misc;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.perceler.utils.Variables;

/**
 * Abstract Item to migrate class
 *
 * @author Alexandre RATEL
 */
public abstract class ItemToMigrate implements ItemToMigrateImpl
	{
	/**
	 * Variables
	 */
	public enum itmType
		{
		office,
		isr,
		vg,
		audiocode,
		ascom,
		sip
		};
		
	public enum itmStatus
		{
		init,
		preaudit,
		migration,
		postaudit,
		done,
		error
		};
	
	protected itmType type;
	protected itmStatus status;
	protected String id,name;
	protected int index;
	
	//This List contains the AXL items that should be updated if this item is migrated 
	protected ArrayList<ItemToInject> axlList;
	
	/**
	 * Constructor
	 */
	public ItemToMigrate(itmType type, String name)
		{
		super();
		this.type = type;
		this.name = name;
		id = DigestUtils.md5Hex(System.currentTimeMillis()+name);
		}
	
	@Override
	public void resolve() throws Exception
		{
		Variables.getLogger().debug("Starting resolution for "+type+" "+name);
		for(ItemToInject iti : axlList)
			{
			iti.resolve();
			}
		}
	
	public itmType getType()
		{
		return type;
		}

	public void setType(itmType type)
		{
		this.type = type;
		}

	public itmStatus getStatus()
		{
		return status;
		}

	public void setStatus(itmStatus status)
		{
		this.status = status;
		}

	public String getId()
		{
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public ArrayList<ItemToInject> getAxlList()
		{
		return axlList;
		}

	public void setAxlList(ArrayList<ItemToInject> axlList)
		{
		this.axlList = axlList;
		}

	public int getIndex()
		{
		return index;
		}

	public void setIndex(int index)
		{
		this.index = index;
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
