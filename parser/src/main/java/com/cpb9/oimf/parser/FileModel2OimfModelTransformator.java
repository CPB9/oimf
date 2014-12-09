package com.cpb9.oimf.parser;

import com.cpb9.oimf.model.*;
import com.cpb9.oimf.parser.model.*;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Artem Shein
 */
public class FileModel2OimfModelTransformator
{
    @NotNull
    public TransformationResult<Set<ImmutableOimfTrait>> transform(@NotNull List<File> files)
    {
        TransformationResult<Set<ImmutableOimfTrait>> result = new TransformationResult<>();
        Set<ImmutableOimfTrait> traits = new HashSet<>();
        for (File file : files)
        {
            String namespaceName = file.getNamespace().getName();
            Map<String, ImmutableOimfQualifiedName> imports = processImports(result, file);
            transformTraits(file, result, ImmutableOimfQualifiedName.createFromString(namespaceName), traits, imports);
        }
        result.setValue(traits);
        return result;
    }

    private void transformTraits(@NotNull File file, @NotNull TransformationResult<?> result,
                                 @NotNull ImmutableOimfQualifiedName namespaceName,
                                 @NotNull Set<ImmutableOimfTrait> traits,
                                 @NotNull Map<String, ImmutableOimfQualifiedName> imports)
    {
        for (Trait trait : file.getTraits())
        {
            String name = trait.getName();
            if (traits.contains(name))
            {
                result.addError(String.format("conflicting names for '%s'", trait));
            }
            else
            {
                List<TraitApplication> extendsType = trait.getExtends();
                HashSet<String> excludeNames = Sets.newHashSet(trait.getArguments());
                traits.add(new ImmutableOimfTrait(makeAnnotations(trait.getAnnotations()),
                        ImmutableOimfQualifiedName.createFromString(namespaceName + "." + name),
                        ImmutableList.copyOf(trait.getArguments()),
                        makeOimfTraitApplicationsForFileTraitApplications(extendsType, namespaceName, imports,
                                excludeNames),
                        makeFields(trait.getFields(), result, namespaceName, imports, excludeNames),
                        makeMethods(trait.getMethods(), result, namespaceName, imports, excludeNames)));
            }
        }
    }

    @NotNull
    private List<TraitApplication> expandNames(@NotNull List<TraitApplication> traitApplications,
                                               @NotNull ImmutableOimfQualifiedName namespaceName,
                                               @NotNull Map<String, ImmutableOimfQualifiedName> imports,
                                               @NotNull Set<String> excludeNames)
    {
        List<TraitApplication> result = new ArrayList<>(traitApplications.size());
        for (TraitApplication traitApplication : traitApplications)
        {
            result.add(expandName(traitApplication, namespaceName, imports, excludeNames));
        }
        return result;
    }

    private String expandName(String name, ImmutableOimfQualifiedName namespaceName,
                              Map<String, ImmutableOimfQualifiedName> imports)
    {
        ImmutableOimfQualifiedName importedName = imports.get(name);
        return name.contains(".") ? name :
                (importedName == null ? namespaceName.newAppend(name) : importedName).toString();
    }

    @NotNull
    private TraitApplication expandName(@NotNull TraitApplication traitApplication, @NotNull
    ImmutableOimfQualifiedName namespaceName,
                                        @NotNull Map<String, ImmutableOimfQualifiedName> imports,
                                        @NotNull Set<String> excludeNames)
    {
        return new TraitApplication(excludeNames.contains(traitApplication.getName()) ? traitApplication.getName() :
                expandName(traitApplication.getName(), namespaceName, imports),
                expandNames(traitApplication.getArguments(), namespaceName, imports, excludeNames));
    }

    @NotNull
    private ImmutableList<ImmutableOimfMethod> makeMethods(@NotNull List<Method> methods,
                                                           @NotNull TransformationResult<?> result,
                                                           @NotNull ImmutableOimfQualifiedName namespaceName,
                                                           @NotNull Map<String, ImmutableOimfQualifiedName> imports,
                                                           @NotNull Set<String> excludeNames)
    {
        List<ImmutableOimfMethod> resultCommands = new ArrayList<>(methods.size());
        for (Method method : methods)
        {
            resultCommands
                    .add(new ImmutableOimfMethod(method.getName(),
                            makeOimfTraitApplicationForFileTraitApplication(
                                    expandName(method.getReturnType(), namespaceName, imports, excludeNames),
                                    namespaceName, imports, excludeNames),
                            makeMethodArguments(method.getArguments(), result, namespaceName, imports, excludeNames)));
        }
        return ImmutableList.copyOf(resultCommands);
    }

