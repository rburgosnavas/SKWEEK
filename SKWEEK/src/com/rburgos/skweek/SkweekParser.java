package com.rburgos.skweek;

import java.util.*;
import java.util.regex.*;

public class SkweekParser
{
	private static Stack<String> calculations;
	private static Stack<String> postfixStack;
	private static Stack<String> operators;
	private static final String REGEX = "((t\\b)|(x\\b)|(y\\b)|(z\\b)|(\\d*\\.\\d+)|(\\d+)|([\\+\\-\\*/\\(\\)]|(\\|{1,2})|(\\&{1,2})|(\\<{1,2})|(\\>{1,2})|(\\^{1})))";
	private static int t, x = 0, y = 0, z = 0;

	public SkweekParser() { }
	
	public static String evalToString(String exp)
	{
		String[] tokenArray = split(exp);
		String[] postfixTokenArray = convertToPostfix(tokenArray);
		return computePostfix(postfixTokenArray);
	}
	
	public static int evalToInt(String exp, int time, int xval, int yval, int zval)
	{
		t = time;
		x = xval;
		y = yval;
		z = zval;
		String[] tokenArray = split(exp);
		String[] postfixTokenArray = convertToPostfix(tokenArray);
		return computePostfixToInt(postfixTokenArray);
	}
	
	public static int evalToInt(String exp)
	{
		String[] tokenArray = split(exp);
		String[] postfixTokenArray = convertToPostfix(tokenArray);
		return computePostfixToInt(postfixTokenArray);
	}
	
	protected static String[] split(String expression)
	{
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(expression);
		ArrayList<String> tokenList = new ArrayList<>();

		while(matcher.find()) 
		{
		    tokenList.add(matcher.group());
		}

		String[] tokenArray = new String[tokenList.size()];
		
		for (int i = 0; i < tokenList.size(); i++)
		{
			tokenArray[i] = tokenList.get(i);
		}
		
		return tokenArray; 
	}
	
	protected static String[] convertToPostfix(String[] tokenArray)
	{
		postfixStack = new Stack<>();
		operators = new Stack<>();
		for (int i = 0; i < tokenArray.length; i++)
		{
			if (tokenArray[i].equals("+") || tokenArray[i].equals("-"))
			{
				if (operators.isEmpty())
				{
					operators.push(tokenArray[i]);
				}
				else
				{
					if (operators.peek().equals("("))
					{
						operators.push(tokenArray[i]);
					}
					else
					{
						postfixStack.push(operators.pop());
						operators.push(tokenArray[i]);						
					}
				}
			}
			else if (tokenArray[i].equals("*") || tokenArray[i].equals("/") || 
					tokenArray[i].equals("^") || tokenArray[i].equals("<<") ||
					tokenArray[i].equals(">>") || tokenArray[i].equals("|") ||
					tokenArray[i].equals("&"))
			{
				if (operators.isEmpty())
				{
					operators.push(tokenArray[i]);
				}
				else
				{
					if (operators.peek().equals("*") || 
							operators.peek().equals("/") || 
							operators.peek().equals("^") ||
							operators.peek().equals("<<") ||
							operators.peek().equals(">>") ||
							operators.peek().equals("|") ||
							operators.peek().equals("&"))
					{
						postfixStack.push(operators.pop());
						operators.push(tokenArray[i]);
					}
					else
					{
						operators.push(tokenArray[i]);
					}
				}
			}
			else if (tokenArray[i].equals("("))
			{
				operators.push(tokenArray[i]);
			}
			else if (tokenArray[i].equals(")"))
			{
				while (!operators.isEmpty())
				{
					String rightP = operators.pop();
					if (!rightP.equals("("))
					{
						postfixStack.push(rightP);
					}
				}
			}
			else
			{
				postfixStack.push(tokenArray[i]);
			}
			
		} // end for loop
		
		// push remaining elements from operator stack if it's not empty
		while (!operators.isEmpty()) 
		{
			postfixStack.push(operators.pop());	
		}
		
		String[] result = new String[postfixStack.size()];
		
		for (int i = 0; i < postfixStack.size(); i++)
		{
			result[i] = postfixStack.get(i);
		}
		
		return result;
	}
	
