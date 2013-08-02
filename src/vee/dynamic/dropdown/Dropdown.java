package vee.dynamic.dropdown;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Dropdown extends Activity {
	/** Called when the activity is first created. */
	Spinner s1;
	TextView t1;
	List<String> languages;
	// Declares the link of the Xml file in the your Server .
	private String url = "http://demo.venomvendor.com/webservices/language.xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);



		// calls the method
													// getAllXml()
		s1 = (Spinner) findViewById(R.id.spinner1);
		t1 = (TextView) findViewById(R.id.textView1);
		new Getxml().execute("");


		s1.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String temp = languages.get(position);
				t1.setText(temp);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// Does nothing
			}
		});

	}

	// This method parses the Xml file and returns only the name of the
	// languages.
	// and this method is called from above line
	// "final List<String> languages = getAllXML(url);".
	// The parsing method we are gonna use is xmlpullparsing
	public List<String> getAllXML(String url) throws XmlPullParserException,
			IOException, URISyntaxException {
		List<String> list = new ArrayList<String>();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		//System.out.println(url);

		String xml = getUrlData(url);// Calls the method getUrlData()
		//System.out.println(xml);

		xpp.setInput(new StringReader(xml));
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				eventType = xpp.next();
				if (eventType == XmlPullParser.TEXT) {
					list.add(xpp.getText());
				}
			}
			eventType = xpp.next();
		}
		return list;
	}

	// Below method fetches the Xml file from the given url and returns the Xml
	// file
	// in the form of string
	//This method is called from the line "String xml = getUrlData(url);"
	public String getUrlData(String URL) {
		String xml = null;
		try {
			// defaultHttpClient
			 xml = EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(url)).getEntity());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return XML in the form of string
		return xml;
	}



	public class Getxml extends AsyncTask<String, Integer, String>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			s1.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				languages = getAllXML(url);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			s1.setAdapter(new ArrayAdapter<String>(Dropdown.this,
					android.R.layout.simple_spinner_item, languages));
			s1.setVisibility(View.VISIBLE);
			super.onPostExecute(result);

		}

	}


}