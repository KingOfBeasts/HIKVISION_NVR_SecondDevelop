package BackPlay;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.NativeLongByReference;

import BackPlay.HCNetSDK.FPlayDataCallBack;

public class DataCallBack implements FPlayDataCallBack {
	
	 private OutputStream out;
    private HCNetSDK hcNetInter;
    private NativeLong lPlayHandle;
    private boolean loading = false;
    private NativeLong nPort;
    private NativeLongByReference rPort;
    private PlayCtrl playCtrl = PlayCtrl.INSTANCE;
    private HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
    private int STREAM_FILE = 1;
    private int STREAM_REALTIMe = 0;
    

    public DataCallBack(){

    }

    //预览回调


    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

	@Override
	public void invoke(NativeLong lPlayHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, int dwUser) {
		// TODO Auto-generated method stub
		/**
         * 让 loading 一直保持 为true
         * 说明 还有码流数据 在回传
         */
		
		//System.out.println(pBuffer);
        loading = true;
        //System.out.println(dwBufSize);
        Pointer pointer = null;
        switch (dwDataType)
        {
        case HCNetSDK.NET_DVR_STREAMDATA:   //码流数据
            if (dwBufSize > 0) {
                pointer = pBuffer.getPointer();
                byte[] b = pointer.getByteArray(0, dwBufSize);
                System.out.println(b.length);
                try {
                    if (pointer != null && out != null) {
                        try {
                            out.write(pointer.getByteArray(0, dwBufSize));
                            out.flush();
                        } catch (NullPointerException e) {
                            loading = false;
                            e.printStackTrace();
                            hcNetInter.NET_DVR_StopPlayBack(lPlayHandle);
                            out.close();
                            System.out.println("关闭连接");
                            return;
                        }
                    }
                } catch (Exception e) {
                    loading = false;
                    hcNetInter.NET_DVR_StopPlayBack(lPlayHandle);
                    e.printStackTrace();
                }
            }
        break;

        case HCNetSDK.NET_DVR_AUDIOSTREAMDATA:   //码流数据
            if (dwBufSize > 0) {
                pointer = pBuffer.getPointer();
                try {
                    if (pointer != null && out != null) {
                        try {
                            out.write(pointer.getByteArray(0, dwBufSize));
                            out.flush();
                        } catch (NullPointerException e) {
                            loading = false;
                            e.printStackTrace();
                            hcNetInter.NET_DVR_StopPlayBack(lPlayHandle);
                            out.close();
                            System.out.println("关闭连接");
                            return;
                        }
                    }
                } catch (Exception e) {
                    loading = false;
                    hcNetInter.NET_DVR_StopPlayBack(lPlayHandle);
                    e.printStackTrace();
                }
            }
            break;



        case HCNetSDK.NET_DVR_REALPLAYEXCEPTION:   //预览异常
            loading = false;
            break;
        case HCNetSDK.NET_DVR_REALPLAYNETCLOSE:   //预览时连接断开
            loading = false;
            break;
        case HCNetSDK.NET_DVR_REALPLAY5SNODATA:   //预览5s没有收到数据
            loading = false;
            break;
        case HCNetSDK.NET_DVR_PLAYBACKEXCEPTION:   //回放异常
            loading = false;
            break;
        case HCNetSDK.NET_DVR_PLAYBACKNETCLOSE:   //回放时候连接断开
            loading = false;
            break;
        case HCNetSDK.NET_DVR_PLAYBACK5SNODATA:   //回放5s没有收到数据
            loading = false;
            break;
        }
	}

}
