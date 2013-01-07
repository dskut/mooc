// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.Enumeration;
import java.io.PrintStream;
import java.util.*;

class MethodInfo {
    public AbstractSymbol nameType;
    public Formals formals;
    public MethodInfo(AbstractSymbol nameType, Formals formals) {
        this.nameType = nameType;
        this.formals = formals;
    }
}


/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Classes
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}


/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable);
    public abstract String fillMethodsTable(SymbolTable methodsTable);
    public abstract String fillAttrTable(SymbolTable attrTable);
}


/** Defines list phylum Features
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}


/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Formals
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}


/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }
    private AbstractSymbol type = null;                                 
    public AbstractSymbol get_type() { return type; }           
    public Expression set_type(AbstractSymbol s) { type = s; return this; } 
    public abstract void dump_with_types(PrintStream out, int n);
    public void dump_type(PrintStream out, int n) {
        if (type != null)
            { out.println(Utilities.pad(n) + ": " + type.getString()); }
        else
            { out.println(Utilities.pad(n) + ": _no_type"); }
    }

    public abstract void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable);
}


/** Defines list phylum Expressions
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}


/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    private AbstractSymbol type = null;                                 
    protected Case(int lineNumber) {
        super(lineNumber);
    }
    public AbstractSymbol get_type() { return type; }           
    public Case set_type(AbstractSymbol s) { type = s; return this; } 
    public abstract void dump_with_types(PrintStream out, int n);
}


/** Defines list phylum Cases
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}


/** Defines AST constructor 'programc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class programc extends Program {
    protected Classes classes;
    /** Creates "programc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }
    public TreeNode copy() {
        return new programc(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
	    ((Class_)e.nextElement()).dump_with_types(out, n + 2);
        }
    }
    /** This method is the entry point to the semantic checker.  You will
        need to complete it in programming assignment 4.
	<p>
        Your checker should do the following two things:
	<ol>
	<li>Check that the program is semantically correct
	<li>Decorate the abstract syntax tree with type information
        by setting the type field in each Expression node.
        (see tree.h)
	</ol>
	<p>
	You are free to first do (1) and make sure you catch all semantic
    	errors. Part (2) can be done in a second stage when you want
	to test the complete compiler.
    */
    public void semant() {
        /* ClassTable constructor may do some semantic analysis */
        ClassTable classTable = new ClassTable(classes);

        if (classTable.errors()) {
            System.err.println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
	
        /* some semantic analysis code may go here */
        if (!classTable.hasClass(TreeConstants.Main)) {
            classTable.semantError().println("Class Main is not defined.");
        }

        classTable.fillMethodsAttrTables();

        SymbolTable symbolTable = new SymbolTable();
        symbolTable.enterScope();
        symbolTable.addId(TreeConstants.self, TreeConstants.SELF_TYPE);

        for (class_c cl: classTable.getClassList()) {
        	cl.semant(classTable, symbolTable);
        }

        if (classTable.errors()) {
            System.err.println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
    }

}


/** Defines AST constructor 'class_c'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;
    private SymbolTable methodsTable;
    private SymbolTable attrTable;
    /** Creates "class_c" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }
    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), 
                           copy_AbstractSymbol(parent), (Features)features.copy(), 
                           copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }

    
    public AbstractSymbol getFilename()  { return filename; }
    public AbstractSymbol getName()      { return name; }
    public AbstractSymbol getParent()    { return parent; }
    public Features getFeatures()        { return features; }
    public SymbolTable getMethodsTable() { return methodsTable; }
    public SymbolTable getAttrTable()    { return attrTable; }

    public void fillMethodsAttrTables(ClassTable classTable) {
        if (parent != TreeConstants.No_class) {
            class_c parentClass = classTable.getClass(parent);
            if (parentClass == null) {
                return;
            }
            methodsTable = parentClass.getMethodsTable();
            attrTable = parentClass.getAttrTable();
            if (methodsTable == null) {
                parentClass.fillMethodsAttrTables(classTable);
            }
            methodsTable = new SymbolTable(parentClass.getMethodsTable());
            attrTable = new SymbolTable(parentClass.getAttrTable());
        } else {
            methodsTable = new SymbolTable();
            attrTable = new SymbolTable();
        }

        methodsTable.enterScope();
        attrTable.enterScope();
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
            Feature feature = (Feature)e.nextElement();
            String errStr = feature.fillMethodsTable(methodsTable);
            if (!errStr.isEmpty()) {
                classTable.semantError(this).println(errStr);
            }
            errStr = feature.fillAttrTable(attrTable);
            if (!errStr.isEmpty()) {
                classTable.semantError(this).println(errStr);
            }
        }
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
            ((Feature)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }

    public void semant(ClassTable classTable, SymbolTable symbolTable) {
        if (!classTable.hasClass(parent)) {
            classTable.semantError(this).println("Class " + name + " inherits from an undefined class " + parent + ".");
            return;
        }

        symbolTable.enterScope();
        symbolTable.addId(TreeConstants.SELF_TYPE, name);
        symbolTable.addTable(attrTable);

    	for (Enumeration e = features.getElements(); e.hasMoreElements();) {
    	    ((Feature)e.nextElement()).semant(classTable, this, symbolTable);
    	}
        symbolTable.exitScope();
    }
}


/** Defines AST constructor 'method'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;
    /** Creates "method" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for formals
      * @param a2 initial value for return_type
      * @param a3 initial value for expr
      */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }
    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals)formals.copy(), copy_AbstractSymbol(return_type), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n+2, name);
        formals.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, return_type);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
            ((Formal)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump_with_types(out, n + 2);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
    	if (return_type != TreeConstants.SELF_TYPE && !classTable.hasClass(return_type)) {
    	    classTable.semantError(cl).println("Undefined return type " + return_type + " in method " + name + ".");
    	}

        symbolTable.enterScope();
        Set<AbstractSymbol> seenFormals = new HashSet<AbstractSymbol>();
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
            formalc formal = (formalc)e.nextElement();
            formal.semant(classTable, cl);
            if (seenFormals.contains(formal.getName())) {
                classTable.semantError(cl).println("Formal parameter " + formal.getName() + " is multiply defined.");
            } else {
                seenFormals.add(formal.getName());
                formal.fillSymbolTable(symbolTable);
            }
        }

        expr.semant(classTable, cl, symbolTable);
        symbolTable.exitScope();

        //System.err.println("return_type = " + return_type);
        //System.err.println("expr.get_type() = " + expr.get_type());
        if(expr.get_type() == TreeConstants.No_type) {
            return;
        }

        AbstractSymbol exprType = expr.get_type();
        if (exprType == TreeConstants.SELF_TYPE) {
            exprType = cl.getName();
        }

        if (return_type == TreeConstants.SELF_TYPE && expr.get_type() != TreeConstants.SELF_TYPE) {
            classTable.semantError(cl).println("Inferred return type " + expr.get_type() +
                " of method " + name + " does not conform to declared return type " + return_type + ".");
        }
        if (return_type != TreeConstants.SELF_TYPE && !classTable.isBase(return_type, exprType)) {
            classTable.semantError(cl).println("Inferred return type " + expr.get_type() +
                " of method " + name + " does not conform to declared return type " + return_type + ".");
        }
    }

    public String fillAttrTable(SymbolTable attrTable) {
        return "";
    }

    public String fillMethodsTable(SymbolTable methodsTable) {
        MethodInfo parentMethod = (MethodInfo)methodsTable.lookup(name);
        if (parentMethod != null) {
            if (parentMethod.formals.getLength() != formals.getLength()) {
                return "Incompatible number of formal parameters in redefined method " + name + ".";
            } else {
                for (int i = 0; i < formals.getLength(); ++i) {
                    formalc formal = (formalc)formals.getNth(i);
                    formalc parentFormal = (formalc)parentMethod.formals.getNth(i);
                    if (formal.get_type() != parentFormal.get_type()) {
                        return "In redefined method " + name + ", parameter type " + formal.get_type() + 
                            " is different from original type " + parentFormal.get_type();
                    }
                }
            }
        }
        MethodInfo methodInfo = new MethodInfo(return_type, formals);
        methodsTable.addId(name, methodInfo);
        return "";
    }
}


