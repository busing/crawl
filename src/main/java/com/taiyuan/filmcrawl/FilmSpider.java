package com.taiyuan.filmcrawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class FilmSpider {
	
	private URL url;
	private Parser parser;
	private String html;
	
	public FilmSpider(URL url) {
		super();
		this.url = url;
		intiParser();
	}
	
	public FilmSpider(String html) {
		super();
		this.html=html;
		intiParser();
	}
	

	
	public  void intiParser() {
		try {
			if(html!=null && html.length()>0)
			{
				this.setParser(new Parser(html));
			}
			else
			{
				this.setParser(new Parser(url.openConnection())); 
			}
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NodeList getNodeList(NodeFilter nodeFilter) {
		try {
			this.getParser().reset();
			NodeList nl= this.getParser().parse(nodeFilter);
			return nl;
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		}
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Parser getParser() {
		return parser;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}
	
	public static void main(String[] args) {
		try {
//			String url="http://www.1905.com/mdb/film/2231518/";
			String url="http://www.haodf.com/doctor/DE4r08xQdKSLPDNfkKX3HVJ2rF-e.htm";
//			String url="http://v.duba.com/moviedetail/98891.html";
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String html=EntityUtils.toString(entity);
			System.out.println(html);
			if(html!=null && html.length()>0)
			{
				AndFilter af=new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "full_DoctorSpecialize"));
//				TagNameFilter af=new TagNameFilter("table");
				NodeList nodeList=new FilmSpider(html).getNodeList(af);
				System.out.println(nodeList.size());
				if(nodeList!=null && nodeList.size()>0)
				{
					System.out.println(nodeList.elementAt(0).toPlainTextString().trim());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
