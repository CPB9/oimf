package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class Method
{
    private final String name;
    private final TraitApplication returnType;
    private List<MethodArgument> arguments = new ArrayList<>();

    public Method(String name, TraitApplication retType)
    {
        this.name = name;
        this.returnType = retType;
    }

    public boolean setArguments(List<MethodArgument> arguments)
    {
        this.arguments = arguments;
        return true;
    }

    public String getName()
    {
        return name;
    }

    public List<MethodArgument> getArguments()
    {
        return arguments;
    }

    public TraitApplication getReturnType()
    {
        return returnType;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", name)
                .add("arguments", arguments).toString();
    }
}
