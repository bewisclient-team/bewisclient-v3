package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.features.sidebar.Home.addToQuickSettings
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.ObjectSetting

open class Feature(val id: Identifier): ObjectSetting() {
    fun <T: Renderable> T.addToQuickSettings(feature: Feature, quickSettingsId: String): T {
        return addToQuickSettings("${feature.id.namespace}.category.${feature.id.path}", quickSettingsId)
    }

    open fun createTranslation(key: String, translation: String): Translation {
        return Translation(id.namespace, "${id.path}.${key}", translation)
    }
}