// @VersionReplacement

package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.settings.structure.ImageFeature
import net.bewis09.bewisclient.settings.types.BooleanMapSetting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

object TntTimer : ImageFeature("tnt_timer", "TNT Timer") {
    val enabledEntities = create("entities", BooleanMapSetting())

    fun getNameTagForEntity(entity: Entity): Component? {
        val fuse = getFuseForEntity(entity) ?: return null
        if (fuse <= 0) return null
        return Component.literal(((fuse / 2f).toInt() / 10f).toString().let { if (it.contains(".")) it else "$it.0" } + "s")
    }

    fun getFuseForEntity(entity: Entity) = entityTypes.firstOrNull { it.type == entity.type }?.invoke(entity)

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(MultipleBooleanSettingsRenderable(createTranslation("entities", "Show Timer For:"), null) {
            entityTypes.map { MultipleBooleanSettingsRenderable.Part(Component.translatable(it.type.toString()), null, it) }
        })
    }

    val entityTypes = APIEntrypointLoader.mapEntrypoint { it.getTntTimerEntities() }.flatten()

    class FuseProvider<T : Entity>(val type: EntityType<T>, val defaultEnabled: Boolean, val fuseProvider: (T) -> Int): SettingInterfaceWithDefault<Boolean> {
        fun invoke(entity: Entity): Int? {
            if (!get()) return null
            @Suppress("UNCHECKED_CAST")
            return fuseProvider(entity as? T ?: return null)
        }

        override fun get() = enabledEntities[type.toString()] ?: defaultEnabled
        override fun set(value: Boolean?) = enabledEntities.set(type.toString(), value)
        override fun getDefault(): Boolean = defaultEnabled
    }
}