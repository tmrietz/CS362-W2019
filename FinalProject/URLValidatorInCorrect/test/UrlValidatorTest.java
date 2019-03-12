import junit.framework.TestCase;
import java.util.Scanner;
import java.util.Random;

//You can use this as a skeleton for your 3 different test approach
//It is an optional to use this file, you can generate your own test file(s) to test the target function!
// Again, it is up to you to use this file or not!





public class UrlValidatorTest extends TestCase {


   public UrlValidatorTest(String testName) {
      super(testName);
	 }
	 
	 		// - Initialize some valid URL pieces we can use to programatically generate url strings
		// - Instead of initializing invalid URL pieces for *all* of these, we can use valid separators
		//	 in incorrect places
		private String validQuerySeparators = "$-_.+!*'(),";
		private String validSchemeSeparator = "://";
		private String validPortSeparator = ":";
		private String validDomainSeparator = ".";
		private String validPathSeparator = "/";
		private String validFragmentSeparator = "#";
		private String invalidSeparators = "<>\\~{}\"";
		private String[] someValidSubDomains = {"test","www","any","string","0r","numer1c","is--alright"};
		private String[] someValidTLDs = {"com", "org", "aaa", "blog", "net", "uk", "gov", "edu"};
		private String[] someInvalidTLDs = {"20", "inValid", "alsoInvalid", "not_This_Time", "\"", "^", "&"};
		private String[] someValidProtocols = {"http","https","ftp"};

   public void myAssert(String url, boolean actual, boolean expected) {
	   
	   boolean errors = false;
	   try {
		   assertEquals(actual, expected);
	   } catch(AssertionError e) {
		   errors = true;
	   }
	   if (errors) System.out.println(url + " -- did not work."); 
	   
   }
   
   public void testManualTest()
   {
//You can use this function to implement your manual testing

	   // Most of the URL's used here were from the URLValidatorCorrect test	   
	   UrlValidator urlVal = new UrlValidator(null, null, 
			     UrlValidator.ALLOW_2_SLASHES
               + UrlValidator.ALLOW_ALL_SCHEMES
               + UrlValidator.NO_FRAGMENTS
               + UrlValidator.ALLOW_LOCAL_URLS);

	   // It appears that "ALLOW_ALL_SCHEMES" doesn't actually allow all schemes and result in faults
	   // 	faults: the JUnit tests are commented out to allow tests to continue
	   //	fails:  doesn't fault in JUnit test but prints to console
	   String[] shouldWork = {
//			   "hTtP://www.google.com",							// faults
//			   "https://www.google.com",						// faults
//	           "h3t://www.google.com",							// faults
//			   "ftp://www.google.com",							// faults in -- if (!isValidAuthority(authority))
//			   "file:///C:/some.file",							// faults
			   "http://www.google.com/test1//file",				// fails in -- if (!isValidPath(urlMatcher.group(PARSE_URL_PATH)
			   "http://www.google.com/test1/file",				// fails in -- if (!isValidPath(urlMatcher.group(PARSE_URL_PATH)
			   "http://www.google.com:80",						// fails in -- if (authority.contains(":")
			   "http://www.google.com/test1/index.html",		// fails in -- if (!isValidPath(urlMatcher.group(PARSE_URL_PATH)
			   "http://www.google.com/hello..world",			// fails
			   "http://user:@www.apache.org",					// fails
			   "http://www.google.com",
	           "http://www.google.com/",
	           "http://www.google.com/test1",
	           "http://www.google.com/t123",	              
	           "http://www.google.com/$23",
	           "http://www.google.com/1",
	           "http://www.google.com/file(1)",
	           "http://test.google.com",
	           "http://www.google.net",
	           "http://255.255.255.255",
	           "http://www.255.com",
	           "http://www.go.cc",	          
	           "http://www.google.com/?action=edit&mode=up",
	           "http://xn--d1abbgf6aiiy.xn--p1ai",
	           "http://localhost/test",
	           "http://hostname",
	           "http://www.google.com/.../world",
	           "http://www.google.com/hell",
	           "http://example.com/serach?address=Main%20Avenue",
	           "http://www.google.com/file.c",
	           
	   };
	   
	   // This list of URLs are bad -- failures here mean that the application failed to report the failure
	   String[] shouldNotWork = {
//			   "httpd://www.google.com",						// faults
			   "http://broke.hostname",							// fails
			   "http://www.google.com../",						// fails
			   "http://www.google@com",							// fails
			   "http://256.256.256.256",						// fails
			   "http://1.2.3.4.5",								// fails
			   "http://broke.my-test/test/index.html",
			   "http://first.my-testing/test/index.html",
			   "http://example.com/serach?address=Main Avenue",
			   "http://www.google.com:65536",
			   "http:\\www.google.com",
			   "/http://www.google.com",
			   "ht_tp://www.google.com",
			   "http:.//www.google.com",
			   "http://www.google.com/..",
			   "http://www.google.com/../file",
			   "http://www.google.com/#/file",
			   "http://www.google.com/#",
			   "http://www.google.com:-1",
			   "http://www.google.com:65a",

			   
	   };
	   
	   System.out.println("URLs that should work but errors out");
	   for (int i = 0; i < shouldWork.length; i++) {
		   myAssert(shouldWork[i], urlVal.isValid(shouldWork[i]), true);
	   }
	   
	   System.out.println("\nURLs that should NOT work but errors out");
	   for (int i = 0; i < shouldNotWork.length; i++) {
		   myAssert(shouldNotWork[i], urlVal.isValid(shouldNotWork[i]), false);
	   }
	   
   }
   
   
   public void testYourFirstPartition()
   {
	 //You can use this function to implement your First Partition testing	   

   }
   
