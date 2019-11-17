package com.alex.perceler.misc;

import com.alex.perceler.utils.Variables.actionType;

/**
 * Interface used to define a basic item to migrate
 *
 * @author Alexandre RATEL
 */
public interface ItemToMigrateImpl
	{
	public void startSurvey() throws Exception;//To get the status of the item
	public void migrate() throws Exception;//To start the migration process of this item
	public void resolve() throws Exception;//To resolve content
	public void init() throws Exception;//To init the item
	public void build(actionType action) throws Exception;//To build the item
	public String getInfo();//To display item info
	
	/*2019*//*RATEL Alexandre 8)*/
	}
