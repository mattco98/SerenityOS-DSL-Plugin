package me.mattco.serenityos.gml.formatting

import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import me.mattco.serenityos.gml.GMLLanguage

class GMLLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = GMLLanguage

    override fun getCodeSample(settingsType: SettingsType): String {
        return "TODO: Add code sample here"
    }
}
