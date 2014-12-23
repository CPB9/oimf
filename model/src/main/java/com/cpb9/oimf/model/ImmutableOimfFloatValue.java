package com.cpb9.oimf.model;

/**
 * @author Artem Shein
 */
public class ImmutableOimfFloatValue extends ImmutableOimfValue
{
    private final Float value;

    public ImmutableOimfFloatValue(float value)
    {
        this.value = value;
    }
}
