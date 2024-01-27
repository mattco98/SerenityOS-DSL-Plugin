package me.mattco.serenityos.gml;

import com.intellij.psi.tree.IElementType;

import com.intellij.lexer.FlexLexer;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static me.mattco.serenityos.gml.GMLTypes.*;

%%

%{
  public GMLLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class GMLLexer
%extends GMLLexerBase
%implements FlexLexer
%function advance
%type IElementType
%unicode

WHITE_SPACE=[\t\n\r ]+
NUMBER=-?([1-9][0-9]*|0[Xx][0-9A-Fa-f]+|0[0-7]*)
IDENTIFIER=[A-Za-z_]\w*
COMMENT=\/\/.*|\/\*[^]*?\*
BOOLEAN=true|false

%s YYSTRING

%%
<YYINITIAL> {
    {WHITE_SPACE} { return WHITE_SPACE; }
    {COMMENT}     { return COMMENT; }

    "{"           { return OPEN_CURLY; }
    "}"           { return CLOSE_CURLY; }
    "["           { return OPEN_BRACKET; }
    "]"           { return CLOSE_BRACKET; }
    "@"           { return AT; }
    ":"           { return COLON; }
    ","           { return COMMA; }
    "::"          { return NAMESPACE; }

    "\""          { yybegin(YYSTRING); }

    {BOOLEAN}     { return BOOLEAN; }
    {IDENTIFIER}  { return IDENTIFIER; }
    {NUMBER}      { return NUMBER; }
}

<YYSTRING> {
  "\\"            { zzMarkedPos += 1; }
  <<EOF>>         { yybegin(YYINITIAL); return STRING; }
  "\n"            { yybegin(YYINITIAL); return STRING; }
  "\""            { yybegin(YYINITIAL); return STRING; }
  [^]             { }
}

[^]               { return BAD_CHARACTER; }
