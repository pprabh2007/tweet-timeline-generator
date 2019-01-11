import java.io.*;

import java.util.zip.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import com.cybozu.labs.langdetect.*; 

public class ReadJSON {

	public static void main (String args[])
	{	
		/*
		System.out.println(Language.get_lang("salut commment"));
		System.out.println(Language.get_lang("Despacito"));
		System.out.println(Language.get_lang("Hey there"));
		System.exit(0);
		*/
		
		/*FIRST TWEET SHOULD BE WRTITTEN WITH NORMAL OBJECT OUTPUT STREAM*/
		try
		{
			Tweet streamWriterTweet=new Tweet();
			ObjectOutputStream os_demo=new ObjectOutputStream(new FileOutputStream("tweets.ser"));
			os_demo.writeObject(streamWriterTweet);
			os_demo.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		/*File currentDir=new File(".");  //USE "." for current directory
		File[] fileList=currentDir.listFiles();
		for(File f: fileList)
		{
			System.out.println(f.getName());
		}*/
		
		
		
		File dataDir=new File("data");
		FilenameFilter gzFiles;
		gzFiles=(File dir, String name)->name.endsWith(".gz"); 
		
		File[] fileList=dataDir.listFiles(gzFiles);
	
		for(File f: fileList)
		{
			//System.out.println(f.getName());
			read(dataDir.getName()+"/"+f.getName());
		}
		
		//read("data/statuses.log.2013-03-28-09.gz");
		
	}
	
	public static void read(String name) 
	{

	    try  
	    {   	
	    	FileInputStream fos=new FileInputStream(name);
	    	GZIPInputStream gos=new GZIPInputStream(fos);
	    	BufferedReader reader=new BufferedReader(new InputStreamReader(gos));
	    	
	    	FileOutputStream fileStream=new FileOutputStream("tweets.ser", true);
		    AppendingObjectOutputStream os=new AppendingObjectOutputStream(fileStream);
	    	
	    	String line;
	    	while((line=reader.readLine())!=null)
	    	{
	    		try
	    		{
		    		Tweet t=new Tweet(line);
		    		if(Language.get_lang(t.content).equals("en"))
		    		{
		    			os.writeObject(t);
		    		}
		    		//t.printTweet();
	    		}
	    		catch(ArrayIndexOutOfBoundsException e)
	    		{
	    			e.printStackTrace();
	    		}
	    		catch(NullPointerException e)
	    		{
	    			e.printStackTrace();
	    		}
	    		catch (Exception e)
	    		{
	    			e.printStackTrace();
	    			System.exit(-1);
	    		}
	    	}
	    	
	    	reader.close();
	    	os.close();
	    	
	    } 
		catch (IOException e) 
	    {
				e.printStackTrace();
	    }

	}
}

class Tweet implements Serializable{
	
	static final long serialVersionUID=1L; 
	
	Tweet()
	{
		this.id="";
		this.content="";
		this.day_of_week="";
		this.month="";
		this.date=0;
		this.hour=0;
		this.second=0;
		this.minute=0;
	}
	
	Tweet(String t)
	{
		JSONParser parser = new JSONParser();
		try
		{
			Object obj = parser.parse(t);
			JSONObject dom= (JSONObject)(obj);
			
			this.id=dom.get("id")+"";
			this.content=dom.get("text")+"";
			
			String[] temp=(dom.get("created_at") + "").split(" ", 0);
			this.day_of_week=temp[0];
			this.month=temp[1];
			this.date=Integer.parseInt(temp[2]);
			
			String[] temp2=temp[3].split(":", 3);
			this.hour=Integer.parseInt(temp2[0]);
			this.minute=Integer.parseInt(temp2[1]);
			this.second=Integer.parseInt(temp2[2]);
			
		
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
	}
	
	String id;
	
	String month;
	int date;
	String day_of_week;
	
	int hour;
	int minute;
	int second;

	String content;
	
	void printTweet()
	{
		System.out.println("ID: "+this.id);
		System.out.println("Day: " + this.date+" of " + this.month+ " ("+this.day_of_week+")");
		System.out.println("Time: "+this.hour+":"+this.minute+":"+this.second);
		System.out.println("Content: "+this.content);
	}
}

class AppendingObjectOutputStream extends ObjectOutputStream {

	  public AppendingObjectOutputStream(FileOutputStream out) throws IOException {
	    super(out);
	  }

	  @Override
	  protected void writeStreamHeader() throws IOException {
	    // do not write a header, but reset:
	    // this line added after another question
	    // showed a problem with the original
	    reset();
	  }

	}

class Language
{
	
	static {
		try
		{
			DetectorFactory.loadProfile("C:/Users/HP/Desktop/langdetect/profiles");
			System.out.println("Language Profiles Loaded");
		}
		catch(LangDetectException e)
		{
			e.printStackTrace();
		}
	}
	public static String get_lang(String sample)
	{
		try
		{	
			Detector d =DetectorFactory.create();
			d.append(sample);
			return (d.detect());
		}
		catch(LangDetectException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}