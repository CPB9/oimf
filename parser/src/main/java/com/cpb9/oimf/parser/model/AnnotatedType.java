package com.cpb9.oimf.parser.model;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Artem Shein
 */
public class AnnotatedType
{
    @NotNull
    protected Map<String, Annotation> annotations;

    protected AnnotatedType(@NotNull Map<String, Annotation> annotations)
    {
        this.annotations = Preconditions.checkNotNull(annotations, "annotations");
    }

    @NotNull
    public Map<String, Annotation> getAnnotations()
    {
        return annotations;
    }

    public boolean setAnnotations(@NotNull Map<String, Annotation> annotations)
    {
        this.annotations = annotations;
        return true;
    }
}
