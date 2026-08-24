package net.bewis09.bewisclient.features.cosmetics

import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.structure.Grid
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.Bewisclient
import net.minecraft.network.chat.Component

object AcceptPrivacyPage : SimpleRenderable() {
    val headerText = Translation("privacy.header", "Read and accept privacy notice to continue")
    val decline = Translation("privacy.reject", "Reject privacy notice")
    val accept = Translation("privacy.accept", "Accept privacy notice and enable online mode")

    val notice = """
        By enabling the online mode of Bewisclient some of your data is sent to the Bewisclient servers in the following manner:
        
        When starting the game:
        - One request containing your uuid is sent to the Bewisclient servers. Authorization is the same as when you are joining a Minecraft server, no private data is sent to the Bewisclient servers.
        - The time and quantity of you joining does not get saved, although the total amount of players starting the game in online mode is stored.
        
        When selecting a cosmetic:
        - One request including your uuid and the selected cosmetics is sent to the Bewisclient servers.
        - When you deselect all cosmetics, your uuid is removed from the database, so no data is stored when you are not using any cosmetics.
        
        How the data is sent to other players:
        - When someone starts the game in online mode, they get a list of all players that have cosmetics enabled, including the cosmetics they have selected.
        - This list does not contain the direct uuids, but a sha-256 hash of the uuid, so you can only check if a specific uuid has cosmetics enabled, but you cannot get a list of all uuids.
        
        For the backend of the online mode, we use cloudflare workers and a cloudflare d1 database.
        
        The data is stored in a secure database and is only used for the purpose of enabling online features such as cosmetic syncing and special cosmetics. The data is not getting shared with any third parties and is only accessible by the Bewisclient team.
        When using Bewisclient in online mode, all data is sent over a secure connection and is handled in accordance with the applicable data protection laws. You can request the deletion of your data at any time by contacting the Bewisclient support team.
    """.trimIndent()

    fun openPrivacyPage() {
        val screen = Bewisclient.getCurrentRenderableScreen()?.renderable as? OptionScreen ?: return

        screen.openPage(
            headerText(),
            AcceptPrivacyPage
        )
    }

    override fun init() {
        addRenderable(Grid {
            gap = 5
            fitType = Grid.FitType.SCROLL
            init = {
                listOf(
                    Grid {
                        gap = 2
                        children = listOf(
                            InfoTextRenderable { text = Component.literal(notice) },
                            SimpleRenderable(),
                            Button {
                                text = decline()
                                dark = true
                                onClick = onClick@{
                                    val screen = Bewisclient.getCurrentRenderableScreen()?.renderable as? OptionScreen ?: return@onClick
                                    screen.goBack()
                                    General.acceptedEULA.set(false)
                                }
                            }.updateSize(100, SelectiveScreenDrawer.getSideButtonHeight()),
                            Button {
                                text = accept()
                                onClick = onClick@{
                                    General.acceptedEULA.set(true)
                                    General.onlineMode.set(true)
                                    val screen = Bewisclient.getCurrentRenderableScreen()?.renderable as? OptionScreen ?: return@onClick
                                    screen.goBack()
                                }
                            }.updateSize(100, SelectiveScreenDrawer.getSideButtonHeight())
                        )
                    }
                )
            }
        }(x, y, width, height))
    }
}