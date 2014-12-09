package com.cpb9.oimf.parser;

import com.cpb9.oimf.parser.model.*;
import org.jetbrains.annotations.NotNull;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.support.Var;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Artem Shein
 */
@BuildParseTree
public class OimfParser extends BaseParser<Object>
{
    public Rule File()
    {
        Var<File> file = new Var<>();
        Var<Map<String, Annotation>> annotations = new Var<>();
        return Sequence(Namespace(),
                file.set(new File((Namespace) pop())),
                Optional(Imports(),
                        file.get().setImports((List<Import>) pop())),
                ZeroOrMore(Optional(ExtendedWhitespaces()),
                        FirstOf(Sequence(Annotations(), annotations.set((Map<String, Annotation>) pop()),
                                        Optional(Whitespaces()), StmtEnd()),
                                annotations.set(new HashMap<String, Annotation>())),
                        Sequence(Trait(),
                                ((Trait) peek()).setAnnotations(annotations.get()),
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

    Rule Annotations()
    {
        Var<Map<String, Annotation>> annotations = new Var<Map<String, Annotation>>(new HashMap<String, Annotation>());
        return Sequence("@", Annotation(),
                annotations.get().put(((Annotation) peek()).getName(), (Annotation) pop()) == null,
                ZeroOrMore(Optional(Whitespaces()), ',', Optional(Whitespaces()),
                        Annotation(),
                        annotations.get().put(((Annotation) peek()).getName(), (Annotation) pop()) == null),
                push(annotations.get()));
    }

    Rule Annotation()
    {
        return Sequence(Ident(), push(new Annotation(match())),
                Optional(Optional(Whitespaces()), ':', Optional(Whitespaces()),
                        YamlValue(), ((Annotation) peek(1)).setValue(pop())));
    }

    Rule YamlValue()
    {
        return FirstOf(Boolean(), YamlString(), Float(),
                Sequence(Integer(), push(Integer.parseInt(match()))),
                YamlArray(), YamlMap(), Sequence(Ident(), push(match())));
    }

    Rule YamlString()
    {
        return FirstOf(Sequence('\"', ZeroOrMore(TestNot('\"'), ANY), push(match()), '\"'),
                Sequence(OneOrMore(TestNot(AnyOf("\"\'\n,{}[]")), ANY), push(match())));
    }

    Rule Boolean()
    {
        return FirstOf(Sequence("true", push(true)),
                Sequence("false", push(false)));
    }

    Rule BigInteger()
    {
        return FirstOf(Sequence(AnyOf("+-"), push(match()), Number(), push(new BigInteger(pop() + match()))),
                Sequence(Number(), push(new BigInteger(match()))));
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

    Rule Float()
    {
        return Sequence(Integer(), '.', Number(), push(match()),
                Optional(AnyOf("eE"), push(match()), Integer(), push((String) pop(2) + pop(1) + pop())),
                push(java.lang.Float.parseFloat(pop(1) + "." + pop())));
    }

    Rule BigDecimal()
    {
        return Sequence(Sequence(Optional(AnyOf("+-")), Number(), Optional('.', Number(),
                Optional(AnyOf("eE"), Optional(AnyOf("+-")), Number()))), push(new BigDecimal(match())));
    }

    Rule YamlArray()
    {
        Var<List<Object>> list = new Var<>();
        return Sequence('[',
                list.set(new ArrayList<>()),
                Optional(ExtendedWhitespaces()),
                Optional(YamlValue(),
                        listAdd(list.get(), pop()),
                        ZeroOrMore(',', Optional(ExtendedWhitespaces()), YamlValue(), list.get().add(pop()))),
                Optional(ExtendedWhitespaces()),
                ']', push(list.get()));
    }

    Rule YamlMap()
    {
        Var<Map<String, Object>> map = new Var<>();
        return Sequence('{',
                map.set(new HashMap<String, Object>()),
                Optional(YamlMapElement(),
                        ACTION(map.get().put((String) pop(1), pop()) == null),
                        ZeroOrMore(',', YamlMapElement(),
                                ACTION(map.get().put((String) pop(1), pop()) == null))),
                '}', push(map.get()));
    }

    Rule YamlMapElement()
    {
        return Sequence(Ident(), push(match()),
                Optional(Whitespaces()), ':', Optional(ExtendedWhitespaces()), YamlValue());
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
        return Sequence("trait", Whitespaces(), Ident(), traitVar.set(new Trait(match())),
                Optional('[', Ident(), listAdd(traitVar.get().getArguments(), match()), ZeroOrMore(',', Whitespaces(),
                        Ident(), listAdd(traitVar.get().getArguments(), match())), ']'),
                Optional(Whitespaces(), "extends", Whitespaces(), TraitArgument(), listAdd(traitVar.get().getExtends(),
                        (TraitApplication) pop()), ZeroOrMore(',', ExtendedWhitespaces(),
                        TraitArgument(), listAdd(traitVar.get().getExtends(), (TraitApplication) pop()))),
                Optional(Whitespaces()), StmtEnd(), ZeroOrMore(FieldOrMethod(traitVar)),
                push(traitVar.get()));
    }

    Rule TraitArgument()
    {
        Var<List<TraitApplication>> traitApplicationsVar =
                new Var<List<TraitApplication>>(new ArrayList<TraitApplication>());
        return Sequence(QualifiedIdent(), push(match()),
                Optional('[', TraitArgument(),
                        listAdd(traitApplicationsVar.get(), (TraitApplication) pop()),
                        ZeroOrMore(',', Whitespaces(), TraitArgument(),
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
        return Sequence(Indent(), ZeroOrMore(Comment(), StmtEnd(), Indent()), TraitArgument(), Whitespaces(),
                Ident(), push(match()),
                FirstOf(
                        Sequence('(', push(new Method((String) pop(), (TraitApplication) pop())),
                                Optional(TraitMethodArguments(),
                                        ((Method) peek(1)).setArguments((List<MethodArgument>) pop())),
                        ')', listAdd(traitVar.get().getMethods(), (Method) pop())),
                        Sequence(EMPTY,
                                listAdd(traitVar.get().getFields(),
                                        new Field((String) pop(), (TraitApplication) pop())))),
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
        return Sequence(TraitArgument(), Whitespaces(), Ident(),
                push(new MethodArgument(match(), (TraitApplication) pop())));
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
