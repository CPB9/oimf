package com.cpb9.oimf.code.gen.cpp;

import com.cpb9.oimf.model.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cpb9.oimf.code.gen.cpp.OimfCppGeneratorConfiguration.OwnershipStyle;

import java.io.*;
import java.util.*;

/**
 * @author Artem Shein
 */
public class OimfCppGenerator extends AbstractOimfGenerator<OimfCppGeneratorConfiguration>
{
	private static final HppEol EOL = new HppEol();
	public static final String Q_SHARED_POINTER = "QSharedPointer";
	private final Logger LOG = LoggerFactory.getLogger(OimfCppGenerator.class);
	private final boolean isQtSharedPointerOwnershipStyle;
	private final boolean isPointerOwnershipStyle;

	public OimfCppGenerator(@NotNull OimfCppGeneratorConfiguration config, @NotNull Set<ImmutableOimfTrait> traits)
	{
		super(config, traits);
		OwnershipStyle ownershipStyle = config.getOwnershipStyle();
		isQtSharedPointerOwnershipStyle = OwnershipStyle.QT_SHARED_POINTER.equals(ownershipStyle);
		isPointerOwnershipStyle = OwnershipStyle.POINTER.equals(ownershipStyle);
	}

	public void generate()
	{
		Set<ImmutableOimfTraitApplication> traitApplications = new HashSet<>();
		for (ImmutableOimfTrait trait : traits)
		{
			ImmutableOimfQualifiedName guid = trait.getGuid();
			if (trait.getArguments().isEmpty() && !config.getTraitMappings().containsKey(guid.toString()))
			{
				ImmutableOimfTraitApplication traitApplication =
						new ImmutableOimfTraitApplication(guid,
								ImmutableList.<ImmutableOimfTraitApplication>of());
				resolveDependentTraitApplicationsForTraitApplication(traitApplication, traitApplications);
			}
		}
		for (ImmutableOimfTraitApplication traitApplication : traitApplications)
		{
			generateTraitApplication(traitApplication);
		}
	}

	private void generateTraitApplication(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		LOG.debug("Generating trait application '{}'", traitApplication);
		if (config.getTraitMappings().containsKey(getTraitFor(traitApplication).getGuid().toString()))
		{
			LOG.trace("Skipping mapped trait application '{}'", traitApplication);
			return;
		}
		ImmutableOimfQualifiedName traitApplicationName = traitApplication.getName();
		ensureNamespaceDirExistsOrCreate(traitApplicationName);
		File hppFile = new File(config.getOutputPath(),
				StringUtils.join(getCombinedQualifiedNameFor(traitApplication).getParts(), "/") + ".hpp");
		ensureFileDoesNotExistOrDeleteIt(hppFile);

		LOG.debug("Generating files: '{}'", hppFile.getAbsolutePath());
		try (FileOutputStream hppStream = new FileOutputStream(hppFile);
		     OutputStreamWriter hppWriter = new OutputStreamWriter(hppStream))
		{
			CppGeneratorConfiguration codeGenConfig = new CppGeneratorConfiguration();
			hppWriter.write(config.getHppPrologue());

			List<String> extendsClassNames = resolveExtendsClassNames(traitApplication);
			List<HppClassMethod> methodDecls = resolveMethodDeclarations(traitApplication);
			methodDecls.addAll(resolveFieldDeclarations(traitApplication));

			Set<ImmutableOimfTraitApplication> dependencies = new HashSet<>();
			resolveDependenciesForTraitApplication(traitApplication, dependencies);

			Set<String> includes = makeIncludesForTraitApplications(dependencies);

			HppFile hpp = new HppFile();
			hpp.append(new HppPragma("once"));
			for (String include : includes)
			{
				hpp.append(new HppInclude(include));
			}
			hpp.append(EOL);
			hpp.append(new HppComment("External dependencies"));
			hpp.append(EOL);
			makeExternalClassesDeclarations(dependencies, hpp);
			hpp.append(EOL);
			hpp.append(new HppComment("Class declaration"));
			hpp.append(EOL);
			List<HppElement> namespaceElements = makeUsings(dependencies, traitApplicationName.newDropLast());
			if (!namespaceElements.isEmpty())
			{
				namespaceElements.add(EOL);
			}
			namespaceElements.add(new HppClassDef(getCombinedNameFor(traitApplication), extendsClassNames, methodDecls));
			hpp.append(new HppNamespaceDef(StringUtils.join(traitApplicationName.newDropLast().getParts(), "::"),
					namespaceElements));
			codeGenConfig.setAppendable(hppWriter);
			hpp.generate(hppWriter, codeGenConfig);

			hppWriter.write(config.getHppEpilogue());
		}
		catch (IOException e)
		{
			throw new CodeGenerationException(e);
		}
	}

