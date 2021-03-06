package com.alex.perceler.office.items;

import java.util.ArrayList;

import com.alex.perceler.axlitems.linkers.MobilityInfoLinker;
import com.alex.perceler.misc.CollectionTools;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.itemType;


/**********************************
 * Class used to define an item of type "Mobility Info"
 * 
 * @author RATEL Alexandre
 **********************************/

public class MobilityInfo extends ItemToInject
	{
	/**
	 * Variables
	 */
	private String subnet,
	subnetMask;//Has to be a number of bit (CIDR format) example 24
	
	private ArrayList<String> members;//Devicepool list

	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public MobilityInfo(String name, String subnet,
			String subnetMask, ArrayList<String> members) throws Exception
		{
		super(itemType.devicemobilityinfo, name, new MobilityInfoLinker(name));
		this.subnet = subnet;
		this.subnetMask = subnetMask;
		this.members = members;
		}
	
	public MobilityInfo(String name) throws Exception
		{
		super(itemType.devicemobilityinfo, name, new MobilityInfoLinker(name));
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
		MobilityInfo myP = (MobilityInfo) linker.get();
		this.UUID = myP.getUUID();
		//Has to be written
		
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		return true;
		}
	
	/**
	 * Method used to resolve pattern into real value
	 */
	public void resolve() throws Exception
		{
		name = CollectionTools.getRawValue(name, this, true);
		subnet = CollectionTools.getRawValue(subnet, this, true);
		subnetMask = CollectionTools.getRawValue(subnetMask, this, true);
		
		//Convert 255.255.255.0 into 24
		subnetMask = UsefulMethod.convertlongMaskToShortOne(subnetMask);//Has to be a number of bit
		members = CollectionTools.resolveStringList(members, this, true);
		
		Variables.getLogger().debug("Device Mobility Info details : "+subnet+" "+subnetMask);
		
		/**
		 * We set the item parameters
		 */
		MobilityInfoLinker mil= ((MobilityInfoLinker) linker);
		mil.setName(this.getName());
		mil.setSubnet(this.subnet);
		mil.setSubnetMask(this.subnetMask);
		mil.setMembers(this.members);
		/*********/
		}
	
	//Temp
	public void getReady()
		{
		/**
		 * We set the item parameters
		 */
		MobilityInfoLinker mil= ((MobilityInfoLinker) linker);
		mil.setName(this.getName());
		mil.setSubnet(this.subnet);
		mil.setSubnetMask(this.subnetMask);
		mil.setMembers(this.members);
		/*********/
		}
	//temp

	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(subnet))tuList.add(MobilityInfoLinker.toUpdate.subnet);
		if(UsefulMethod.isNotEmpty(subnetMask))tuList.add(MobilityInfoLinker.toUpdate.subnetMask);
		if(UsefulMethod.isNotEmpty(members))tuList.add(MobilityInfoLinker.toUpdate.members);
		}
	
	public String getSubnet()
		{
		return subnet;
		}

	public void setSubnet(String subnet)
		{
		this.subnet = subnet;
		((MobilityInfoLinker) linker).setSubnet(subnet);
		}

	public String getSubnetMask()
		{
		return subnetMask;
		}

	public void setSubnetMask(String subnetMask)
		{
		this.subnetMask = subnetMask;
		((MobilityInfoLinker) linker).setSubnetMask(subnetMask);
		}

	public ArrayList<String> getMembers()
		{
		return members;
		}

	public void setMembers(ArrayList<String> members)
		{
		this.members = members;
		((MobilityInfoLinker) linker).setMembers(members);
		}	
	
	
	/*2015*//*RATEL Alexandre 8)*/
	}

