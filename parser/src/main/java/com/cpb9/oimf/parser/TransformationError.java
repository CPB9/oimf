package com.cpb9.oimf.parser;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class TransformationError
{
    private final TransformationErrorStatus status;
    private final String message;

    public TransformationError(@NotNull TransformationErrorStatus status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public TransformationErrorStatus getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("status", status).add("message", message).toString();
    }
}
