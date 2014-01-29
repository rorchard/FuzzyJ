
package fuzzytesting.granddaddytest;

import java.net.URL;

public class FileAndUrlResolution {
	
	public static String applicationDirectory = "";
	
	/**
	 *  Try to create a full pathname (URL format) to find the required file.
	 *  For example if we are trying to get the URL for the file "graphics/arrowR.gif"
	 *  we could expect that the URL might look like:
	 *  
	 *  	file:////Users/boborchard/fuzzytesting/granddaddytest/graphics/arrowR.gif
	 *  
	 *  so we should make sure that the applicationDirectory is set to 
	 *  
	 *  	"file:////Users/boborchard/fuzzytesting/granddaddytest/"
	 *  
	 *  by passing this string as an argument when we start this program.
	 *  If the default directory is this string then there can be no argument passed when program is started.
	 * 
	 * 
	 * @param file - the file name to be turned into a full URL pathname
	 * @return
	 */
	public static URL getFileUrl(String file)
	{
		URL url = null;
		
		try 
		{
			if (applicationDirectory.equals(new String("")))
    		    url = symantec.itools.net.RelativeURL.getURL(file);
			else
			{
				String path = applicationDirectory;
				char lastCharOfPath = path.charAt(path.length()-1);
				char firstCharOfFile = file.charAt(0);
				if ((lastCharOfPath == '/' && firstCharOfFile != '/') ||
				    (lastCharOfPath != '/' && firstCharOfFile == '/')
				   )
				    path = path + file;
				else if (lastCharOfPath == '/' && firstCharOfFile == '/')
				    path = path + file.substring(1);
				else
				    path = path + "/" + file;
			    url = new java.net.URL(path);
			}
    	} 
    	catch(java.net.MalformedURLException e)
    	{
    	    System.out.println(""+e.getMessage());
    	}  
    	
    	return url;  
	}
	
	public static String getFullFileName(String file)
	{
		String fullName = "";
		
		if (applicationDirectory.equals(new String("")))
    		    fullName = file;
		else
		{
			fullName = applicationDirectory;
			char lastCharOfPath = fullName.charAt(fullName.length()-1);
			char firstCharOfFile = file.charAt(0);
			if ((lastCharOfPath == '/' && firstCharOfFile != '/') ||
				    (lastCharOfPath != '/' && firstCharOfFile == '/')
				 )
				 fullName = fullName + file;
			else if (lastCharOfPath == '/' && firstCharOfFile == '/')
				 fullName = fullName + file.substring(1);
			else
				 fullName = fullName + "/" + file;
		}
     	
    	return fullName;  
	}
}