	private List<HppElement> makeUsings(@NotNull Set<ImmutableOimfTraitApplication> traitApplications,
	                                    @NotNull ImmutableOimfQualifiedName namespace)
	{
		List<HppElement> usings = new ArrayList<>();
		for (ImmutableOimfTraitApplication traitApplication : traitApplications)
		{
			if (!traitApplication.getName().newDropLast().equals(namespace) && !isTraitMapped(traitApplication))
			{
				usings.add(new HppUsing(StringUtils.join(getCombinedQualifiedNameFor(traitApplication).getParts(), "::")));
				usings.add(EOL);
			}
		}
		return usings;
	}

	private void makeExternalClassesDeclarations(@NotNull Set<ImmutableOimfTraitApplication> traitApplications,
	                                             @NotNull HppFile hpp)
	{
		LOG.debug("External classes: {}", traitApplications);
		Multimap<String, ImmutableOimfTraitApplication> traitApplicationsByNamespace = HashMultimap.create();
		for (ImmutableOimfTraitApplication traitApplication : traitApplications)
		{
			if (!isTraitMapped(traitApplication))
			{
				traitApplicationsByNamespace.put(
						StringUtils.join(traitApplication.getName().newDropLast().getParts(), "::"), traitApplication);
			}
		}
		for (Map.Entry<String, Collection<ImmutableOimfTraitApplication>> entry : traitApplicationsByNamespace.asMap().entrySet())
		{
			Collection<ImmutableOimfTraitApplication> namespaceTraitApplications = entry.getValue();
			List<HppElement> classes = new ArrayList<>(namespaceTraitApplications.size());
			for (ImmutableOimfTraitApplication traitApplication : namespaceTraitApplications)
			{
				classes.add(new HppForwardClassDef(resolveTypeNameForTraitApplication(traitApplication)));
				classes.add(EOL);
			}
			hpp.append(new HppNamespaceDef(entry.getKey(), classes));
		}
	}

	private boolean isTraitMapped(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		return config.getTraitMappings().containsKey(traitApplication.getName().toString());
	}

	@NotNull
	private Set<String> makeIncludesForTraitApplications(@NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		Set<String> result = new HashSet<>();
		for (ImmutableOimfTraitApplication traitApplication : traitApplications)
		{
			resolveIncludeForTraitApplication(traitApplication, result);
		}
		return result;
	}

	private void resolveDependentTraitApplicationsForMethod(@NotNull ImmutableOimfMethod method,
	                                                        @NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		LOG.trace("resolveDependentTraitApplicationsForMethod('{}', '{}')",
				method, traitApplications);
		ImmutableOimfTraitApplication returnType = method.getReturnType();
		Preconditions.checkArgument(returnType.getName().size() > 1,
				"Return type mapping error for method '%s' return type '%s'",
				method, returnType.getName());
		resolveDependentTraitApplicationsForTraitApplication(returnType, traitApplications);
		for (ImmutableOimfMethodArgument arg : method.getArguments())
		{
			resolveDependentTraitApplicationsForTraitApplication(arg.getType(), traitApplications);
		}
	}

	private void resolveDependentTraitApplicationsForField(@NotNull ImmutableOimfField field,
	                                                        @NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		LOG.trace("resolveDependentTraitApplicationsForMethod('{}', '{}')",
				field, traitApplications);
		ImmutableOimfTraitApplication type = field.getType();
		Preconditions.checkArgument(type.getName().size() > 1,
				"Type mapping error for field '%s' return type '%s'", field, type.getName());
		resolveDependentTraitApplicationsForTraitApplication(type, traitApplications);
	}

