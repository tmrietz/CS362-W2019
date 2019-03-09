

import junit.framework.TestCase;

//You can use this as a skeleton for your 3 different test approach
//It is an optional to use this file, you can generate your own test file(s) to test the target function!
// Again, it is up to you to use this file or not!





public class UrlValidatorTest extends TestCase {


   public UrlValidatorTest(String testName) {
      super(testName);
   }

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
//	           "ftp://www.google.com",							// faults in -- if (!isValidAuthority(authority))
//	           "file:///C:/some.file",							// faults
			   "http://www.google.com/test1//file",				// fails in -- if (!isValidPath(urlMatcher.group(PARSE_URL_PATH)
			   "http://www.google.com/test1/file",				// fails in -- if (!isValidPath(urlMatcher.group(PARSE_URL_PATH)
			   "http://www.google.com:80",						// fails in -- if (authority.contains(":")
			   "http://www.google.com/test1/index.html",		// fails in -- if (!isValidPath(urlMatcher.group(PARSE_URL_PATH)
			   "http://www.google.com/hello..world",			// fails
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
	           
	   };
	   
	   String[] shouldNotWork = {
			   "http://broke.hostname",							// fails
			   "http://www.google.com../",						// fails
			   "http://broke.my-test/test/index.html",
			   "http://first.my-testing/test/index.html",
			   "http://example.com/serach?address=Main Avenue",
			   "http://www.google.com:65536",
			   
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
   
   public void testIsValid()
   {
	   //You can use this function for programming based testing

   }
   


}
