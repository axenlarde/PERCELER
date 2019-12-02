package com.alex.perceler.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.log4j.Level;

import com.alex.perceler.cli.CliConnection;
import com.alex.perceler.cli.CliInjector;
import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.cli.OneLine;
import com.alex.perceler.cli.OneLine.cliType;
import com.alex.perceler.core.LetsParty;
import com.alex.perceler.device.misc.Device;
import com.alex.perceler.utils.InitLogging;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.actionType;
import com.alex.perceler.utils.Variables.itmType;


/**
 * Perceler main class
 *
 * @author Alexandre RATEL
 */
public class Main
	{
	
	public Main()
		{
		//Set the software name
		Variables.setSoftwareName("PERCELER");
		//Set the software version
		Variables.setSoftwareVersion("1.0");
		
		/****************
		 * Initialization of the logging
		 */
		Variables.setLogger(InitLogging.init(Variables.getSoftwareName()+"_LogFile.txt"));
		Variables.getLogger().info("\r\n");
		Variables.getLogger().info("#### Entering application");
		Variables.getLogger().info("## Welcome to : "+Variables.getSoftwareName()+" version "+Variables.getSoftwareVersion());
		Variables.getLogger().info("## Author : RATEL Alexandre : 2019");
		/*******/
		
		/******
		 * Initialization of the variables
		 */
		new Variables();
		/************/
		
		/**********
		 * We check if the java version is compatible
		 */
		UsefulMethod.checkJavaVersion();
		/***************/
		
		/**********************
		 * Reading of the configuration file
		 */
		try
			{
			//Config file reading
			Variables.setTabConfig(UsefulMethod.readMainConfigFile(Variables.getConfigFileName()));
			
			//Database file reading
			UsefulMethod.initDatabase();
			
			/***
			 * Server connection initialization
			 * 
			 * We do it now to avoid latency during real request
			 */
			UsefulMethod.initAXLConnectionToCUCM();
			UsefulMethod.initRISConnectionToCUCM();
			}
		catch(Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/********************/
		
		/*****************
		 * Setting of the inside variables from what we read in the configuration file
		 */
		try
			{
			UsefulMethod.initInternalVariables();
			}
		catch(Exception exc)
			{
			Variables.getLogger().error(exc.getMessage());
			Variables.getLogger().setLevel(Level.INFO);
			}
		/*********************/
		
		/****************
		 * Init email server
		 */
		try
			{
			UsefulMethod.initEMailServer();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			Variables.getLogger().error("Failed to init the eMail server : "+e.getMessage());
			}
		/*************/
		
		/*******************
		 * Start main user interface
		 */
		try
			{
			Variables.getLogger().info("Launching Main Class");
			new LetsParty();//main file
			}
		catch (Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/******************/
		
		//End of the main class
		}
	
	
	

	public static void main(String[] args)
		{
		new Main();
		}
	/*2019*//*RATEL Alexandre 8)*/
	}
