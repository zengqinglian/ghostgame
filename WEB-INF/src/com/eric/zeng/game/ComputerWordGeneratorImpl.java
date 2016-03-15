package com.eric.zeng.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class ComputerWordGeneratorImpl implements ComputerWordGenerator {
	
	private static Set<Character> letterSet = new HashSet<Character>();
	
	static{
		char letter;
		for (letter='a'; letter <= 'z'; letter++) {
			letterSet.add(new Character(letter));
		}
	}
	
	public ComputerWordGeneratorImpl(String tomcatRootDir) throws WordListFileCannotBeLoadedException{
		this(tomcatRootDir,null);
	}

	public ComputerWordGeneratorImpl(String tomcatRootDir,String currentWord) throws WordListFileCannotBeLoadedException{
		setExistingWord(currentWord);
		setTomcatRootDir(tomcatRootDir);
		loadingWordFile(_tomcatRootDir + WORDLISTFILE);
	}
	
	public int generateNextCharacter(String currentWord){
		setExistingWord(currentWord);
		return generateNextCharacter();
	}
	
	public int generateNextCharacter() {
		if(_existingWord == null){
			//TODO: random letter.
			
			return GameConstant.eOK;
		}
		//the algorithm just ask cpu considered one more move. 
		//Considering more moves will make cpu more difficult to beat. 
		//I have not figured out how to choose best character when consider 2 or more moves 
		 
		//save all the words length is _existingWord.length+3. since that will be cpu move again.
		//this round cpu will put one char and user will put one and cpu again. so +3.
		//1. this round cpu should not choose the character that only can make currentword length+3 chars word.
		//2. could not choose letters which could not find any word start with.
		//3. if there is a winning letter, choose winning letter. see iswinningletter method 
		Set<Character> availableLetterSet = new HashSet<Character>();
		Set<Character> badLettersSet = new HashSet<Character>();
		
		for(Character letter: letterSet){
			if(isAvailableLetter(letter)){
				availableLetterSet.add(letter);
			}			
		}
		
		//could not continue the game, user lose
		if(availableLetterSet.isEmpty()){
			return GameConstant.USR_LOST_CODE;
		}
		
		for(Character letter:availableLetterSet){
			if(_existingWord.length()+2>=MIN_WORD_LENGTH_TO_LOSE){
				if(isWinningLetter(letter)){
					_computerReturnedStr=_existingWord+letter.toString();
					return GameConstant.eOK;
				}
			}
			
			//adding next letter, string should not be a word. 
			if(_existingWord.length()+1>=MIN_WORD_LENGTH_TO_LOSE){
				if(isLosingLetterForThisRound(letter)){
					badLettersSet.add(letter);
				}
			}
			
			//find letter will let cpu lose in next round.
			if(isLosingLetterForNextRound(letter)){
				badLettersSet.add(letter);
			}
		}
		
		
		if(availableLetterSet.isEmpty()){
			return GameConstant.CPU_LOST_CODE;
		}
		
		availableLetterSet.removeAll(badLettersSet);
		
		if(availableLetterSet.isEmpty()){
			//all letter may make cpu lost next round.
			//random choose one letter from bad letter
			Character c=getRandomCharcterFromSet(badLettersSet);
			_computerReturnedStr = _existingWord + c.toString();
			if(_computerReturnedStr.length()>=MIN_WORD_LENGTH_TO_LOSE 
					&& _wordsSet.contains(_computerReturnedStr)){
				return GameConstant.CPU_LOST_CODE;
			}
			return GameConstant.eOK;
		}
		
		//choose random character from available list
		Character c=getRandomCharcterFromSet(availableLetterSet);
		if(c!=null){
			_computerReturnedStr = _existingWord + c.toString();
			return GameConstant.eOK;
		}
		
		return GameConstant.CPU_LOST_CODE;
	}
	
	private Character getRandomCharcterFromSet(Set<Character> charSet){
		int max = charSet.size();
		int min = 1;
		int randomnumber = (int)(Math.random() * charSet.size())+min;
		int i=1;
		if(randomnumber>max){
			_computerReturnedStr = _existingWord + charSet.iterator().next().toString();
			return GameConstant.eOK;
		}
        for(Character c : charSet){
        	if(i == randomnumber){
        		return c;
        	}else{
        		i++;
        	}
        }
        return null;
	}
	
	private boolean isAvailableLetter(Character c){
		Set<Character> uniqueNextLetterSet = findUniqueNextLetter();
		if(uniqueNextLetterSet.contains(c)){
			return true;
		}
		return false;
	}
	
	private boolean isLosingLetterForThisRound(Character c){
		//can not be a word
		for(String word : _wordsSet){
			if(word.equalsIgnoreCase(_existingWord+c.toString())){
				return true;
			}
		}
		return false;
	}
	
	private boolean isLosingLetterForNextRound(Character c){
		Set<String> tempSet1 = new HashSet<String>(); 
	
		for(String word : _wordsSet){
			if(word.length() == _existingWord.length() + 3){
				tempSet1.add(word);
			}
		}
		
		if(tempSet1.isEmpty()){
			return false;
		}
		
		//all the letter
		for(String word: tempSet1){
			if(word.charAt(_existingWord.length())==c.charValue()){
				return true;
			}
		}
		
		
		return true;
	}
	
	//choose this character, only word/words will leave to user.
	private boolean isWinningLetter(Character c){
		boolean isPotentialWinningCharacter = false;
		for(String word: _wordsSet){
			if(word.length()==(_existingWord.length()+2) &&
					word.indexOf(_existingWord+c.toString())>0){
				isPotentialWinningCharacter = true;
			}
		}
		
		if(isPotentialWinningCharacter){
			for(String word: _wordsSet){
				if(word.length()>(_existingWord.length()+2) &&
						word.indexOf(_existingWord+c.toString())>0){
					isPotentialWinningCharacter = false;
				}
			}
		}
		return isPotentialWinningCharacter;
	}
	
	private Set<Character> findUniqueNextLetter(){
		Set<Character> returnSet = new HashSet<Character>();
		for(String word: _wordsSet){
			if(word.length()>_existingWord.length() && word.startsWith(_existingWord)){
				returnSet.add(word.charAt(_existingWord.length()));
			}
		}
		return returnSet;
	}
	
	public int checkUserLost(String existingWord){
		setExistingWord(existingWord);
		return checkUserLost();
	}
	
	public int checkUserLost(){
		if(_existingWord==null){
			//skip checking and  return ok
			return GameConstant.eOK;
		}
		//could not continue, no word. user lost
		if(_wordsSet.isEmpty()){
			return GameConstant.USR_LOST_CODE;
		}
		//4 chars and it is a word. user lost
		if(_existingWord.length()>=MIN_WORD_LENGTH_TO_LOSE && 
				_wordsSet.contains(_existingWord)){
			return GameConstant.USR_LOST_CODE;
		}
		
		return GameConstant.eOK;
	}
	
	 private void loadingWordFile(String filePath) throws WordListFileCannotBeLoadedException {
		 try{
			 FileReader fileReader = new FileReader(filePath);
			 BufferedReader bufferedReader = new BufferedReader(fileReader);
		        String line = null;
		        while ((line = bufferedReader.readLine()) != null) {
		        	if(_existingWord!=null && line.startsWith(_existingWord)){
		        		_wordsSet.add(line);
		        	}
		        }
		        bufferedReader.close();
		 }catch (IOException e){
			 throw new WordListFileCannotBeLoadedException("Could not load word list file.");
		 }
	}

	public String getComputerReturnedString() {
		return _computerReturnedStr;
	}

	public void setExistingWord(String existingWord) {
		this._existingWord = existingWord;
	}

	public String getExistingWord() {
		return _existingWord;
	}

	public void setTomcatRootDir(String tomcatRootDir) {
		this._tomcatRootDir = tomcatRootDir;
	}

	public String getTomcatRootDir() {
		return _tomcatRootDir;
	}
	
	public void addIntoWardsSet(String s){
		_wordsSet.add(s);
	}

	private Set<String> _wordsSet = new HashSet<String>();
	private String _computerReturnedStr;
	private static final int MIN_WORD_LENGTH_TO_LOSE = 4; 
	private static final String WORDLISTFILE = "/../res/word.lst";
	private String _existingWord;
	private String _tomcatRootDir;
}