    @NotNull
    private ImmutableList<ImmutableOimfField> makeFields(@NotNull List<Field> fields,
                                                           @NotNull TransformationResult<?> result,
                                                           @NotNull ImmutableOimfQualifiedName namespaceName,
                                                           @NotNull Map<String, ImmutableOimfQualifiedName> imports,
                                                           @NotNull Set<String> excludeNames)
    {
        List<ImmutableOimfField> resultFields = new ArrayList<>(fields.size());
        for (Field field : fields)
        {
            resultFields
                    .add(new ImmutableOimfField(field.getName(), makeOimfTraitApplicationForFileTraitApplication(
                            expandName(field.getType(), namespaceName, imports, excludeNames), namespaceName,
                            imports, excludeNames)));
        }
        return ImmutableList.copyOf(resultFields);
    }

    @NotNull
    private ImmutableList<ImmutableOimfMethodArgument> makeMethodArguments(@NotNull List<MethodArgument> arguments,
                                                                           @NotNull TransformationResult<?> result,
                                                                           @NotNull
                                                                           ImmutableOimfQualifiedName namespaceName,
                                                                           @NotNull
                                                                           Map<String, ImmutableOimfQualifiedName> imports,
                                                                           @NotNull Set<String> excludeNames)
    {
        List<ImmutableOimfMethodArgument> args = new ArrayList<>();
        for (MethodArgument arg : arguments)
        {
            args.add(new ImmutableOimfMethodArgument(
                    makeOimfTraitApplicationForFileTraitApplication(
                            arg.getType(),
                            namespaceName, imports, excludeNames),
                    arg.getName()));
        }
        return ImmutableList.copyOf(args);
    }

    @NotNull
    private ImmutableOimfTraitApplication makeOimfTraitApplicationForFileTraitApplication(
            @NotNull TraitApplication traitApplication,
            @NotNull
            ImmutableOimfQualifiedName namespaceName,
            @NotNull Map<String, ImmutableOimfQualifiedName> imports,
            @NotNull Set<String> excludeNames)
    {
        String name = traitApplication.getName();
        return new ImmutableOimfTraitApplication(ImmutableOimfQualifiedName.createFromString(excludeNames.contains(name)
                ? name : expandName(name, namespaceName, imports)),
                makeOimfTraitApplicationsForFileTraitApplications(
                        traitApplication.getArguments(), namespaceName, imports, excludeNames));
    }

    @NotNull
    private ImmutableList<ImmutableOimfTraitApplication> makeOimfTraitApplicationsForFileTraitApplications(
            @NotNull List<TraitApplication> traitApplications, @NotNull ImmutableOimfQualifiedName namespaceName,
            @NotNull Map<String, ImmutableOimfQualifiedName> imports, @NotNull Set<String> excludeNames)
    {
        List<ImmutableOimfTraitApplication> result = new ArrayList<>(traitApplications.size());
        for (TraitApplication traitApplication : traitApplications)
        {
            result.add(new ImmutableOimfTraitApplication(
                    ImmutableOimfQualifiedName.createFromString(
                            expandName(traitApplication, namespaceName, imports, excludeNames).getName()),
                    makeOimfTraitApplicationsForFileTraitApplications(
                            traitApplication.getArguments(), namespaceName, imports, excludeNames)));
        }
        return ImmutableList.copyOf(result);
    }

    @NotNull
    private ImmutableMap<String, ImmutableOimfAnnotation> makeAnnotations(@NotNull Map<String, Annotation> annotations)
    {
        Map<String, ImmutableOimfAnnotation> result =
                new HashMap<>(Preconditions.checkNotNull(annotations, "annotations").size());
        for (Map.Entry<String, Annotation> annotationEntry : annotations.entrySet())
        {
            Annotation annotation = annotationEntry.getValue();
            result.put(annotationEntry.getKey(), new ImmutableOimfAnnotation(annotation.getName(),
                    Optional.fromNullable(annotation.getValue())));
        }
        return ImmutableMap.copyOf(result);
    }

    @NotNull
    private Map<String, ImmutableOimfQualifiedName> processImports(@NotNull TransformationResult<?> result,
                                                                   @NotNull File file)
    {
        Map<String, ImmutableOimfQualifiedName> imports = new HashMap<>();
        for (Import anImport : file.getImports())
        {
            List<String> multipleImports = anImport.getMultipleImports();
            if (multipleImports.isEmpty())
            {
                appendImport(imports, ImmutableOimfQualifiedName.createFromString(anImport.getPrefix()), result);
            }
            else
            {
                for (String tail : multipleImports)
                {
                    appendImport(imports, ImmutableOimfQualifiedName.createFromString(anImport.getPrefix()).newAppend(
                            tail), result);
                }
            }
        }
        return imports;
    }

    private void appendImport(@NotNull Map<String, ImmutableOimfQualifiedName> imports, @NotNull
    ImmutableOimfQualifiedName anImport,
                              @NotNull TransformationResult<?> result)
    {
        String name = anImport.getLast().get();
        ImmutableOimfQualifiedName _import = imports.get(name);
        if (_import != null && !_import.equals(anImport))
        {
            result.addError(String.format("Name conflict '%s' and '%s'", _import, anImport));
            return;
        }
        imports.put(name, anImport);
    }
}
