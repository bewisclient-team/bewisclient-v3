package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.core.beforeHeadYaw
import net.minecraft.entity.LivingEntity
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan

interface EntityDrawing : ScreenDrawingInterface {
    fun drawEntity(
        x1: Int, y1: Int, x2: Int, y2: Int, size: Int, scale: Float, mouseX: Float, mouseY: Float, entity: LivingEntity
    ) {
        val f = (x1 + x2).toFloat() / 2.0f
        val g = (y1 + y2).toFloat() / 2.0f
        enableScissors(x1, y1, x2 - x1, y2 - y1)
        val h = atan(((f - mouseX) / 40.0f).toDouble()).toFloat()
        val i = atan(((g - mouseY) / 40.0f).toDouble()).toFloat()
        val quaternion = Quaternionf().rotateZ(Math.PI.toFloat())
        val quaternion2 = Quaternionf().rotateX(i * 20.0f * (Math.PI.toFloat() / 180))
        quaternion.mul(quaternion2)
        val j = entity.bodyYaw
        val k = entity.yaw
        val l = entity.pitch
        val m = entity.beforeHeadYaw
        val n = entity.headYaw
        entity.bodyYaw = 180.0f + h * 20.0f
        entity.yaw = 180.0f + h * 40.0f
        entity.pitch = -i * 20.0f
        entity.headYaw = entity.yaw
        entity.beforeHeadYaw = entity.yaw
        val o = entity.scale
        val vector3f = Vector3f(0.0f, entity.height / 2.0f + scale * o, 0.0f)
        val p = size.toFloat() / o
        core.drawEntity(x1, y1, x2, y2, p, vector3f, quaternion, quaternion2, entity)
        entity.bodyYaw = j
        entity.yaw = k
        entity.pitch = l
        entity.beforeHeadYaw = m
        entity.headYaw = n
        disableScissors()
    }
}