package com.alex.perceler.office.items;

import java.util.ArrayList;

import com.alex.perceler.axlitems.linkers.TrunkSipLinker;
import com.alex.perceler.misc.CollectionTools;
import com.alex.perceler.misc.ItemToInject;
import com.alex.perceler.soap.items.SipTrunkDestination;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.itemType;

/**********************************
 * Class used to define an item of type "Device Pool"
 * 
 * @author RATEL Alexandre
 **********************************/

public class TrunkSip extends ItemToInject
	{
	/**
	 * Variables
	 */
	private String description,
	product,//"SIP Trunk"
	xClass,//Trunk
	protocol,//SIP
	protocolSide,//User
	callingSearchSpaceName,
	devicePoolName,
	commonDeviceConfigName,
	locationName,
	securityProfileName,
	sipProfileName,
	callingPTransformationCssName,
	useDevicePoolCallingPTransformCss,
	calledPTransformationCssName,
	useDevicePoolCalledPTransformCss,
	subscribeCallingSearchSpaceName,
	sipTrunkType,//None(Default)
	rerouteCallingSearchSpaceName,
	unknownPrefix,
	unknownStripDigits,
	cgpnTransformationUnknownCssName,
	useDevicePoolCgpnTransformCssUnkn;
	
	private ArrayList<SipTrunkDestination> myDestinations;

	/***************
	 * Constructor
	 * @throws Exception 
	 ***************/
	public TrunkSip(String name,
			String description,
			String callingSearchSpaceName,
			String devicePoolName, String commonDeviceConfigName,
			String locationName, String securityProfileName,
			String sipProfileName, String callingPTransformationCssName,
			String useDevicePoolCallingPTransformCss,
			String calledPTransformationCssName,
			String useDevicePoolCalledPTransformCss,
			String subscribeCallingSearchSpaceName,
			String rerouteCallingSearchSpaceName, String unknownPrefix,
			String unknownStripDigits, String cgpnTransformationUnknownCssName,
			String useDevicePoolCgpnTransformCssUnkn,
			ArrayList<SipTrunkDestination> myDestinations) throws Exception
		{
		super(itemType.trunksip, name, new TrunkSipLinker(name));
		this.description = description;
		this.product = "SIP Trunk";
		this.xClass = "Trunk";
		this.protocol = "SIP";
		this.protocolSide = "Network";
		this.callingSearchSpaceName = callingSearchSpaceName;
		this.devicePoolName = devicePoolName;
		this.commonDeviceConfigName = commonDeviceConfigName;
		this.locationName = locationName;
		this.securityProfileName = securityProfileName;
		this.sipProfileName = sipProfileName;
		this.callingPTransformationCssName = callingPTransformationCssName;
		this.useDevicePoolCallingPTransformCss = useDevicePoolCallingPTransformCss;
		this.calledPTransformationCssName = calledPTransformationCssName;
		this.useDevicePoolCalledPTransformCss = useDevicePoolCalledPTransformCss;
		this.subscribeCallingSearchSpaceName = subscribeCallingSearchSpaceName;
		this.sipTrunkType = "None(Default)";
		this.rerouteCallingSearchSpaceName = rerouteCallingSearchSpaceName;
		this.unknownPrefix = unknownPrefix;
		this.unknownStripDigits = unknownStripDigits;
		this.cgpnTransformationUnknownCssName = cgpnTransformationUnknownCssName;
		this.useDevicePoolCgpnTransformCssUnkn = useDevicePoolCgpnTransformCssUnkn;
		this.myDestinations = myDestinations;
		}

	public TrunkSip(String name) throws Exception
		{
		super(itemType.trunksip, name, new TrunkSipLinker(name));
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
		TrunkSip myT = (TrunkSip) linker.get();
		this.UUID = myT.getUUID();
		//Has to be written
		
		Variables.getLogger().debug("Item "+this.name+" already exist in the CUCM");
		return true;
		}
	
	public String getInfo()
		{
		return name+" "+
		UUID+" "+
		description+" "+
		product+" "+
		xClass+" "+
		protocol+" "+
		protocolSide+" "+
		callingSearchSpaceName+" "+
		devicePoolName+" "+
		commonDeviceConfigName+" "+
		locationName+" "+
		securityProfileName+" "+
		sipProfileName+" "+
		callingPTransformationCssName+" "+
		useDevicePoolCallingPTransformCss+" "+
		calledPTransformationCssName+" "+
		useDevicePoolCalledPTransformCss+" "+
		subscribeCallingSearchSpaceName+" "+
		sipTrunkType+" "+
		rerouteCallingSearchSpaceName+" "+
		unknownPrefix+" "+
		unknownStripDigits+" "+
		cgpnTransformationUnknownCssName+" "+
		useDevicePoolCgpnTransformCssUnkn;
		}

	/**
	 * Method used to resolve pattern into real value
	 */
	public void resolve() throws Exception
		{
		name = CollectionTools.getRawValue(name, this, true);
		description = CollectionTools.getRawValue(description, this, true);
		callingSearchSpaceName = CollectionTools.getRawValue(callingSearchSpaceName, this, true);
		devicePoolName = CollectionTools.getRawValue(devicePoolName, this, true);
		commonDeviceConfigName = CollectionTools.getRawValue(commonDeviceConfigName, this, true);
		locationName = CollectionTools.getRawValue(locationName, this, true);
		securityProfileName = CollectionTools.getRawValue(securityProfileName, this, true);
		sipProfileName = CollectionTools.getRawValue(sipProfileName, this, true);
		callingPTransformationCssName = CollectionTools.getRawValue(callingPTransformationCssName, this, true);
		useDevicePoolCallingPTransformCss = CollectionTools.getRawValue(useDevicePoolCallingPTransformCss, this, true);
		calledPTransformationCssName = CollectionTools.getRawValue(calledPTransformationCssName, this, true);
		useDevicePoolCalledPTransformCss = CollectionTools.getRawValue(useDevicePoolCalledPTransformCss, this, true);
		subscribeCallingSearchSpaceName = CollectionTools.getRawValue(subscribeCallingSearchSpaceName, this, true);
		rerouteCallingSearchSpaceName = CollectionTools.getRawValue(rerouteCallingSearchSpaceName, this, true);
		unknownPrefix = CollectionTools.getRawValue(unknownPrefix, this, true);
		unknownStripDigits = CollectionTools.getRawValue(unknownStripDigits, this, true);
		cgpnTransformationUnknownCssName = CollectionTools.getRawValue(cgpnTransformationUnknownCssName, this, true);
		useDevicePoolCgpnTransformCssUnkn = CollectionTools.getRawValue(useDevicePoolCgpnTransformCssUnkn, this, true);
		
		for(SipTrunkDestination d : myDestinations)
			{
			d.resolve();
			}
		
		/**
		 * We set the item parameters
		 */
		TrunkSipLinker tsl = (TrunkSipLinker)linker;
		tsl.setName(name);
		tsl.setDescription(description);
		tsl.setCalledPTransformationCssName(calledPTransformationCssName);
		tsl.setCallingPTransformationCssName(callingPTransformationCssName);
		tsl.setCallingSearchSpaceName(callingSearchSpaceName);
		tsl.setCommonDeviceConfigName(commonDeviceConfigName);
		tsl.setDevicePoolName(devicePoolName);
		tsl.setLocationName(locationName);
		tsl.setMyDestinations(myDestinations);
		tsl.setProduct(product);
		tsl.setProtocol(protocol);
		tsl.setProtocolSide(protocolSide);
		tsl.setSecurityProfileName(securityProfileName);
		tsl.setSipProfileName(sipProfileName);
		tsl.setSipTrunkType(sipTrunkType);
		tsl.setSubscribeCallingSearchSpaceName(subscribeCallingSearchSpaceName);
		tsl.setUseDevicePoolCalledPTransformCss(useDevicePoolCalledPTransformCss);
		tsl.setUseDevicePoolCallingPTransformCss(useDevicePoolCallingPTransformCss);
		tsl.setxClass(xClass);
		tsl.setRerouteCallingSearchSpaceName(rerouteCallingSearchSpaceName);
		tsl.setUnknownPrefix(unknownPrefix);
		tsl.setUnknownStripDigits(unknownStripDigits);
		tsl.setUseDevicePoolCgpnTransformCssUnkn(useDevicePoolCgpnTransformCssUnkn);
		tsl.setCgpnTransformationUnknownCssName(cgpnTransformationUnknownCssName);
		/*********/
		}
	
	/**
	 * Manage the content of the "To Update List"
	 */
	public void manageTuList() throws Exception
		{
		if(UsefulMethod.isNotEmpty(description))tuList.add(TrunkSipLinker.toUpdate.description);
		if(UsefulMethod.isNotEmpty(calledPTransformationCssName))tuList.add(TrunkSipLinker.toUpdate.calledPTransformationCssName);
		if(UsefulMethod.isNotEmpty(callingPTransformationCssName))tuList.add(TrunkSipLinker.toUpdate.callingPTransformationCssName);
		if(UsefulMethod.isNotEmpty(callingSearchSpaceName))tuList.add(TrunkSipLinker.toUpdate.callingSearchSpaceName);
		if(UsefulMethod.isNotEmpty(commonDeviceConfigName))tuList.add(TrunkSipLinker.toUpdate.commonDeviceConfigName);
		if(UsefulMethod.isNotEmpty(devicePoolName))tuList.add(TrunkSipLinker.toUpdate.devicePoolName);
		if(UsefulMethod.isNotEmpty(locationName))tuList.add(TrunkSipLinker.toUpdate.locationName);
		if(UsefulMethod.isNotEmpty(securityProfileName))tuList.add(TrunkSipLinker.toUpdate.securityProfileName);
		if(UsefulMethod.isNotEmpty(sipProfileName))tuList.add(TrunkSipLinker.toUpdate.sipProfileName);
		if(UsefulMethod.isNotEmpty(subscribeCallingSearchSpaceName))tuList.add(TrunkSipLinker.toUpdate.subscribeCallingSearchSpaceName);
		if(UsefulMethod.isNotEmpty(useDevicePoolCalledPTransformCss))tuList.add(TrunkSipLinker.toUpdate.useDevicePoolCallingPTransformCss);
		if(UsefulMethod.isNotEmpty(rerouteCallingSearchSpaceName))tuList.add(TrunkSipLinker.toUpdate.rerouteCallingSearchSpaceName);
		if(UsefulMethod.isNotEmpty(unknownPrefix))tuList.add(TrunkSipLinker.toUpdate.unknownPrefix);
		if(UsefulMethod.isNotEmpty(unknownStripDigits))tuList.add(TrunkSipLinker.toUpdate.unknownStripDigits);
		if(UsefulMethod.isNotEmpty(useDevicePoolCgpnTransformCssUnkn))tuList.add(TrunkSipLinker.toUpdate.useDevicePoolCgpnTransformCssUnkn);
		if(UsefulMethod.isNotEmpty(cgpnTransformationUnknownCssName))tuList.add(TrunkSipLinker.toUpdate.cgpnTransformationUnknownCssName);
		
		if((myDestinations == null) || (myDestinations.size() == 0))
			{
			//Nothing to do
			}
		else
			{
			tuList.add(TrunkSipLinker.toUpdate.myDestinations);
			}
		}
	
	/**
	 * Used to reset the device pool and therefore the associated devices
	 * @throws Exception 
	 */
	public void reset() throws Exception
		{
		if(this.UUID == null)
			{
			linker.get();
			}	
		((TrunkSipLinker)linker).reset();
		}
	
	public String getDescription()
		{
		return description;
		}

	public void setDescription(String description)
		{
		this.description = description;
		((TrunkSipLinker)linker).setDescription(description);
		}

	public String getProduct()
		{
		return product;
		}

	public void setProduct(String product)
		{
		this.product = product;
		((TrunkSipLinker)linker).setProduct(product);
		}

	public String getxClass()
		{
		return xClass;
		}

	public void setxClass(String xClass)
		{
		this.xClass = xClass;
		((TrunkSipLinker)linker).setxClass(xClass);
		}

	public String getProtocol()
		{
		return protocol;
		}

	public void setProtocol(String protocol)
		{
		this.protocol = protocol;
		((TrunkSipLinker)linker).setProtocol(protocol);
		}

	public String getProtocolSide()
		{
		return protocolSide;
		}

	public void setProtocolSide(String protocolSide)
		{
		this.protocolSide = protocolSide;
		((TrunkSipLinker)linker).setProtocolSide(protocolSide);
		}

	public String getCallingSearchSpaceName()
		{
		return callingSearchSpaceName;
		}

	public void setCallingSearchSpaceName(String callingSearchSpaceName)
		{
		this.callingSearchSpaceName = callingSearchSpaceName;
		((TrunkSipLinker)linker).setCallingSearchSpaceName(callingSearchSpaceName);
		}

	public String getDevicePoolName()
		{
		return devicePoolName;
		}

	public void setDevicePoolName(String devicePoolName)
		{
		this.devicePoolName = devicePoolName;
		((TrunkSipLinker)linker).setDevicePoolName(devicePoolName);
		}

	public String getCommonDeviceConfigName()
		{
		return commonDeviceConfigName;
		}

	public void setCommonDeviceConfigName(String commonDeviceConfigName)
		{
		this.commonDeviceConfigName = commonDeviceConfigName;
		((TrunkSipLinker)linker).setCommonDeviceConfigName(commonDeviceConfigName);
		}

	public String getLocationName()
		{
		return locationName;
		}

	public void setLocationName(String locationName)
		{
		this.locationName = locationName;
		((TrunkSipLinker)linker).setLocationName(locationName);
		}

	public String getSecurityProfileName()
		{
		return securityProfileName;
		}

	public void setSecurityProfileName(String securityProfileName)
		{
		this.securityProfileName = securityProfileName;
		((TrunkSipLinker)linker).setSecurityProfileName(securityProfileName);
		}

	public String getSipProfileName()
		{
		return sipProfileName;
		}

	public void setSipProfileName(String sipProfileName)
		{
		this.sipProfileName = sipProfileName;
		((TrunkSipLinker)linker).setSipProfileName(sipProfileName);
		}

	public String getCallingPTransformationCssName()
		{
		return callingPTransformationCssName;
		}

	public void setCallingPTransformationCssName(
			String callingPTransformationCssName)
		{
		this.callingPTransformationCssName = callingPTransformationCssName;
		((TrunkSipLinker)linker).setCallingPTransformationCssName(callingPTransformationCssName);
		}

	public String getUseDevicePoolCallingPTransformCss()
		{
		return useDevicePoolCallingPTransformCss;
		}

	public void setUseDevicePoolCallingPTransformCss(
			String useDevicePoolCallingPTransformCss)
		{
		this.useDevicePoolCallingPTransformCss = useDevicePoolCallingPTransformCss;
		((TrunkSipLinker)linker).setUseDevicePoolCallingPTransformCss(useDevicePoolCallingPTransformCss);
		}

	public String getCalledPTransformationCssName()
		{
		return calledPTransformationCssName;
		}

	public void setCalledPTransformationCssName(String calledPTransformationCssName)
		{
		this.calledPTransformationCssName = calledPTransformationCssName;
		((TrunkSipLinker)linker).setCalledPTransformationCssName(calledPTransformationCssName);
		}

	public String getUseDevicePoolCalledPTransformCss()
		{
		return useDevicePoolCalledPTransformCss;
		}

	public void setUseDevicePoolCalledPTransformCss(
			String useDevicePoolCalledPTransformCss)
		{
		this.useDevicePoolCalledPTransformCss = useDevicePoolCalledPTransformCss;
		((TrunkSipLinker)linker).setUseDevicePoolCalledPTransformCss(useDevicePoolCalledPTransformCss);
		}

	public String getSubscribeCallingSearchSpaceName()
		{
		return subscribeCallingSearchSpaceName;
		}

	public void setSubscribeCallingSearchSpaceName(
			String subscribeCallingSearchSpaceName)
		{
		this.subscribeCallingSearchSpaceName = subscribeCallingSearchSpaceName;
		((TrunkSipLinker)linker).setSubscribeCallingSearchSpaceName(subscribeCallingSearchSpaceName);
		}

	public String getSipTrunkType()
		{
		return sipTrunkType;
		}

	public void setSipTrunkType(String sipTrunkType)
		{
		this.sipTrunkType = sipTrunkType;
		((TrunkSipLinker)linker).setSipTrunkType(sipTrunkType);
		}

	public ArrayList<SipTrunkDestination> getMyDestinations()
		{
		return myDestinations;
		}

	public void setMyDestinations(ArrayList<SipTrunkDestination> myDestinations)
		{
		this.myDestinations = myDestinations;
		((TrunkSipLinker)linker).setMyDestinations(myDestinations);
		}

	public String getRerouteCallingSearchSpaceName()
		{
		return rerouteCallingSearchSpaceName;
		}

	public void setRerouteCallingSearchSpaceName(
			String rerouteCallingSearchSpaceName)
		{
		this.rerouteCallingSearchSpaceName = rerouteCallingSearchSpaceName;
		((TrunkSipLinker)linker).setRerouteCallingSearchSpaceName(rerouteCallingSearchSpaceName);
		}

	public String getUnknownPrefix()
		{
		return unknownPrefix;
		}

	public void setUnknownPrefix(String unknownPrefix)
		{
		this.unknownPrefix = unknownPrefix;
		((TrunkSipLinker)linker).setUnknownPrefix(unknownPrefix);
		}

	public String getUnknownStripDigits()
		{
		return unknownStripDigits;
		}

	public void setUnknownStripDigits(String unknownStripDigits)
		{
		this.unknownStripDigits = unknownStripDigits;
		((TrunkSipLinker)linker).setUnknownStripDigits(unknownStripDigits);
		}

	public String getCgpnTransformationUnknownCssName()
		{
		return cgpnTransformationUnknownCssName;
		}

	public void setCgpnTransformationUnknownCssName(
			String cgpnTransformationUnknownCssName)
		{
		this.cgpnTransformationUnknownCssName = cgpnTransformationUnknownCssName;
		((TrunkSipLinker)linker).setCgpnTransformationUnknownCssName(cgpnTransformationUnknownCssName);
		}

	public String getUseDevicePoolCgpnTransformCssUnkn()
		{
		return useDevicePoolCgpnTransformCssUnkn;
		}

	public void setUseDevicePoolCgpnTransformCssUnkn(
			String useDevicePoolCgpnTransformCssUnkn)
		{
		this.useDevicePoolCgpnTransformCssUnkn = useDevicePoolCgpnTransformCssUnkn;
		((TrunkSipLinker)linker).setUseDevicePoolCgpnTransformCssUnkn(useDevicePoolCgpnTransformCssUnkn);
		}
	
	
	/*2015*//*RATEL Alexandre 8)*/
	}

