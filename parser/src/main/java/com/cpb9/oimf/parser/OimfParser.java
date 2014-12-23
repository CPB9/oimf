package com.cpb9.oimf.parser;

import com.cpb9.oimf.model.*;
import com.cpb9.oimf.parser.model.*;
import com.google.common.base.*;
import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.support.Var;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author Artem Shein
 */
@BuildParseTree
public class OimfParser extends BaseParser<Object>
{
    public Rule File()
    {
        Var<File> file = new Var<>();
        return Sequence(Namespace(),
                file.set(new File((Namespace) pop())),
                Optional(Imports(),
                        file.get().setImports((List<Import>) pop())),
                ZeroOrMore(Optional(ExtendedWhitespaces()),
                        Sequence(Trait(),
                                file.get().addTrait((Trait) pop()))),
                Optional(ExtendedWhitespaces()), EOI, push(file.get()));
    }

    // Imports

    Rule Imports()
    {
        Var<List<Import>> list = new Var<List<Import>>(new ArrayList<Import>());
        return Sequence(ZeroOrMore(Import(),
                        list.get().add((Import) pop())),
                push(list.get()));
    }

    Rule Import()
    {
        return Sequence(Optional(ExtendedWhitespaces()), "import", Whitespaces(), QualifiedIdent(),
                push(new Import(match())),
                Optional(MultipleImportsBlock(),
                        ((Import) peek(1)).setMultipleImports((List<String>) pop())), StmtEnd());
    }

    Rule MultipleImportsBlock()
    {
        Var<List<String>> list = new Var<>();
        return Sequence(".{",
                list.set(new ArrayList<String>()),
                Optional(ExtendedWhitespaces()),
                Ident(),
                list.get().add(match()),
                ZeroOrMore(Optional(ExtendedWhitespaces()), ',', Optional(ExtendedWhitespaces()), Ident(),
                        list.get().add(match())),
                Optional(ExtendedWhitespaces()), '}',
                push(list.get()));
    }

    Rule Namespace()
    {
        return Sequence(Optional(ExtendedWhitespaces()), "namespace",
                Whitespaces(), QualifiedIdent(),
                push(new Namespace(match())), StmtEnd());
    }

    // Annotations

    Rule Annotations(Var<List<Annotation>> annotationsVar)
    {
        return Sequence("@", Annotation(annotationsVar),
                ZeroOrMore(Optional(ExtendedWhitespaces()), '@',
                        Annotation(annotationsVar)));
    }

    Rule Annotation(Var<List<Annotation>> annotationsVar)
    {
        Var<ImmutableOimfValue> valueVar = new Var<>();
        return Sequence(TraitApplication(), Optional(Whitespaces()), '(',
                Optional(ExtendedWhitespaces()),
                Optional(Value(), valueVar.set((ImmutableOimfValue) pop())),
                Optional(ExtendedWhitespaces()), ')', listAdd(annotationsVar.get(), new Annotation((TraitApplication) pop(), Optional.fromNullable(valueVar.get()))));
    }

    Rule Value()
    {
        return FirstOf(BooleanValue(), StringValue(), FloatValue(),
                IntegerValue(), ListValue(), MapValue());
    }

    Rule StringValue()
    {
        return Sequence('\"', ZeroOrMore(TestNot('\"'), ANY), push(new ImmutableOimfStringValue(match())), '\"');
    }

    Rule BooleanValue()
    {
        return FirstOf(Sequence("true", push(ImmutableOimfBooleanValue.TRUE)),
                Sequence("false", push(ImmutableOimfBooleanValue.FALSE)));
    }

    Rule BigInteger()
    {
        return FirstOf(Sequence(AnyOf("+-"), push(match()), Number(), push(new BigInteger(pop() + match()))),
                Sequence(Number(), push(new BigInteger(match()))));
    }

    Rule IntegerValue()
    {
        return Sequence(Integer(),
                push(new ImmutableOimfIntegerValue((Integer) pop())));
    }

