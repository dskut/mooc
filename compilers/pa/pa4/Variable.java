
import java.util.*;
import java.io.PrintStream;

abstract class Variable {
    public abstract void emitRef(PrintStream s);
    public abstract void emitAssign(PrintStream s);
}

class AttrVariable extends Variable {
    private int offset;

    public AttrVariable(int offset) {
        this.offset = offset + CgenSupport.DEFAULT_OBJFIELDS;
    }

    public void emitRef(PrintStream s) {
        CgenSupport.emitLoad(CgenSupport.ACC, offset, CgenSupport.SELF, s);
    }

    public void emitAssign(PrintStream s) {
        CgenSupport.emitStore(CgenSupport.ACC, offset, CgenSupport.SELF, s);
    }
}

class FormalVariable extends Variable {
    private int offset;

    public FormalVariable(int offset) {
        this.offset = offset;
    }

    public void emitRef(PrintStream s) {
        CgenSupport.emitLoad(CgenSupport.ACC, -offset, CgenSupport.FP, s);
    }

    public void emitAssign(PrintStream s) {
        CgenSupport.emitStore(CgenSupport.ACC, -offset, CgenSupport.FP, s);
    }
}

class LetVariable extends Variable {
    private int offset;

    public LetVariable(int offset) {
        this.offset = offset;
    }

    public void emitRef(PrintStream s) {
        CgenSupport.emitLoad(CgenSupport.ACC, -offset, CgenSupport.FP, s);
    }

    public void emitAssign(PrintStream s) {
        CgenSupport.emitStore(CgenSupport.ACC, -offset, CgenSupport.FP, s);
    }
}

class BranchVariable extends Variable {
    private int offset;

    public BranchVariable(int offset) {
        this.offset = offset;
    }

    public void emitRef(PrintStream s) {
        CgenSupport.emitLoad(CgenSupport.ACC, -offset, CgenSupport.FP, s);
    }

    public void emitAssign(PrintStream s) {
        CgenSupport.emitStore(CgenSupport.ACC, -offset, CgenSupport.FP, s);
    }
}
