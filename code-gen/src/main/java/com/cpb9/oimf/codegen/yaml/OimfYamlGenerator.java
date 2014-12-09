package com.cpb9.oimf.codegen.yaml;

import com.cpb9.oimf.codegen.CodeGenerationException;
import com.cpb9.oimf.model.*;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author Artem Shein
 */
public class OimfYamlGenerator
{
    private final OimfYamlGeneratorConfiguration config;
    private final Set<ImmutableOimfTrait> traits;

    public OimfYamlGenerator(@NotNull OimfYamlGeneratorConfiguration config, @NotNull Set<ImmutableOimfTrait> traits)
    {
        this.config = config;
        this.traits = traits;
    }

    public void generate()
    {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setPrettyFlow(true);
        Yaml yaml = new Yaml(dumperOptions);
        List<Object> result = new ArrayList<>();
        for (ImmutableOimfTrait trait : traits)
        {
            generateTrait(trait, result);
        }
        try (Writer writer = new OutputStreamWriter(System.out))
        {

            yaml.dump(result, writer);
        }
        catch (IOException e)
        {
            throw new CodeGenerationException(e);
        }
    }

    private void generateTrait(@NotNull ImmutableOimfTrait trait, @NotNull List<Object> result)
    {
        Map<String, Object> traitResult = new LinkedHashMap<>();
        traitResult.put("guid", trait.getGuid().toString());
        if (!trait.getArguments().isEmpty())
        {
            traitResult.put("arguments", trait.getArguments());
        }
        ImmutableList<ImmutableOimfTraitApplication> _extends = trait.getExtends();
        if (!_extends.isEmpty())
        {
            List<Object> traitExtendsResult = new ArrayList<>(_extends.size());
            for (ImmutableOimfTraitApplication extendsTrait : _extends)
            {
                traitExtendsResult.add(generateTraitApplication(extendsTrait));
            }
            traitResult.put("extends", traitExtendsResult);
        }
        ImmutableList<ImmutableOimfMethod> methods = trait.getMethods();
        if (!methods.isEmpty())
        {
            List<Object> methodsResult = new ArrayList<>(methods.size());
            for (ImmutableOimfMethod method : methods)
            {
                methodsResult.add(generateMethod(method));
            }
            traitResult.put("methods", methodsResult);
        }
        ImmutableList<ImmutableOimfField> fields = trait.getFields();
        if (!fields.isEmpty())
        {
            List<Object> fieldsResult = new ArrayList<>(fields.size());
            for (ImmutableOimfField field : fields)
            {
                fieldsResult.add(generateField(field));
            }
            traitResult.put("fields", fieldsResult);
        }
        result.add(traitResult);
    }

    private Map<String, Object> generateMethod(@NotNull ImmutableOimfMethod method)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", method.getName());
        result.put("returnType", generateTraitApplication(method.getReturnType()));
        if (!method.getArguments().isEmpty())
        {
            List<Object> methodArgs = new ArrayList<>(method.getArguments().size());
            for (ImmutableOimfMethodArgument arg : method.getArguments())
            {
                methodArgs.add(generateMethodArgument(arg));
            }
            result.put("arguments", methodArgs);
        }
        return result;
    }

    private Map<String, Object> generateField(@NotNull ImmutableOimfField field)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", field.getName());
        result.put("returnType", generateTraitApplication(field.getType()));
        return result;
    }

    private Map<String, Object> generateMethodArgument(@NotNull ImmutableOimfMethodArgument arg)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", arg.getName());
        result.put("type", generateTraitApplication(arg.getType()));
        return result;
    }

    private Map<String, Object> generateTraitApplication(@NotNull ImmutableOimfTraitApplication traitApplication)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", traitApplication.getName().toString());
        if (!traitApplication.getArguments().isEmpty())
        {
            List<Object> arguments = new ArrayList<>(traitApplication.getArguments().size());
            for (ImmutableOimfTraitApplication arg : traitApplication.getArguments())
            {
                arguments.add(generateTraitApplication(arg));
            }
            result.put("arguments", arguments);
        }
        return result;
    }
}
