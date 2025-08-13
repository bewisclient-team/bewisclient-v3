package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.renderables.option_screen.SidebarCategory
import net.bewis09.bewisclient.game.Translation

object Contact: SidebarCategory(Translation("menu.category.contact", "Contact"), contact)

val contact = object: Renderable() { override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {} }