	private void resolveDependentTraitApplicationsForTraitApplication(@NotNull
																	  ImmutableOimfTraitApplication traitApplication,
	                                                                  @NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		LOG.trace("resolveDependentTraitApplicationsForTraitApplication('{}', '{}')",
				traitApplication, traitApplications);
		ImmutableOimfQualifiedName traitApplicationName = traitApplication.getName();
		Preconditions.checkArgument(traitApplicationName.size() > 1, "Illegal trait name '%s'", traitApplicationName);
		if (!traitApplications.add(traitApplication))
		{
			return;
		}
		for (ImmutableOimfTraitApplication arg : traitApplication.getArguments())
		{
			resolveDependentTraitApplicationsForTraitApplication(arg, traitApplications);
		}
		ImmutableOimfTrait trait = getMappedTraitFor(traitApplication);
		for (ImmutableOimfTraitApplication traitExtends : trait.getExtends())
		{
			LOG.trace("Resolving extends trait '{}'", traitExtends.getName());
			resolveDependentTraitApplicationsForTraitApplication(traitExtends, traitApplications);
		}
		for (ImmutableOimfMethod method : trait.getMethods())
		{
			LOG.trace("Resolving method '{}' for '{}'",
					method.getName(), traitApplication);
			resolveDependentTraitApplicationsForMethod(method, traitApplications);
		}
		for (ImmutableOimfField field : trait.getFields())
		{
			LOG.trace("Resolving field '{}' for '{}'",
					field.getName(), traitApplication);
			resolveDependentTraitApplicationsForField(field, traitApplications);
		}
	}

	@NotNull
	private ImmutableOimfTrait applyArguments(@NotNull ImmutableOimfTrait trait, @NotNull
	ImmutableOimfTraitApplication traitApplication)
	{
		ImmutableList<ImmutableOimfTraitApplication> args = traitApplication.getArguments();
		Preconditions.checkArgument(trait.getArguments().size() == args.size(),
				"Trait application mismatch: '%s', '%s'", trait, traitApplication);
		if (!args.isEmpty())
		{
			ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> mappings = makeMappings(trait.getArguments(), args);
			trait = new ImmutableOimfTrait(trait.getAnnotations(), applyName(trait.getGuid(),
					args), ImmutableList.<String>of(), applyExtends(trait.getExtends(), mappings),
					applyFields(trait.getFields(), mappings), applyMethods(trait.getMethods(), mappings));
		}
		return trait;
	}

	private ImmutableList<ImmutableOimfMethod> applyMethods(@NotNull ImmutableList<ImmutableOimfMethod> methods,
															@NotNull
															ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> mappings)
	{
		if (!methods.isEmpty())
		{
			boolean isMethodsMapped = false;
			List<ImmutableOimfMethod> mappedMethods = new ArrayList<>(methods.size());
			for (ImmutableOimfMethod method : methods)
			{
				ImmutableOimfMethod mappedMethod = applyMethod(method, mappings);
				if (mappedMethod != method)
				{
					isMethodsMapped = true;
				}
				mappedMethods.add(mappedMethod);
			}
			if (isMethodsMapped)
			{
				methods = ImmutableList.copyOf(mappedMethods);
			}
		}
		return methods;
	}

	private ImmutableList<ImmutableOimfField> applyFields(@NotNull ImmutableList<ImmutableOimfField> fields,
	                                                        @NotNull ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> mappings)
	{
		if (!fields.isEmpty())
		{
			boolean isFieldsMapped = false;
			List<ImmutableOimfField> mappedFields = new ArrayList<>(fields.size());
			for (ImmutableOimfField field : fields)
			{
				ImmutableOimfField mappedField = applyField(field, mappings);
				if (mappedField != field)
				{
					isFieldsMapped = true;
				}
				mappedFields.add(mappedField);
			}
			if (isFieldsMapped)
			{
				fields = ImmutableList.copyOf(mappedFields);
			}
		}
		return fields;
	}

