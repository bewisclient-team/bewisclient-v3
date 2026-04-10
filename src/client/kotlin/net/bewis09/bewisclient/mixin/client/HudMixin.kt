// @VersionReplacement

package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.version.GuiGraphics
import net.bewis09.bewisclient.version.Hud
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.functionalities.HeldItemTooltip
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings
import net.bewis09.bewisclient.impl.settings.functionalities.ScoreboardSettings
import net.bewis09.bewisclient.impl.settings.functionalities.ScoreboardSettings.scale
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.minecraft.client.gui.Font
import net.minecraft.world.item.ItemStack
import net.minecraft.world.scores.Objective
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Hud::class)
abstract class HudMixin: BewisclientInterface {
    @Shadow
    abstract fun getFont(): Font?

    @Shadow
    private val toolHighlightTimer = 0

    @Shadow
    private val lastToolHighlight: ItemStack? = null

    // @[1.21.11] "renderSelectedItemName" @[] "extractSelectedItemName"
    @Inject(method = [/*[@]*/"extractSelectedItemName"/*[!@]*/], at = [At("HEAD")], cancellable = true)
    private fun bewisclientRenderHeldItemTooltip(drawContext: GuiGraphics, ci: CallbackInfo) {
        if (HeldItemTooltipSettings.isEnabled()) {
            HeldItemTooltip.render(ScreenDrawing(drawContext, getFont()!!), toolHighlightTimer, lastToolHighlight!!)
            ci.cancel()
        }
    }

    @Inject(method = ["displayScoreboardSidebar"], at = [At("HEAD")])
    private fun bewisclientRenderScoreboardSidebar(guiGraphics: GuiGraphics, objective: Objective?, ci: CallbackInfo?) {
        val scale = if (ScoreboardSettings.isEnabled()) scale.get() else 1.0f

        val screenDrawing = ScreenDrawing(guiGraphics, getFont()!!)

        screenDrawing.push()
        screenDrawing.scale(scale, scale)
        screenDrawing.translate((-screenWidth).toFloat() * (1.0f - 1 / scale), (-screenHeight).toFloat() * (1.0f - 1 / scale) / 2.0f)
    }

    @Inject(method = ["displayScoreboardSidebar"], at = [At("RETURN")])
    private fun bewisclientRenderScoreboardSidebarReturn(guiGraphics: GuiGraphics, objective: Objective?, ci: CallbackInfo?) {
        ScreenDrawing(guiGraphics, getFont()!!).pop()
    }
}