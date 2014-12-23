package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public class MethodArgument extends Annotated
{
    private final String name;
    private final TraitApplication type;

    public MethodArgument(@NotNull List<Annotation> annotations, String name, TraitApplication type)
    {
        super(annotations);
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