	private ImmutableOimfMethod applyMethod(@NotNull ImmutableOimfMethod method,
											@NotNull
											ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> mappings)
	{
		ImmutableOimfTraitApplication returnType = method.getReturnType();
		ImmutableOimfTraitApplication mappedReturnType = applyTraitApplication(returnType, mappings);
		ImmutableList<ImmutableOimfMethodArgument> arguments = method.getArguments();
		boolean isArgumentsMapped = false;
		List<ImmutableOimfMethodArgument> mappedArguments = new ArrayList<>(arguments.size());
		if (!arguments.isEmpty())
		{
			for (ImmutableOimfMethodArgument argument : arguments)
			{
				ImmutableOimfTraitApplication type = argument.getType();
				ImmutableOimfTraitApplication mappedMethodArgumentType = applyTraitApplication(type, mappings);
				if (mappedMethodArgumentType != type)
				{
					isArgumentsMapped = true;
					mappedArguments.add(new ImmutableOimfMethodArgument(mappedMethodArgumentType, argument.getName()));
				}
				else
				{
					mappedArguments.add(argument);
				}
			}
		}
		if (mappedReturnType != returnType || isArgumentsMapped)
		{
			method = new ImmutableOimfMethod(method.getName(), mappedReturnType, ImmutableList.copyOf(mappedArguments));
		}
		return method;
	}

	private ImmutableOimfField applyField(@NotNull ImmutableOimfField field,
	                                        @NotNull
	                                        ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> mappings)
	{
		ImmutableOimfTraitApplication type = field.getType();
		ImmutableOimfTraitApplication mappedType = applyTraitApplication(type, mappings);
		if (mappedType != type)
		{
			field = new ImmutableOimfField(field.getName(), mappedType);
		}
		return field;
	}

	private ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> makeMappings(@NotNull ImmutableList<String> traitArguments,
																		  @NotNull
																		  ImmutableList<ImmutableOimfTraitApplication> argumentsValues)
	{
		Map<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> result = new HashMap<>();
		UnmodifiableListIterator<String> traitArgumentsIterator = traitArguments.listIterator();
		while (traitArgumentsIterator.hasNext())
		{
			result.put(ImmutableOimfQualifiedName.createFromString(traitArgumentsIterator.next()),
					argumentsValues.get(traitArgumentsIterator.previousIndex()));
		}
		return ImmutableMap.copyOf(result);
	}

	private ImmutableList<ImmutableOimfTraitApplication> applyExtends(
			@NotNull ImmutableList<ImmutableOimfTraitApplication> traitExtends,
			@NotNull ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> mappings)
	{
		List<ImmutableOimfTraitApplication> result = new ArrayList<>(traitExtends.size());
		for (ImmutableOimfTraitApplication traitExtend : traitExtends)
		{
			result.add(applyTraitApplication(traitExtend, mappings));
		}
		return ImmutableList.copyOf(result);
	}

	private ImmutableOimfTraitApplication applyTraitApplication(@NotNull ImmutableOimfTraitApplication traitApplication,
																@NotNull
																ImmutableMap<ImmutableOimfQualifiedName, ImmutableOimfTraitApplication> mappings)
	{
		ImmutableOimfTraitApplication mappedTraitApplication = mappings.get(traitApplication.getName());
		if (mappedTraitApplication != null)
		{
			return mappedTraitApplication;
		}
		ImmutableList<ImmutableOimfTraitApplication> arguments = traitApplication.getArguments();
		if (!arguments.isEmpty())
		{
			boolean isArgumentsMapped = false;
			List<ImmutableOimfTraitApplication> mappedArguments = new ArrayList<>(arguments.size());
			for (ImmutableOimfTraitApplication argument : arguments)
			{
				ImmutableOimfTraitApplication mappedArgument = applyTraitApplication(argument, mappings);
				if (mappedArgument != argument)
				{
					isArgumentsMapped = true;
				}
				mappedArguments.add(mappedArgument);
			}
			if (isArgumentsMapped)
			{
				traitApplication = new ImmutableOimfTraitApplication(traitApplication.getName(),
						ImmutableList.copyOf(mappedArguments));
			}
		}
		return traitApplication;
	}

