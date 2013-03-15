package com.rburgos.skweek;

import java.util.*;
import java.util.regex.*;

public class SkweekParser
{
	private static final String REGEX = "((t\\b)|(x\\b)|(y\\b)|(z\\b)|" + 
			"(\\d*\\.\\d+)|(\\d+)|([\\+\\-\\*/\\%\\(\\)]|" + 
			"(\\|{1,2})|(\\&{1,2})|(\\<{1,2})|(\\>{1,2})|(\\^{1})))";
	private static int t, x = 0, y = 0, z = 0;

	private SkweekParser() { }
	
	public static int evalToInt(String exp, int time, int xval, int yval, 
			int zval)
	{
		t = time;
		x = xval;
		y = yval;
		z = zval;
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
		
		return tokenList.toArray(new String[tokenList.size()]);
	}
	
	protected static String[] convertToPostfix(String[] tokenArray)
    {
        Stack<String> postfixStack = new Stack<>();
        Stack<String> operators = new Stack<>();
        for (int i = 0; i < tokenArray.length; i++)
        {
            if (isLowPrecedence(tokenArray[i]))
            {
                if (operators.isEmpty())
                {
                    operators.push(tokenArray[i]);
                }
                else
                {
                    if (isLeftParen(operators.peek()))
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
            else if (isHighPrecedence(tokenArray[i]))
            {
                if (operators.isEmpty())
                {
                    operators.push(tokenArray[i]);
                }
                else
                {
                    if (isHighPrecedence(operators.peek()))
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
            else if (isLeftParen(tokenArray[i]))
            {
                operators.push(tokenArray[i]);
            }
            else if (isRightParen(tokenArray[i]))
            {
                while (!operators.isEmpty())
                {
                    String rightP = operators.pop();
                    if (!isLeftParen(rightP))
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
        
        return postfixStack.toArray(new String[postfixStack.size()]);
    }
	
	protected static int computePostfixToInt(String[] postfixTokenArray)
	{
		Stack<String>calc = new Stack<>();
		for (int i = 0; i < postfixTokenArray.length; i++)
		{
			switch(postfixTokenArray[i])
			{
			case "+":
				calc.push(SkweekMath.add(calc.pop(), calc.pop()));
				break;
			case "-":
				calc.push(SkweekMath.sub(calc.pop(), calc.pop()));
				break;
			case "*":
				calc.push(SkweekMath.mul(calc.pop(), calc.pop()));
				break;
			case "/":
				calc.push(SkweekMath.div(calc.pop(), calc.pop()));
				break;
			case "^":
				calc.push(SkweekMath.pow(calc.pop(), calc.pop()));
				break;
			case "<<":
				calc.push(SkweekMath.lshift(calc.pop(), calc.pop()));
				break;
			case ">>":
				calc.push(SkweekMath.rshift(calc.pop(), calc.pop()));
				break;
			case "|":
				calc.push(SkweekMath.or(calc.pop(), calc.pop()));
				break;
			case "&":
				calc.push(SkweekMath.and(calc.pop(), calc.pop()));
				break;
			case "%":
				calc.push(SkweekMath.mod(calc.pop(), calc.pop()));
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
    
    private static boolean isLowPrecedence(String op)
    {
        return op.equals("+") || op.equals("-");
    }
    
    private static boolean isHighPrecedence(String op)
    {
        return op.equals("*") || op.equals("/") || op.equals("^") || 
            op.equals("<<") || op.equals(">>") || op.equals("|") || 
            op.equals("&") || op.equals("%");
    }
    
    private static boolean isLeftParen(String op)
    {
        return op.equals("(");
    }
    
    private static boolean isRightParen(String op)
    {
        return op.equals(")");
    }
}
