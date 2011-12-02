package net.csci5271.group3d;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;

/**
 * HTTP Request
 * @author Phil Brown
 *
 */
public class Request extends AsyncTask<Void, String, String> {

	private Context context;
	private int requestType;
	private String URL;
	private String data;
	private Callback callback;
	//private ProgressDialog dialog;
	HttpClient client;
	HttpGet get;
	HttpPost post;
	
	/** Type of post */
	public static final int POST = 0, GET = 1;
	
	/** Constructs a new Request */
	public Request(Context context, int requestType, String url, String data, Callback callback) {
		this.context = context;
		this.requestType = requestType;
		this.URL = url;
		this.data = data;
		this.callback = callback;
	}//Request
	
	@Override
	public void onPreExecute() {
		client = new DefaultHttpClient();
		post = new HttpPost(this.URL);
		get = new HttpGet(this.URL);
		/*
		dialog = new ProgressDialog(context);
		dialog.setTitle("Verifying Permissions..."); //TODO custom dialog layout
		dialog.setCancelable(true);
		dialog.show();
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				if (requestType == GET) {
					get.abort();
				}
				else {
					post.abort();
				}
				Toast.makeText(context, 
						       "Permissions Checker canceled", 
						       Toast.LENGTH_SHORT)
					 .show();
			}//onCancel
		});
		*/
	}//onPreExecute
	
	@Override
	protected String doInBackground(Void... arg0) {
		
		
		ResponseHandler<String> handler = new BasicResponseHandler();
		if (this.requestType == GET) {
			
			try {
				return client.execute(get, handler);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (this.requestType == POST) {
			
			try {
				StringEntity entity = new StringEntity(this.data);
				entity.setContentType("text/plain");
				post.setEntity(entity);
		        return client.execute(post, handler);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}//doInBackground
	
	@Override
	public void onPostExecute(String response) {
		//dialog.dismiss();
		if (this.callback != null) {
			callback.invoke(response);
		}
	}//onPostExecute

	/** Returns the response received from the server. */
	public interface Callback {
		public void invoke(String s);
	}//Callback
	
}//Request
