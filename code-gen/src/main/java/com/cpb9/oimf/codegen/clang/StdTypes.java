package com.cpb9.oimf.codegen.clang;

/**
 * @author Artem Shein
 */
public class StdTypes
{
	public static final SimpleCType UINT8_T = new SimpleCType("uint8_t");
	public static final SimpleCType UINT64_T = new SimpleCType("uint64_t");
	public static final SimpleCType INT64_T = new SimpleCType("int64_t");
	public static final SimpleCType UINT32_T = new SimpleCType("uint32_t");
	public static final SimpleCType INT32_T = new SimpleCType("int32_t");
	public static final SimpleCType UINT16_T = new SimpleCType("uint16_t");
	public static final SimpleCType INT16_T = new SimpleCType("int16_t");
	public static final SimpleCType INT8_T = new SimpleCType("int8_t");
	public static final SimpleCType VOID = new SimpleCType("void");
	public static final SimpleCType GUID = new SimpleCType("oimf_guid");
	public static final SimpleCType FLOAT64_T = new SimpleCType("float64_t");
	public static final SimpleCType FLOAT32_T = new SimpleCType("float32_t");
}
