package ca.concordia.comp6651;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class AlgorithmAssignment {
	private static final String FILE_NAME_ROOT = "./resources/ca/concordia/comp6651/ProgrammingAssignment1Sample";
	
	private static String FILE_NAME = "./resources/ca/concordia/comp6651/ProgrammingAssignment1SampleInput2.txt";
	private static final String OUTPUT_FILE_NAME = "./resources/ca/concordia/comp6651/ProgrammingAssignment1SampleOutput2.txt";
	private static final String DIVIDE_LINE = "-----";
	private static final String OUTPUT_DIVIDE_LINE = "--------------------\n";
	private static final String NO_ANSWER_FOUND = "No Answer Found\n";
	
	private int totleLines = 0;
	private int jumIndex = 0; //begin index of jumbled word
	private DicStruct[] dicStruct= null;
	
	private class DicStruct implements Comparable<DicStruct>{
		
		public String word;
		public int index = 0;
		public int[] wordAbs = {0,
								0,0,0,0,0,0,0,0,0,0,0,0,0,
								0,0,0,0,0,0,0,0,0,0,0,0,0}; //to restore the frequency of the single character
		
		
		public DicStruct(String word, int index, int[] wordAbs) {
			this.word = word;
			this.index = index;
			this.wordAbs = wordAbs;
		}

		public int compareWordto(DicStruct dic) {
			if (this == dic)
				return 0;
			if(dic == null && this.word == null)
				return 0;
			else if(dic == null && this.word != null) {
				return 1;
			}
			int i = 0;
			for(i=0;i<this.word.length() || i<dic.word.length();i++) {
				if(this.word.charAt(i) < dic.word.charAt(i)) {
					return -1;
				} else if(this.word.charAt(i) > dic.word.charAt(i)){
					return 1;
				}
			}
			if(i==this.word.length()  && i!=dic.word.length()) {
				return -1;
			} else if(i!=this.word.length()  && i==dic.word.length()){
				return 1;
			}
			return 0;
		}
		
		public boolean equals(DicStruct dic) {
			if (this == dic)
				return true;
			if (dic == null)
				return false;
			for(int i=1;i<27;i++) {
				if(dic.wordAbs[i] != this.wordAbs[i])
					return false;
			}
			return true;
		}
		
		@Override
		public int compareTo(DicStruct dic) {
			if (this == dic)
				return 0;
			for(int i=1;i<27;i++) {
				if(this.wordAbs[i] < dic.wordAbs[i]) {
					return -1;
				} else if(this.wordAbs[i] > dic.wordAbs[i]){
					return 1;
				}
			}
			if(this.wordAbs[0] > dic.wordAbs[0]) {
				return 1;
			}
			return 0;
		}
		
		
	}
	/**
	 * read all the word lines
	 * @return
	 */
	private int readFileLines() {
		int lines = 0;
		try {
			File file = new File(FILE_NAME);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					String word = lineTXT.toString().trim();
					if(!word.equals(DIVIDE_LINE)) {
						lines++;
					} else {
						if(jumIndex == 0) {
							jumIndex = lines + 1;
//							System.out.println("find jumbled index:" + jumIndex);
						}
					}
				}
				read.close();
			} else {
				System.out.println("can not find the file!");
			}
		} catch (Exception e) {
			System.out.println("unknow error when open file!");
			e.printStackTrace();
		}
		return lines;
	}
	/**
	 * create dictionary class 
	 * @param dicStruct
	 */
	private void createFileStruct(DicStruct[] dicStruct) {
		int index = 0;
		try {
			File file = new File(FILE_NAME);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					String word = lineTXT.toString().trim();
					if(!word.equals(DIVIDE_LINE)) {
						index++;
						DicStruct dic = createStruct(word, index);
						dicStruct[index-1] = dic;
//						System.out.println(index+"-"+word);
					}
				}
				read.close();
			} else {
				System.out.println("can not find the file!");
			}
		} catch (Exception e) {
			System.out.println("unknow error when open file!");
			e.printStackTrace();
		}
	}
	
	private DicStruct createStruct(String word, int index) {
		int[] wordAbs = parserWord(word, index);
		return new DicStruct(word, index, wordAbs);
	}
	/**
	 * counting every single character frequency
	 * @param word
	 * @param index
	 * @return
	 */
	private int[] parserWord(String word, int index) {
		int[] wordAbs = {0,
						 0,0,0,0,0,0,0,0,0,0,0,0,0,
						 0,0,0,0,0,0,0,0,0,0,0,0,0};
		
		if(word != null) {
			if(index >= jumIndex) {
				wordAbs[0] = 1;
			}
			for(int i=0;i<word.length();i++) {
				char c = word.charAt(i);
				if(c>=97 && c<=122)
					wordAbs[c-97+1]++;
			}
//			System.out.println(index+"-"+wordAbs.toString());
		}
		
		return wordAbs;
	}
	
	/**
	 * Quicksort for all the struct word
	 * @param dic
	 * @param start
	 * @param end
	 */
	private void quickSortDicStruct(DicStruct[] dic, int start, int end) {
		if(start < end) {
			int middle = partition(dic, start, end);
			quickSortDicStruct(dic,start, middle-1);
			quickSortDicStruct(dic,middle+1, end);
		}
	}
	
	private int partition(DicStruct[] dic, int start, int end) {
		
		DicStruct d = dic[end];
		int middle = start - 1;
		for(int j=start;j<end;j++) {
			if(dic[j].compareTo(d)>0) {
				middle++;
				exchangeDicStruct(middle, j);
				
			}
		}
		exchangeDicStruct(middle+1, end);
		
		return middle+1;
	}
	
	private void exchangeDicStruct(int a, int b) {
		if(a == b)
			return;
		DicStruct temp = null;
		
		temp = dicStruct[b];
		dicStruct[b] = dicStruct[a];
		dicStruct[a] = temp;
	}
	
	private void exchangeDicStruct(DicStruct[] dic, int a, int b) {
		if(a == b)
			return;
		DicStruct temp = null;
		
		temp = dic[b];
		dic[b] = dic[a];
		dic[a] = temp;
	}
	
	private void sortsingleDic(DicStruct[] dic) {
		if(dic == null || dic.length == 0)
			return;
		for(int i=0;i<dic.length;i++) {
			for(int j = i+1;j<dic.length;j++) {
				DicStruct d = dic[i];
				DicStruct dj = dic[j];
				if(d.compareWordto(dj) >= 0) {
					exchangeDicStruct(dic, i, j);
				}
			}
		}
	}
	
	/**
	 * echo to File
	 */
	private void echo2File() {
		String[] outPut = new String[totleLines-jumIndex + 1];
		for(int i=0;i<outPut.length;i++) {
			outPut[i] = "";
		}
		int original = -1;
		int dicLength = 0;
		int jumbledLength = 0;
		
		for(int i=0;i<dicStruct.length;i++) {
			DicStruct dic = dicStruct[i];
//			System.out.println(i+"-"+dic.wordAbs[0]+"-"+dic.word);
			if(dic.wordAbs[0] == 1 && original == -1) {
				if(i<dicStruct.length-1 && dic.equals(dicStruct[i+1])) {
					original = i;
				} else {
					outPut[dic.index-jumIndex] = NO_ANSWER_FOUND + OUTPUT_DIVIDE_LINE;
					jumbledLength = 0;
					dicLength = 0;
					original = -1;
				}
			} else {
				if(original != -1) {
					DicStruct d = dicStruct[original];
					if(d.equals(dic)) {
						if(dic.wordAbs[0] == 1) {
							jumbledLength ++;
						}
						dicLength++;
						if(!(i<dicStruct.length-1 && dic.equals(dicStruct[i+1]))) {
							DicStruct[] jumbDic = new DicStruct[jumbledLength+1];
							DicStruct[] singleDic = new DicStruct[dicLength-jumbledLength];
							for(int m=0;m<=jumbledLength;m++) {
								jumbDic[m] = dicStruct[original+m];
							}
							for(int m=0;m<dicLength-jumbledLength;m++) {
								singleDic[m] = dicStruct[original+jumbledLength+1+m];
								for(int n=0;n<m;n++) {
									if(singleDic[n]!=null&&singleDic[n].compareWordto(singleDic[m]) == 0) {
										singleDic[m].wordAbs[0] = -1;
									}
								}
							}
							//resort singleDic
							sortsingleDic(singleDic);
							String out = NO_ANSWER_FOUND;
							for(int m=0;m<singleDic.length;m++) {
								if(m == 0) {
									out = "";
								}
								if(singleDic[m].wordAbs[0] == 0) {
									out += singleDic[m].word+"\n";
								}
							}
							for(int m=0;m<jumbDic.length;m++) {
								outPut[jumbDic[m].index-jumIndex] = out;
								outPut[jumbDic[m].index-jumIndex] += OUTPUT_DIVIDE_LINE;
							}
							jumbledLength = 0;
							dicLength = 0;
							original = -1;
						}
					} else {
						if(dicLength == 0) {
//							for(int j=0;j<jumbledLength;j++) {
//								
//							}
							outPut[dic.index-jumIndex] = NO_ANSWER_FOUND + OUTPUT_DIVIDE_LINE;
						}
						jumbledLength = 0;
						dicLength = 0;
						original = -1;
					}
				}
			}
		}
		
//		System.out.println("Echo2- find all the dictionary time is: " + (System.currentTimeMillis()-begin)/1000.0 + "s");
		
		/*bad method to amend string*/
//		String s = "";
//		for(int i=0;i<outPut.length;i++) {
//			s += outPut[i];
//		}
//		System.out.println("Echo2- finish amend String time is: " + (System.currentTimeMillis()-begin)/1000.0 + "s");
		FileWriter fw;
		try {
			fw = new FileWriter(OUTPUT_FILE_NAME, false);
			for(int i=0;i<outPut.length;i++) {
				fw.write(outPut[i]);
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("Echo2- finish write to file time is: " + (System.currentTimeMillis()-begin)/1000.0 + "s");
	}
	private long begin = 0;
	public void finishIt(AlgorithmAssignment assignment) {
		begin=System.currentTimeMillis();
		
		totleLines = assignment.readFileLines();
//		long readFilelines = System.currentTimeMillis()-begin;
//		System.out.println("Read file lines time is: " + readFilelines/1000.0 + "s");
		
		dicStruct = new DicStruct[totleLines];
		createFileStruct(dicStruct);
//		long createFileStruct = System.currentTimeMillis()-readFilelines;
//		System.out.println("Read file and create struct time is: " + createFileStruct/1000.0 + "s");
		
		quickSortDicStruct(dicStruct, 0, dicStruct.length-1);
		long quickSortDicStruct = System.currentTimeMillis()-begin;
		System.out.println("After Quick sort time is: " + quickSortDicStruct/1000.0 + "s");
		
		echo2File();
		
		System.out.println("Total process time is: " + (System.currentTimeMillis()-begin)/1000.0 + "s");
		totleLines = 0;
		jumIndex = 0;
	}
	
	public static void main(String[] args) {
		AlgorithmAssignment assignment = new AlgorithmAssignment();
		System.out.println("I-test original file, size is 78k");
		assignment.finishIt(assignment);
		System.out.println("\nII-test 10-times than original file, size is 775k");
		FILE_NAME = FILE_NAME_ROOT + "InputAll10.txt";
		assignment.finishIt(assignment);
		System.out.println("\nIII-test 20-times than original file, size is 1563k");
		FILE_NAME = FILE_NAME_ROOT + "InputAll20.txt";
		assignment.finishIt(assignment);
		System.out.println("\nIV-test 30-times than original file, size is 2344k");
		FILE_NAME = FILE_NAME_ROOT + "InputAll30.txt";
		assignment.finishIt(assignment);
		System.out.println("\nV-test 40-times than original file, size is 3125k");
		FILE_NAME = FILE_NAME_ROOT + "InputAll40.txt";
		assignment.finishIt(assignment);
		System.out.println("\nVI-test 50-times than original file, size is 3906k");
		FILE_NAME = FILE_NAME_ROOT + "InputAll50.txt";
		assignment.finishIt(assignment);
		System.out.println("\nVII-test 100-times than original file, size is 7745k");
		FILE_NAME = FILE_NAME_ROOT + "InputAll100.txt";
		assignment.finishIt(assignment);
	}

}