/** Defines AST constructor 'attr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;
    /** Creates "attr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }
    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)init.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
    }
    
    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        if (name == TreeConstants.self) {
            classTable.semantError(cl).println("'self' cannot be the name of an attribute.");
        }
        init.semant(classTable, cl, symbolTable);
    }

    public String fillMethodsTable(SymbolTable methodsTable) {
        return "";
    }

    public String fillAttrTable(SymbolTable attrTable) {
        if (attrTable.lookup(name) != null) {
            return "Attribute " + name + " is an attribute of an inherited class.";
        } else {
            attrTable.addId(name, type_decl);
            return "";
        }
    }
}


/** Defines AST constructor 'formalc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    /** Creates "formalc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
    }

    public AbstractSymbol get_type() { return type_decl; }
    public AbstractSymbol getName()  { return name; }
    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

    public void semant(ClassTable classTable, class_c cl) {
        if (type_decl == TreeConstants.SELF_TYPE) {
            classTable.semantError(cl).println("Formal parameter " + name + " cannot have type SELF_TYPE.");
        } 
        if (name == TreeConstants.self) {
            classTable.semantError(cl).println("'self' cannot be the name of a formal parameter.");
        }
    }

    public void fillSymbolTable(SymbolTable symbolTable) {
        symbolTable.addId(name, type_decl);
    }
}


/** Defines AST constructor 'branch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;
    /** Creates "branch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for expr
      */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }
    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)expr.copy());
    }

    public AbstractSymbol getTypeDecl() { return type_decl; }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        expr.dump(out, n+2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump_with_types(out, n + 2);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        symbolTable.enterScope();
        symbolTable.addId(name, type_decl);
        expr.semant(classTable, cl, symbolTable);
        set_type(expr.get_type());
        symbolTable.exitScope();
    }
}


