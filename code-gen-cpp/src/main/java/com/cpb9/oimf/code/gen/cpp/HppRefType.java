package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppRefType implements HppType
{
    private final HppType baseType;

    public HppRefType(@NotNull HppType baseType)
    {
        this.baseType = baseType;
    }

    @Override
    public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
            throws IOException
    {
        baseType.generate(appendable, config);
        appendable.append("&");
    }
}