	@NotNull
	private ImmutableOimfQualifiedName applyName(@NotNull ImmutableOimfQualifiedName name,
												 @NotNull ImmutableList<ImmutableOimfTraitApplication> args)
	{
		assert !name.isEmpty();
		StringBuilder sb = new StringBuilder();
		for (ImmutableOimfTraitApplication arg : args)
		{
			sb.append(arg.getName().getLast().get());
		}
		sb.append(name.getLast().get());
		return name.newDropLast().newAppend(sb.toString());
	}

	private void ensureFileDoesNotExistOrDeleteIt(@NotNull File file)
	{
		Preconditions.checkState(!file.exists() || file.delete());
	}

	private void ensureNamespaceDirExistsOrCreate(@NotNull ImmutableOimfQualifiedName name)
	{
		//noinspection ResultOfMethodCallIgnored
		new File(config.getOutputPath(), StringUtils.join(name.newDropLast().getParts(), "/")).mkdirs();
	}

	private void resolveDependenciesForTraitApplication(@NotNull ImmutableOimfTraitApplication traitApplication,
	                                                    @NotNull Set<ImmutableOimfTraitApplication> dependencies)
	{
		ImmutableOimfTrait trait = getMappedTraitFor(traitApplication);
		resolveDependenciesForTraitApplications(trait.getExtends(), dependencies);
		resolveDependenciesForMethods(trait.getMethods(), dependencies);
		resolveDependenciesForFields(trait.getFields(), dependencies);
	}

	private void resolveDependenciesForMethods(@NotNull ImmutableList<ImmutableOimfMethod> methods,
	                                           @NotNull Set<ImmutableOimfTraitApplication> dependencies)
	{
		for (ImmutableOimfMethod method : methods)
		{
			resolveDependenciesForMethod(method, dependencies);
		}
	}

	private void resolveDependenciesForFields(@NotNull ImmutableList<ImmutableOimfField> fields,
	                                           @NotNull Set<ImmutableOimfTraitApplication> dependencies)
	{
		for (ImmutableOimfField field : fields)
		{
			resolveDependenciesForField(field, dependencies);
		}
	}

	private void resolveDependenciesForMethod(@NotNull ImmutableOimfMethod method,
	                                          @NotNull Set<ImmutableOimfTraitApplication> dependencies)
	{
		dependencies.add(method.getReturnType());
		for (ImmutableOimfMethodArgument arg : method.getArguments())
		{
			dependencies.add(arg.getType());
		}
	}

	private void resolveDependenciesForField(@NotNull ImmutableOimfField field,
	                                          @NotNull Set<ImmutableOimfTraitApplication> dependencies)
	{
		dependencies.add(field.getType());
	}

	private void resolveIncludeForTraitApplication(@NotNull ImmutableOimfTraitApplication traitApplication,
	                                               @NotNull Set<String> includes)
	{
		ImmutableOimfQualifiedName name = getCombinedQualifiedNameFor(traitApplication);
		String mapping = config.getTraitMappings().get(name.toString());
		if (mapping == null || mapping.contains("/"))
		{
			if (mapping == null)
			{
				includes.add(StringUtils.join(name.getParts(), "/") + ".hpp");
				if (isQtSharedPointerOwnershipStyle)
				{
					includes.add(Q_SHARED_POINTER);
				}
			}
			else
			{
				includes.add(mapping.split("/")[0]);
			}
		}
	}

	private void resolveDependenciesForTraitApplications(@NotNull ImmutableList<ImmutableOimfTraitApplication> traitApplications,
	                                                     @NotNull Set<ImmutableOimfTraitApplication> dependencies)
	{
		for (ImmutableOimfTraitApplication traitApplication : traitApplications)
		{
			dependencies.add(traitApplication);
			resolveDependenciesForTraitApplication(traitApplication, dependencies);
		}
	}

