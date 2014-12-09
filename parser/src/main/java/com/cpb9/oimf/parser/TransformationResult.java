package com.cpb9.oimf.parser;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class TransformationResult<T>
{
    private T value;
    @NotNull
    private List<TransformationError> errors = new ArrayList<>();

    public TransformationResult()
    {
    }

    public TransformationResult(T value)
    {
        this.value = value;
    }

    public boolean isSuccess()
    {
        for (TransformationError error : errors)
        {
            if (error.getStatus().equals(TransformationErrorStatus.ERROR))
            {
                return false;
            }
        }
        return true;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public void addError(String message)
    {
        errors.add(new TransformationError(TransformationErrorStatus.ERROR, message));
    }

    @NotNull
    public List<TransformationError> getErrors()
    {
        return errors;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("value", value).add("errors", errors).toString();
    }

    public void addError(String msg, Object... args)
    {
        addError(String.format(msg, args));
    }
}
