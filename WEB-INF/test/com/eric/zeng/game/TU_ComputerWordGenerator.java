package com.eric.zeng.game;

import junit.framework.TestCase;

public class TU_ComputerWordGenerator extends TestCase{
	//Need to find proper words to test, 
	//I did not spend too much time on it. this test just showed my roughly idea
	//to make test run , you need to change WORD_FILE_DIR setting in the tests.
	//in real env, i will create env variable so that i can get the directory by calling "%wordlistDir%".
	public void testUserLoseGame(){
		ComputerWordGenerator userTester = null;
		try {
			userTester = new ComputerWordGeneratorImpl(WORD_FILE_DIR,"a");
		} catch (WordListFileCannotBeLoadedException e) {
			fail("Could not find word list file");
		}
		
	    //existingword is null, return ok
		userTester.setExistingWord(null);
		assertEquals(GameConstant.eOK,userTester.checkUserLost());

		//user lose
		userTester.setExistingWord(EXISTING_WORD_1);

		assertEquals(GameConstant.USR_LOST_CODE, userTester.checkUserLost());
		
		//test user can continue
		userTester.setExistingWord(EXISTING_WORD_2);
		assertEquals(GameConstant.eOK,userTester.checkUserLost());
	}
	
	
	public void testGenerateNextCharacter(){
		
		ComputerWordGenerator cpuTester = null;
		try {
			cpuTester = new ComputerWordGeneratorImpl(WORD_FILE_DIR);
		} catch (WordListFileCannotBeLoadedException e) {
			fail("fail to load word list.");
		}
		cpuTester.addIntoWardsSet(EXISTING_WORD_3);
		cpuTester.addIntoWardsSet(EXISTING_WORD_4);
		cpuTester.addIntoWardsSet(EXISTING_WORD_5);
		cpuTester.addIntoWardsSet(EXISTING_WORD_6);
		cpuTester.addIntoWardsSet(EXISTING_WORD_7);
		cpuTester.addIntoWardsSet(EXISTING_WORD_8);
		cpuTester.addIntoWardsSet(EXISTING_WORD_9);
        
		//test user lose since game could not continue
		cpuTester.setExistingWord(EXISTING_WORD_10);
		assertEquals(GameConstant.USR_LOST_CODE,cpuTester.generateNextCharacter());
		
		
		//test computer lose
		cpuTester.setExistingWord(EXISTING_WORD_11);
		assertEquals(GameConstant.CPU_LOST_CODE,cpuTester.generateNextCharacter());
		
		//test computer choose next letter
		cpuTester.setExistingWord(EXISTING_WORD_12);
		assertEquals(GameConstant.eOK,cpuTester.generateNextCharacter());
		assertEquals("abac",cpuTester.getComputerReturnedString());
	}
	//need to change accordingly
	//in real env, i will create env variable so that i can get the directory by calling "%wordlistDir%".
	private String WORD_FILE_DIR = "C:\\eclipse\\workspace\\ghostgame\\res";
	private final String EXISTING_WORD_1="abaci";
	private final String EXISTING_WORD_2="abando"; 
	
	private final String EXISTING_WORD_3="abaca";
	private final String EXISTING_WORD_4="abacas";
	private final String EXISTING_WORD_5="abaci";
	private final String EXISTING_WORD_6="aback";
	private final String EXISTING_WORD_7="abacterial";
	private final String EXISTING_WORD_8="abcd";
	private final String EXISTING_WORD_9="abacuses";
	
	private final String EXISTING_WORD_10="abt";
	private final String EXISTING_WORD_11="abc";
	private final String EXISTING_WORD_12="aba";

}
