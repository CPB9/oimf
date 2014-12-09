package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class File
{
    private final Namespace namespace;

    private List<Import> imports;
    @NotNull
    private List<Trait> traits = new ArrayList<>();

    public File(Namespace namespace)
    {
        this.namespace = namespace;
    }

    public Namespace getNamespace()
    {
        return namespace;
    }

    public boolean setImports(@NotNull List<Import> imports)
    {
        this.imports = imports;
        return true;
    }

    public List<Import> getImports()
    {
        return imports;
    }

    public boolean addTrait(@NotNull Trait trait)
    {
        traits.add(trait);
        return true;
    }

    @NotNull
    public List<Trait> getTraits()
    {
        return traits;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("namespace", namespace).add("imports", imports)
                .add("traits", traits).toString();
    }
}
