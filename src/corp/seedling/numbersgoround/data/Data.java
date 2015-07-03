package corp.seedling.numbersgoround.data;

import java.util.ArrayList;
import java.util.Random;

public class Data {

	private static Data data = null;
	private static ArrayList<String> list;
	public static int GAME_MODE_EASY = 1;
	public static int GAME_MODE_HARD = 2;
	public static int GAME_MODE_CRAZY = 3;
	public static int GAME_MODE = GAME_MODE_EASY;


	public static ArrayList<String> getData(){
		System.out.println("ankur Data Class: prep data");
		list = new ArrayList<String>();
		int n;

		//if game mode CRAZY then let first operation not be fixed to subtraction
		if (GAME_MODE == GAME_MODE_CRAZY){
			//determine the 1st number
			n = new Random().nextInt(10);

			while ( n == 0 ){
				n = new Random().nextInt(10);
			}

			//add the 1st number
			list.add(String.valueOf(n) );
			//add the 1st operator
			addOperator();
		}
		else{
			//determine the 1st number
			n = new Random().nextInt(10);

			while (n == 0 || n == 1){
				n = new Random().nextInt(10);
			}

			//add the number
			list.add(String.valueOf(n) );

			//add the 1st operator which will always be -
			list.add("-");
		}
		//determine the 2nd number
		n = new Random().nextInt(10);

		if (GAME_MODE != GAME_MODE_CRAZY){
			//if not crazy mode, then 1st operator wud be -.. so ensure tht 2nd operand is > 1st operand
			while ( 
					n == 0 || 
					n > Integer.parseInt(list.get(0))
					){
				n = new Random().nextInt(10);
			}
		}
		else{
			while (	n == 0 ){
				n = new Random().nextInt(10);
			}
		}
		//add the 2nd number
		list.add(String.valueOf(n) );

		//add the 2nd operator
		addOperator();

		//determine the 3rd number
		n = new Random().nextInt(10);

		while ( 
				n == 0 ||
				n == Integer.parseInt(list.get(0)) || 
				n == Integer.parseInt(list.get(2))
				){
			n = new Random().nextInt(10);
		}

		//add the 3rd number
		list.add(String.valueOf(n) );

		//add the 3rd operator
		addOperator();

		//determine the 4th number
		n = new Random().nextInt(10);

		while ( 
				n == 0 || 
				n == Integer.parseInt(list.get(0)) || 
				n == Integer.parseInt(list.get(2)) ||
				n == Integer.parseInt(list.get(4))
				){
			n = new Random().nextInt(10);
		}

		//add the 4th number
		list.add(String.valueOf(n) );


		if (GAME_MODE != GAME_MODE_EASY){
			//add the 4th operator
			addOperator();

			//determine the 5th number
			n = new Random().nextInt(10);

			while ( 
					n == 0 ||
					n == Integer.parseInt(list.get(0)) ||
					n == Integer.parseInt(list.get(2)) ||
					n == Integer.parseInt(list.get(4)) ||
					n == Integer.parseInt(list.get(6))
					){
				n = new Random().nextInt(10);
			}

			//add the 4th number
			list.add(String.valueOf(n) );
		}
		
		if (evaluateExpression() == 0)
			getData();

		return list;
	}

	public static String EXPRESSION = "";
	private static int evaluateExpression() {

		EXPRESSION = "";

		int result = 0;

		for (int i = 0 ; i < list.size() ; i++){
			EXPRESSION += list.get(i) ;
		}

		System.out.println("ankur rec exp = " +EXPRESSION);

		if (GAME_MODE == GAME_MODE_CRAZY){
			//handle 1st operator
			if (list.get(1) == "-")
				result = Integer.parseInt(list.get(0)) - Integer.parseInt(list.get(2));

			else if (list.get(1) == "+")
				result = Integer.parseInt(list.get(0)) + Integer.parseInt(list.get(2));

			else if (list.get(1) == "x")
				result = Integer.parseInt(list.get(0)) * Integer.parseInt(list.get(2));

			else if (list.get(1) == "÷")
				result = (int) ( Integer.parseInt(list.get(0)) / Integer.parseInt(list.get(2)) ) ;

		}
		else{
			//1st operator was "-" which is already handled in EASY and HARD case
			result = Integer.parseInt(list.get(0)) - Integer.parseInt(list.get(2));
		}

		//handle 2nd operator
		if (list.get(3) == "-")
			result -= Integer.parseInt(list.get(4));

		else if (list.get(3) == "+")
			result += Integer.parseInt(list.get(4));

		else if (list.get(3) == "x")
			result *= Integer.parseInt(list.get(4));

		else if (list.get(3) == "÷")
			result = (int) ( result / Integer.parseInt(list.get(4)) ) ;

		//handle 3rd operator
		if (list.get(5) == "-")
			result -= Integer.parseInt(list.get(6));

		else if (list.get(5) == "+")
			result += Integer.parseInt(list.get(6));

		else if (list.get(5) == "x")
			result *= Integer.parseInt(list.get(6));

		else if (list.get(5) == "÷")
			result = (int) ( result / Integer.parseInt(list.get(6)) ) ;

		if (GAME_MODE != GAME_MODE_EASY){
			//handle 4th operator
			if (list.get(7) == "-")
				result -= Integer.parseInt(list.get(8));

			else if (list.get(7) == "+")
				result += Integer.parseInt(list.get(8));

			else if (list.get(7) == "x")
				result *= Integer.parseInt(list.get(8));

			else if (list.get(7) == "÷")
				result = (int) ( result / Integer.parseInt(list.get(8)) ) ;
		}

		list.add(""+result);

		System.out.println("ankur result = " + result);
		return result;
	}

	private static void addOperator(){
		int n = 1;

		switch (GAME_MODE) {
		case 1:
			n = new Random().nextInt(2);
			break;

		case 2:
			n = new Random().nextInt(3);
			break;

		case 3:
			n = new Random().nextInt(4);
			break;

		default:
			break;
		}

		switch (n) {
		case 0:
			list.add("+");
			break;

		case 1:
			list.add("x");
			break;

		case 2:
			list.add("÷");
			break;


		case 3:
			list.add("-");
			break;

		default:
			list.add("+");
			break;
		}

	}
}