    Rule Integer()
    {
        return FirstOf(Sequence(AnyOf("+-"), push(match()), Number(), push(Integer.parseInt(pop() + match()))),
                Sequence(Number(), tryParseInt(match())));
    }

    boolean tryParseInt(String str)
    {
        try
        {
            push(Integer.parseInt(str));
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    Rule FloatValue()
    {
        return Sequence(Integer(), '.', Number(), push(match()),
                Optional(AnyOf("eE"), push(match()), Integer(), push((String) pop(2) + pop(1) + pop())),
                push(new ImmutableOimfFloatValue(Float.parseFloat(pop(1) + "." + pop()))));
    }

    Rule BigDecimal()
    {
        return Sequence(Sequence(Optional(AnyOf("+-")), Number(), Optional('.', Number(),
                Optional(AnyOf("eE"), Optional(AnyOf("+-")), Number()))), push(new BigDecimal(match())));
    }

    Rule ListValue()
    {
        Var<List<ImmutableOimfValue>> list = new Var<>();
        return Sequence('[',
                list.set(new ArrayList<ImmutableOimfValue>()),
                Optional(ExtendedWhitespaces()),
                Optional(Value(),
                        listAdd(list.get(), (ImmutableOimfValue) pop()),
                        ZeroOrMore(',', Optional(ExtendedWhitespaces()), Value(), list.get().add((ImmutableOimfValue) pop()))),
                Optional(ExtendedWhitespaces()),
                ']', push(new ImmutableOimfListValue(list.get())));
    }

    Rule MapValue()
    {
        Var<Map<String, ImmutableOimfValue>> map = new Var<>();
        return Sequence('{',
                map.set(new LinkedHashMap<String, ImmutableOimfValue>()),
                Optional(MapValueElement(),
                        mapPut(map.get(), (String) pop(1), (ImmutableOimfValue) pop()),
                        ZeroOrMore(',', MapValueElement(),
                                mapPut(map.get(), (String) pop(1), (ImmutableOimfValue) pop()))),
                '}', push(map.get()));
    }

    protected <T> boolean mapPut(Map<String, T> map, String key, T value)
    {
        map.put(key, value);
        return true;
    }

    Rule MapValueElement()
    {
        return Sequence(Ident(), push(match()),
                Optional(Whitespaces()), ':', Optional(ExtendedWhitespaces()), Value());
    }

    @NotNull
    Rule Indent()
    {
        return OneOrMore(' ');
    }

    // Trait

    Rule Trait()
    {
        Var<Trait> traitVar = new Var<>();
        Var<List<Annotation>> annotationsVar = new Var<List<Annotation>>(new ArrayList<Annotation>());
        return Sequence(Optional(Annotations(annotationsVar), ExtendedWhitespaces()), "trait", Whitespaces(),
                Ident(), traitVar.set(new Trait(annotationsVar.get(), match())),
                Optional('[', Ident(), listAdd(traitVar.get().getArguments(), match()), ZeroOrMore(',', Whitespaces(),
                        Ident(), listAdd(traitVar.get().getArguments(), match())), ']'),
                Optional(Whitespaces(), "extends", Whitespaces(), TraitApplication(), listAdd(traitVar.get().getExtends(),
                        (TraitApplication) pop()), ZeroOrMore(',', ExtendedWhitespaces(),
                        TraitApplication(), listAdd(traitVar.get().getExtends(), (TraitApplication) pop()))),
                Optional(Whitespaces()), StmtEnd(), ZeroOrMore(FieldOrMethod(traitVar)),
                push(traitVar.get()));
    }

    Rule TraitApplication()
    {
        Var<List<TraitApplication>> traitApplicationsVar =
                new Var<List<TraitApplication>>(new ArrayList<TraitApplication>());
        return Sequence(QualifiedIdent(), push(match()),
                Optional('[', TraitApplication(),
                        listAdd(traitApplicationsVar.get(), (TraitApplication) pop()),
                        ZeroOrMore(',', Whitespaces(), TraitApplication(),
                                listAdd(traitApplicationsVar.get(), (TraitApplication) pop())),
                        ']'),
                push(new TraitApplication((String) pop(), traitApplicationsVar.get())));
    }

    protected <T> boolean listAdd(@NotNull List<T> list, @NotNull T element)
    {
        list.add(element);
        return true;
    }

    Rule FieldOrMethod(Var<Trait> traitVar)
    {
        Var<List<Annotation>> annotationsVar = new Var<List<Annotation>>(new ArrayList<Annotation>());
        Var<ImmutableOimfValue> defaultValueVar = new Var<>();
        return Sequence(Indent(), ZeroOrMore(FirstOf(Comment(), Annotation(annotationsVar)), StmtEnd(), Indent()), TraitApplication(), Whitespaces(),
                Ident(), push(match()),
                FirstOf(
                        Sequence('(', Optional(Annotations(annotationsVar), Whitespaces()), push(new Method(annotationsVar.get(), (String) pop(), (TraitApplication) pop())),
                                Optional(TraitMethodArguments(),
                                        ((Method) peek(1)).setArguments((List<MethodArgument>) pop())),
                        ')', listAdd(traitVar.get().getMethods(), (Method) pop())),
                        Sequence(Optional(Whitespaces(), "default", Whitespaces(), Value(),
                                        defaultValueVar.set((ImmutableOimfValue) pop())),
                                listAdd(traitVar.get().getFields(),
                                        new Field(annotationsVar.get(), (String) pop(), (TraitApplication) pop(), Optional.fromNullable(defaultValueVar.get()))))),
                Optional(Whitespaces()), StmtEnd());
    }

    Rule TraitMethodArguments()
    {
        Var<List<MethodArgument>> list = new Var<>();
        return Sequence(TraitMethodArgument(),
                list.set(new ArrayList<MethodArgument>()),
                list.get().add((MethodArgument) pop()),
                Optional(Whitespaces()),
                ZeroOrMore(',', Optional(Whitespaces()), TraitMethodArgument(), list.get().add((MethodArgument) pop())),
                push(list.get()));
    }

    Rule TraitMethodArgument()
    {
        Var<List<Annotation>> annotationsVar = new Var<List<Annotation>>(new ArrayList<Annotation>());
        return Sequence(Optional(Annotations(annotationsVar), Whitespaces()), TraitApplication(), Whitespaces(), Ident(),
                push(new MethodArgument(annotationsVar.get(), match(), (TraitApplication) pop())));
    }

    Rule Number()
    {
        return OneOrMore(Digit());
    }

    @SuppressNode
    Rule Digit()
    {
        return CharRange('0', '9');
    }

    Rule Ident()
    {
        return Sequence(IdentFirstChar(), ZeroOrMore(IdentInnerChar()));
    }

    Rule QualifiedIdent()
    {
        return Sequence(Ident(), ZeroOrMore(Sequence('.', Ident())));
    }

    @SuppressNode
    Rule IdentFirstChar()
    {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'));
    }

    @SuppressNode
    Rule IdentInnerChar()
    {
        return FirstOf(IdentFirstChar(), '_', Digit());
    }

    @NotNull
    @SuppressNode
    Rule Whitespaces()
    {
        return OneOrMore(FirstOf(' ', Comment()));
    }

    @NotNull
    @SuppressNode
    Rule ExtendedWhitespaces()
    {
        return OneOrMore(FirstOf(AnyOf(" \t\n\r"), Comment()));
    }

    @SuppressNode
    Rule Comment()
    {
        return Sequence('#', ZeroOrMore(TestNot('\n'), ANY));
    }

    @SuppressNode
    Rule StmtEnd()
    {
        return FirstOf(Eol(), EOI);
    }

    @NotNull
    @SuppressNode
    Rule Eol()
    {
        return Ch('\n');
    }
}
