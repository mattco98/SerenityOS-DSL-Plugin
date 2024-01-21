package me.mattco.serenityos.gml.psi.mixins

// TODO: This doesn't work at all, in theory the code is correct, but it always leads to
//       a JobCancelledException when resolving the GMLEndpoint

// import com.intellij.lang.ASTNode
// import com.intellij.openapi.progress.ProgressManager
// import com.intellij.psi.PsiElement
// import com.intellij.psi.PsiReferenceService
// import com.intellij.psi.util.parentOfType
// import com.jetbrains.cidr.lang.psi.OCElement
// import com.jetbrains.cidr.lang.psi.OCStructLike
// import com.jetbrains.cidr.lang.symbols.OCResolveContext
// import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
// import me.mattco.serenityos.gml.psi.GMLNamedElement
// import me.mattco.serenityos.gml.psi.api.GMLComponent
// import me.mattco.serenityos.gml.psi.api.GMLPropertyIdentifier
// import me.mattco.serenityos.gml.psi.singleRef
//
// abstract class GMLPropertyIdentifierMixin(node: ASTNode) : GMLNamedElement(node), GMLPropertyIdentifier {
//     override fun getReference() = singleRef { resolveCppDecl() }
//
//     private fun resolveCppDecl(): OCElement? {
//         val p1 = parentOfType<GMLComponent>() ?: return null
//         val p2: PsiElement?
//         try {
//             p2 = PsiReferenceService.getService().getReferences(p1, PsiReferenceService.Hints.NO_HINTS).map {
//                 it.resolve()
//             }.firstNotNullOfOrNull { it }
//         } catch (e: Throwable) {
//             throw e
//         }
//         val p3 = p2 as? OCStructLike ?: return null
//         val sym = p3.symbol ?: return null
//         val ctx = OCResolveContext.forPsi(p3)
//         var setter = findSetter(sym)
//         if (setter != null)
//             return setter
//         sym.processAllBaseClasses({ s, _ ->
//             setter = findSetter(s as? OCStructSymbol ?: return@processAllBaseClasses true)
//             setter == null
//         }, ctx)
//         return setter
//     }
//
//     private fun findSetter(symbol: OCStructSymbol): OCElement? {
//         return symbol.findMember("set_${identifier.text}")?.locateDefinition(project) as? OCElement
//     }
// }
