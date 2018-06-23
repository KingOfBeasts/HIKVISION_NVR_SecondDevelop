/**
 * Play.java
 */
/**
 * @author KingOfBeasts
 */
package test;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import test.HCNetSDK;
import test.HCNetSDK.NET_DVR_PLAYCOND;
import test.HCNetSDK.NET_DVR_STREAM_INFO;
import test.HCNetSDK.NET_DVR_VOD_PARA;

public class Play {
    public static HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
	 public static DeviceInfo devInfo;  //设备信息
	 public static NativeLong playHandle; //播放句柄
	 
	 private static final String OPERATION_EXIT = "EXIT";
	 
	 //PlayCtrl
	 public static int PLAYSTART = 1; //宏定义 开始播放
	 public static int PLAYPAUSE = 3; //暂停播放
	 public static int PLAYRESTART = 4;//恢复播放
	 public static int PLAYGETPOS = 13;//获取进度
	 public static int SETSPEED = 24;//设置下载速度，速度单位： kbps，最小为 256kbps，最大为设备带宽
	 public static int TRANS_TYPE = 32;//设置封装类型
	 
	 public static int produce = 0; //进度
	 
	 public static void main(String[] args) {
		 System.out.println(args[0]);  //IP
		 System.out.println(args[1]);  //port
		 System.out.println(args[2]);  //UserName
		 System.out.println(args[3]);  //PassWord
		 System.out.println(args[4]);  //channel
		 System.out.println(args[5]);  //startTime
		 System.out.println(args[6]);  //endTime
		 System.out.println(args[7]);  //download path
		 
		 String IP = args[0];                                  
		 int Port = Integer.parseInt(args[1]);
		 String UserName = args[2];
		 String PassWord = args[3];
		 int Channel = Integer.parseInt(args[4]);
		 Date startTime = new Date(Long.parseLong(args[5]));
		 Date endTime = new Date(Long.parseLong(args[6]));
		 String path = args[7];
		 
		 //初始化
		 Boolean init = hcNetSDK.NET_DVR_Init();
		 System.out.println(init);  //返回值为布尔值 fasle初始化失败
		 
		 //新建一个设备信息类
		 //devInfo = new DeviceInfo("192.168.2.65",8000,"admin","wuzhilian2018");
		 devInfo = new DeviceInfo(IP,Port,UserName,PassWord);
		 
		 
		 devInfo.SetUserID(loginDev(devInfo));
		 
		 //按时间下载文件
		 //String path = "/root/test.mp4";   //下载路径
		 //int Channel = 33;                  //通道号，通道号是从33开始的
		 //Date startTime = new Date(1529110000000L);   //开始时间
		 //Date endTime = new Date(1529120836335L);     //结束时间
		 
		 playHandle = getFileByTime(devInfo.GetUserID(),path,Channel,startTime,endTime);  //得到播放句柄
		  
		 
		 Boolean transResult = new PlayController(playHandle,TRANS_TYPE,1).start(); //转码
		 System.out.println(transResult);    //false 转码失败
		 
		 Boolean downResult = new PlayController(playHandle,PLAYSTART,0).start(); //开始下载
		 System.out.println(downResult);     //false 下载失败
		
		 if(downResult) {
			 while(produce != 100 && produce !=200) {    //进度为100代表下载结束，进度为200则代表下载异常结束
				 produce = new PlayController(playHandle,PLAYGETPOS).produce();
				 System.out.println(produce);                    //输出进度
			 }
		 }
		 
		 
		 System.out.print(hcNetSDK.NET_DVR_GetLastError());   //输出最终异常退出的代码以判断问题出在什么地方
		 
		 
	
	}
	 
	 //登陆设备
	public static NativeLong loginDev(DeviceInfo devInfo) {
		HCNetSDK.NET_DVR_USER_LOGIN_INFO struLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();
	      HCNetSDK.NET_DVR_DEVICEINFO_V40 struDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();
	      
	      Pointer PointerstruDeviceInfoV40 = struDeviceInfo.getPointer();
			Pointer PointerstruLoginInfo = struLoginInfo.getPointer();
	      
	        for (int i = 0; i < devInfo.GetIP().length(); i++) {
	            struLoginInfo.sDeviceAddress[i] = (byte) devInfo.GetIP().charAt(i);
	        }
	        for (int i = 0; i < devInfo.GetPassWord().length(); i++) {
	            struLoginInfo.sPassword[i] = (byte) devInfo.GetPassWord().charAt(i);
	        }
	        for (int i = 0; i < devInfo.GetUserName().length(); i++) {
	            struLoginInfo.sUserName[i] = (byte) devInfo.GetUserName().charAt(i);
	        }
	        struLoginInfo.wPort = (short) devInfo.GetPort();
	        struLoginInfo.write();

	      return  hcNetSDK.NET_DVR_Login_V40( PointerstruLoginInfo, PointerstruDeviceInfoV40);
		
	}
	
	//按时间下载
	public static NativeLong getFileByTime(NativeLong UserID,String Path,int Channel,Date StartTime,Date EndTime) {
		HCNetSDK.NET_DVR_TIME startTime = new HCNetSDK.NET_DVR_TIME();//开始时间
		HCNetSDK.NET_DVR_TIME endTime = new HCNetSDK.NET_DVR_TIME();//结束时间
		
		Calendar start = Calendar.getInstance();
		start.setTime(StartTime);
		
		startTime.dwYear = start.get(Calendar.YEAR);
		startTime.dwMonth = start.get(Calendar.MONTH)+1;
		startTime.dwDay = start.get(Calendar.DATE);
		startTime.dwHour = start.get(Calendar.HOUR_OF_DAY);
		startTime.dwMinute = start.get(Calendar.MINUTE);
		startTime.dwSecond = start.get(Calendar.SECOND);
		
		Calendar end = Calendar.getInstance();
		end.setTime(EndTime);
		
		endTime.dwYear = end.get(Calendar.YEAR);
		endTime.dwMonth = end.get(Calendar.MONTH)+1;
		endTime.dwDay = end.get(Calendar.DATE);
		endTime.dwHour = end.get(Calendar.HOUR_OF_DAY);
		endTime.dwMinute = end.get(Calendar.MINUTE);
		endTime.dwSecond = end.get(Calendar.SECOND);
		
		NET_DVR_PLAYCOND playCond = new NET_DVR_PLAYCOND();
		
		playCond.struStartTime = startTime;
		playCond.struStopTime = endTime;
		playCond.dwChannel = Channel;
		
		return hcNetSDK.NET_DVR_GetFileByTime_V40(UserID, Path, playCond);
		
	}
	

}
