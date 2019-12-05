package com.alex.perceler.utils;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.alex.perceler.action.Task;
import com.alex.perceler.cli.CliInjector;
import com.alex.perceler.cli.CliProfile;
import com.alex.perceler.device.misc.BasicDevice;
import com.alex.perceler.misc.ValueMatcher;
import com.alex.perceler.misc.storedUUID;
import com.alex.perceler.office.misc.BasicOffice;
import com.cisco.schemas.ast.soap.RisPortType;


/**********************************
 * Used to store static variables
 * 
 * @author RATEL Alexandre
 **********************************/
public class Variables
	{
	/**
	 * Variables
	 */
	//Enum
	/***
	 * itemType :
	 * Is used to give a type to the request ready to be injected
	 * This way we can manage or sort them more easily
	 * 
	 * The order is important here, indeed, it will define later
	 * how the items are injected
	 */
	public enum itemType
		{
		location,
		region,
		srstreference,
		devicepool,
		commondeviceconfig,
		commonPhoneConfig,
		securityProfile,
		conferencebridge,
		mediaresourcegroup,
		mediaresourcegrouplist,
		partition,
		callingsearchspace,
		trunksip,
		vg,
		routegroup,
		routelist,
		translationpattern,
		callingpartytransformationpattern,
		calledpartytransformationpattern,
		physicallocation,
		devicemobilityinfo,
		devicemobilitygroup,
		datetimesetting,
		callmanagergroup,
		phone,
		udp,
		user,
		line,
		voicemail,
		telecasterservice,
		siptrunksecurityprofile,
		sipprofile,
		phonetemplatename,
		linegroup,
		huntlist,
		huntpilot,
		callpickupgroup,
		udplogin,
		aargroup,
		usercontrolgroup,
		analog,
		gateway,
		sqlRequest,
		associateanalog,
		userlocal,
		softkeytemplate,
		unknown
		};
	
	/********************************************
	 * statusType :
	 * Is used to set the status of a request as followed :
	 * - init : the request has to be built
	 * - waiting : The request is ready to be injected. We gonna reach this status after the request has been built or has been deleted
	 * - processing : The injection or the deletion of the request is currently under progress
	 * - disabled : The request has not to be injected
	 * - injected : The request has been injected with success
	 * - error : Something went wrong and an exception has been thrown
	 ***************************************/
	public enum statusType
		{
		injected,
		error,
		processing,
		waiting,
		disabled,
		init,
		deleted,
		updated
		};
		
	/********************************************
	 * cucmAXLVersion :
	 * Is used to set the cucm AXL version used for the injection
	 ***************************************/
	public enum cucmAXLVersion
		{
		version80,
		version85,
		version90,
		version91,
		version100,
		version105,
		version110,
		version115
		};
	
	/********************************************
	 * actionType :
	 * Is used to set the type of action is going to do a 
	 ***************************************/
	public enum actionType
		{
		inject,
		delete,
		update,
		rollback,
		reset
		};
	
	/********************************************
	 * actionType :
	 * Is used to set the type of action is going to do a 
	 ***************************************/
	public enum provisioningType
		{
		user,
		office,
		isr,
		vg,
		audiocode,
		ascom
		};
		
	/********************************************
	 * substituteType :
	 * Is used to know what is the current data source to use
	 ***************************************/
	public enum SubstituteType
		{
		phone,
		pbt,
		css,
		profile,
		misc
		};
		
	/**
	 * ItemToMigrate type
	 */
	public enum itmType
		{
		office,
		isr,
		vg,
		audiocode,
		ascom,
		phone,
		sip
		};
		
	/**
	 * ascom type
	 */
	public enum ascomType
		{
		master,
		standby,
		ims3,
		slave
		};
	
	/**
	 * Office Type
	 */
	public enum officeType
		{
		CAF,
		ANT,
		CER,
		CNA,
		CNE,
		PRM
		};
		
	//Misc
	private static String softwareName;
	private static String softwareVersion;
	private static cucmAXLVersion CUCMVersion;
	private static Logger logger;
	private static ArrayList<String[][]> tabConfig;
	private static ArrayList<BasicOffice> OfficeList;
	private static ArrayList<BasicDevice> DeviceList;
	private static eMailSender eMSender;
	private static String mainDirectory;
	private static String configFileName;
	private static String matcherFileName;
	private static String officeListFileName;
	private static String deviceListFileName;
	private static String cliProfileListFileName;
	private static String substitutesFileName;
	private static ArrayList<String> matcherList;
	private static ArrayList<ValueMatcher> substituteList;
	private static ArrayList<storedUUID> uuidList;
	private static boolean CUCMReachable;
	private static String collectionFileName;
	private static Workbook myWorkbook;
	private static ArrayList<Task> taskList;
	private static ArrayList<String> migratedItemList;
	private static String migratedItemFileName;
	private static String logFileName;
	
	//Langage management
	public enum language{english,french};
	private static String languageFileName;
	private static ArrayList<ArrayList<String[][]>> languageContentList;
	
	//AXL
    private static com.cisco.axlapiservice10.AXLPort AXLConnectionToCUCMV105;//Connection to CUCM version 105
	
    //RISPORT
    private static RisPortType risConnection;
    
    //CLI
    private static ArrayList<CliProfile> cliProfileList;
    
    /**************
     * Constructor
     **************/
	public Variables()
		{
		CUCMReachable = true;
		uuidList = new ArrayList<storedUUID>();
		mainDirectory = ".";
		configFileName = "configFile.xml";
		matcherFileName = "matchers.xml";
		officeListFileName = "officeList.xml";
		deviceListFileName = "deviceList.xml";
		cliProfileListFileName = "cliProfileList.xml";
		languageFileName = "languages.xml";
		substitutesFileName = "substitutes.xml";
		collectionFileName = "database.xlsx";
		migratedItemFileName = "migratedItemsList.xml";
		}

	public static String getSoftwareName()
		{
		return softwareName;
		}

	public static void setSoftwareName(String softwareName)
		{
		Variables.softwareName = softwareName;
		}

	public static String getSoftwareVersion()
		{
		return softwareVersion;
		}

	public static void setSoftwareVersion(String softwareVersion)
		{
		Variables.softwareVersion = softwareVersion;
		}

	public static cucmAXLVersion getCUCMVersion()
		{
		if(CUCMVersion == null)
			{
			//It has to be initiated
			try
				{
				CUCMVersion = UsefulMethod.convertStringToCUCMAXLVersion(UsefulMethod.getTargetOption("axlversion"));
				Variables.getLogger().info("CUCM version : "+Variables.getCUCMVersion());
				}
			catch(Exception e)
				{
				getLogger().debug("The AXL version couldn't be parsed. We will use the default version", e);
				CUCMVersion = cucmAXLVersion.version85;
				}
			}
		
		return CUCMVersion;
		}

	public static void setCUCMVersion(cucmAXLVersion cUCMVersion)
		{
		CUCMVersion = cUCMVersion;
		}

	public synchronized static Logger getLogger()
		{
		return logger;
		}

	public static void setLogger(Logger logger)
		{
		Variables.logger = logger;
		}

	public static ArrayList<String[][]> getTabConfig()
		{
		return tabConfig;
		}

	public static void setTabConfig(ArrayList<String[][]> tabConfig)
		{
		Variables.tabConfig = tabConfig;
		}

	public static ArrayList<BasicOffice> getOfficeList() throws Exception
		{
		if(OfficeList == null)
			{
			Variables.getLogger().debug("Initialisation of OfficeList");
			Variables.setOfficeList(UsefulMethod.initOfficeList());
			}
		
		return OfficeList;
		}

	public static void setOfficeList(ArrayList<BasicOffice> officeList)
		{
		OfficeList = officeList;
		}

	public static eMailSender geteMSender()
		{
		return eMSender;
		}

	public static void seteMSender(eMailSender eMSender)
		{
		Variables.eMSender = eMSender;
		}

	public static String getMainDirectory() throws Exception
		{
		return mainDirectory;
		}

	public static void setMainDirectory(String mainConfigFileDirectory)
		{
		Variables.mainDirectory = mainConfigFileDirectory;
		}

	public static String getConfigFileName()
		{
		return configFileName;
		}

	public static void setConfigFileName(String configFileName)
		{
		Variables.configFileName = configFileName;
		}

	public static String getMatcherFileName()
		{
		return matcherFileName;
		}

	public static void setMatcherFileName(String matcherFileName)
		{
		Variables.matcherFileName = matcherFileName;
		}

	public static String getOfficeListFileName()
		{
		return officeListFileName;
		}

	public static void setOfficeListFileName(String officeListFileName)
		{
		Variables.officeListFileName = officeListFileName;
		}

	public static String getLanguageFileName()
		{
		return languageFileName;
		}

	public static void setLanguageFileName(String languageFileName)
		{
		Variables.languageFileName = languageFileName;
		}

	public static ArrayList<String> getMatcherList() throws Exception
		{
		if(matcherList == null)
			{
			Variables.getLogger().debug("Initialisation of matcherList");
			Variables.setMatcherList(UsefulMethod.readFile("matchers", Variables.getMatcherFileName()));
			}
		return matcherList;
		}

	public static void setMatcherList(ArrayList<String> matcherList)
		{
		Variables.matcherList = matcherList;
		}

	public static ArrayList<storedUUID> getUuidList()
		{
		return uuidList;
		}

	public static void setUuidList(ArrayList<storedUUID> uuidList)
		{
		Variables.uuidList = uuidList;
		}

	public static boolean isCUCMReachable()
		{
		return CUCMReachable;
		}

	public static void setCUCMReachable(boolean cUCMReachable)
		{
		CUCMReachable = cUCMReachable;
		}

	public static ArrayList<ArrayList<String[][]>> getLanguageContentList() throws Exception
		{
		if(languageContentList == null)
			{
			Variables.getLogger().debug("Initialisation of languageContentList");
			Variables.setLanguageContentList(UsefulMethod.readExtFile("language", Variables.getLanguageFileName()));
			}
		
		return languageContentList;
		}

	public static void setLanguageContentList(
			ArrayList<ArrayList<String[][]>> languageContentList)
		{
		Variables.languageContentList = languageContentList;
		}

	public static com.cisco.axlapiservice10.AXLPort getAXLConnectionToCUCMV105() throws Exception
		{
		if(AXLConnectionToCUCMV105 == null)
			{
			UsefulMethod.initAXLConnectionToCUCM();
			}
		return AXLConnectionToCUCMV105;
		}

	public static void setAXLConnectionToCUCMV105(
			com.cisco.axlapiservice10.AXLPort aXLConnectionToCUCMV105)
		{
		AXLConnectionToCUCMV105 = aXLConnectionToCUCMV105;
		}

	public static String getSubstitutesFileName()
		{
		return substitutesFileName;
		}

	public static void setSubstitutesFileName(String substitutesFileName)
		{
		Variables.substitutesFileName = substitutesFileName;
		}

	public static ArrayList<ValueMatcher> getSubstituteList() throws Exception
		{
		if(substituteList == null)
			{
			Variables.getLogger().debug("Initialisation of substituteList");
			Variables.setSubstituteList(UsefulMethod.initSubstituteList(UsefulMethod.readFileTab("substitute", Variables.getSubstitutesFileName())));
			}
		
		return substituteList;
		}

	public static void setSubstituteList(ArrayList<ValueMatcher> substituteList)
		{
		Variables.substituteList = substituteList;
		}
	
	public static Workbook getMyWorkbook() throws Exception
		{
		if(myWorkbook == null)
			{
			Variables.getLogger().debug("Initialisation of myWorkbook");
			myWorkbook = WorkbookFactory.create(new FileInputStream(Variables.getCollectionFileName()));
			}
		
		return myWorkbook;
		}

	public static void setMyWorkbook(Workbook myWorkbook)
		{
		Variables.myWorkbook = myWorkbook;
		}

	public static ArrayList<BasicDevice> getDeviceList() throws Exception
		{
		if(DeviceList == null)
			{
			Variables.getLogger().debug("Initialisation of DeviceList");
			Variables.setDeviceList(UsefulMethod.initDeviceList());
			}
		
		return DeviceList;
		}

	public static void setDeviceList(ArrayList<BasicDevice> deviceList)
		{
		DeviceList = deviceList;
		}

	public static String getCollectionFileName()
		{
		return collectionFileName;
		}

	public static void setCollectionFileName(String collectionFileName)
		{
		Variables.collectionFileName = collectionFileName;
		}

	public static String getDeviceListFileName()
		{
		return deviceListFileName;
		}

	public static void setDeviceListFileName(String deviceListFileName)
		{
		Variables.deviceListFileName = deviceListFileName;
		}

	public static synchronized ArrayList<Task> getTaskList()
		{
		if(taskList == null)
			{
			taskList = new ArrayList<Task>();
			}
		return taskList;
		}

	public static synchronized void setTaskList(ArrayList<Task> taskList)
		{
		Variables.taskList = taskList;
		}

	public static RisPortType getRisConnection() throws Exception
		{
		if(risConnection == null)
			{
			UsefulMethod.initRISConnectionToCUCM();
			}
		return risConnection;
		}

	public static void setRisConnection(RisPortType risConnection)
		{
		Variables.risConnection = risConnection;
		}

	public static ArrayList<CliProfile> getCliProfileList() throws Exception
		{
		if(cliProfileList == null)
			{
			UsefulMethod.initCliProfileList();
			}
		return cliProfileList;
		}

	public static void setCliProfileList(ArrayList<CliProfile> cliProfileList)
		{
		Variables.cliProfileList = cliProfileList;
		}

	public static String getCliProfileListFileName()
		{
		return cliProfileListFileName;
		}

	public static void setCliProfileListFileName(String cliProfileListFileName)
		{
		Variables.cliProfileListFileName = cliProfileListFileName;
		}

	public static ArrayList<String> getMigratedItemList()
		{
		return migratedItemList;
		}

	public static void setMigratedItemList(ArrayList<String> migratedItemList)
		{
		Variables.migratedItemList = migratedItemList;
		}

	public static String getMigratedItemFileName()
		{
		return migratedItemFileName;
		}

	public static void setMigratedItemFileName(String migratedItemFileName)
		{
		Variables.migratedItemFileName = migratedItemFileName;
		}

	public static String getLogFileName()
		{
		return logFileName;
		}

	public static void setLogFileName(String logFileName)
		{
		Variables.logFileName = logFileName;
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}

