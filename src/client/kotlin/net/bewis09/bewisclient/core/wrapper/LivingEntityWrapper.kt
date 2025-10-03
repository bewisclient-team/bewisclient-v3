package net.bewis09.bewisclient.core.wrapper

import net.minecraft.entity.LivingEntity

@JvmInline
value class LivingEntityWrapper<T: LivingEntity>(val entity: T) {
    fun getText(): TextWrapper = entity.name.copy().wrap()

    var bodyYaw: Float
        get() = entity.bodyYaw
        set(value) { entity.bodyYaw = value }

    var yaw: Float
        get() = entity.yaw
        set(value) { entity.yaw = value }

    var pitch: Float
        get() = entity.pitch
        set(value) { entity.pitch = value }

    var lastHeadYaw: Float
        get() = entity.lastHeadYaw
        set(value) { entity.lastHeadYaw = value }

    var headYaw: Float
        get() = entity.headYaw
        set(value) { entity.headYaw = value }

    val height: Float
        get() = entity.height

    val scale: Float
        get() = entity.scale
}

internal fun <T: LivingEntity> T.wrap() = LivingEntityWrapper(this)