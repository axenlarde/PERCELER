package com.alex.perceler.misc;

import org.apache.commons.codec.digest.DigestUtils;

import com.alex.perceler.utils.Variables;

public class SimpleItem
	{
	public enum basicItemStatus
		{
		tomigrate,
		migrated
		};	
	
	protected String id;
	protected basicItemStatus status; 
	
	public SimpleItem(String patternID)
		{
		this.id = DigestUtils.md5Hex(patternID);
		if(Variables.getMigratedItemList().contains(id))status = basicItemStatus.migrated;
		else status = basicItemStatus.tomigrate;
		}
	
	public basicItemStatus getStatus()
		{
		return status;
		}

	public void setStatus(basicItemStatus status)
		{
		this.status = status;
		}

	public String getId()
		{
		return id;
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
