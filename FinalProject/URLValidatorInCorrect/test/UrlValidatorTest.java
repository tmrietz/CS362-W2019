import junit.framework.TestCase;
import java.util.Scanner;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

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
		private String validPathChars = "//////////////////////////////.-_~!$&'()*+,;=";		// 66/33 probability of '/' for more realistic paths
		private String validFragmentSeparator = "#";
		private String invalidSeparators = "<>\\~{}\"";
		private String[] someValidSubDomains = {"test","www","any","string","0r","numer1c","is--alright", "sup3r"};
		private String[] someValidTLDs = {"com", "org", "aaa", "blog", "net", "uk", "gov", "edu", "CoM", "COM"};
		private String[] someInvalidTLDs = {"20", "inValid", "alsoInvalid", "not_This_Time", "\"", "^", "&", ""};
		private String[] someValidProtocols = {"http","https","ftp", "h3t", "hTtP", "httpS", "file"};
		private String[] someInValidProtocols = {":", "", "htp"};
		private String[] someValidPorts = {"80", "0", "65535"};
		private String[] someInvalidPorts = {"100000", "65536", "-1", "65a"};

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
						 "http://www.google.com/file.c"
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
			   "http://www.google.com:65a"
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
	 

	public void testRandomLengthPartition() {

		// set up url lengths (tested in ranges) for testing steps
		//		- in theory, URL can be any length, but browsers have limits and 10000 is longer than any
		//		realistic URL one could expect
		int[] urlLengths = {
			0,
			30, 
			50,
			100,
			500,
			1000,
			2000,
			5000,
			10000																		
		};

		// String[] validSchemes = {"http://","https://","ftp://", "h3t://", "hTtP://", "httpS://", "file://"};
		// initialize our validator
		UrlValidator urlVal = new UrlValidator (
			null, 
			null,
			UrlValidator.ALLOW_2_SLASHES +
			UrlValidator.ALLOW_ALL_SCHEMES +								// I'm allowing fragments here
			UrlValidator.ALLOW_LOCAL_URLS
		);

		// do some more setup
		Random randInt = new Random();
		int positiveTestedPerRangeCount = 0;							// tracks positive test cases per range
		int negativeTestedPerRangeCount = 0;							// tracks negative test cases per range
		boolean valid;
		int rangesTested = 0;											  			// 0-50, 50-100, etc.
		String url = "";

		System.out.println("/******************RANDOM: length testing | START******************/\n");
		System.out.println("\n^^^RANGE: 0 - 0\n");
		
		// while not all ranges yet tested, generate urls and test for that length range
		while (rangesTested < urlLengths.length - 1) {
			int max = urlLengths[rangesTested + 1];
			int min = urlLengths[rangesTested];
			int length = randInt.nextInt(max - min) + min;
			
			if (rangesTested == 0) {
				myAssert(url, urlVal.isValid(""), false);			// an empty string is not a URL
				rangesTested++;
			} else {
				// do the test
				valid = randInt.nextBoolean();
				url = randGenerateUrlOfLength(false, valid, length);
				boolean test = customAssertEquals(urlVal.isValid(url), valid);
				
				// print the test result
				if (valid && !test) {
					System.out.println("expected valid:\t\t" + url);
				} else if (test && !valid) {
					System.out.println("expected invalid:\t" + url);
				} /*else {
					System.out.println("passed [valid == " + valid + "]: " + url);
				}*/

				// increment test counters
				if (valid) {
					positiveTestedPerRangeCount++;
				} else {
					negativeTestedPerRangeCount++;
				}

				// make sure each range sees a specified number of positive and negative test cases
				//		(arbitrary number at the moment)
				if (positiveTestedPerRangeCount > 3 && negativeTestedPerRangeCount > 3) {
					rangesTested++;
					System.out.println("\n^^^RANGE: " + min + " - " + max + "\n");
					positiveTestedPerRangeCount = 0;
					negativeTestedPerRangeCount = 0;
				}
			}
		}
		System.out.println("/******************RANDOM: length testing | END******************/\n");
	}


	public void testRandomPartOrderPartition() {
		// initialize our validator
		UrlValidator urlVal = new UrlValidator (
			null, 
			null,
			UrlValidator.ALLOW_2_SLASHES +
			UrlValidator.ALLOW_ALL_SCHEMES +								// I'm allowing fragments here
			UrlValidator.ALLOW_LOCAL_URLS
		);

		// do some more setup
		Random randInt = new Random();
		int positiveTested = 0;														// tracks positive test cases per range
		int negativeTested = 0;														// tracks negative test cases per range
		String url = "";
		boolean changeOrder = false;

		System.out.println("\n/******************RANDOM: part order testing | START******************/\n");
		while (positiveTested < 10 && negativeTested < 10) {
			changeOrder = randInt.nextBoolean();
			url = randGenerateUrlOfLength(changeOrder, true, 50);
			boolean test = customAssertEquals(urlVal.isValid(url), !changeOrder);
			
			// print the test result
			if (!changeOrder && test) {
				System.out.println("expected valid:\t\t" + url);
			} else if (changeOrder && !test) {
				System.out.println("expected invalid:\t" + url);
			}
			
			if (changeOrder) {
				negativeTested++;
			} else {
				positiveTested++;
			}
		}
		System.out.println("\n/******************RANDOM: part order testing | END******************/\n");


	}

      
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

