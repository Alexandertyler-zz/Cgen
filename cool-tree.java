// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////



import java.util.Enumeration;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Collections;
import java.util.ArrayList;


/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();
    public abstract void cgen(PrintStream s);

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract AbstractSymbol getName();
    public abstract AbstractSymbol getParent();
    public abstract AbstractSymbol getFilename();
    public abstract Features getFeatures();

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

    public abstract void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable);    
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
    public abstract void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable);

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
    protected Case(int lineNumber) {
        super(lineNumber);
    }
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
	
	/* some semantic analysis code may go here */

	if (classTable.errors()) {
	    System.err.println("Compilation halted due to static semantic errors.");
	    System.exit(1);
	}
    }
    /** This method is the entry point to the code generator.  All of the work
      * of the code generator takes place within CgenClassTable constructor.
      * @param s the output stream 
      * @see CgenClassTable
      * */
    public void cgen(PrintStream s) {

    // if you want to print out comments use #

        //this initializes the Code Gen table and does some generation of its own
	CgenClassTable cgTable = new CgenClassTable(classes, s);
        SymbolTable sTable = new SymbolTable();

        //this is the initial call that makes all the setup for running a program
        cgTable.code();

        //once that is done we need to walk through all of our classes and call cgen for them.
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            class_c curr_class = (class_c) e.nextElement();
            //for each call we need to pass in the cgentable, symboltable, 
            curr_class.code(s, curr_class, cgTable, sTable);
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
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
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
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }
    public AbstractSymbol getFilename() { return filename; }
    public Features getFeatures()       { return features; }

    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {

        s.println("#Entering Class");
        //our first step into code generation. This splits into features for each class
        sTable.enterScope();

        //populate the symbol table
        ArrayList<attr> attributes = cgTable.getClassInfoAttr(curr_class.getName());
         if (attributes != null) {
           int count = 0;
            for (Enumeration e_attr = Collections.enumeration(attributes); e_attr.hasMoreElements(); ) {
                attr curr_attr = (attr) e_attr.nextElement();
                //Count ++ might need to start at -1
                int attr_offset = (4 * count++) + 12;
                sTable.addId(curr_attr.name, "" + attr_offset + "($s0)");
            }
        }

        for (Enumeration e = features.getElements(); e.hasMoreElements(); ) {
            Feature curr_feat = (Feature) e.nextElement();
            //assuming it should be a method. if its an attr i have it blank out.
            curr_feat.code(s, curr_class, cgTable, sTable );
        }
	sTable.exitScope();
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
    
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#Entering Method");

        sTable.enterScope();

        for (int iter = 0; iter < formals.getLength(); iter++) {
            formalc curr_formal = (formalc) formals.getNth(iter);
            sTable.addId(curr_formal.name, ("" + (4*(formals.getLength() - iter) + 8) + "($fp)"));
        }

	CgenSupport.emitMethodRef(curr_class.getName(), name, s);
        s.print(CgenSupport.LABEL);
	
        CgenSupport.emitPush(CgenSupport.FP, s);
    	CgenSupport.emitPush(CgenSupport.SELF, s);
    	CgenSupport.emitPush(CgenSupport.RA, s);
  	
    	CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 16, s);
        CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, s);

    	s.println("#Before expr.code");
        cgTable.zeroExprOffset();
        expr.code(s, curr_class, cgTable, sTable);
    	s.println("#After expr.code");

        CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, s);
        CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, s);
        CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, (4*formals.getLength()+12), s);
        CgenSupport.emitReturn(s);

        sTable.exitScope();
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

    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
    
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

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
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

    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        expr.code(s, curr_class, cgTable, sTable);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        expr.code(s, curr_class, cgTable, sTable);
	
	s.println(CgenSupport.SW + CgenSupport.ACC + "\t" + sTable.lookup(name));
		
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#static_dispatch");
	int numActuals = actual.getLength();
        for (int i  = 0; i < numActuals; i++) {
            Expression curr_expr = (Expression) actual.getNth(i);
            curr_expr.code(s, curr_class, cgTable, sTable);
            CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
        }
        
        expr.code(s, curr_class, cgTable, sTable);
        int nextLabel = cgTable.getLabelNum();
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, nextLabel, s);
        s.print(CgenSupport.LA + CgenSupport.ACC + " ");
        ((StringSymbol) AbstractTable.stringtable.lookup(curr_class.getFilename().getString())).codeRef(s);
        s.println();
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal("_dispatch_abort", s);
        CgenSupport.emitLabelDef(nextLabel, s);

        AbstractSymbol type;
        if (type_name == TreeConstants.SELF_TYPE) {
            type = curr_class.name;
        } else {
            type = type_name;
        }

        CgenSupport.emitLoadAddress(CgenSupport.T1, type.getString() + CgenSupport.DISPTAB_SUFFIX, s);
        int methodOffset = cgTable.methodOffset(type, name);
        
        CgenSupport.emitLoad(CgenSupport.T1, methodOffset, CgenSupport.T1, s);
        CgenSupport.emitJalr(CgenSupport.T1, s);        
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#dispatch");
	int numActuals = actual.getLength();
        for (int i  = 0; i < numActuals; i++) {
            Expression curr_expr = (Expression) actual.getNth(i);
            curr_expr.code(s, curr_class, cgTable, sTable);
            CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
        }
        
        expr.code(s, curr_class, cgTable, sTable);
        int nextLabel = cgTable.getLabelNum();
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, nextLabel, s);
        
	//s.println("#spacing error");
	s.print(CgenSupport.LA + CgenSupport.ACC + " ");
        ((StringSymbol) AbstractTable.stringtable.lookup(curr_class.getFilename().getString())).codeRef(s);
        s.println();
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal("_dispatch_abort", s);
        CgenSupport.emitLabelDef(nextLabel, s);

        AbstractSymbol type = expr.get_type();
        if (type == TreeConstants.SELF_TYPE) {
            type = curr_class.name;
        }

        CgenSupport.emitLoad(CgenSupport.T1, 2, CgenSupport.ACC, s);
        int methodOffset = cgTable.methodOffset(type, name);
        CgenSupport.emitLoad(CgenSupport.T1, methodOffset, CgenSupport.T1, s);
        CgenSupport.emitJalr(CgenSupport.T1, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {        
        int falseLabel = cgTable.getLabelNum();
        int endLabel = cgTable.getLabelNum();
        
        pred.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s);
        CgenSupport.emitBeqz(CgenSupport.T1, falseLabel, s);
        
        then_exp.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitBranch(endLabel, s);

        CgenSupport.emitLabelDef(falseLabel, s);
        else_exp.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitLabelDef(endLabel, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        int preLabel = cgTable.getLabelNum();
        int postLabel = cgTable.getLabelNum();
        
        CgenSupport.emitLabelDef(preLabel, s);
        
        pred.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s);
        CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.ZERO, postLabel, s);
        
        body.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitBranch(preLabel, s);
        CgenSupport.emitLabelDef(postLabel, s);

        CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.ZERO, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#typcase");
	   sTable.enterScope();

        ArrayList<branch> branchList = new ArrayList<branch>();
        for (Enumeration e = cases.getElements(); e.hasMoreElements(); ) {
            branch curr_branch = (branch) e.nextElement();
            branchList.add(curr_branch);
        }

        expr.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        cgTable.decExprOffset();

        int label = cgTable.getLabelNum();
        int nextLabel = cgTable.getLabelNum();

        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, label, s);
        s.print(CgenSupport.LA + CgenSupport.ACC + " ");
	    //s.println("#checking");
	    ((StringSymbol) AbstractTable.stringtable.lookup(curr_class.getFilename().getString())).codeRef(s);
	    s.println();
        s.println(CgenSupport.LI + CgenSupport.T1 + " " + lineNumber);
        CgenSupport.emitJal("_case_abort2", s);
        CgenSupport.emitLabelDef(label, s);
        CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.ACC, s);
        
        for (int iter = 0; iter<branchList.size(); iter++) {
            branch curr_branch = (branch) branchList.get(iter);
            sTable.enterScope();
            int classTag = cgTable.getClassInfo(curr_branch.type_decl).classTag;
            int innerLabel = cgTable.getLabelNum();
            int maximumTag = cgTable.getMaxTag(curr_branch.type_decl);
            CgenSupport.emitBlti(CgenSupport.T2, classTag, innerLabel, s);
            CgenSupport.emitBgti(CgenSupport.T2, maxTag, innerLabel, s);
            sTable.addId(curr_branch.name, cgTable.getExprOffset() + "($fp)");
            curr_branch.code(s, curr_class, cgTable, sTable);
            
	       CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
            CgenSupport.emitBranch(nextLabel, s);
            CgenSupport.emitLabelDef(innerLabel, s);
            sTable.exitScope();
        }

        CgenSupport.emitJal("_case_abort", s);
        CgenSupport.emitLabelDef(nextLabel, s);
        cgTable.incExprOffset();
        sTable.exitScope();

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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        for (int i = 0; i < body.getLength(); i++) {
            ((Expression)body.getNth(i)).code(s, curr_class, cgTable, sTable);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#let");
	sTable.enterScope();
        s.println("#type_decl = " + type_decl.getString());
        if(init instanceof no_expr) {
            if(type_decl.getString().equals("Int")
               || type_decl.getString().equals("Bool")
               || type_decl.getString().equals("String")
               || type_decl.getString().equals("IO")) {
                CgenSupport.emitLoadAddress(CgenSupport.ACC, type_decl.getString() + CgenSupport.PROTOBJ_SUFFIX, s);
                CgenSupport.emitJal("Object.copy" , s); 
                CgenSupport.emitJal(type_decl.getString() + CgenSupport.CLASSINIT_SUFFIX, s);
            }
        }
        init.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        cgTable.decExprOffset();
        sTable.addId(identifier, cgTable.getExprOffset() + "($fp)");
        body.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        cgTable.incExprOffset();
        sTable.exitScope();
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {

        e1.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        e2.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.T3, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.T3, s);

        CgenSupport.emitAdd(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        
        e1.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        e2.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.T3, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.T3, s);
        CgenSupport.emitSub(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        
        e1.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        e2.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.T3, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.T3, s);
        CgenSupport.emitMul(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        
        e1.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        e2.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.T3, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.T3, s);
        CgenSupport.emitDiv(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#neg");
	e1.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitJal("Object.copy", s);
        
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s);
        CgenSupport.emitNeg(CgenSupport.T1, CgenSupport.T1, s);
        CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#lt");
	e1.code(s, curr_class, cgTable, sTable); 
        CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
        
        e2.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.T1, s);
        
        CgenSupport.emitLoadAddress(CgenSupport.ACC, "bool_const1", s);
        
        int label = cgTable.getLabelNum();
        CgenSupport.emitBlt(CgenSupport.T1, CgenSupport.T2, label, s);
        
        CgenSupport.emitLoadAddress(CgenSupport.ACC, "bool_const0", s);
        
        CgenSupport.emitLabelRef(label, s);
        s.print(CgenSupport.LABEL);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#eq");
	e1.code(s, curr_class, cgTable, sTable);
        CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
        
        e2.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitMove(CgenSupport.T2, CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.ACC, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);
        
        CgenSupport.emitLoadAddress(CgenSupport.ACC, "bool_const1", s);
        
        int label = cgTable.getLabelNum();
        CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.T2, label, s);
        
        CgenSupport.emitLoadAddress(CgenSupport.A1, "bool_const0", s);
        
        CgenSupport.emitJal("equality_test", s);
        CgenSupport.emitLabelDef(label, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#leq");
	e1.code(s, curr_class, cgTable, sTable); 
        CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
        
        e2.code(s, curr_class, cgTable, sTable);
        
        CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.T1, s);
        
        CgenSupport.emitLoadAddress(CgenSupport.ACC, "bool_const1", s);
        
        int label = cgTable.getLabelNum();
        CgenSupport.emitBleq(CgenSupport.T1, CgenSupport.T2, label, s);
        
        CgenSupport.emitLoadAddress(CgenSupport.ACC, "bool_const0", s);
        
        CgenSupport.emitLabelRef(label, s);
        s.print(CgenSupport.LABEL);

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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#comp");
	int label = cgTable.getLabelNum();
        e1.code(s, curr_class, cgTable, sTable);
        
        // lw $t1 12($a0)
        CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s);
        
        // la $a0 bool_const_true
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);

        //compare 
        CgenSupport.emitBeqz(CgenSupport.T1, label, s);
        
        // la $a0 bool_const_false
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);
        
        // label def
        CgenSupport.emitLabelDef(label, s);
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
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
	s.println("#int const");
	CgenSupport.emitLoadInt(CgenSupport.ACC,
                                (IntSymbol)AbstractTable.inttable.lookup(token.getString()), s);
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
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
  	s.println("bool_const"); 
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), s);
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
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
	s.println("#string_const");
	CgenSupport.emitLoadString(CgenSupport.ACC,
                                   (StringSymbol)AbstractTable.stringtable.lookup(token.getString()), s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
    	s.println("#new_");
        if (type_name != TreeConstants.SELF_TYPE) {
            s.println(CgenSupport.LA + type_name.getString() + CgenSupport.PROTOBJ_SUFFIX);
    	    CgenSupport.emitJal("Object.copy", s);
    	    CgenSupport.emitJal(type_name.getString() + CgenSupport.CLASSINIT_SUFFIX, s);
	    } else {
            s.println(CgenSupport.LA + CgenSupport.T1 + " class_objTab");
    	    CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.SELF, s);
    	    CgenSupport.emitSll(CgenSupport.T2, CgenSupport.T2, 3, s);
    	    CgenSupport.emitAddu(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
    	    
    	    CgenSupport.emitPush(CgenSupport.T1, s);
    	    CgenSupport.emitLoad(CgenSupport.ACC, 0, CgenSupport.T1, s);
    	    CgenSupport.emitJal("Object.copy", s);

    	    CgenSupport.emitLoad(CgenSupport.T2, 1, CgenSupport.SP, s);
    	    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
    	    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.T2, s);
    	    CgenSupport.emitJalr(CgenSupport.T1, s);
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#isvoid");
	int label = cgTable.getLabelNum();
       
        e1.code(s, curr_class, cgTable, sTable);
        
        //move $t1 $a0
        CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);

        //load true by default
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);

        //beq $t1 0 end_label
        CgenSupport.emitBeqz(CgenSupport.T1, label, s);
        
        // load false if reached
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);
        
        // emit label to jump to if void 
        CgenSupport.emitLabelDef(label, s);        
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#no_expr");
	CgenSupport.emitLoadImm(CgenSupport.ACC, 0, s);          
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, class_c curr_class, CgenClassTable cgTable, SymbolTable sTable) {
        s.println("#Object");
	if (name != TreeConstants.self) {
            if (sTable.lookup(name) != null) {
	    	//CgenSupport.emitLoadAddress(CgenSupport.ACC, (String) sTable.lookup(name), s);
                CgenSupport.emitLoad(CgenSupport.ACC, 0, CgenSupport.FP, s);
	    } else {
		//error?
            }
        } else {
            CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);
        }
    }


}


