package com.alex.perceler.office.items;

import com.alex.perceler.axlitems.linkers.SRSTReferenceLinker;
import com.alex.perceler.misc.CollectionTools;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.itemType;

/**********************************
 * Class used to define an item of type "SRST Reference"
 * 
 * @author RATEL Alexandre
 **********************************/

public class SRSTReference extends ItemToInject
	{
	/**
	 * Variables
	 */
	private String ipAddress;
	
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public SRSTReference(String name, String ipAddress) throws Exception
		{
		super(itemType.srstreference, name, new SRSTReferenceLinker(name));
		this.ipAddress = ipAddress;
		}

	public SRSTReference(String name) throws Exception
		{
		super(itemType.srstreference, name, new SRSTReferenceLinker(name));
		}

	/***********
	 * Method used to prepare the item for the injection
	 * by gathering the needed UUID from the CUCM 
	 */
	public void doBuild() throws Exception
		{
		this.errorList.addAll(linker.init());
		}
	
	
	/**
	 * Method used to inject data in the CUCM using
	 * the Cisco API
	 * 
	 * It also return the item's UUID once injected
	 */
	public String doInject() throws Exception
		{
		return linker.inject();//Return UUID
		}

	/**
	 * Method used to delete data in the CUCM using
	 * the Cisco API
	 */
	public void doDelete() throws Exception
		{
		linker.delete();
		}

	/**
	 * Method used to delete data in the CUCM using
	 * the Cisco API
	 */
	public void doUpdate() throws Exception
		{
		linker.update(tuList);
		}
	
	/**
	 * Method used to check if the element exist in the CUCM
	 */
	public boolean isExisting() throws Exception
		{
		SRSTReference myS = (SRSTReference) linker.get();
		this.UUID = myS.getUUID();
		this.ipAddress = myS.getIpAddress();
		
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		return true;
		}
	
	public String getInfo()
		{
		return name+" "
		+UUID+" "
		+ipAddress;
		}

	/**
	 * Method used to resolve pattern into real value
	 */
	public void resolve() throws Exception
		{
		name = CollectionTools.getRawValue(name, this, true);
		ipAddress = CollectionTools.getRawValue(ipAddress, this, true);
		
		/**
		 * We set the item parameters
		 */
		SRSTReferenceLinker sl = (SRSTReferenceLinker)linker;
		sl.setName(this.getName());
		sl.setIpAddress(this.ipAddress);
		/*********/
		}
	
	/**
	 * Manage the content of the "To Update List"
	 */
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(ipAddress))tuList.add(SRSTReferenceLinker.toUpdate.ipAddress);
		}
	
	public String getIpAddress()
		{
		return ipAddress;
		}

	public void setIpAddress(String ipAddress)
		{
		this.ipAddress = ipAddress;
		((SRSTReferenceLinker)linker).setIpAddress(ipAddress);
		}

	
	
	
	/*2015*//*RATEL Alexandre 8)*/
	}

