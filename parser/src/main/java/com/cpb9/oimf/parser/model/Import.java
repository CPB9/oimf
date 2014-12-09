package com.cpb9.oimf.parser.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * File import model element.
 *
 * @author Artem Shein
 */
public class Import
{
    private final String prefix;
    private List<String> multipleImports = new ArrayList<>();

    public Import(String prefix)
    {
        this.prefix = prefix;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public boolean setMultipleImports(@NotNull List<String> multipleImports)
    {
        this.multipleImports = multipleImports;
        return true;
    }

    public List<String> getMultipleImports()
    {
        return multipleImports;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("prefix", prefix)
                .add("multipleImports", multipleImports).toString();
    }
}
