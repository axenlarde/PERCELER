package com.alex.perceler.risport;

import java.util.ArrayList;

import com.alex.perceler.device.misc.BasicPhone;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;
import com.cisco.schemas.ast.soap.ArrayOfCmDevice;
import com.cisco.schemas.ast.soap.ArrayOfCmNode;
import com.cisco.schemas.ast.soap.ArrayOfSelectItem;
import com.cisco.schemas.ast.soap.CmDevice;
import com.cisco.schemas.ast.soap.CmNode;
import com.cisco.schemas.ast.soap.CmSelectBy;
import com.cisco.schemas.ast.soap.CmSelectionCriteria;
import com.cisco.schemas.ast.soap.SelectCmDevice;
import com.cisco.schemas.ast.soap.SelectCmDeviceReturn;
import com.cisco.schemas.ast.soap.SelectItem;

/**
 * Static method used for risport request
 *
 * @author Alexandre RATEL
 */
public class RisportTools
	{
	
	/**
	 * To get a phone status using RIS
	 */
	public static BasicPhone getPhoneStatus(String deviceName)
		{
		try
			{
			SelectCmDevice sxmlParams = new SelectCmDevice();
			CmSelectionCriteria criteria = new CmSelectionCriteria();
			long maxNum = 1000;
			long modelNum = 255;
			ArrayOfSelectItem items = new ArrayOfSelectItem();
			
			SelectItem item = new SelectItem();
			item.setItem(deviceName);
			items.getItem().add(item);
			
			criteria.setMaxReturnedDevices(maxNum);
			criteria.setModel(modelNum);
			criteria.setDeviceClass("Phone");
			criteria.setStatus("Any");
			criteria.setSelectBy(CmSelectBy.NAME);
			criteria.setSelectItems(items);
			sxmlParams.setCmSelectionCriteria(criteria);

			//make selectCmDevice request
			SelectCmDeviceReturn selectResponse = Variables.getRisConnection().selectCmDevice("",criteria);
			
			return getDeviceFromRISResponse(selectResponse).get(0);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving phone status using RIS for device : "+deviceName);
			}
		
		return null;
		}
	
	/**
	 * To get the deviceList from the RIS Response
	 */
	private static ArrayList<BasicPhone> getDeviceFromRISResponse(SelectCmDeviceReturn selectResponse)
		{
		ArrayList<BasicPhone> pl = new ArrayList<BasicPhone>();
		
		for(CmNode node : selectResponse.getSelectCmDeviceResult().getCmNodes().getItem())
			{
			ArrayOfCmDevice dl = node.getCmDevices();
			
			for(CmDevice d : dl.getItem())
				{
				pl.add(new BasicPhone(d.getName(),
						d.getDescription(),
						d.getModel().toString(),
						d.getIPAddress().getItem().get(0).getIP(),
						d.getStatus().name()));
				}
			}
		
		Variables.getLogger().debug(pl.size()+" phone found");
		
		return pl;
		}
	
	
	/**
	 * Used to get the status of the given phones using risport
	 */
	public static ArrayList<BasicPhone> doPhoneSurvey(ArrayList<BasicPhone> phoneList)
		{
		try
			{
			if(phoneList.size() == 0)throw new Exception("Phone list cannot be empty");
			/**
			 * We limit the number of phone to request at the same time because
			 * we know the RIS service works better with less phones
			 */
			int maxPhoneRequest = Integer.parseInt(UsefulMethod.getTargetOption("rismaxphonerequest"));
			ArrayList<BasicPhone> result = new ArrayList<BasicPhone>();
			ArrayList<BasicPhone> temp = new ArrayList<BasicPhone>();
			int index = 0;
			while(index <= phoneList.size())
				{
				temp.add(phoneList.get(index));
				index++;
				if((temp.size()==maxPhoneRequest) || (index == phoneList.size()))
					{
					CmSelectionCriteria criteria = buildRISRequest(temp);
					
					//make selectCmDevice request
					SelectCmDeviceReturn selectResponse = Variables.getRisConnection().selectCmDevice("",criteria);
					result.addAll(getDeviceFromRISResponse(selectResponse));
					temp.clear();
					}
				}
			return result;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving phone status using RIS : "+e.getMessage());
			}
		return null;
		}
	
	/**
	 * To build a RIS request
	 */
	private static CmSelectionCriteria buildRISRequest(ArrayList<BasicPhone> phoneList)
		{
		SelectCmDevice sxmlParams = new SelectCmDevice();
		CmSelectionCriteria criteria = new CmSelectionCriteria();
		long maxNum = 1000;
		long modelNum = 255;
		ArrayOfSelectItem items = new ArrayOfSelectItem();
		
		for(BasicPhone p : phoneList)
			{
			SelectItem item = new SelectItem();
			item.setItem(p.getName());
			items.getItem().add(item);
			}
		
		criteria.setMaxReturnedDevices(maxNum);
		criteria.setModel(modelNum);
		criteria.setDeviceClass("Phone");
		criteria.setStatus("Any");
		criteria.setSelectBy(CmSelectBy.NAME);
		criteria.setSelectItems(items);
		sxmlParams.setCmSelectionCriteria(criteria);
		
		return criteria;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
