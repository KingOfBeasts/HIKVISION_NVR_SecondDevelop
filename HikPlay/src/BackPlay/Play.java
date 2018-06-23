/**
 * Play.java
 * 
 * Created on 2018-6-9 14:42:25
 */

/**
 * 
 * @author KingOfBeasts
 */

package BackPlay;

import java.util.Scanner;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.NativeLongByReference;

import BackPlay.HCNetSDK.NET_DVR_STREAM_INFO;
import BackPlay.HCNetSDK.NET_DVR_VOD_PARA;

import BackPlay.DataCallBack;

public class Play {
	static HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
	private static DeviceInfo devInfo;
	private static NativeLong PlayHandle;
	private static final String OPERATION_EXIT = "EXIT";
	//private static FPlayDataCallBack DataCallBack;
	static PlayCtrl playCtrl = PlayCtrl.INSTANCE; 
	
	public static void main(String[] args) {
		
		hcNetSDK.NET_DVR_Init(); //   initialize
		
		//hcNetSDK.NET_DVR_SetConnectTime(2000, 1);  //Connect time
		//hcNetSDK.NET_DVR_SetReconnect(1000,true);  //Reconnect time
		
		//Register device
		
	   devInfo = new DeviceInfo("192.168.2.65",8000,"admin","wuzhilian2018");
		
		NativeLong UserID = LoginDevice(devInfo);
		
		System.out.println(UserID);
		
		devInfo.SetUserID(UserID);
		
	   PlayHandle = PlayBackByTime();
	   
		System.out.println(PlayHandle);
		Boolean value = hcNetSDK.NET_DVR_PlayBackControl(PlayHandle, HCNetSDK.NET_DVR_PLAYSTART, 0 , null);
		
		if(value == false) {
			System.out.println("play failed");
		}
		
	
		
		
		DataCallBack dataCallBack = new DataCallBack();
	   
		Boolean result = hcNetSDK.NET_DVR_SetPlayDataCallBack(PlayHandle, dataCallBack, devInfo.GetUserID().intValue());
		
	   System.out.println(result);
	   
	   
	   Scanner scan = new Scanner(System.in);
       while(scan.hasNext()) {
           String in = scan.next().toString();
           if(OPERATION_EXIT.equals(in.toUpperCase())
                   || OPERATION_EXIT.substring(0, 1).equals(in.toUpperCase())) {
               System.out.println("您成功已退出！");
               break;
           }
           System.out.println("您输入的值："+in);
       }
		System.out.print(hcNetSDK.NET_DVR_GetLastError());
	}
	

	private static NativeLong LoginDevice(DeviceInfo deviceInfo) {
	      HCNetSDK.NET_DVR_USER_LOGIN_INFO struLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();
	      HCNetSDK.NET_DVR_DEVICEINFO_V40 struDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();
	      
	      Pointer PointerstruDeviceInfoV40 = struDeviceInfo.getPointer();
			Pointer PointerstruLoginInfo = struLoginInfo.getPointer();
	      
	        for (int i = 0; i < deviceInfo.GetIP().length(); i++) {
	            struLoginInfo.sDeviceAddress[i] = (byte) deviceInfo.GetIP().charAt(i);
	        }
	        for (int i = 0; i < deviceInfo.GetPassWord().length(); i++) {
	            struLoginInfo.sPassword[i] = (byte) deviceInfo.GetPassWord().charAt(i);
	        }
	        for (int i = 0; i < deviceInfo.GetUserName().length(); i++) {
	            struLoginInfo.sUserName[i] = (byte) deviceInfo.GetUserName().charAt(i);
	        }
	        struLoginInfo.wPort = (short) deviceInfo.GetPort();
	        struLoginInfo.write();

	      return  hcNetSDK.NET_DVR_Login_V40( PointerstruLoginInfo, PointerstruDeviceInfoV40);
	}
	
	public static NativeLong PlayBackByTime() {
		HCNetSDK.NET_DVR_TIME struStartTime;
		HCNetSDK.NET_DVR_TIME struEndTime;
		
		struStartTime = new HCNetSDK.NET_DVR_TIME();
		struEndTime = new HCNetSDK.NET_DVR_TIME();
		
		
		//Start Time
		struStartTime.dwYear = 2018;
		struStartTime.dwMonth = 6;
		struStartTime.dwDay = 12;
		struStartTime.dwHour = 15;
		struStartTime.dwMinute = 0;
		struStartTime.dwSecond = 0;
		
		//EndTime
		struEndTime.dwYear = 2018;
		struEndTime.dwMonth = 6;
		struEndTime.dwDay = 12;
		struEndTime.dwHour = 16;
		struEndTime.dwMinute = 0;
		struEndTime.dwSecond = 0;
		
		
		NET_DVR_VOD_PARA struVoidParam = new NET_DVR_VOD_PARA();
		
		struVoidParam.dwSize = struVoidParam.size();
		struVoidParam.struBeginTime = struStartTime;
		struVoidParam.struEndTime = struEndTime;
		
		NET_DVR_STREAM_INFO struInfo = new NET_DVR_STREAM_INFO();
		
		struInfo.dwSize = struInfo.size();
		struInfo.dwChannel = 33;
		
		struVoidParam.struIDInfo = struInfo;
		
		return hcNetSDK.NET_DVR_PlayBackByTime_V40(devInfo.GetUserID(),struVoidParam);
		
		
	}
	

}
