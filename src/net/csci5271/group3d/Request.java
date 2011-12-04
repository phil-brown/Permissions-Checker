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
 */
public class Request extends AsyncTask<Void, String, String> {

	/** GET or POST request */
	private int requestType;
	/** The URL to access */
	private String URL;
	/** The data to append to the URL*/
	private String data;
	/** The method to call after the request has completed*/
	private Callback callback;
	/** The client used for sending the request to the server*/
	private HttpClient client;
	/** Used for get requests*/
	private HttpGet get;
	/** Used for post requests*/
	private HttpPost post;
	
	/** Type of post */
	public static final int POST = 0, GET = 1;
	
	/** Constructs a new Request */
	public Request(Context context, int requestType, String url, String data, Callback callback) {
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
				if (data != null) {
					StringEntity entity = new StringEntity(this.data);
					entity.setContentType("text/plain");
					post.setEntity(entity);
				}
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
		if (this.callback != null) {
			callback.invoke(response);
		}
	}//onPostExecute

	/** Returns the response received from the server. */
	public interface Callback {
		public void invoke(String s);
	}//Callback
	
}//Request
