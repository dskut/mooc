/*
	sample simple scanner

Compile with:
java -classpath /afs/ir/class/cs143/cool/sp12/lib/jlex.jar JLex.Main sample.jlex
javac sample.jlex.java

Run with:
java Sample < input

*/

import java.lang.System;

class Sample {
    public static void main(String argv[]) throws java.io.IOException {
    Yylex yy = new Yylex(System.in);
    yy.yylex();
  }
}
%%

%{
private int num_lines = 0;
%}
%integer
%state COMMENT
%%
\n                      { num_lines++; }
\\                      { System.out.print("LAMBDA "); }
[A-Za-z][A-Za-z0-9]*    { System.out.print("VAR: " + yytext()); }
"."                     { System.out.print("DOT "); }
"("                     { System.out.print("OPEN "); }
")"                     { System.out.print("CLOSE "); }
"+"                     { System.out.print("PLUS "); }
"*"                     { System.out.print("MULT "); }
[0-9]+                  { System.out.print("NUM: " + yytext()); }
[ \t]+                  { /* empty block */ }
.                       { System.out.print("Invalid character on line " + num_lines + "\n"); }
"//"                    { yybegin(COMMENT); }
<COMMENT>.*             { /* need an empty block here */ }
<COMMENT>\n             { num_lines++; yybegin(YYINITIAL); }