/** Defines AST constructor 'assign'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;
    /** Creates "assign" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for expr
      */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }
    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n+2, name);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        if (name == TreeConstants.self) {
            classTable.semantError(cl).println("Cannot assign to 'self'.");
            set_type(TreeConstants.No_type);
            return;
        }
        AbstractSymbol name_type = (AbstractSymbol)symbolTable.lookup(name);
        expr.semant(classTable, cl, symbolTable);
        AbstractSymbol expr_type = expr.get_type();
        if (!classTable.isBase(name_type, expr_type)) {
            classTable.semantError(cl).println("Type " + expr_type + 
                " of assigned expression does not conform to declared type " + name_type + " of identifier " + name + ".");
        }
        set_type(name_type);
    }
}


/** Defines AST constructor 'static_dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "static_dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for type_name
      * @param a2 initial value for name
      * @param a3 initial value for actual
      */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }
    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(type_name), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, type_name);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        expr.semant(classTable, cl, symbolTable);
        AbstractSymbol expr_type = expr.get_type();
        if (expr_type == TreeConstants.SELF_TYPE) {
            expr_type = (AbstractSymbol)symbolTable.lookup(TreeConstants.SELF_TYPE);
        }

        if (!classTable.isBase(type_name, expr_type)) {
            classTable.semantError(cl).println("Expression type " + expr.get_type() + 
                " does not conform to declared static dispatch type " + type_name + ".");
        }

        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression)e.nextElement()).semant(classTable, cl, symbolTable);
        }

        SymbolTable methodsTable = classTable.getMethodsTable(type_name);
        MethodInfo methodInfo = (MethodInfo)methodsTable.lookup(name);
        AbstractSymbol name_type = methodInfo.nameType;
        set_type(name_type);
    }
}


/** Defines AST constructor 'dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for name
      * @param a2 initial value for actual
      */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }
    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        expr.semant(classTable, cl, symbolTable);
        AbstractSymbol expr_type = expr.get_type();
        if (expr_type == TreeConstants.SELF_TYPE) {
            expr_type = (AbstractSymbol)symbolTable.lookup(TreeConstants.SELF_TYPE);
        }
        SymbolTable methodsTable = classTable.getMethodsTable(expr_type);
        AbstractSymbol name_type = TreeConstants.Object_;
        Object lookedUp = methodsTable.lookup(name);
        //System.err.println(methodsTable);
        if (lookedUp == null) {
            classTable.semantError(cl).println("Dispatch to undefined method " + name + ".");
        } else {
            MethodInfo methodInfo = (MethodInfo)methodsTable.lookup(name);
            name_type = methodInfo.nameType;
            Formals formals = methodInfo.formals;

            for (int i = 0; i < actual.getLength(); ++i) {
                Expression param = (Expression)actual.getNth(i);
                param.semant(classTable, cl, symbolTable);
                formalc formal = (formalc)formals.getNth(i);
                AbstractSymbol paramType = param.get_type();
                if (paramType == TreeConstants.SELF_TYPE) {
                    paramType = cl.getName();
                }
                //System.err.println("formal: " + formal.getName() + ": " + formal.get_type());
                //System.err.println("param: " + paramType);
                if (!classTable.isBase(formal.get_type(), paramType)) {
                    classTable.semantError(cl).println("In call of method " + name + ", type " + param.get_type() +
                        " of parameter " + formal.getName() + " does not conform to declared type " + formal.get_type() + ".");
                }
            }
        }
        if (name_type == TreeConstants.SELF_TYPE) {
            set_type(expr.get_type());
        } else {
            set_type(name_type);
        }
    }
}


