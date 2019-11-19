package com.alex.perceler.axlitems.linkers;

import java.util.ArrayList;

import com.alex.perceler.axlitems.misc.AXLItemLinker;
import com.alex.perceler.axlitems.misc.ToUpdate;
import com.alex.perceler.misc.ErrorTemplate;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.office.items.SRSTReference;
import com.alex.perceler.utils.Variables;

/**********************************
 * Is the AXLItem design to link the item "SRST reference"
 * and the Cisco AXL API without version dependencies
 * 
 * @author RATEL Alexandre
 **********************************/
public class SRSTReferenceLinker extends AXLItemLinker
	{
	/**
	 * Variables
	 */
	private String ipAddress;
	
	public enum toUpdate implements ToUpdate
		{
		ipAddress
		}
	
	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public SRSTReferenceLinker(String name) throws Exception
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
		com.cisco.axl.api._10.StandardResponse resp = Variables.getAXLConnectionToCUCMV105().removeSrst(deleteReq);//We send the request to the CUCM
		}
	/**************/

	/***************
	 * Injection
	 */
	public String doInjectVersion105() throws Exception
		{
		com.cisco.axl.api._10.AddSrstReq req = new com.cisco.axl.api._10.AddSrstReq();
		com.cisco.axl.api._10.XSrst params = new com.cisco.axl.api._10.XSrst();
		
		/*************
		 * We set the item parameters
		 */
		params.setName(this.getName());//Name
		params.setIpAddress(this.ipAddress);
		params.setSipNetwork(this.ipAddress);
		/************/
		
		req.setSrst(params);//We add the parameters to the request
		com.cisco.axl.api._10.StandardResponse resp = Variables.getAXLConnectionToCUCMV105().addSrst(req);//We send the request to the CUCM
		
		return resp.getReturn();//Return UUID
		}
	/**************/
	
	/***************
	 * Update
	 */
	public void doUpdateVersion105(ArrayList<ToUpdate> tuList) throws Exception
		{
		com.cisco.axl.api._10.UpdateSrstReq req = new com.cisco.axl.api._10.UpdateSrstReq();
		
		/***********
		 * We set the item parameters
		 */
		req.setName(this.getName());
		req.setIpAddress(this.ipAddress);
		req.setSipNetwork(this.ipAddress);
		/************/
		
		com.cisco.axl.api._10.StandardResponse resp = Variables.getAXLConnectionToCUCMV105().updateSrst(req);//We send the request to the CUCM
		}
	/**************/
	
	
	/*************
	 * Get
	 */
	public ItemToInject doGetVersion105() throws Exception
		{
		com.cisco.axl.api._10.GetSrstReq req = new com.cisco.axl.api._10.GetSrstReq();
		
		/**
		 * We set the item parameters
		 */
		req.setName(this.getName());
		/************/
		
		com.cisco.axl.api._10.GetSrstRes resp = Variables.getAXLConnectionToCUCMV105().getSrst(req);//We send the request to the CUCM
		
		SRSTReference myS = new SRSTReference(this.getName());
		myS.setUUID(resp.getReturn().getSrst().getUuid());
		myS.setIpAddress(resp.getReturn().getSrst().getIpAddress());
		
		return myS;//Return a location
		}
	/****************/

	public String getIpAddress()
		{
		return ipAddress;
		}

	public void setIpAddress(String ipAddress)
		{
		this.ipAddress = ipAddress;
		}

	
	/*2015*//*RATEL Alexandre 8)*/
	}

