package com.cpb9.oimf.model;

import java.util.List;

/**
 * @author Artem Shein
 */
public class ImmutableOimfListValue extends ImmutableOimfValue
{
    private final List<ImmutableOimfValue> value;

    public ImmutableOimfListValue(List<ImmutableOimfValue> value)
    {
        this.value = value;
    }
}