/** Defines AST constructor 'cond'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;
    /** Creates "cond" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for then_exp
      * @param a2 initial value for else_exp
      */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }
    public TreeNode copy() {
        return new cond(lineNumber, (Expression)pred.copy(), (Expression)then_exp.copy(), (Expression)else_exp.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n+2);
        then_exp.dump(out, n+2);
        else_exp.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
        pred.dump_with_types(out, n + 2);
        then_exp.dump_with_types(out, n + 2);
        else_exp.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        pred.semant(classTable, cl, symbolTable);
        then_exp.semant(classTable, cl, symbolTable);
        else_exp.semant(classTable, cl, symbolTable);
        //System.err.println("then_exp: " + then_exp.get_type());
        //System.err.println("else_exp: " + else_exp.get_type());
        AbstractSymbol then_type = then_exp.get_type();
        if (then_type == TreeConstants.SELF_TYPE) {
            then_type = cl.getName();
        }
        AbstractSymbol else_type = else_exp.get_type();
        if (else_type == TreeConstants.SELF_TYPE) {
            else_type = cl.getName();
        }
        set_type(classTable.leastCommonAncestor(then_type, else_type));
    }
}


/** Defines AST constructor 'loop'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;
    /** Creates "loop" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for body
      */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }
    public TreeNode copy() {
        return new loop(lineNumber, (Expression)pred.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
        pred.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        pred.semant(classTable, cl, symbolTable);
        if (pred.get_type() != TreeConstants.Bool) {
            classTable.semantError(cl).println("Loop condition does not have type Bool.");
        }
        body.semant(classTable, cl, symbolTable);
        set_type(TreeConstants.Object_);
    }
}


/** Defines AST constructor 'typcase'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;
    /** Creates "typcase" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for cases
      */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }
    public TreeNode copy() {
        return new typcase(lineNumber, (Expression)expr.copy(), (Cases)cases.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n+2);
        cases.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
        expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
            ((Case)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        expr.semant(classTable, cl, symbolTable);
        List<AbstractSymbol> types = new ArrayList<AbstractSymbol>();
        Set<AbstractSymbol> declTypes = new HashSet<AbstractSymbol>();
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
            branch br = (branch)e.nextElement();
            AbstractSymbol type_decl = br.getTypeDecl();
            if (declTypes.contains(type_decl)) {
                classTable.semantError(cl).println("Duplicate branch " + type_decl + " in case statement.");
            }
            declTypes.add(type_decl);
            br.semant(classTable, cl, symbolTable);
            types.add(br.get_type());
        }
        set_type(classTable.leastCommonAncestor(types));
    }
}


/** Defines AST constructor 'block'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class block extends Expression {
    protected Expressions body;
    /** Creates "block" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for body
      */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }
    public TreeNode copy() {
        return new block(lineNumber, (Expressions)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
            ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
    	for (Enumeration e = body.getElements(); e.hasMoreElements();) {
            Expression expr = (Expression)e.nextElement();
    	    expr.semant(classTable, cl, symbolTable);
            set_type(expr.get_type());
    	}
    }
}


/** Defines AST constructor 'let'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;
    /** Creates "let" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for identifier
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      * @param a3 initial value for body
      */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }
    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl), (Expression)init.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n+2, identifier);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        if (identifier == TreeConstants.self) {
            classTable.semantError(cl).println("'self' cannot be bound in a 'let' expression.");
            set_type(TreeConstants.No_type);
            return;
        }

        init.semant(classTable, cl, symbolTable);
        AbstractSymbol init_type = init.get_type();
        if (init_type != TreeConstants.No_type && init_type != TreeConstants.SELF_TYPE && 
            !classTable.isBase(type_decl, init_type)) 
        {
            classTable.semantError(cl).println("Inferred type " + init_type + " of initialization of " + 
                identifier + " does not conform to identifier's declared type " + type_decl + ".");
            set_type(TreeConstants.No_type);
            return;
        }

        symbolTable.enterScope();
        symbolTable.addId(identifier, type_decl);
        body.semant(classTable, cl, symbolTable);
        symbolTable.exitScope();

        set_type(body.get_type());
    }
}


