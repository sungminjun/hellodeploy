package kr.jwor.hello.deploy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Hello {

	public static final String VERSION = "1.1";
	public static final int DEFAULT_WIDTH = 80;
	public static final String DEFAULT_FILLER = "=";
	ArrayList<String> menu = new ArrayList<String>();
	{
		menu.add("0");
		menu.add("print hello");
		menu.add("1");
		menu.add("print hello");
		menu.add("2");
		menu.add("print hello 5times");
		menu.add("exit");
		menu.add("exit this program");
	}

	public static void main(String[] args) {
		Hello hello = new Hello();
		try {
			hello.exec();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exec() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String HEADER = fillString(String.format("== HELLO DEPLOY == v.%s ==", VERSION), DEFAULT_FILLER);
		String HR = fillString("", DEFAULT_FILLER);
		String PROMPT = "input COMMAND> ";
		
		// showing menu
		String in;
		while (true) {
			System.out.println(HEADER);
			System.out.println(HR);
			String fmtMenu = "--- %10s ----- %.20s -----";
			System.out.println(String.format(fmtMenu, "COMMAND", "DESCRIPTION"));
			for (int i = 0; i < menu.size(); i++) {
				System.out.println(String.format(fmtMenu, menu.get(i), menu.get(++i)));
			}
			System.out.println(HR);
			System.out.print(PROMPT);
			switch (in = br.readLine()) {
				case "0": 
					print();
					break;
				case "1":
					print(1);
					break;
				case "2":
					print(5);
					break;
				case "exit":
					return;
				default:
					System.out.println("no such command:" + in);
			}
			System.out.println();
		}
	}

	private void print() {
		print(1);
	}
	
	private void print(int i) {
		for (int j = 0; j < i; j++) {
			System.out.println("HELLO DEPLOY");
		}
	}

	private String fillString(String str, String fillWith) {
		return fillString(str, fillWith, DEFAULT_WIDTH);
	}

	private String fillString(String str, String fillWith, int length) {
		while (str.length() < length) {
			str += fillWith;
		}
		return str;
	}
}
