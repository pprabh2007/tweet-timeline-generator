import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class ReadJSON {

	public static void main (String args[])
	{
		File f=new File("tweets.ser");
		try
		{
			if(f.exists())
			{
				f.delete();
			}
			
			f.createNewFile();
			
		}
		catch(Exception e)
		{
			System.out.println("Error!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		read("sample.zip");
		
		//read("sample.zip");
	}
	
	public static void read(String name) 
	{

	    try  
	    {
	    	ZipFile zf = new ZipFile(name);
		    Enumeration<?> entries = zf.entries();
	
		    FileOutputStream fileStream=new FileOutputStream("tweets.ser", true);
		    AppendingObjectOutputStream os=new AppendingObjectOutputStream(fileStream);
		      
			while (entries.hasMoreElements()) 
			{
				ZipEntry ze = (ZipEntry) entries.nextElement();
		        BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
		        String line;
		        
		        while ((line = br.readLine()) != null) 
		        {
						Tweet t=new Tweet(line);
						//Tweet t2=new Tweet(line);
						//t.printTweet();;
						os.writeObject(t);
						os.write('\n'); /*MAKE SURE TO CONSIDER THIS WHILE READING THE OBJECTS*/
						//os.writeObject(t2);
				}
		        br.close();
			}
			
			zf.close();
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
	
	Tweet(String t)
	{
		JSONParser parser = new JSONParser();
		try
		{
			Object obj = parser.parse(t);
			JSONArray array = (JSONArray)(obj);
			JSONObject dom= (JSONObject)(array.get(0));
			
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