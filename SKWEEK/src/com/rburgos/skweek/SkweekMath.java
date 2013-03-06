package com.rburgos.skweek;

public class SkweekMath
{

	private SkweekMath() {}
	
	static String add(String a, String b)
	{
		return String.valueOf((Double.parseDouble(a) + Double.parseDouble(b)));
	}

	static String sub(String a, String b)
	{
		return String.valueOf((Double.parseDouble(b) - Double.parseDouble(a)));
	}

	static String mul(String a, String b)
	{
		return String.valueOf((Double.parseDouble(a) * Double.parseDouble(b)));
	}

	static String div(String a, String b)
	{
		return String.valueOf((Double.parseDouble(b) / Double.parseDouble(a)));
	}

	static String mod(String a, String b)
	{
		return String.valueOf((Double.parseDouble(b) % Double.parseDouble(a)));
	}

	static String pow(String a, String b)
	{
		return String.valueOf(Math.pow(Double.parseDouble(b), 
				Double.parseDouble(a)));
	}

	static String lshift(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() << aa.intValue()));
	}

	static String rshift(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() >> aa.intValue()));
	}

	static String or(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() | aa.intValue()));
	}

	static String and(String a, String b)
	{
		Double aa = Double.parseDouble(a);
		Double bb = Double.parseDouble(b);
		return String.valueOf((double)(bb.intValue() & aa.intValue()));
	}

}