   public void testYourSecondPartition(){
		 //You can use this function to implement your Second Partition testing	   

   }
   //You need to create more test cases for your Partitions if you need to 
   
   
   
   public void testUnitTestFullURL()
   {
	   
	   UrlValidator urlVal = new UrlValidator(null, null,
			   UrlValidator.ALLOW_2_SLASHES 
			   + UrlValidator.ALLOW_ALL_SCHEMES
			   + UrlValidator.NO_FRAGMENTS
			   + UrlValidator.ALLOW_LOCAL_URLS
			   );
	   
	   
	   String[] validURL = {
			   "www.google.com/russell/hi.pdf",
			   "www.google.com/help/me",
			   "www.google.com/helloworld.pdf",
			   "www.pets.com",
			   "www.pets.com:80"
	   };
	   
	   
	   String[] invalidURL = {
			   "www.google.com/../",
			   "256.256.256.256"
	   };
	   
	   System.out.println("\n*******TESTING FULL URLs*******");
	   
	   urlTester(urlVal, "", "", validURL, invalidURL);
	   
	   System.out.println("********************************\n");
   }
   
   
   
   public void testUnitTestAuthority()
   {
	   
	   UrlValidator urlVal = new UrlValidator(null, null,
			   UrlValidator.ALLOW_2_SLASHES 
			   + UrlValidator.ALLOW_ALL_SCHEMES
			   + UrlValidator.NO_FRAGMENTS
			   + UrlValidator.ALLOW_LOCAL_URLS
			   );
	      
	   String[] validPart = {
			   "www.google.com",
			   "go.com",
			   "0.0.0.0",
			   "go.cc",
			   "255.255.255.255",
			   "255.com"
	   };
	  
	   String[] invalidPart = {
			   "256.256.256.256",
			   "1.2.3.4.5",
			   "go.a",
			   "255.com",
			   "aaa",
			   ""
	   };
	   
	   System.out.println("\n*******TESTING AUTHORITY*******");
	   
	   urlTester(urlVal, "", "", validPart, invalidPart);
	    
	   System.out.println("********************************\n");
   }
   
   
   
   
   public void testUnitTestPort()
   {
	   
	   UrlValidator urlVal = new UrlValidator(null, null,
			   UrlValidator.ALLOW_2_SLASHES 
			   + UrlValidator.ALLOW_ALL_SCHEMES
			   + UrlValidator.NO_FRAGMENTS
			   + UrlValidator.ALLOW_LOCAL_URLS
			   );
	      
	   String[] validPort = {
			   ":80",
			   ":65535",
			   ":0",
			   ""

	   };
	  
	   String[] invalidPort = {
			   ":65536",
			   ":-1",
			   ":65636",
			   ":999999999999999999",
			   ":65a"

	   };
	   
	   System.out.println("\n*******TESTING PORT*******");
	   
	   urlTester(urlVal, "www.google.com", "", validPort, invalidPort);
	    
	   System.out.println("********************************\n");
   }
   
   
   
