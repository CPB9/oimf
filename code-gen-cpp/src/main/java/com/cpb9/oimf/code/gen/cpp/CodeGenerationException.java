package com.cpb9.oimf.code.gen.cpp;

/**
 * @author Artem Shein
 */
public class CodeGenerationException extends RuntimeException
{
	public CodeGenerationException(Throwable e)
	{
		super(e);
	}

	public CodeGenerationException(String message)
	{
		super(message);
	}
}
