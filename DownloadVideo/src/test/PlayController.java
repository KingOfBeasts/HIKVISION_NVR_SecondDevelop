package test;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class PlayController {
	public NativeLong playHandle;
	public int controlCode;
	public Pointer inBuffer; 
	public Pointer outBuffer;
	public IntByReference outValue;
   public HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;	
	public PlayController(NativeLong playHandle,int controlCode,int inBuffer) {
		this.playHandle = playHandle;
		this.controlCode = controlCode;
		IntByReference intByRef = new IntByReference(inBuffer);
		Pointer lpInBuffer = intByRef.getPointer();		
		this.inBuffer = lpInBuffer;
	}
	
	public PlayController(NativeLong playHandle,int controlCode) {
		this.playHandle = playHandle;
		this.controlCode = controlCode;
		this.inBuffer = null;
		outValue = new IntByReference(0);
		Pointer lpInBuffer = outValue.getPointer();		
		this.outBuffer = lpInBuffer;
		
	}
	
	public Boolean start() {
		//该值可以确定是否开始下载
		return hcNetSDK.NET_DVR_PlayBackControl_V40(playHandle, controlCode, inBuffer, 4, null, null);
	}
	
	public int produce() {
		//该值可以确定获取进度的成功或者是失败
		Boolean result = hcNetSDK.NET_DVR_PlayBackControl_V40(playHandle, controlCode, null, 0, outBuffer, outValue);
		
		return outBuffer.getInt(0);
	}

}
