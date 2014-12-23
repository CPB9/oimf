package com.cpb9.oimf.parser.model;

import com.cpb9.oimf.model.ImmutableOimfValue;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class Annotation
{
    @NotNull
    private final TraitApplication traitApplication;
    @NotNull
    private Optional<ImmutableOimfValue> value;

    public Annotation(@NotNull TraitApplication traitApplication, @NotNull Optional<ImmutableOimfValue> value)
    {
        this.traitApplication = traitApplication;
        this.value = value;
    }

    public void setValue(Optional<ImmutableOimfValue> value)
    {
        this.value = value;
    }

    @NotNull
    public Optional<ImmutableOimfValue> getValue()
    {
        return value;
    }

    @NotNull
    public TraitApplication getTraitApplication()
    {
        return traitApplication;
    }

    @NotNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("traitApplication", traitApplication).add("value", value).toString();
    }
}
