package me.mattco.serenityos.gml.formatting

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import me.mattco.serenityos.gml.GMLLanguage

class GMLFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val block = GMLFormattingBlock(
            formattingContext.containingFile.node,
            null,
            null,
            null,
            buildGMLSpacingRules(formattingContext.codeStyleSettings.getCommonSettings(GMLLanguage)),
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            block,
            formattingContext.codeStyleSettings,
        )
    }
}
