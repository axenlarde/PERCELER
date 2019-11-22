package com.alex.perceler.axlitems.linkers;

import java.util.ArrayList;

import com.alex.perceler.axlitems.misc.AXLItemLinker;
import com.alex.perceler.axlitems.misc.ToUpdate;
import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.misc.SimpleRequest;
import com.alex.perceler.office.items.MobilityInfo;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.itemType;

/**********************************
 * Is the AXLItem design to link the item "Mobility info"
 * and the Cisco AXL API without version dependencies
 * 
 * @author RATEL Alexandre
 **********************************/
public class MobilityInfoLinker extends AXLItemLinker
	{
	/**
	 * Variables
	 */
	private String subnet,subnetMask;
	
	private ArrayList<String> members;//Devicepool list
	
	public enum toUpdate implements ToUpdate
		{
		subnet,
		subnetMask,
		members
		}
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public MobilityInfoLinker(String name) throws Exception
		{
		super(name);
		}
	
	/***************
	 * Initialization
	 */
	public ArrayList<ErrorTemplate> doInitVersion105() throws Exception
		{
		ArrayList<ErrorTemplate> errorList = new ArrayList<ErrorTemplate>();
		//To be written
		
		return errorList;
		}
	/**************/
	
	/***************
	 * Delete
	 */
	public void doDeleteVersion105() throws Exception
		{
		com.cisco.axl.api._10.NameAndGUIDRequest deleteReq = new com.cisco.axl.api._10.NameAndGUIDRequest();
		
		deleteReq.setName(this.getName());//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = Variables.getAXLConnectionToCUCMV105().removeDeviceMobility(deleteReq);//We send the request to the CUCM
		}
	/**************/

	/***************
	 * Injection
	 */
	public String doInjectVersion105() throws Exception
		{
		com.cisco.axl.api._10.AddDeviceMobilityReq req = new com.cisco.axl.api._10.AddDeviceMobilityReq();
		com.cisco.axl.api._10.XDeviceMobility params = new com.cisco.axl.api._10.XDeviceMobility();
		
		/*********
		 * We set the item parameters
		 */
		params.setName(this.getName());
		params.setSubNet(this.subnet);
		params.setSubNetMaskSz(this.subnetMask);
		
		com.cisco.axl.api._10.XDeviceMobility.Members myMembers = new com.cisco.axl.api._10.XDeviceMobility.Members();
		
		for(String s : members)
			{
			com.cisco.axl.api._10.XDevicePoolDeviceMobility myDP = new com.cisco.axl.api._10.XDevicePoolDeviceMobility();
			myDP.setDevicePoolName(SimpleRequest.getUUIDV105(itemType.devicepool, s));
			
			myMembers.getMember().add(myDP);
			}
		
		params.setMembers(myMembers);
		
		/************/
		
		req.setDeviceMobility(params);//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = Variables.getAXLConnectionToCUCMV105().addDeviceMobility(req);//We send the request to the CUCM
		
		return resp.getReturn();//Return UUID
		}
	
	/***************
	 * Update
	 */
	public void doUpdateVersion105(ArrayList<ToUpdate> tuList) throws Exception
		{
		com.cisco.axl.api._10.UpdateDeviceMobilityReq req = new com.cisco.axl.api._10.UpdateDeviceMobilityReq();
		
		/***********
		 * We set the item parameters
		 */
		req.setName(this.getName());
		req.setSubNet(this.subnet);
		req.setSubNetMaskSz(this.subnetMask);
		/************/
		
		com.cisco.axl.api._10.StandardResponse resp = Variables.getAXLConnectionToCUCMV105().updateDeviceMobility(req);//We send the request to the CUCM
		}
	
	/*************
	 * Get
	 */
	public ItemToInject doGetVersion105() throws Exception
		{
		com.cisco.axl.api._10.GetDeviceMobilityReq req = new com.cisco.axl.api._10.GetDeviceMobilityReq();
		
		/**
		 * We set the item parameters
		 */
		req.setName(this.getName());
		/************/
		com.cisco.axl.api._10.GetDeviceMobilityRes resp = Variables.getAXLConnectionToCUCMV105().getDeviceMobility(req);//We send the request to the CUCM
		
		MobilityInfo myMI = new MobilityInfo(this.getName());
		myMI.setUUID(resp.getReturn().getDeviceMobility().getUuid());
		//Has to be completed
		
		return myMI;//Return a Physical Location
		}

	public String getSubnet()
		{
		return subnet;
		}

	public void setSubnet(String subnet)
		{
		this.subnet = subnet;
		}

	public String getSubnetMask()
		{
		return subnetMask;
		}

	public void setSubnetMask(String subnetMask)
		{
		this.subnetMask = subnetMask;
		}

	public ArrayList<String> getMembers()
		{
		return members;
		}

	public void setMembers(ArrayList<String> members)
		{
		this.members = members;
		}

	

	
	
	
	/*2015*//*RATEL Alexandre 8)*/
	}