	private List<HppClassMethod> resolveMethodDeclarations(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		List<HppClassMethod> result = new ArrayList<>();
		ImmutableOimfTrait originalTrait = getTraitFor(traitApplication);
		ImmutableOimfTrait mappedTrait = applyArguments(originalTrait, traitApplication);
		ImmutableOimfQualifiedName guid = originalTrait.getGuid();
		String guidAsString = guid.toString();
		boolean isAs = guidAsString.equals("oimf.As");
		if (guidAsString.equals("oimf.From"))
		{
//			result.add(new HppClassMethodDecl(getCombinedNameFor(traitApplication), HppSimpleType.OMMITED_TYPE,
//					Lists.newArrayList(new HppFuncArgument(originalTrait.getMethods().get(0).getName(),
//							makeTypeForTraitApplication(mappedTrait.getMethods().get(0).getArguments().get(0).getType()))),
//					HppClassMethodVisibility.PUBLIC));
		}
		else
		{
			for (ImmutableOimfMethod method : mappedTrait.getMethods())
			{
				List<HppFuncArgument> args = new ArrayList<>();
				for (ImmutableOimfMethodArgument arg : method.getArguments())
				{
					args.add(new HppFuncArgument(arg.getName(),
							makeTypeForTraitApplication(arg.getType())));
				}
				ImmutableOimfTraitApplication returnType = method.getReturnType();
				result.add(new HppClassMethodDecl(isAs ? method.getName() + resolveTypeNameForTraitApplication(
						returnType) : method.getName(),
						makeTypeForTraitApplication(returnType),
						args, HppClassMethodVisibility.PUBLIC, true, true));
			}
		}
		result.add(new HppClassMethodImpl("~" + getCombinedNameFor(traitApplication),
				HppSimpleType.OMMITED_TYPE,
				new ArrayList<HppFuncArgument>(), HppClassMethodVisibility.PUBLIC, true, new ArrayList<HppElement>()));
		return result;
	}

	private List<HppClassMethod> resolveFieldDeclarations(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		List<HppClassMethod> result = new ArrayList<>();
		ImmutableOimfTrait originalTrait = getTraitFor(traitApplication);
		ImmutableOimfTrait mappedTrait = applyArguments(originalTrait, traitApplication);
		ImmutableOimfQualifiedName guid = originalTrait.getGuid();
		String guidAsString = guid.toString();
		boolean isAs = guidAsString.equals("oimf.As");
		if (guidAsString.equals("oimf.From"))
		{
			//			result.add(new HppClassMethodDecl(getCombinedNameFor(traitApplication), HppSimpleType.OMMITED_TYPE,
			//					Lists.newArrayList(new HppFuncArgument(originalTrait.getMethods().get(0).getName(),
			//							makeTypeForTraitApplication(mappedTrait.getMethods().get(0).getArguments().get(0).getType()))),
			//					HppClassMethodVisibility.PUBLIC));
		}
		else
		{
			for (ImmutableOimfField field : mappedTrait.getFields())
			{
				ImmutableOimfTraitApplication returnType = field.getType();
				result.add(new HppClassMethodDecl(
						isAs ? field.getName() + resolveTypeNameForTraitApplication(returnType) : field.getName(),
						makeTypeForTraitApplication(returnType), new ArrayList<HppFuncArgument>(), HppClassMethodVisibility.PUBLIC, true, true));
			}
		}
		return result;
	}

	private HppType makeTypeForTraitApplication(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		ImmutableOimfQualifiedName combinedQualifiedName = getCombinedQualifiedNameFor(traitApplication);
		String mapping = config.getTraitMappings().get(combinedQualifiedName.toString());
		if (mapping == null)
		{
			HppSimpleType type = new HppSimpleType(combinedQualifiedName.getLast().get());
			return isQtSharedPointerOwnershipStyle ? new HppTemplateType(Q_SHARED_POINTER,
					Arrays.asList(type)) : new HppPointerType(type);
		}
		else
		{
			return new HppSimpleType(mapping.contains("/") ? mapping.split("/")[1] : mapping);
		}
	}

	@NotNull
	private ImmutableOimfTrait getMappedTraitFor(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		return applyArguments(getTraitFor(traitApplication), traitApplication);
	}

