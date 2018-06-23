package test;

import com.sun.jna.NativeLong;

public class DeviceInfo {
	private String DVRIP;
	private int DVRPort;	
	private String PassWord;
	private NativeLong UserID;
	private String UserName;
	
	public DeviceInfo(String DVRIP,int DVRPort,String UserName, String PassWord) {
		this.DVRIP = DVRIP;
		this.DVRPort = DVRPort;
		this.PassWord = PassWord;
		this.UserName = UserName;

	}
	
	public void SetUserID(NativeLong UserID) {
		this.UserID = UserID;
	}
	
	public NativeLong GetUserID() {
		return this.UserID;
	}
	
	public String GetIP() {
		return this.DVRIP;
	}

	public String GetUserName() {
		return this.UserName;
	}
	
	public int GetPort() {
		return this.DVRPort;
	}
	
	public String GetPassWord() {
		return this.PassWord;
	}
}
