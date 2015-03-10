package uploader;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class YoukuUploader implements IYoukuUploader {

	private static YoukuUploader instance = null;

	private static Uploader uploader = null;

	private Thread uploadThread = null;

	private IUploadResponseHandler uploadResponseHandler = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RES_START:
				uploadResponseHandler.onStart();
				break;

			case Config.RES_SUCCESS:
				uploadResponseHandler.onSuccess((JSONObject) msg.obj);
				uploadResponseHandler.onFinished();
				uploadThread = null;
				break;

			case Config.RES_FAILURE:
				try {
					uploadResponseHandler.onFailure(new JSONObject(msg.getData().getString("failed")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				uploadResponseHandler.onFinished();
				uploadThread = null;
				break;

			case Config.RES_PROGRESS_UPDATE:
				uploadResponseHandler.onProgressUpdate((Integer) msg.obj);
				break;

			case Config.RES_FINISHED:
				uploadResponseHandler.onFinished();
				uploadThread = null;
				break;
			}
		}
	};

	private YoukuUploader() {
	}

	public static YoukuUploader getInstance(String client_id, String client_secret, Context context) {
		if (instance == null) {
			synchronized (YoukuUploader.class) {
				if (instance == null) {
					uploader = new Uploader(client_id, client_secret, context);
					instance = new YoukuUploader();
				}
			}
		}
		return instance;
	}

	@Override
	public void upload(final HashMap<String, String> params, final HashMap<String, String> uploadInfo,
			final IUploadResponseHandler responseHandler) {
		if (uploadThread == null) {
			this.uploadResponseHandler = responseHandler;
			Util.Log("upload ", "start Thread");
			uploadThread = new Thread(new Runnable() {

				@Override
				public void run() {
					Util.Log("upload ", "Thread run");
					// onStart
					handler.obtainMessage(Config.RES_START).sendToTarget();

					// upload video
					uploader.upload(params, uploadInfo, handler);
				}
			});
			uploadThread.start();
		} else {
			try {
				uploadResponseHandler.onFailure(new JSONObject(Util.getErrorMsg(Config.ERROR_TYPE_UPLOAD_TASK, Config.ERROR_50001,
						50001)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Boolean cancel() {
		if (uploadThread == null) {
			return false;
		}
		
		VideoInfo videoInfo = uploader.getVideoInfo();
		if (videoInfo == null) {
			return false;
		}
		
		uploader.cancel();
		// check sleep
		if (!uploadThread.isInterrupted()) {
			uploadThread.interrupt();
		}
		uploadThread = null;
		return true;
	}
}
