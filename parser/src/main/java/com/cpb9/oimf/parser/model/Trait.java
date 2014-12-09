package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Artem Shein
 */
public class Trait extends AnnotatedType
{
    private final String name;
    @NotNull
    private List<TraitApplication> anExtends = new ArrayList<>();
    @NotNull
    private List<Method> methods = new ArrayList<>();
    @NotNull
    private List<String> arguments = new ArrayList<>();
    @NotNull
    private List<Field> fields = new ArrayList<>();

    public Trait(@NotNull String name)
    {
        super(new HashMap<String, Annotation>());
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public List<TraitApplication> getExtends()
    {
        return anExtends;
    }

    @NotNull
    public List<Method> getMethods()
    {
        return methods;
    }

    public List<String> getArguments()
    {
        return arguments;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", name).add("arguments", arguments)
                .add("extends", anExtends).add("fields", fields).add("methods", methods).add("annotations", annotations).toString();
    }

    public List<Field> getFields()
    {
        return fields;
    }
}
