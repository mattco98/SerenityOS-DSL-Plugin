package me.mattco.serenityos.gml.formatting

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import me.mattco.serenityos.gml.GMLLanguage

class GMLCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createCustomSettings(settings: CodeStyleSettings) = Settings(settings)

    override fun getConfigurableDisplayName() = GMLLanguage.displayName

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, this.configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return Panel(currentSettings, settings)
            }
        }
    }

    class Settings(
        settings: CodeStyleSettings,
    ) : CustomCodeStyleSettings("GMLCodeStyleSettings", settings)

    class Panel(
        currentSettings: CodeStyleSettings,
        settings: CodeStyleSettings,
    ) : TabbedLanguageCodeStylePanel(GMLLanguage, currentSettings, settings)
}
