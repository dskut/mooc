import java.io.PrintStream;
import java.util.*;

/** This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.  */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    private List<class_c> basicClasses;
    private List<class_c> classList;
    //private Map<AbstractSymbol, List<AbstractSymbol>> classChildren;

    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
        AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");
	
	// The following demonstrates how to create dummy parse trees to
	// refer to basic Cool classes.  There's no need for method
	// bodies -- these are already built into the runtime system.

	// IMPORTANT: The results of the following expressions are
	// stored in local variables.  You will want to do something
	// with those variables at the end of this method to make this
	// code meaningful.

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

	/* Do somethind with Object_class, IO_class, Int_class,
           Bool_class, and Str_class here */
        basicClasses = new ArrayList<class_c>();

		basicClasses.add(Object_class);
		basicClasses.add(IO_class);
		basicClasses.add(Int_class);
		basicClasses.add(Bool_class);
		basicClasses.add(Str_class);

        /*
        addChild(TreeConstants.Object_, TreeConstants.IO);
        addChild(TreeConstants.Object_, TreeConstants.Int);
        addChild(TreeConstants.Object_, TreeConstants.Bool);
        addChild(TreeConstants.Object_, TreeConstants.Str);
        */
    }
	
    // FIXME: use map!
    private class_c getBasicClass(AbstractSymbol name) {
        for (class_c cl: basicClasses) {
            if (cl.getName() == name) {
                return cl;
            }
        }
        return null;
    }

    // FIXME: use map!
    private class_c getNonBasicClass(AbstractSymbol name) {
        for (class_c cl: classList) {
            if (cl.getName() == name) {
                return cl;
            }
        }
        return null;
    }

    /*
    private void addChild(AbstractSymbol parent, AbstractSymbol child) {
        List<AbstractSymbol> children = classChildren.get(parent);
        if (children == null) {
            children = new ArrayList<AbstractSymbol>();
        }
        children.add(child);
        classChildren.put(parent, children); // TODO: ????
    }
    */

    public ClassTable(Classes classes) {
        semantErrors = 0;
        errorStream = System.err;
        
        /* fill this in */
        classList = new ArrayList<class_c>();
        //classChildren = new HashMap<AbstractSymbol, List<AbstractSymbol>>();
        installBasicClasses();

        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            class_c cl = (class_c)e.nextElement();
            AbstractSymbol parent = cl.getParent();
            AbstractSymbol name = cl.getName();
            Features features = cl.getFeatures(); 
            if (parent == TreeConstants.Bool || parent == TreeConstants.Str ||
            	parent == TreeConstants.SELF_TYPE) 
            {
                semantError(cl).println("Class " + name + " cannot inherit class " + parent + ".");
                continue;
            }
            if (name == TreeConstants.SELF_TYPE || getBasicClass(name) != null) {
                semantError(cl).println("Redefinition of basic class " + name + ".");
                continue;
            }
            if (getNonBasicClass(name) != null) {
                semantError(cl).println("Class " + name + " was previously defined.");
                continue;
            }
            classList.add(cl);
            /*
            if (parent == TreeConstants.No_class) {
                parent = TreeConstants.Object_;
            }
            addChild(parent, name);
            */
        }
    }

    /** Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(class_c c) {
        return semantError(c.getFilename(), c);
    }

    /** Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return semantError();
    }

    /** Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError() {
        semantErrors++;
        return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
        return semantErrors != 0;
    }
    
    public List<class_c> getClassList() {
    	return classList;
    }

    public class_c getClass(AbstractSymbol name) {
        class_c basicClass = getBasicClass(name);
        if (basicClass != null) {
            return basicClass;
        }
        return getNonBasicClass(name);
    }

    public boolean hasClass(AbstractSymbol name) {
        return getClass(name) != null;
    }

    public boolean hasBasicClass(AbstractSymbol name) {
        return getBasicClass(name) != null;
    }

    public boolean isBase(AbstractSymbol base, AbstractSymbol derived) {
        return leastCommonAncestor(base, derived) == base;
    }

    private int depth(class_c cl) {
        return cl.getName() == TreeConstants.Object_ ? 0 : depth(getClass(cl.getParent())) + 1;
    }

    public AbstractSymbol leastCommonAncestor(AbstractSymbol first, AbstractSymbol second) {
        if (Flags.semant_debug) {
            System.err.println("lca: first = " + first);
            System.err.println("lca: second = " + second);
            return first;
        }
        class_c u = (class_c)getClass(first).copy();
        class_c v = (class_c)getClass(second).copy();
        int h1 = depth(u);
        int h2 = depth(v);

        while (h1 != h2) {
            if (h1 > h2) {
                u = (class_c)getClass(u.getParent()).copy();
                --h1;
            } else {
                v = (class_c)getClass(v.getParent()).copy();
                --h2;
            }
        }

        while (u.getName() != v.getName()) {
            u = (class_c)getClass(u.getParent()).copy();
            v = (class_c)getClass(v.getParent()).copy();
        }

        return u.getName();
    }

    public AbstractSymbol leastCommonAncestor(List<AbstractSymbol> types) {
        AbstractSymbol res = types.get(0);
        for (int i = 1; i < types.size(); ++i) {
            res = leastCommonAncestor(res, types.get(i));
        }
        return res;
    }

    public void fillMethodsAttrTables() {
        for (class_c cl: basicClasses) {
            cl.fillMethodsAttrTables(this);
        }

        for (class_c cl: classList) {
            cl.fillMethodsAttrTables(this);
        }
    }

    public SymbolTable getMethodsTable(AbstractSymbol type) {
        //System.err.println("type = " + type);
        return getClass(type).getMethodsTable();
    }
}
			  
    
