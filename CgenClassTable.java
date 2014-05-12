/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;

/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;
    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;
	
	private int currClassTag = 0;
	private int getNextClassTag() {
		return currClassTag++;
	}
	
	class ClassInfo {
		public int classTag;
		public CgenNode node;
		public ArrayList<attr> attributes;
		public LinkedHashMap<method, CgenNode> methods;
		public int offset = 0;
		ClassInfo(CgenNode n) {
			classTag = getNextClassTag();
			node = n;
			attributes = new ArrayList<attr>();
			methods = new LinkedHashMap<method, CgenNode>();
		}
	}

	public int getMaxTag(AbstractSymbol curr_class) {
    	    ClassInfo curr_class_info = class_ToClassInfo.get(curr_class);
    	    int maxTag = curr_class_info.classTag;
            for (Enumeration e = curr_class_info.node.getChildren(); e.hasMoreElements(); ) {
	        CgenNode child_node = (CgenNode) e.nextElement();
	        int newMax = getMaxTag(child_node.getName());
	        if (maxTag < newMax) {
	            maxTag = newMax;
	        }
	    }
	    return maxTag;
	}
		
    private HashMap<AbstractSymbol, ClassInfo> class_ToClassInfo = new HashMap<AbstractSymbol, ClassInfo>();

    public ArrayList<attr> getClassInfoAttr(AbstractSymbol curr_class) {
		ClassInfo curr_class_info = class_ToClassInfo.get(curr_class);
		if (curr_class_info.attributes != null) {
			return curr_class_info.attributes;
		}
		return null;
		
    }

    public ClassInfo getClassInfo(AbstractSymbol curr_class) {
    	ClassInfo curr_class_info = class_ToClassInfo.get(curr_class);
    	return curr_class_info;
    }
	
    private int labelNum = -1;

    public int getLabelNum() {
        labelNum++;
        return labelNum;
    }
    private int exprOffset = 0;
    public int getExprOffset() {
        return exprOffset;
    }
    public void incExprOffset() {
        exprOffset += 4;
    }
    public void decExprOffset() {
        exprOffset -= 4;
    }
    public void zeroExprOffset() {
    	exprOffset = 0;
    }
    public int methodOffset(AbstractSymbol className, AbstractSymbol methodName) {
        ClassInfo curr_nodeCI = class_ToClassInfo.get(className);
        int i = 0;
        for (method currMethod : curr_nodeCI.methods.keySet()) {
            if (methodName == currMethod.name) {
                return i;
            }
            i++;
        }
        return -1;
    }
    
    private void populateClass_ToClassInfo() {
        for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
            CgenNode n = (CgenNode) e.nextElement();
            class_ToClassInfo.put(n.getName(), new ClassInfo(n));
        }
    }
    
    private void populateFeatures() {
        //step through all children using stack
        Stack<CgenNode> stack = new Stack<CgenNode>();
        stack.push(root());
        while(!stack.isEmpty()) {
            CgenNode currNode = (CgenNode) stack.pop();
            CgenNode parent = currNode.getParentNd();
            ClassInfo currNdInfo = class_ToClassInfo.get(currNode.getName());
            
            //if not Object, add parent methods
            if (parent.getName() != TreeConstants.No_class) {
                ClassInfo parentInfo = class_ToClassInfo.get(parent.getName());
                for (Enumeration e = Collections.enumeration(parentInfo.attributes); e.hasMoreElements(); ) {
                    currNdInfo.attributes.add((attr) e);
                }
                for (method m : parentInfo.methods.keySet()) {
                    currNdInfo.methods.put(m, parentInfo.methods.get(m));
                }
            }
            
            //add current features
	    //overwrites because it should already be in there
            for (Enumeration e = currNode.getFeatures().getElements(); e.hasMoreElements(); ) {
                Object currFeat = e.nextElement();
                if (currFeat instanceof method) {
                    currNdInfo.methods.put((method) currFeat, currNode);
                }
                if (currFeat instanceof attr) {
                    currNdInfo.attributes.add((attr) currFeat);
                }
            }
            
            //add remaining children
            for (Enumeration e = currNode.getChildren(); e.hasMoreElements(); ) {
                CgenNode child = (CgenNode)e.nextElement();
                stack.push(child);
            }
        }
    }
    
    private void codeClass_NameObjectDispatch_Tables() {
        
        //Code the Name Table
        str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
        //str.print("#CLASS NAME TABLE:\n");
        for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
            CgenNode curr_node = (CgenNode) e.nextElement();
            StringSymbol name = (StringSymbol) AbstractTable.stringtable.lookup(curr_node.getName().getString());
            str.print(CgenSupport.WORD);
            name.codeRef(str);
            str.print("\n");
        }

        //Code the Object Table
        str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
        //str.print("#CLASS OBJECT TABLE\n");
        for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
            CgenNode curr_node = (CgenNode) e.nextElement();
            str.print(CgenSupport.WORD + curr_node.getName() + CgenSupport.PROTOBJ_SUFFIX + "\n");
            str.print(CgenSupport.WORD + curr_node.getName() + CgenSupport.CLASSINIT_SUFFIX + "\n");
        }

        //Code the Dispatch Table
        //str.print("#DISPATCH TABLE\n");
        for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
            CgenNode curr_node = (CgenNode) e.nextElement();
            str.print(curr_node.getName() + CgenSupport.DISPTAB_SUFFIX + CgenSupport.LABEL);
            ClassInfo curr_class = class_ToClassInfo.get(curr_node.getName());
            for (method m : curr_class.methods.keySet()) {
                str.print(CgenSupport.WORD + curr_class.methods.get(m).getName() + "." + m.name.getString() + "\n");
            }
        }

    }

    
    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

        // Add globals for prototype objects

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + stringclasstag);

    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD 
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Add constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	AbstractTable.inttable.addString("0");

        
	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);
    }


    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_c(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_c(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));
	
	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_c(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_c Object_class = 
	    new class_c(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_c IO_class = 
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	CgenNode IO_node = new CgenNode(IO_class, CgenNode.Basic, this);
	installClass(IO_node);

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_c Int_class = 
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_c Bool_class = 
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_c Str_class =
	    new class_c(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }
	
    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.
    
    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
	    installClass(new CgenNode((Class_)e.nextElement(), 
				       CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
	nds = new Vector();

	this.str = str;
	
        
	//stringclasstag = 2 /* Change to your String class tag here*/ ;
	//intclasstag =    3 /* Change to your Int class tag here*/ ;
	//boolclasstag =   4 /* Change to your Bool class tag here*/ ;
	
	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");
	
	installBasicClasses();
	installClasses(cls);
	buildInheritanceTree();

	//creates HashMap of AbstractSymbols to CgenNodes
	populateClass_ToClassInfo(); 
	
        stringclasstag = class_ToClassInfo.get(TreeConstants.Str).classTag;
        intclasstag = class_ToClassInfo.get(TreeConstants.Int).classTag;
        boolclasstag = class_ToClassInfo.get(TreeConstants.Bool).classTag;
        
     	//set tags -- already done when we make the hashmap
	//set features - attr and methods
	populateFeatures();
	
	exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

	codeClass_NameObjectDispatch_Tables();
    
    //                 Add your code to emit
	//                   - prototype objects
	//                   - class_nameTab
	//                   - dispatch tables

        codePrototypeObjectsAndBasics();

	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

	//                 Add your code to emit
	//                   - object initializer
	//                   - the class methods
	//                   - etc...
        Initialize();
    }

    public void codePrototypeObjectsAndBasics() {
        
        for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
            CgenNode curr_node = (CgenNode) e.nextElement();
            str.println(CgenSupport.WORD + "-1");

            ClassInfo curr_class = class_ToClassInfo.get(curr_node.getName());
            str.print(curr_node.getName() + CgenSupport.PROTOBJ_SUFFIX + CgenSupport.LABEL);
            str.println(CgenSupport.WORD + curr_class.classTag);
            str.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + curr_class.attributes.size()));
            str.println(CgenSupport.WORD + curr_node.getName() + CgenSupport.DISPTAB_SUFFIX);

            for (Enumeration e_att = Collections.enumeration(curr_class.attributes); e_att.hasMoreElements(); ) {
                attr curr_attr = (attr) e_att.nextElement();
                str.print(CgenSupport.WORD);
                if (curr_attr.type_decl == TreeConstants.Bool) {
                    BoolConst b = new BoolConst(false);
                    b.codeRef(str);
                }
                else if (curr_attr.type_decl == TreeConstants.Str) {
                    StringSymbol s = (StringSymbol) (AbstractTable.stringtable.lookup(""));
                    s.codeRef(str);
                }
                else if (curr_attr.type_decl == TreeConstants.Int) {
                    IntSymbol i = (IntSymbol) (AbstractTable.inttable.addInt(0));
                    i.codeRef(str);
                }
                else {
                    str.print("0");
                }
                str.print("\n");
            }
        }
    }
    public void Initialize() {
        for (Enumeration e = nds.elements(); e.hasMoreElements();) {
            CgenNode curr_node = (CgenNode) e.nextElement();
            ClassInfo curr_nodeCI = class_ToClassInfo.get(curr_node.getName());
            //Update symbol table somehow?
            //Since cgenclass table extends symtable ill just use symtables methods and hope our current cgentable keeps track of it appropriately
            enterScope();
            int count = 0;
            for (Enumeration e_attr = Collections.enumeration(curr_nodeCI.attributes); e_attr.hasMoreElements();) {
                    attr curr_attr = (attr) e_attr.nextElement();
                    int attr_offset = (4 * count++) + 12;
                    addId(curr_attr.name, "" + attr_offset + "($s0)");
            }
            
            str.print(curr_node.getName() + CgenSupport.CLASSINIT_SUFFIX + CgenSupport.LABEL);

            //emit initial code
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -12, str);
            CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP, str);
            CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP, str);
            CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP, str);
            CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 16, str);
            CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);
            
            //get parent node
            CgenNode curr_nodeParent = curr_node.getParentNd();
            ClassInfo curr_nodeParentCI = class_ToClassInfo.get(curr_nodeParent.getName());

            int currAttrs = curr_nodeCI.attributes.size();
            int parentAttrs = 0;
            //if not no class, then jal to parents attributes
            if (curr_nodeParent.getName() != TreeConstants.No_class) {
                CgenSupport.emitJal(curr_nodeParent.getName() + CgenSupport.CLASSINIT_SUFFIX, str);
                parentAttrs = curr_nodeParentCI.attributes.size();
            }

            for (int i = parentAttrs; i < currAttrs; i++) {
                attr curr_attr = curr_nodeCI.attributes.get(i);
                
                //code expression curr_attr.init.code(all the shit)
                //needs to be completed in cooltree
                if (!(curr_attr.init instanceof no_expr)) {
                    
                    int offset = 3 + i;
                    CgenSupport.emitStore(CgenSupport.ACC, offset,
                                          CgenSupport.SELF, str);
		  
                    
                   
                }
            }

            CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str);
            CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, str);
            CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, str);
            CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, str);
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 12, str);
            CgenSupport.emitReturn(str);

            
            exitScope();
    }
}
                            
            
            
    /** Gets the root of the inheritance tree */
    public CgenNode root() {
	return (CgenNode)probe(TreeConstants.Object_);
    }
}
			  
    
