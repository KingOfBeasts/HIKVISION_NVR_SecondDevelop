/**
 * DeviceInfo.java
 * 
 * Created on 2018-6-11, 19:26
 */

/**
 * 
 * @author KingOfBeasts
 */

package BackPlay;

import com.sun.jna.NativeLong;

public class DeviceInfo {
	private String DVRIP;//IP
	private int DVRPort;//Port
	private String UserName;//UserName
	private String PassWord;//PassWord
	private NativeLong UserID;//UserID
	
	DeviceInfo(String DVRIP, int DVRPort, String UserName, String PassWord){
		this.DVRIP = DVRIP;
		this.DVRPort = DVRPort;
		this.UserName = UserName;
		this.PassWord = PassWord;
	}
	
	public void SetUserID(NativeLong UserID){
		this.UserID = UserID;
	}
	
	public String GetIP() {
		return this.DVRIP;
	}
	
	public int GetPort() {
		return this.DVRPort;
	}
	
	public String GetUserName() {
		return this.UserName;
	}
	
	public String GetPassWord() {
		return this.PassWord;
	}
	
	public NativeLong GetUserID() {
		return this.UserID;
	}
	
    

}
