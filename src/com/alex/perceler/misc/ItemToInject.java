package com.alex.perceler.misc;

import java.util.ArrayList;

import com.alex.perceler.axlitems.misc.AXLItemLinker;
import com.alex.perceler.axlitems.misc.ToUpdate;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itemType;
import com.alex.perceler.utils.Variables.statusType;


/**********************************
 * Interface aims to define one item to inject
 * 
 * @author RATEL Alexandre
 **********************************/
public abstract class ItemToInject implements ItemToInjectImpl
	{
	/**********
	 * Variables
	 */
	protected itemType type;
	protected String UUID;
	protected String name;
	protected statusType status;
	protected actionType action;
	protected ArrayList<ErrorTemplate> errorList;
	protected ArrayList<Correction> correctionList;
	protected ArrayList<ToUpdate> tuList;
	protected AXLItemLinker linker;
	
	/***************
	 * Constructor
	 ***************/
	public ItemToInject(itemType type, String name, AXLItemLinker linker)
		{
		this.type = type;
		this.name = name;
		this.linker = linker;
		this.UUID = "";
		this.status = statusType.init;
		tuList = new ArrayList<ToUpdate>();
		errorList = new ArrayList<ErrorTemplate>();
		correctionList = new ArrayList<Correction>();
		}
	
	/****
	 * Method used to launch the build process :
	 * - Prepare the request for injection
	 * - Check if the item already exist and if yes get the UUID
	 */
	public void build() throws Exception
		{
		try
			{
			//We check that the item doesn't already exist
			boolean exists = false;
			
			try
				{
				exists = isExisting();//Will throw an exception if not
				}
			catch (Exception e)
				{
				if(UsefulMethod.itemNotFoundInTheCUCM(e.getMessage()))
					{
					Variables.getLogger().debug("Item "+this.name+" doesn't already exist in the CUCM");
					exists = false;
					}
				else
					{
					//Another error happened
					throw e;
					}
				}
			
			
			if(exists)
				{
				if((action.equals(actionType.delete))||(action.equals(actionType.update))||(action.equals(actionType.rollback))||(action.equals(actionType.reset)))
					{
					this.status = statusType.waiting;
					}
				else
					{
					this.status = statusType.injected;
					}
				}
			else
				{
				//The item doesn't already exist we have to inject it except if it is a deletion task
				if(action.equals(actionType.delete))
					{
					this.status = statusType.disabled;
					}
				else
					{
					this.status = statusType.waiting;
					}
				}
			
			this.errorList.addAll(linker.init());
			
			doBuild();
			}
		catch (Exception e)
			{
			this.status = statusType.error;
			throw e;
			}
		}
	
	
	/**
	 * Method used to launch the injection process
	 * @throws Exception 
	 */
	public void inject() throws Exception
		{
		Variables.getLogger().info("Item "+this.getName()+" of type "+this.getType().name()+" injection process begin");
		
		if(this.status.equals(statusType.waiting))
			{
			try
				{
				this.status = statusType.processing;
				this.UUID = doInject();//Item successfully injected
				Variables.getLogger().info("Item "+this.getName()+" successfuly injected");
				this.status = statusType.injected;
				}
			catch (Exception e)
				{
				this.status = statusType.error;
				errorList.add(new ErrorTemplate(e.getMessage()));
				Variables.getLogger().error("Error while injecting the item \""+this.getName()+"\": "+e.getMessage(), e);
				}
			}
		else if(this.status.equals(statusType.init))
			{
			throw new Exception("Item "+this.getName()+" was not ready for the injection : build it first");
			}
		else
			{
			Variables.getLogger().info("Not to inject : "+this.getName()+" Status : "+this.getStatus().name());
			}
		}
	
	/**
	 * Method used to launch the deletion process
	 * @throws Exception 
	 */
	public void delete() throws Exception
		{
		Variables.getLogger().info("Item "+this.getName()+" deletion process begin");
		
		//If we got the UUID we can proceed
		if((!this.UUID.equals(""))&&(this.UUID != null)&&(status.equals(statusType.waiting)))
			{
			try
				{
				this.status = statusType.processing;
				doDelete();
				Variables.getLogger().info("Item "+this.getName()+" deleted successfully");
				this.status = statusType.deleted;//Item successfully deleted
				}
			catch (Exception e)
				{
				this.status = statusType.error;
				errorList.add(new ErrorTemplate(e.getMessage()));
				Variables.getLogger().error("Error while deleting the item \""+this.getName()+"\": "+e.getMessage(), e);
				}
			}
		else
			{
			Variables.getLogger().info("The item "+this.getName()+" of type "+this.getType().name()+" can't be deleted because it doesn't exist in the CUCM");
			status = statusType.disabled;
			}
		}
	
	/**
	 * Method used to launch the update process
	 * @throws Exception 
	 */
	public void update() throws Exception
		{
		Variables.getLogger().info("Item "+this.getName()+" update process begin");
		
		//If we got the UUID we can proceed
		if((!this.UUID.equals(""))&&(this.UUID != null)&&(status.equals(statusType.waiting)))
			{
			try
				{
				this.status = statusType.processing;
				manageTuList();//To setup what to update
				doUpdate();//Item successfully updated
				Variables.getLogger().info("Item "+this.getName()+" updated successfully");
				this.status = statusType.updated;//Item successfully deleted
				}
			catch (Exception e)
				{
				this.status = statusType.error;
				errorList.add(new ErrorTemplate(e.getMessage()));
				Variables.getLogger().error("Error while updating the item \""+this.getName()+"\": "+e.getMessage(), e);
				}
			}
		else
			{
			Variables.getLogger().info("The item "+this.getName()+" of type "+this.getType().name()+" can't be updated because it doesn't exist in the CUCM");
			status = statusType.disabled;
			}
		}
	
	/**
	 * Method used to display the item informations
	 */
	public String getInfo()
		{
		return name+" "
		+UUID;
		}
	
	public void addNewError(ErrorTemplate error)
		{
		//We check for duplicate
		boolean exists = false;
		for(ErrorTemplate e : errorList)
			{
			if(e.getErrorDesc().equals(error.getErrorDesc()))
				{
				exists = true;
				break;
				}
			}
		if(!exists)errorList.add(error);
		}
	
	public void addNewCorrection(Correction correction)
		{
		//We check for duplicate
		boolean exists = false;
		for(Correction c : correctionList)
			{
			if(c.getDescription().equals(correction.getDescription()))
				{
				exists = true;
				break;
				}
			}
		if(!exists)correctionList.add(correction);
		}

	public itemType getType()
		{
		return type;
		}
	public void setType(itemType type)
		{
		this.type = type;
		}
	public String getUUID()
		{
		return UUID;
		}
	public void setUUID(String uUID)
		{
		UUID = uUID;
		}
	public String getName()
		{
		return name;
		}
	public void setName(String name)
		{
		this.name = name;
		linker.setName(name);
		}
	public ArrayList<ErrorTemplate> getErrorList()
		{
		return errorList;
		}
	public void setErrorList(ArrayList<ErrorTemplate> errorList)
		{
		this.errorList = errorList;
		}
	public synchronized statusType getStatus()
		{
		return status;
		}
	public synchronized void setStatus(statusType status)
		{
		this.status = status;
		}

	public actionType getAction()
		{
		return action;
		}

	public void setAction(actionType action) throws Exception
		{
		this.action = action;
		if(action.equals(actionType.update))
			{
			manageTuList();
			}
		}

	public ArrayList<ToUpdate> getTuList()
		{
		return tuList;
		}

	public void setTuList(ArrayList<ToUpdate> tuList)
		{
		this.tuList = tuList;
		}

	public ArrayList<Correction> getCorrectionList()
		{
		return correctionList;
		}

	public void setCorrectionList(ArrayList<Correction> correctionList)
		{
		this.correctionList = correctionList;
		}
	
	
	
	
	/*2017*//*RATEL Alexandre 8)*/
	}

