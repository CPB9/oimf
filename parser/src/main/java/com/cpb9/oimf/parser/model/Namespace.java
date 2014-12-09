package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

/**
 * File namespace model element.
 *
 * @author Artem Shein
 */
public class Namespace
{

    private final String name;

    public Namespace(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).addValue(name).toString();
    }
}