	protected static String computePostfix(String[] postfixTokenArray)
	{
		calculations = new Stack<>();
		for (int i = 0; i < postfixTokenArray.length; i++)
		{
			switch(postfixTokenArray[i])
			{
			case "+":
				calculations.push(add(calculations.pop(), calculations.pop()));
				break;
			case "-":
				calculations.push(sub(calculations.pop(), calculations.pop()));
				break;
			case "*":
				calculations.push(mul(calculations.pop(), calculations.pop()));
				break;
			case "/":
				calculations.push(div(calculations.pop(), calculations.pop()));
				break;
			case "^":
				calculations.push(pow(calculations.pop(), calculations.pop()));
				break;
			case "<<":
				calculations.push(lshift(calculations.pop(), calculations.pop()));
				break;
			case ">>":
				calculations.push(rshift(calculations.pop(), calculations.pop()));
				break;
			case "|":
				calculations.push(or(calculations.pop(), calculations.pop()));
				break;
			case "&":
				calculations.push(and(calculations.pop(), calculations.pop()));
				break;
			case "t":
				calculations.push(String.valueOf(t));
				break;
			case "x":
				calculations.push(String.valueOf(x));
				break;
			case "y":
				calculations.push(String.valueOf(y));
				break;
			case "z":
				calculations.push(String.valueOf(z));
				break;
			default:
				calculations.push(postfixTokenArray[i]);
			}
		}		
		return calculations.pop();
	}
	
	protected static int computePostfixToInt(String[] postfixTokenArray)
	{
		Stack<String>calc = new Stack<>();
		for (int i = 0; i < postfixTokenArray.length; i++)
		{
			switch(postfixTokenArray[i])
			{
			case "+":
				calc.push(add(calc.pop(), calc.pop()));
				break;
			case "-":
				calc.push(sub(calc.pop(), calc.pop()));
				break;
			case "*":
				calc.push(mul(calc.pop(), calc.pop()));
				break;
			case "/":
				calc.push(div(calc.pop(), calc.pop()));
				break;
			case "^":
				calc.push(pow(calc.pop(), calc.pop()));
				break;
			case "<<":
				calc.push(lshift(calc.pop(), calc.pop()));
				break;
			case ">>":
				calc.push(rshift(calc.pop(), calc.pop()));
				break;
			case "|":
				calc.push(or(calc.pop(), calc.pop()));
				break;
			case "&":
				calc.push(and(calc.pop(), calc.pop()));
				break;
			case "t":
				calc.push(String.valueOf(t));
				break;
			case "x":
				calc.push(String.valueOf(x));
				break;
			case "y":
				calc.push(String.valueOf(y));
				break;
			case "z":
				calc.push(String.valueOf(z));
				break;
			default:
				calc.push(postfixTokenArray[i]);
			}
		}		
		Double result = Double.parseDouble(calc.pop());
		return result.intValue();
	}
	
	private static String add(String a, String b)
	{
		return String.valueOf((Double.parseDouble(a) + Double.parseDouble(b)));
	}
	
	private static String sub(String a, String b)
	{
		return String.valueOf((Double.parseDouble(b) - Double.parseDouble(a)));
	}
	
	private static String mul(String a, String b)
	{
		return String.valueOf((Double.parseDouble(a) * Double.parseDouble(b)));
	}
	
	private static String div(String a, String b)
	{
		return String.valueOf((Double.parseDouble(b) / Double.parseDouble(a)));
	}
	
	private static String pow(String a, String b)
	{
		return String.valueOf(Math.pow(Double.parseDouble(b), 
				Double.parseDouble(a)));
	}
	
	private static String lshift(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() << aa.intValue()));
	}
	
	private static String rshift(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() >> aa.intValue()));
	}
	
	private static String or(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() | aa.intValue()));
	}
	
	private static String and(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() & aa.intValue()));
	}
}
