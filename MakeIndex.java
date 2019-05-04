import java.io.*;

import java.util.zip.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import lemurproject.indri.*;


public class MakeIndex {

	public static void main (String args[])
	{	
		
		File dataDir=new File("data");
		File outputDir=new File("trectext");
		outputDir.mkdir();

		FilenameFilter gzFiles;
		gzFiles=(File dir, String name)->name.endsWith(".gz"); 
		
		File[] fileList=dataDir.listFiles(gzFiles);
	
		for(File f: fileList)
		{
			//System.out.println(f.getName());
			read(dataDir.getName(), f.getName());
		}
		
		//read("data/statuses.log.2013-03-28-09.gz");


		makeIndex();
		
	}

	public static void makeIndex()
	{
		String[] stopWordList =  {"a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he", "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with"};
		
		

	}
	
	public static void read(String dir_name, String f_name) 
	{

		String input_file_name=dir_name+"/"+f_name;
		String output_file_name="trectext/"+f_name.substring(0, f_name.lastIndexOf(".")+1)+"xml";

		
	    try  
	    {   	
	    	FileInputStream fos=new FileInputStream(input_file_name);
	    	GZIPInputStream gos=new GZIPInputStream(fos);
	    	BufferedReader reader=new BufferedReader(new InputStreamReader(gos));
	    	
	    	BufferedWriter out = new BufferedWriter(new FileWriter(output_file_name, false));
	    	out.close();  // to create new file every time
	   
	    	out = new BufferedWriter(new FileWriter(output_file_name, true)); 

	    	String line;
	    	while((line=reader.readLine())!=null)
	    	{
	    		try
	    		{
		    		Tweet t=new Tweet(line);
		    		
		    		if(!(t.content.equals("")))
		    		{
		    			out.write(t.getWritableDoc());
		    		}
		    		
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
	    	out.close();
	    	
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
		this.year=0;
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
			this.year=Integer.parseInt(temp[5]);
			
			String[] temp2=temp[3].split(":", 3);
			this.hour=Integer.parseInt(temp2[0]);
			this.minute=Integer.parseInt(temp2[1]);
			this.second=Integer.parseInt(temp2[2]);
			
		
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			this.id="";
			this.content="";
			this.day_of_week="";
			this.month="";
			this.date=0;
			this.hour=0;
			this.second=0;
			this.year=0;
			this.minute=0;
			System.out.println(t);
			
		}
	}
		
	

	String id;
	
	String month;
	int date;
	String day_of_week;
	
	int hour;
	int minute;
	int second;
	int year;
	String content;
	
	void printTweet()
	{
		System.out.println("ID: "+this.id);
		System.out.println("Day: " + this.date+" of " + this.month+ " ("+this.day_of_week+")");
		System.out.println("Time: "+this.hour+":"+this.minute+":"+this.second);
		System.out.println("Content: "+this.content);
	}

	String getHead()
	{

		return this.id+"#"+this.date+"#"+this.month+"#"+this.year;
	}

	String getText()
	{
		return this.content;
	}

	String getWritableDoc()
	{

		return "<DOC>\n\t<DOCNO>"+this.getHead()+"</DOCNO>\n\t<TEXT>\n\t\t"+this.getText()+"\n\t</TEXT>\n</DOC>\n";
		
	}
	
}








