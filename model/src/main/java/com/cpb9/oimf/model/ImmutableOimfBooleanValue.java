package com.cpb9.oimf.model;

/**
 * @author Artem Shein
 */
public class ImmutableOimfBooleanValue extends ImmutableOimfValue
{
    public static final ImmutableOimfBooleanValue TRUE = new ImmutableOimfBooleanValue();
    public static final ImmutableOimfBooleanValue FALSE = new ImmutableOimfBooleanValue();

    private ImmutableOimfBooleanValue()
    {
    }
}
