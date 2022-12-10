package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.util.Colors
import juuxel.adorn.util.Displayable
import juuxel.adorn.util.animation.AnimationEngine
import juuxel.adorn.util.animation.AnimationTask
import juuxel.adorn.util.color
import net.minecraft.client.gui.screen.NoticeScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.CyclingButtonWidget
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import kotlin.random.Random
import kotlin.reflect.KMutableProperty

abstract class AbstractConfigScreen(title: Text, private val parent: Screen) : Screen(title) {
    private val random: Random = Random.Default
    private val hearts: MutableList<Heart> = ArrayList()
    private var restartRequired = false
    private val animationEngine = AnimationEngine()

    init {
        animationEngine.add(HeartAnimationTask())
    }

    override fun init() {
        animationEngine.start()
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        synchronized(hearts) {
            renderHearts(matrices, delta)
        }
        drawCenteredText(matrices, textRenderer, title, width / 2, 20, Colors.WHITE)
        super.render(matrices, mouseX, mouseY, delta)
    }

    private fun renderHearts(matrices: MatrixStack, delta: Float) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram)
        RenderSystem.setShaderTexture(0, HEART_TEXTURE)

        for (heart in hearts) {
            RenderSystem.setShaderColor(Colors.redOf(heart.color), Colors.greenOf(heart.color), Colors.blueOf(heart.color), 1f)
            matrices.push()
            matrices.translate(heart.x.toDouble(), MathHelper.lerp(delta.toDouble(), heart.previousY, heart.y), 0.0)
            matrices.translate(HEART_SIZE.toDouble() / 2, HEART_SIZE.toDouble() / 2, 0.0)
            matrices.multiply(RotationAxis.POSITIVE_Z.rotation(heart.angle.toFloat()))
            matrices.translate(-HEART_SIZE.toDouble() / 2, -HEART_SIZE.toDouble() / 2, 0.0)
            drawTexture(matrices, 0, 0, HEART_SIZE, HEART_SIZE, 0f, 0f, 8, 8, 8, 8)
            matrices.pop()
        }
    }

    override fun close() {
        client!!.setScreen(
            if (restartRequired) {
                NoticeScreen(
                    { client!!.setScreen(parent) },
                    Text.translatable("gui.adorn.config.restart_required.title"),
                    Text.translatable("gui.adorn.config.restart_required.message"),
                    Text.translatable("gui.ok"),
                    true
                )
            } else {
                parent
            }
        )
    }

    override fun removed() {
        animationEngine.stop()
    }

    private fun tickHearts() {
        val iter = hearts.iterator()
        while (iter.hasNext()) {
            val heart = iter.next()

            if (heart.y - HEART_SIZE > height) {
                iter.remove()
            } else {
                heart.move()
            }
        }

        if (random.nextInt(HEART_CHANCE) == 0) {
            val x = random.nextInt(width)
            val color = HEART_COLORS.random(random)
            val speed = random.nextDouble(MIN_HEART_SPEED, MAX_HEART_SPEED)
            val angularSpeed = random.nextDouble(-MAX_HEART_ANGULAR_SPEED, MAX_HEART_ANGULAR_SPEED)
            hearts += Heart(x, -HEART_SIZE.toDouble(), color, speed, angularSpeed)
        }
    }

    private fun <T> createConfigButton(
        builder: CyclingButtonWidget.Builder<T>, x: Int, y: Int, width: Int, property: KMutableProperty<T>, restartRequired: Boolean
    ) = builder.tooltip {
        val text = Text.translatable(getTooltipTranslationKey(property.name))
        if (restartRequired) {
            text.append(Text.literal("\n"))
                .append(Text.translatable("gui.adorn.config.requires_restart").formatted(Formatting.ITALIC, Formatting.GOLD))
        }
        Tooltip.of(text)
    }.build(x, y, width, BUTTON_HEIGHT, Text.translatable(getOptionTranslationKey(property.name))) { _, value ->
        property.setter.call(value)
        ConfigManager.INSTANCE.save()

        if (restartRequired) {
            this.restartRequired = true
        }
    }

    protected fun createConfigToggle(
        x: Int, y: Int, width: Int, property: KMutableProperty<Boolean>, restartRequired: Boolean = false
    ): CyclingButtonWidget<Boolean> = createConfigButton(
        CyclingButtonWidget.onOffBuilder(property.getter.call()),
        x, y, width, property, restartRequired
    )

    protected fun <T : Displayable> createConfigButton(
        x: Int, y: Int, width: Int, property: KMutableProperty<T>, values: List<T>, restartRequired: Boolean = false
    ): CyclingButtonWidget<T> = createConfigButton(
        CyclingButtonWidget.builder<T> { it.displayName }.values(values).initially(property.getter.call()),
        x, y, width, property, restartRequired
    )

    protected open fun getOptionTranslationKey(name: String): String =
        "gui.adorn.config.option.$name"

    private fun getTooltipTranslationKey(name: String): String =
        "${getOptionTranslationKey(name)}.description"

    companion object {
        const val BUTTON_HEIGHT = 20
        const val BUTTON_GAP = 4
        const val BUTTON_SPACING = BUTTON_HEIGHT + BUTTON_GAP
        private const val HEART_SIZE = 12
        private val HEART_COLORS: List<Int> = listOf(
            color(0xFF0000), // Red
            color(0xFC8702), // Orange
            color(0xFFFF00), // Yellow
            color(0xA7FC58), // Green
            color(0x2D61FC), // Blue
            color(0xA002FC), // Purple
            color(0x58E9FC), // Light blue
            color(0xFCA1DF), // Pink
        )
        private val HEART_TEXTURE = AdornCommon.id("textures/gui/heart.png")
        private const val MIN_HEART_SPEED = 0.05
        private const val MAX_HEART_SPEED = 1.5
        private const val MAX_HEART_ANGULAR_SPEED = 0.07
        private const val HEART_CHANCE = 65
    }

    private class Heart(val x: Int, var y: Double, val color: Int, val speed: Double, val angularSpeed: Double) {
        var previousY: Double = y
        var previousAngle: Double = 0.0
        var angle: Double = 0.0

        fun move() {
            previousY = y
            y += speed
            previousAngle = angle
            angle = (angle + angularSpeed) % MathHelper.TAU
        }
    }

    private inner class HeartAnimationTask : AnimationTask {
        override fun isAlive(): Boolean = true
        override fun tick() {
            synchronized(hearts) {
                tickHearts()
            }
        }
    }
}
