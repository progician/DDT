Ⓗ▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂
#@IF_CONDITION《
 ►#?STRUCTURE_EXPECTED!【 #error(EXP_OPEN_PARENS)¤【】 #@BREAK● StatementIf( #@BREAK】●
 ►#?STRUCTURE_EXPECTED!【 ( #@EXP_NOREFS_SAMPLE #@CLOSEPARENS_OR_BREAK ● StatementIf( #@EXP_NOREFS_SAMPLE #@CLOSEPARENS_OR_BREAK】●
 ►#?STRUCTURE_EXPECTED!【 ( #@EXP_ANY #?EXP__DECL_AMBIG【#:DISCARD_CASE】) ● StatementIf( #@EXP_ANY 】●

 ►#?STRUCTURE_EXPECTED!【(auto defId #error(EXP_ASSIGN)【】 ) ●StatementIfVar( VariableDefWithInit(AutoRef DefSymbol )】●
 ►#?STRUCTURE_EXPECTED!【(auto defId = #@EXP_ANY__LITE) ●StatementIfVar( VariableDefWithInit(AutoRef DefSymbol #@EXP_ANY__LITE)】●
 ►#?STRUCTURE_EXPECTED!【(#@TYPEREF_NBNE       defId = 123)                ●StatementIfVar( VariableDefWithInit(#@TYPEREF_NBNE DefSymbol Integer)】●
 ►#?STRUCTURE_EXPECTED!【(#@TYPEREF_NBNE__LITE  #?TYPE_REF_POINTER【#:DISCARD_CASE】 defId #error(EXP_ASSIGN)【】) ●StatementIfVar( VariableDefWithInit(#@TYPEREF_NBNE__LITE DefSymbol)】●
 ►#?STRUCTURE_EXPECTED!【(#@TYPEREF_NBNE__LITE #?TYPE_REF_POINTER!【#:DISCARD_CASE】 defId #parser(AllowAnyErrors) ) ●StatementIf( *】●
 ►#?STRUCTURE_EXPECTED!【(xxxA * defId ) ●StatementIf( ExpInfix(#@ExpIdentifier #@ExpIdentifier) 】●
 ►#?STRUCTURE_EXPECTED!【(const xxxB * defId #error(EXP_ASSIGN)【】 ) ●StatementIfVar( VariableDefWithInit(RefTypeModifier(#@_FOO_PTR) DefSymbol) 】●

 ►#?STRUCTURE_EXPECTED!【(#@TYPEREF_NBParens #?CANT_FULLY_EXP_PARSE【#:DISCARD_CASE】 #parser(AllowAnyErrors) )●StatementIf( * 】●
¤》
▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂ 
#PARSE(STATEMENT)    if #@IF_CONDITION  #@ST__SAMPLE
#STRUCTURE_EXPECTED:    #@IF_CONDITION  #@ST__SAMPLE )
Ⓗ▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂
#@IF_BODY《
 ►#?STRUCTURE_EXPECTED!【 #@ST__ANY ●               #@ST__ANY 】●
 ►#?STRUCTURE_EXPECTED!【 #@ST__NB else #@ST__ANY ● #@ST__NB #@ST__ANY 】●
¤》
▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂ 
#PARSE(STATEMENT)    if ( #@EXP_ANY__LITE ) #@IF_BODY
#STRUCTURE_EXPECTED: StatementIf( #@EXP_ANY__LITE #@IF_BODY )

▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂  dangling else
#PARSE(STATEMENT)    if ( #@EXP_ANY__LITE ) if ( #@EXP_ANY__LITE ) #@ST__SAMPLE else #@ST__SAMPLE
#STRUCTURE_EXPECTED: StatementIf( #@EXP_ANY__LITE StatementIf(#@EXP_ANY__LITE #@ST__SAMPLE #@ST__SAMPLE) )
