package com.cpb9.oimf.model;

/**
 * @author Artem Shein
 */
public class ImmutableOimfIntegerValue extends ImmutableOimfValue
{
    private final Integer value;

    public ImmutableOimfIntegerValue(Integer value)
    {
        this.value = value;
    }
}
