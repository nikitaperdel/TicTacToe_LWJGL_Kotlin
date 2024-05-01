import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryUtil
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    val size = 3
    val scale = 140
    val board = IntArray(size * size)
    var crossTurn = true
    glfwInit()
    val windowId = glfwCreateWindow(size * scale, size * scale, "Tic Tac Toe", MemoryUtil.NULL, MemoryUtil.NULL)
    glfwSetKeyCallback(windowId) { _, key, _, action, _ ->
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_R) board.fill(0) else {
                val x = (key - GLFW_KEY_KP_1) % size
                val y = size - 1 - (key - GLFW_KEY_KP_1) / size
                if (board[x * size + y] == 0) {
                    board[x * size + y] = if (crossTurn) 1 else 2
                    crossTurn = !crossTurn
                }
            }
        }
    }
    glfwMakeContextCurrent(windowId)
    glfwSwapInterval(0)
    glfwShowWindow(windowId)
    GL.createCapabilities()
    while (!glfwWindowShouldClose(windowId)) {
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glColor3f(0.0f, 0.0f, 0.0f)
        glBegin(GL_LINES)
        for (i in 1 until size) {
            glVertex2f((i.toFloat() / size * 2 - 1), -1f)
            glVertex2f((i.toFloat() / size * 2 - 1), 1f)
            glVertex2f(-1f, (i.toFloat() / size * 2 - 1))
            glVertex2f(1f, (i.toFloat() / size * 2 - 1))
        }
        glEnd()
        for (x in 0 until size) {
            for (y in 0 until size) {
                if (board[x * size + y] != 0) {
                    glColor3f(if (board[x * size + y] == 1) 1.0f else 0.0f, 0.0f, if (board[x * size + y] == 2) 1.0f else 0.0f)
                    if (board[x * size + y] == 1) {
                        glBegin(GL_LINES)
                        glVertex2f((x.toFloat() / size * 2 - 1), ((size - 1 - y).toFloat() / size * 2 - 1))
                        glVertex2f(((x + 1).toFloat() / size * 2 - 1), ((size - y).toFloat() / size * 2 - 1))
                        glVertex2f(((x + 1).toFloat() / size * 2 - 1), ((size - 1 - y).toFloat() / size * 2 - 1))
                        glVertex2f((x.toFloat() / size * 2 - 1), ((size - y).toFloat() / size * 2 - 1))
                        glEnd()
                    } else {
                        glBegin(GL_LINE_LOOP)
                        for (i in 0..300) {
                            val angle = 2 * Math.PI * i / 300
                            val dx = cos(angle) / size
                            val dy = sin(angle) / size
                            glVertex2f(((x + 0.5 + dx).toFloat() / size * 2 - 1), ((size - 0.5 - y + dy).toFloat() / size * 2 - 1))
                        }
                        glEnd()
                    }
                }
            }
        }
        glfwSwapBuffers(windowId)
        glfwPollEvents()
    }
    glfwDestroyWindow(windowId)
    glfwTerminate()
}