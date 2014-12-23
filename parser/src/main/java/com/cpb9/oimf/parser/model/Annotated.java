package com.cpb9.oimf.parser.model;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @author Artem Shein
 */
public class Annotated
{
    @NotNull
    protected List<Annotation> annotations;

    protected Annotated(@NotNull List<Annotation> annotations)
    {
        this.annotations = Preconditions.checkNotNull(annotations, "annotations");
    }

    @NotNull
    public List<Annotation> getAnnotations()
    {
        return annotations;
    }

    public void setAnnotations(@NotNull List<Annotation> annotations)
    {
        this.annotations = annotations;
    }
}
