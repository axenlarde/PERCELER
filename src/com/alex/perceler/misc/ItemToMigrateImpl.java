package com.alex.perceler.misc;

/**
 * Interface used to define a basic item to migrate
 *
 * @author Alexandre RATEL
 */
public interface ItemToMigrateImpl
	{
	public void startSurvey() throws Exception;//To get the status of the item
	public void migrate() throws Exception;//To start the migration process of this item
	public void rollback() throws Exception;//To rollback the migration
	public void resolve() throws Exception;//To resolve content
	
	/*2019*//*RATEL Alexandre 8)*/
	}