/***************************************Start Rand Testing Functions***************************************/


	public String randGenerateUrlOfLength(boolean changeOrder, boolean valid, int length) {
		// set up some randomization to determine whether url will be valid
		if (length < 30) return "";
		Random rand = new Random();
		int remainingLength = length;

		// set *semi-random* lengths for each url part
		int protocolLength = 7;
		remainingLength -= protocolLength;
		
		// domain is mandatory so we need to guarantee randomization returns a realistic domain
		int domainLength = 0;
		int domainLimit = remainingLength;
		if (valid && remainingLength > 253) {												// if valid, only allow 253 domain chars
			domainLimit = 254;
		}
		while (domainLength < 20) {																	// make sure to reserve enough space for a real domain
			domainLength = rand.nextInt(domainLimit);									// limit the chars here
		}

		remainingLength -= domainLength;
		int fragmentLength = remainingLength/(rand.nextInt(3) + 2);	// make fragments no more than half of length
		remainingLength -= fragmentLength;
		int pathLength = remainingLength/(rand.nextInt(3) + 1);			// don't divide by zero
		remainingLength -= pathLength;
		int queryLength = remainingLength;


		// create and store individual url parts
		String[] resultParts = new String[length];
		resultParts[0] = "http://";																	// we may wish to make the protocol random but we'll get faults
		resultParts[1] = randDomainOfLength(domainLength, valid);
		resultParts[2] = randPathOfLength(pathLength, valid);
		resultParts[3] = randQueryOfLength(queryLength, valid);
		resultParts[4] = randFragmentOfLength(fragmentLength, valid);

		// make a url part order swap if requested in the function call
		int order[] = {0,1,2,3,4};
		String resultStr = "";
		if (changeOrder == true) {
			int tmp = rand.nextInt(4);
			order[tmp+1] = tmp;
			order[tmp] = tmp+1;
		}

		// concatenate together the url string
		for(int i=0; i<order.length; i++) {
			resultStr += resultParts[order[i]];
		}
		return resultStr;
	}


	public String randDomainOfLength(int length, boolean valid) {
		Random randInt = new Random();
		boolean useIP = randInt.nextBoolean();				// choose whether to use an IP for domain
		int lengthRemaining = length;
		if (useIP) {
			return randIp(valid);
		} else {
			// create domain parts, decrementing length along the way
			String subdomain = "";
			String addSub = "";
			String topLevelDomain = randStringFromArray(this.someValidTLDs, randInt);
			lengthRemaining -= topLevelDomain.length();
			
			// either add one subdomain, or many until <= 63 characters left for the domain name
			if (lengthRemaining < 63 && lengthRemaining > 10) {				// leave some room for domain name												
				subdomain = randStringFromArray(this.someValidSubDomains, randInt) + ".";
			} else {
				while (lengthRemaining > 63) {													// each domain label may have at max 63 chars
					addSub = randStringFromArray(this.someValidSubDomains, randInt);
					subdomain += (addSub + ".");
					lengthRemaining -= (addSub.length() + 1);
				}
			}
			String domain = RandomStringUtils.randomAlphanumeric(lengthRemaining-3, lengthRemaining-2);		// -3 for separators
			lengthRemaining -= domain.length();
			
			// use the created domain parts to build a string
			StringBuilder result = new StringBuilder(subdomain);
			result.append(domain);
			result.append(this.validDomainSeparator);
			result.append(topLevelDomain);
			result.append("/");
			
			// if not valid, invalidate the domain string with bad characters
			if (! valid) {
				int invalidCharCount = randInt.nextInt(length/2);
				while (invalidCharCount > 0) {
					result.setCharAt(randInt.nextInt(result.length()), randCharFromString(this.invalidSeparators, randInt));
					invalidCharCount--;
				}
			}
			return result.toString();
		}

	}


	public String randPathOfLength(int length, boolean valid) {
		if (length <= 2) return "";
		Random randInt = new Random();
		String str = RandomStringUtils.randomAlphanumeric(length, length+1);			//create a random alphanumeric string
		int numSeparators = randInt.nextInt(length/2);														//get a random number of separators
		StringBuilder result = new StringBuilder(str);														//use this for easier char replacement
		int index = 0;

		while (numSeparators > 0) {
			// use valid characters for a valid path, else use randomized invalid chars
			if (valid) {
				index = randInt.nextInt(length);
				if (index > 0 && result.charAt(index-1) != '/') {
					result.setCharAt(index, this.validPathSeparator.charAt(0));					// may need to change to just '/' ...
				}
				result.setCharAt(length-1, '/');																			// finish path with '/' (kind of ugly that this is in the loop)
			} else {
				result.setCharAt(randInt.nextInt(length), randCharFromString(this.invalidSeparators, randInt));
			}
			numSeparators--;
		}
		return result.toString();
	}


	public String randQueryOfLength(int length, boolean valid) {
		if (length <= 2) return "";
		Random randInt = new Random();
		char[] queryChars = setRandomQueryChars(valid);
		int numQueryChars = randInt.nextInt(length/2);
		String str = RandomStringUtils.randomAlphanumeric(length, length+1);
		StringBuilder result = new StringBuilder(str);
		result.setCharAt(0, '?');			// set the first character of query to '?' whether valid or invalid

		// don't overwrite the '?', and randomly emplace the other query control chars
		while (numQueryChars > 0) {
			int index = randInt.nextInt(length);
			if (index != 0) {
				result.setCharAt(index, queryChars[randInt.nextInt(2)]);
			}	
			numQueryChars--;
		}
		return result.toString();
	}


	public String randFragmentOfLength(int length, boolean valid) {
		if (length <= 0) return "";
		Random randInt = new Random();
		String str = RandomStringUtils.randomAlphanumeric(length, length+1);			//create a random alphanumeric string
		StringBuilder result = new StringBuilder(str);
		result.setCharAt(0, this.validFragmentSeparator.charAt(0));					// use '#' whether valid or invalid
		
		// We don't *guarantee* here that the number of invalid chars matches invalidCharCount because
		// the main point is just to invalidate the string in a randomized fashion.
		if (! valid) {
			int invalidCharCount = randInt.nextInt(length);
			while (invalidCharCount > 0) {
				int index = randInt.nextInt(length);
				if (index != 0)
					result.setCharAt(randInt.nextInt(length), randCharFromString(this.invalidSeparators, randInt));
				invalidCharCount--;
			}
		}
		return result.toString();
	}


	public char randCharFromString(String inputStr, Random rand) {
		return inputStr.charAt(rand.nextInt(inputStr.length()));
	}


	public String randStringFromArray(String[] inputStrings, Random rand) {
		return inputStrings[rand.nextInt(inputStrings.length)];
	}


	public char[] setRandomQueryChars(boolean valid) {
		Random randInt = new Random();
		char[] queryChars = {'\0','\0'};
		
		// str contains either valid or invalid chars
		String str = this.validQuerySeparators;
		if (! valid) {
			str = this.invalidSeparators;
		}
		queryChars[0] = randCharFromString(str, randInt);

		// use the valid or invalid chars, and get 2 of them (distinct)
		char nextChar;
		while (true) {
			nextChar = randCharFromString(str, randInt);
			if (nextChar != queryChars[0]) {
				queryChars[1] = randCharFromString(str, randInt);
				break;
			}
		}
		return queryChars;
	}


	public boolean intInArray(int[] arr, int testInt) {
		for(int i=0; i<arr.length; i++) {
			if (arr[i] == testInt)
				return true;
		}
		return false;
	}


	public String randIp(boolean valid) {
		Random rand = new Random();
		String result;
		if (valid) {					// unsure of how legit this is :(
			result = rand.nextInt(256) + "." + rand.nextInt(256) + "." + rand.nextInt(256) + "." + rand.nextInt(256) + ":";
			result += randStringFromArray(someValidPorts, rand) + "/";
		} else {
			result = rand.nextInt(1000) + "." + rand.nextInt(1000) + "." + rand.nextInt(1000) + "." + rand.nextInt(1000) + ":";
			result += randStringFromArray(someInvalidPorts, rand);
		}
		return result;		
	}


/*****************************************End Rand Testing Functions***************************************/


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



