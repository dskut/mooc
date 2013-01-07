/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1024;

    private StringBuffer _str = new StringBuffer(); // For assembling string constants
    private int curr_lineno = 1;
    private AbstractSymbol filename;
    private int _commentsLevel = 0;

    int get_curr_lineno() {
	    return curr_lineno;
    }

    void set_filename(String fname) {
        filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
        return filename;
    }

    /**
     * @return: 0 - for success
     *          1 - escaped null
     */
    private int unescapeString() {
        int slashIndex = 0;
        while (true) {
            slashIndex = _str.indexOf("\\", slashIndex);
            if (slashIndex == -1 || slashIndex == _str.length() - 1) {
                break;
            }
            _str.deleteCharAt(slashIndex);
            if (_str.charAt(slashIndex) == 'n') {
                _str.setCharAt(slashIndex, '\n');
            } else if (_str.charAt(slashIndex) == 't') {
                _str.setCharAt(slashIndex, '\t');
            } else if (_str.charAt(slashIndex) == 'f') {
                _str.setCharAt(slashIndex, '\f');
            } else if (_str.charAt(slashIndex) == 'b') {
                _str.setCharAt(slashIndex, '\b');
            } else if (_str.charAt(slashIndex) == '\0') {
                return 1;
            } else if (_str.charAt(slashIndex) == '\\') {
                ++slashIndex;
            }
        }
        return 0;
    }

    private int countEndBackslashes() {
        int res = 0;
        for (int i = _str.length() - 1; i >= 0; --i) {
            if (_str.charAt(i) == '\\') {
                ++res;
            } else {
                break;
            }
        }
        return res;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}


%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
	case MULTICOMMENT:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state COMMENT
%state MULTICOMMENT
%state STRING

WHITESPACE = [ \t\b\r\f\013]+
DIGIT = [0-9]
INTEGER = {DIGIT}+
SELF = self
SELF_TYPE = SELF_TYPE
TYPEID = [A-Z][A-Za-z0-9_]*
OBJECTID = [a-z][A-Za-z0-9_]*

CLASS = [cC][lL][aA][sS][sS]
INHERITS = [iI][nN][hH][eE][rR][iI][tT][sS]
IF = [iI][fF]
THEN = [tT][hH][eE][nN]
ELSE = [eE][lL][sS][eE]
FI = [fF][iI]
CASE = [cC][aA][sS][eE]
ESAC = [eE][sS][aA][cC]
WHILE = [wW][hH][iI][lL][eE]
LOOP = [lL][oO][oO][pP]
POOL = [pP][oO][oO][lL]
LET = [lL][eE][tT]
NOT = [nN][oO][tT]
IN = [iI][nN]
OF = [oO][fF]
NEW = [nN][eE][wW]
ISVOID = [iI][sS][vV][oO][iI][dD]

TRUE = t[rR][uU][eE]
FALSE = f[aA][lL][sS][eE]

%%

<YYINITIAL> {CLASS} { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL> {INHERITS} { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL> {IF} { return new Symbol(TokenConstants.IF); }
<YYINITIAL> {THEN} { return new Symbol(TokenConstants.THEN); }
<YYINITIAL> {ELSE} { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL> {FI} { return new Symbol(TokenConstants.FI); }
<YYINITIAL> {CASE} { return new Symbol(TokenConstants.CASE); }
<YYINITIAL> {ESAC} { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL> {WHILE} { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL> {LOOP} { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL> {POOL} { return new Symbol(TokenConstants.POOL); }
<YYINITIAL> {LET} { return new Symbol(TokenConstants.LET); }
<YYINITIAL> {NOT} { return new Symbol(TokenConstants.NOT); }
<YYINITIAL> {IN} { return new Symbol(TokenConstants.IN); }
<YYINITIAL> {OF} { return new Symbol(TokenConstants.OF); }
<YYINITIAL> {NEW} { return new Symbol(TokenConstants.NEW); }
<YYINITIAL> {ISVOID} { return new Symbol(TokenConstants.ISVOID); }

<YYINITIAL> {WHITESPACE} {}

<YYINITIAL> "--" { yybegin(COMMENT); }
<COMMENT> .* {}
<COMMENT> \n { 
    yybegin(YYINITIAL); 
    ++curr_lineno;
}

<YYINITIAL> "(*" { 
    yybegin(MULTICOMMENT); 
    _commentsLevel = 1;
}
<YYINITIAL> "*)" { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
<MULTICOMMENT> "(*" { ++_commentsLevel; }
<MULTICOMMENT> "*)" { 
    --_commentsLevel;
    if (_commentsLevel == 0) {
        yybegin(YYINITIAL);
    }
}
<MULTICOMMENT> \n { ++curr_lineno; }
<MULTICOMMENT> [^\*\)] {}
<MULTICOMMENT> [\*\)] {}

<YYINITIAL> \" { 
    yybegin(STRING);
    _str = new StringBuffer();
}
<STRING> \n {
    ++curr_lineno;
    if (countEndBackslashes() % 2 != 0) {
        _str.append("\n");
    } else {
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
    }
}
<STRING> [^\"] { _str.append(yytext()); }
<STRING> \" {
    if (countEndBackslashes() % 2 != 0 ) {
        // quote is escaped
        _str.deleteCharAt(_str.length() - 1);
        _str.append('\"');
    } else {
        yybegin(YYINITIAL);
        int unescapeRes = unescapeString();
        if (unescapeRes == 1) {
            return new Symbol(TokenConstants.ERROR, "String contains escaped null character.");
        }
        if (_str.indexOf("\0") != -1) {
            return new Symbol(TokenConstants.ERROR, "String contains null character.");
        }
        if (_str.length() > MAX_STR_CONST) {
            return new Symbol(TokenConstants.ERROR, "String constant too long");
        }
        String str = _str.toString();
        AbstractSymbol symbol = AbstractTable.inttable.addString(str); 
        return new Symbol(TokenConstants.STR_CONST, symbol); 
    }
} 
<YYINITIAL> {INTEGER} {
    AbstractSymbol symbol = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, symbol);
}
<YYINITIAL> {TRUE} { return new Symbol(TokenConstants.BOOL_CONST, true); }
<YYINITIAL> {FALSE} { return new Symbol(TokenConstants.BOOL_CONST, false); }

<YYINITIAL> {TYPEID} {
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}  
<YYINITIAL> {OBJECTID} {
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}

<YYINITIAL> "=>" { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL> "*" { return new Symbol(TokenConstants.MULT); }
<YYINITIAL> "(" { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL> ";" { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL> "-" { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL> ")" { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL> "<" { return new Symbol(TokenConstants.LT); }
<YYINITIAL> "," { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL> "/" { return new Symbol(TokenConstants.DIV); }
<YYINITIAL> "+" { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL> "<-" { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL> "." { return new Symbol(TokenConstants.DOT); }
<YYINITIAL> "<=" { return new Symbol(TokenConstants.LE); }
<YYINITIAL> "=" { return new Symbol(TokenConstants.EQ); }
<YYINITIAL> ":" { return new Symbol(TokenConstants.COLON); }
<YYINITIAL> "~" { return new Symbol(TokenConstants.NEG); }
<YYINITIAL> "{" { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL> "}" { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL> "@" { return new Symbol(TokenConstants.AT); }

\n { ++curr_lineno; }
. { return new Symbol(TokenConstants.ERROR, yytext()); }
