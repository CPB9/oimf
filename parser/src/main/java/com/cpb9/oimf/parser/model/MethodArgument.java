package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class MethodArgument
{
    private final String name;
    private final TraitApplication type;

    public MethodArgument(String name, TraitApplication type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public TraitApplication getType()
    {
        return type;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", name).add("type", type).toString();
    }
}
