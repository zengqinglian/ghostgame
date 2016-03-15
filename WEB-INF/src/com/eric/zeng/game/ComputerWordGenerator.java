package com.eric.zeng.game;

public interface ComputerWordGenerator {
	
	public int generateNextCharacter(String existingWord);
	
	public int generateNextCharacter();
	
	public int checkUserLost(String existingWord);
	
	public int checkUserLost();
	
	public String getComputerReturnedString();
	
	public void setExistingWord(String _existingWord) ;

	public String getExistingWord();
	
	public void setTomcatRootDir(String tomcatRootDir);

	public String getTomcatRootDir();
	
	public void addIntoWardsSet(String s);
}