	@NotNull
	private String resolveTypeNameForTraitApplication(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		ImmutableOimfQualifiedName combinedQualifiedName = getCombinedQualifiedNameFor(traitApplication);
		String mapping = config.getTraitMappings().get(combinedQualifiedName.toString());
		return mapping == null
				? combinedQualifiedName.getLast().get()
				: (mapping.contains("/") ? mapping.split("/")[1] : mapping);
	}

	@NotNull
	private List<String> resolveExtendsClassNames(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		List<String> result = new ArrayList<>();
		ImmutableOimfTrait trait = getTraitFor(traitApplication);
		Map<String, ImmutableOimfTraitApplication> traitMappings = getTraitMappingsFor(traitApplication);
		for (ImmutableOimfTraitApplication extendsTrait
				: trait.getExtends())
		{
			result.add(getShortCombinedNameFor(extendsTrait, traitMappings));
		}
		return result;
	}

	@NotNull
	private Map<String, ImmutableOimfTraitApplication> getTraitMappingsFor(@NotNull
																			   ImmutableOimfTraitApplication traitApplication)
	{
		ImmutableOimfQualifiedName traitApplicationName = traitApplication.getName();
		Preconditions.checkArgument(traitApplicationName.size() > 1, "Illegal trait name '{}'", traitApplicationName);
		Map<String, ImmutableOimfTraitApplication> result = new HashMap<>();
		List<ImmutableOimfTraitApplication> traitApplicationArguments = traitApplication.getArguments();
		ImmutableOimfTrait trait = getTraitFor(traitApplication);
		for (ListIterator<String> argsIterator = trait.getArguments().listIterator(); argsIterator.hasNext();)
		{
			result.put(argsIterator.next(), traitApplicationArguments.get(argsIterator.previousIndex()));
		}
		return result;
	}

	@NotNull
	private ImmutableOimfTrait getTraitFor(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		ImmutableOimfQualifiedName traitApplicationName = traitApplication.getName();
		Preconditions.checkArgument(traitApplicationName.size() > 1, "Illegal trait name");
		return Preconditions.checkNotNull(traitByName.get(traitApplicationName),
				"Trait not found '%s' in: %s", traitApplicationName, traitByName);
	}

	@NotNull
	private static ImmutableOimfQualifiedName getCombinedQualifiedNameFor(
			@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		ImmutableOimfQualifiedName traitApplicationName = traitApplication.getName();
		StringBuilder sb = new StringBuilder();
		for (ImmutableOimfTraitApplication arg : traitApplication.getArguments())
		{
			sb.append(getCombinedNameFor(arg));
		}
		sb.append(traitApplicationName.getLast().get());
		return traitApplicationName.newDropLast().newAppend(sb.toString());
	}

	@NotNull
	private static String getCombinedNameFor(@NotNull ImmutableOimfTraitApplication traitApplication)
	{
		ImmutableOimfQualifiedName traitApplicationName = traitApplication.getName();
		StringBuilder sb = new StringBuilder();
		for (ImmutableOimfTraitApplication arg : traitApplication.getArguments())
		{
			sb.append(getCombinedNameFor(arg));
		}
		sb.append(traitApplicationName.getLast().get());
		return sb.toString();
	}

	@NotNull
	private static String getShortCombinedNameFor(@NotNull ImmutableOimfTraitApplication traitApplication,
	                                              @NotNull Map<String, ImmutableOimfTraitApplication> traitMappings)
	{
		ImmutableOimfQualifiedName traitApplicationName = traitApplication.getName();
		StringBuilder sb = new StringBuilder();
		List<ImmutableOimfTraitApplication> arguments = traitApplication.getArguments();
		if (!arguments.isEmpty())
		{
			for (ImmutableOimfTraitApplication arg : arguments)
			{
				sb.append(getShortCombinedNameFor(arg, traitMappings));
			}
		}
		sb.append(traitApplicationName.size() == 1
				? getShortCombinedNameFor(traitMappings.get(traitApplicationName.getLast().get()), traitMappings)
				: traitApplicationName.getLast().get());
		return sb.toString();
	}

}
