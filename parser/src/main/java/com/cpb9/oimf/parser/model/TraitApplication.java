package com.cpb9.oimf.parser.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public class TraitApplication
{
    @NotNull
    private List<TraitApplication> arguments;
    @NotNull
    private String name;

    public TraitApplication(@NotNull String name, @NotNull List<TraitApplication> arguments)
    {
        this.name = name;
        this.arguments = arguments;
    }

    @NotNull
    public List<TraitApplication> getArguments()
    {
        return arguments;
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    public void setName(@NotNull String name)
    {
        this.name = name;
    }
}
