package com.alex.perceler.misc;

import java.util.ArrayList;

import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itmType;
import com.alex.perceler.utils.Variables.statusType;

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
	public enum itmStatus
		{
		init,
		preaudit,
		update,
		postaudit,
		done,
		disabled,
		error
		};
	
	protected actionType action;
	protected itmType type;
	protected itmStatus status;
	protected String id,name;
	protected int index;
	protected ArrayList<ErrorTemplate> errorList;
	
	//This List contains the AXL items that should be updated if this item is migrated 
	protected ArrayList<ItemToInject> axlList;
	
	/**
	 * Constructor
	 */
	public ItemToMigrate(itmType type, String name, String id, actionType action)
		{
		super();
		this.action = action;
		this.type = type;
		this.name = name;
		this.id = id;
		errorList = new ArrayList<ErrorTemplate>();
		status = itmStatus.init;
		}
	
	@Override
	public void init() throws Exception
		{
		//Write something if needed
		
		doInit();
		}
	
	@Override
	public void build() throws Exception
		{
		Variables.getLogger().debug("Starting build for "+type+" "+name);
		
		doBuild();
		
		for(ItemToInject iti : axlList)
			{
			iti.build();
			}
		}
	
	@Override
	public ArrayList<String> startSurvey() throws Exception
		{
		Variables.getLogger().debug("Starting survey for "+type+" "+name);
		
		doStartSurvey();
		
		for(ItemToInject iti : axlList)
			{
			if(!iti.getStatus().equals(statusType.disabled))
				{
				errorList.add(new ErrorTemplate(name+" : The following item has not been found and therefore will not be updated : "+iti.getInfo()));
				}
			}
		
		ArrayList<String> l = new ArrayList<String>();
		for(ErrorTemplate e : errorList)
			{
			l.add(e.getErrorDesc());
			}
		
		return l;
		}
	
	@Override
	public void update() throws Exception
		{
		Variables.getLogger().debug("Starting migration for "+type+" "+name);
		
		doUpdate();
		
		for(ItemToInject iti : axlList)
			{
			if(!iti.getStatus().equals(statusType.disabled))
				{
				iti.update();
				}
			else
				{
				Variables.getLogger().debug("The following item has been disabled so we do not process it : "+iti.getInfo());
				}
			}
		}
	
	@Override
	public void resolve() throws Exception
		{
		Variables.getLogger().debug("Starting resolution for "+type+" "+name);
		doResolve();
		
		for(ItemToInject iti : axlList)
			{
			iti.resolve();
			}
		}
	
	@Override
	public void reset()
		{
		Variables.getLogger().debug("Starting reset for "+type+" "+name);
		doReset();
		}
	
	@Override
	public String getDetailedStatus()
		{
		StringBuffer result = new StringBuffer("");
		
		result.append("Item error list : \r\n");
		for(ErrorTemplate e : errorList)
			{
			result.append("- "+e.getErrorDesc()+"\r\n");
			}
		
		result.append("\r\n");
		result.append("CUCM items : \r\n");
		for(ItemToInject iti : axlList)
			{
			result.append("- "+iti.getName()+" : "+iti.getType().name()+" : "+iti.getStatus().name()+" : \r\n");
			for(ErrorTemplate e : iti.getErrorList())
				{
				result.append("	+ "+e+"\r\n");
				}
			}
		
		result.append("\r\n");
		result.append(doGetDetailedStatus());
		
		return result.toString();
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

	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}

	public void setErrorList(ArrayList<ErrorTemplate> errorList)
		{
		this.errorList = errorList;
		}

	public actionType getAction()
		{
		return action;
		}

	public void setAction(actionType action)
		{
		this.action = action;
		}

	
	/*2019*//*RATEL Alexandre 8)*/
	}