   public void testUnitTestPath()
   {
	   
	   UrlValidator urlVal = new UrlValidator(null, null,
			   UrlValidator.ALLOW_2_SLASHES 
			   + UrlValidator.ALLOW_ALL_SCHEMES
			   + UrlValidator.NO_FRAGMENTS
			   + UrlValidator.ALLOW_LOCAL_URLS
			   );
	      
	   String[] validPath = {
			   "/test1",
			   "/t123",
			   "/$23",
			   "/test1/",
			   "",
			   "/test1/file",
			   "/t123/file",
			   "/$23/file",
			   "/test1//file"
	   };
	  
	   String[] invalidPath = {
			   "/..",
			   "/../",
			   "/#",
			   "/../file",
			   "/..//file",
			   "/#/file"
	   };
	   
	   System.out.println("\n*******TESTING PATH*******");
	   
	   urlTester(urlVal, "www.google.com", "", validPath, invalidPath);
	    
	   System.out.println("********************************\n");
   }
   
   
   public void testUnitTestQuery()
   {
	   
	   UrlValidator urlVal = new UrlValidator(null, null,
			   UrlValidator.ALLOW_2_SLASHES 
			   + UrlValidator.ALLOW_ALL_SCHEMES
			   + UrlValidator.NO_FRAGMENTS
			   + UrlValidator.ALLOW_LOCAL_URLS
			   );
	      
	   String[] validQuery = {
			   "?action=view",
			   "?action=edit&mode=up",
			   ""
	   };
	  
	   //having trouble deciding what constitutes and invalid query
	   String[] invalidQuery = {

	   };
	   
	   System.out.println("\n*******TESTING QUERY*******");
	   
	   urlTester(urlVal, "www.google.com/test1", "", validQuery, invalidQuery);
	    
	   System.out.println("********************************\n");
   }
   
   
   public void testManualTest_2()
   {
	   UrlValidator urlVal = new UrlValidator(null, null,
			   UrlValidator.ALLOW_2_SLASHES 
			   + UrlValidator.ALLOW_ALL_SCHEMES
			   + UrlValidator.NO_FRAGMENTS
			   + UrlValidator.ALLOW_LOCAL_URLS);
	   
	   @SuppressWarnings("resource")
	   Scanner input = new Scanner(System.in);
	   
	   String url ="";
	   
	   System.out.println("\n**********MANUAL TESTING**********");
	   System.out.println("Enter a url for manual testing (enter 'q' to quit): \n");
	   url = input.nextLine();
	   
	   while (!"q".equals(url))
	   {
		   
		   if ( urlVal.isValid(url) )
		   {
			   System.out.println("Returns valid: " + url);
		   }
		   
		   else
		   {
			   System.out.println("Returns not valid: " + url);
		   }
		   
		   System.out.println("Enter a url for manual testing (enter 'q' to quit): \n");
		   url = input.nextLine();
	   }
	   
	   System.out.println("********************************\n");
	   
	 }
	 

   private boolean customAssertEquals(boolean val1, boolean val2)
   {
	   if (val1 == val2)
	   {
		   return true;
	   }
	    
		return false;
   }
   
   
   
   
   //takes a valid and invalid array of url parts (both the same part type)
   //and the section of the url string that comes before that part and after
   //that part (prePart and postPart); it then builds a full url and tests
   //it against UrlValidator::isValid().
   private void urlTester(UrlValidator urlVal, String prePart, String postPart, 
		   String[] validPart, String[] invalidPart)
   
   {
	   boolean allPass = true;
	   
	   for(int i = 0; i < validPart.length; i++ )
	   {
		   String validURL = "http://" + prePart + validPart[i] + postPart;
		   
		   if ( !customAssertEquals(true, urlVal.isValid(validURL)) )
		   {
			   System.out.println("Expected valid, but test returns invalid: " + validURL);
			   allPass = false;
		   }
	   }
	   
	   
	   
	   for(int i = 0; i < invalidPart.length; i++ )
	   {
		   String invalidURL = "http://" + prePart + invalidPart[i] + postPart;
		   
		   if ( !customAssertEquals(false, urlVal.isValid(invalidURL)) )
		   {
			   System.out.println("Expected invalid, but test returns valid: " + invalidURL);
			   allPass = false;
		   }
	   }
	   
	   if(allPass)
	   {
		   System.out.println("All tests pass.");
	   }
	   
   }
	 


}



