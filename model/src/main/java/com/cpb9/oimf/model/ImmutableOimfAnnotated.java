package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Artem Shein
 */
abstract public class ImmutableOimfAnnotated
{
    protected final ImmutableList<ImmutableOimfAnnotation> annotations;

    protected ImmutableOimfAnnotated(@NotNull ImmutableList<ImmutableOimfAnnotation> annotations)
    {
        this.annotations = annotations;
    }

    @NotNull
    public ImmutableList<ImmutableOimfAnnotation> getAnnotations()
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

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("annotations", annotations).toString();
    }
}