/** Defines AST constructor 'plus'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "plus" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new plus(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        e2.semant(classTable, cl, symbolTable);
        if (e1.get_type() != TreeConstants.Int || e2.get_type() != TreeConstants.Int) {
            classTable.semantError(cl).println("non-Int arguments: " + e1.get_type() + " + " + e2.get_type());
            set_type(TreeConstants.Object_);
        } else {
            set_type(e2.get_type());
        }
    }
}


/** Defines AST constructor 'sub'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "sub" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new sub(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        e2.semant(classTable, cl, symbolTable);
        if (e1.get_type() != TreeConstants.Int || e2.get_type() != TreeConstants.Int) {
            classTable.semantError(cl).println("non-Int arguments: " + e1.get_type() + " + " + e2.get_type());
            set_type(TreeConstants.Object_);
        } else {
            set_type(e2.get_type());
        }
    }
}


/** Defines AST constructor 'mul'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "mul" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new mul(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        e2.semant(classTable, cl, symbolTable);
        if (e1.get_type() != TreeConstants.Int || e2.get_type() != TreeConstants.Int) {
            classTable.semantError(cl).println("non-Int arguments: " + e1.get_type() + " + " + e2.get_type());
            set_type(TreeConstants.Object_);
        } else {
            set_type(e2.get_type());
        }
    }
}


/** Defines AST constructor 'divide'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "divide" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new divide(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        e2.semant(classTable, cl, symbolTable);
        if (e1.get_type() != TreeConstants.Int || e2.get_type() != TreeConstants.Int) {
            classTable.semantError(cl).println("non-Int arguments: " + e1.get_type() + " + " + e2.get_type());
            set_type(TreeConstants.Object_);
        } else {
            set_type(e2.get_type());
        }
    }
}


/** Defines AST constructor 'neg'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class neg extends Expression {
    protected Expression e1;
    /** Creates "neg" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new neg(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        set_type(e1.get_type());
    }
}


/** Defines AST constructor 'lt'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "lt" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new lt(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        e2.semant(classTable, cl, symbolTable);
        set_type(TreeConstants.Bool);
    }
}


/** Defines AST constructor 'eq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "eq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new eq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        e2.semant(classTable, cl, symbolTable);
        if (e1.get_type() != e2.get_type() && classTable.hasBasicClass(e2.get_type())) {
            classTable.semantError(cl).println("Illegal comparison with a basic type.");
        }
        set_type(TreeConstants.Bool);
    }
}


/** Defines AST constructor 'leq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "leq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new leq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        e2.semant(classTable, cl, symbolTable);
        set_type(TreeConstants.Bool);
    }
}


/** Defines AST constructor 'comp'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class comp extends Expression {
    protected Expression e1;
    /** Creates "comp" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new comp(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        set_type(e1.get_type());
    }
}


/** Defines AST constructor 'int_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class int_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "int_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
        dump_AbstractSymbol(out, n + 2, token);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        set_type(TreeConstants.Int);
    }
}


/** Defines AST constructor 'bool_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class bool_const extends Expression {
    protected Boolean val;
    /** Creates "bool_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for val
      */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }
    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n+2, val);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
        dump_Boolean(out, n + 2, val);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        set_type(TreeConstants.Bool);
    }
}


/** Defines AST constructor 'string_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class string_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "string_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, token.getString());
        out.println("\"");
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        set_type(TreeConstants.Str);
    }
}


/** Defines AST constructor 'new_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class new_ extends Expression {
    protected AbstractSymbol type_name;
    /** Creates "new_" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for type_name
      */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }
    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n+2, type_name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        if (type_name != TreeConstants.SELF_TYPE && !classTable.hasClass(type_name)) {
            classTable.semantError(cl).println("'new' used with undefined class " + type_name + ".");
            set_type(TreeConstants.No_type);
        } else {
            set_type(type_name);
        }
    }
}


/** Defines AST constructor 'isvoid'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class isvoid extends Expression {
    protected Expression e1;
    /** Creates "isvoid" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        e1.semant(classTable, cl, symbolTable);
        set_type(TreeConstants.Bool);
    }
}


/** Defines AST constructor 'no_expr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class no_expr extends Expression {
    /** Creates "no_expr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }
    public TreeNode copy() {
        return new no_expr(lineNumber);
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        set_type(TreeConstants.No_type);
    }
}


/** Defines AST constructor 'object'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class object extends Expression {
    protected AbstractSymbol name;
    /** Creates "object" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }
    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n+2, name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
        dump_AbstractSymbol(out, n + 2, name);
        dump_type(out, n);
    }

    public void semant(ClassTable classTable, class_c cl, SymbolTable symbolTable) {
        Object lookedup = symbolTable.lookup(name);
        if (lookedup == null) {
            classTable.semantError(cl).println("Undeclared identifier " + name + ".");
            set_type(TreeConstants.No_type);
        } else {
            set_type((AbstractSymbol)lookedup);
        }
    }
}


