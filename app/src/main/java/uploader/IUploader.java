package uploader;

import java.util.HashMap;
import android.os.Handler;

public interface IUploader {
	void upload(HashMap<String, String> params, HashMap<String, String> uploadInfo, Handler handler);
	
	void cancel();
}
