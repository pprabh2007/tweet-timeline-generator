import com.cybozu.labs.langdetect.*; 

public class DetectLang
{
	public static void main(String args[])
	{
		try
		{
			String sample="Salut! Comment";
			
			
			DetectorFactory.loadProfile("C:/Users/HP/Desktop/langdetect/profiles");
			Detector d =DetectorFactory.create();
			d.append(sample);
			System.out.println(d.detect());
		}
		catch(LangDetectException e)
		{
			e.printStackTrace();
		}
	}
}

/*public class TagText {
    public static void main(String[] args) throws IOException,
            ClassNotFoundException {
 
        // Initialize the tagger
        MaxentTagger tagger = new MaxentTagger(
                "taggers/english-bidirectional-distsim.tagger");
 
        // The sample string
        String sample = "This is a sample text";
 
        // The tagged string
        String tagged = tagger.tagString(sample);
 
        // Output the result
        System.out.println(tagged);
    }
}*/