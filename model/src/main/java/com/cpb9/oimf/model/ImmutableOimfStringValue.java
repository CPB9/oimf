package com.cpb9.oimf.model;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class ImmutableOimfStringValue extends ImmutableOimfValue
{
    @NotNull
    private final String string;

    public ImmutableOimfStringValue(@NotNull String string)
    {
        this.string = string;
    }
}
