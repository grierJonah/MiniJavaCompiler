/*
 * JFlex specification for the lexical analyzer for a simple demo language.
 * Change this into the scanner for your implementation of MiniJava.
 * CSE 401/P501 Au11
 */


package Scanner;

import java_cup.runtime.*;
import Parser.sym;

%%

%public
%final
%class scanner
%unicode
%cup
%line
%column

/* Code copied into the generated scanner class.  */
/* Can be referenced in scanner action code. */
%{
  // Return new symbol objects with line and column numbers in the symbol 
  // left and right fields. This abuses the original idea of having left 
  // and right be character positions, but is   // is more useful and 
  // follows an example in the JFlex documentation.
  private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
  }

  // Return a readable representation of symbol s (aka token)
  public String symbolToString(Symbol s) {
    String rep;
    switch (s.sym) {
      
      /* OPERATORS */
      case sym.PLUS: return "PLUS";
      case sym.MINUS: return "MINUS";
      case sym.TIMES: return "TIMES";
      case sym.AND: return "AND";
      case sym.OR: return "OR";
      case sym.LESS_THAN: return "LESS_THAN";
      case sym.EQUALS: return "EQUALS";
      case sym.NOT: return "NOT";
      
      /* KEYWORDS */
      case sym.CLASS: return "CLASS";
      case sym.PUBLIC: return "PUBLIC";
      case sym.STATIC: return "STATIC";
      case sym.FINAL: return "FINAL";
      case sym.MAIN: return "MAIN";
      case sym.VOID: return "VOID";
      case sym.ARGSTRING: return "String[]";
      case sym.EXTENDS: return "EXTENDS";
      case sym.TRUE: return "TRUE";
      case sym.FALSE: return "FALSE";
      case sym.FOR: return "FOR";
      case sym.WHILE: return "WHILE";
      case sym.IF: return "IF";
      case sym.ELSE: return "ELSE";
      case sym.THIS: return "THIS";
      case sym.INT: return "INT";
      case sym.BOOLEAN: return "BOOLEAN";
      case sym.INTARRAY: return "INTARRAY";
      case sym.NEW: return "NEW";
      case sym.BECOMES: return "BECOMES";
      case sym.SEMICOLON: return "SEMICOLON";
      case sym.SYSTEMOUTPRINTLN: return "System.out.println";
      case sym.LENGTH: return "LENGTH";
      case sym.RETURN: return "RETURN";
      
      /* DELIMITERS */
      case sym.DISPLAY: return "DISPLAY";
      case sym.LPAREN: return "LPAREN";
      case sym.RPAREN: return "RPAREN";
      case sym.LBRACKET: return "LBRACKET";
      case sym.RBRACKET: return "RBRACKET";
      case sym.LCBRACKET: return "LCBRACKET";
      case sym.RCBRACKET: return "RCBRACKET";
      case sym.DOT: return "DOT";
      case sym.COMMA: return "COMMA";
      
      case sym.IDENTIFIER: return "ID(" + (String)s.value + ")";
      
      case sym.INTLITERAL: return (String)s.value;
      case sym.DOUBLELITERAL: return (String)s.value;
      
      case sym.EOF: return "<EOF>";
      case sym.error: return "<ERROR>";
      default: return "<UNEXPECTED TOKEN " + s.toString() + ">";
    }
  }
%}

/* Helper definitions */
InputCharacter = [^\r\n]
LineTerminator = \r|\n|\r\n
letter = [a-zA-Z] 

digit = [0-9]
nonZeroDigit = [1-9]

eol = [\r\n]
white = {eol}|[ \t]


/* Comments (Acquired from jflex manual) */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/" 
CommentContent = ([^*]|\*+[^/*])*

floatingPoint = (0|{nonZeroDigit})[.]{1}({digit})+

%%

/* Token definitions */


/* Reserved Words */
"display" { return symbol(sym.DISPLAY); }
"for" { return symbol(sym.FOR); }
"if" { return symbol(sym.IF); }
"else" { return symbol(sym.ELSE); }
"while" { return symbol(sym.WHILE); }
"public" { return symbol(sym.PUBLIC); }
"private" { return symbol(sym.PRIVATE); }
"static" { return symbol(sym.STATIC); }
"main" { return symbol(sym.MAIN); }
"void" { return symbol(sym.VOID); }
"new" { return symbol(sym.NEW); }
"implements" { return symbol(sym.IMPLEMENTS); }
"extends" { return symbol(sym.EXTENDS); }
"this" { return symbol(sym.THIS); }
"class" { return symbol(sym.CLASS); }
"return" { return symbol(sym.RETURN); }
"System.out.println" { return symbol(sym.SYSTEMOUTPRINTLN); }
"dot" { return symbol(sym.DOT); }


/* type values */
"String[]" { return symbol(sym.ARGSTRING); }
"int[]" { return symbol(sym.INTARRAY); }
"int" { return symbol(sym.INT); }
"boolean" { return symbol(sym.BOOLEAN); }
"double" { return symbol(sym.DOUBLE); }


/* Boolean Values */
"true" { return symbol(sym.TRUE); }
"false" { return symbol(sym.FALSE); }


/* Operators */
"+" { return symbol(sym.PLUS); }
"=" { return symbol(sym.BECOMES); }
"==" { return symbol(sym.EQUALS); }
"*" { return symbol(sym.TIMES); }
"-" { return symbol(sym.MINUS); }
"!" { return symbol(sym.NOT); }
"&&" { return symbol(sym.AND); }
"||" { return symbol(sym.OR); }
"<" { return symbol(sym.LESS_THAN); }


/* Delimiters */
";" { return symbol(sym.SEMICOLON); }
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
"[" { return symbol(sym.LBRACKET); }
"]" { return symbol(sym.RBRACKET); }
"{" { return symbol(sym.LCBRACKET); }
"}" { return symbol(sym.RCBRACKET); }
"," { return symbol(sym.COMMA); }
"." { return symbol(sym.DOT); }


/* Identifiers */
{letter} ({letter}|{digit}|_)* { return symbol(sym.IDENTIFIER, yytext()); }

/* Integer Literal */
{digit}|{nonZeroDigit}{digit}* { return symbol(sym.INTLITERAL, yytext()); }		// [0-9] | [1-9][0-9]*

/* Part 5: Added Double Recognition */
//{floatingPoint}* { return symbol(sym.DOUBLELITERAL, yytext()); }


/* Whitespace */
{white}+ { /* ignore whitespace */ }
{TraditionalComment} | {EndOfLineComment} | {Comment} { /* ignore whitespace */ }


/* lexical errors (put last so other matches take precedence) */
. { System.err.println(
	"\nunexpected character in input: '" + yytext() + "' at line " +
	(yyline+1) + " column " + (yycolumn+1));
  }
