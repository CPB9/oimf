package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class Annotation
{
    private final String name;
    private Object value;

    public Annotation(String name)
    {
        this.name = name;
    }

    public boolean setValue(Object value)
    {
        this.value = value;
        return true;
    }

    public String getName()
    {
        return name;
    }

    public Object getValue()
    {
        return value;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", name).add("value", value).toString();
    }
}
