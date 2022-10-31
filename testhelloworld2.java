package testpackage;

import java.util.*;

public class testhelloworld2 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String sen = sc.nextLine();
		int num = sen.length();
		char ch = ' ';
		
		for(int i=0; i<num ; i++) {
			ch=sen.charAt(i);
			if('a'<= ch && ch <='z') {
				System.out.print(ch);
			}else if('A'<=ch && ch<='Z') {
				char k= (char)(ch+32);
				System.out.print(k);
			}else {continue;}
		}		
	}

}
