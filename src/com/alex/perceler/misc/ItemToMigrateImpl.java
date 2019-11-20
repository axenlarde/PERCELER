package com.alex.perceler.misc;

import java.util.ArrayList;

import com.alex.perceler.utils.Variables.actionType;

/**
 * Interface used to define a basic item to migrate
 *
 * @author Alexandre RATEL
 */
public interface ItemToMigrateImpl
	{
	public ArrayList<String> startSurvey() throws Exception;//To get the status of the item
	public void doStartSurvey() throws Exception;//Is called in addition of the main method
	public void update() throws Exception;//To start the migration process of this item
	public void doUpdate() throws Exception;//Is called in addition of the main method
	public void resolve() throws Exception;//To resolve content
	public void doResolve() throws Exception;//Is called in addition of the main method
	public void init() throws Exception;//To init the item
	public void doInit() throws Exception;//Is called in addition of the main method
	public void build() throws Exception;//To build the item
	public void doBuild() throws Exception;//Is called in addition of the main method
	public String getInfo();//To display item info
	public void reset();//To reset the item
	public void doReset();//Is called in addition of the main method
	
	/*2019*//*RATEL Alexandre 8)*/
	}
