package com.cpb9.oimf.model;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Artem Shein
 */
abstract public class ImmutableOimfAnnotated
{
    protected final ImmutableMap<String, ImmutableOimfAnnotation> annotations;

    protected ImmutableOimfAnnotated(@NotNull ImmutableMap<String, ImmutableOimfAnnotation> annotations)
    {
        this.annotations = annotations;
    }

    @NotNull
    public ImmutableMap<String, ImmutableOimfAnnotation> getAnnotations()
    {
        return annotations;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof ImmutableOimfAnnotated))
        {
            return false;
        }
        ImmutableOimfAnnotated other = (ImmutableOimfAnnotated) o;
        return this == other || Objects.equals(annotations, other.annotations);
    }

    @Override
    public int hashCode()
    {
        return annotations.hashCode();
    }
}
