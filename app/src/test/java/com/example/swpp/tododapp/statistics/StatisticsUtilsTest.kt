package com.example.swpp.tododapp.statistics

import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StatisticsUtilsTest(
    private val scenario: String,
    private val tasks: List<Task>?,
    private val expectedActive: Float,
    private val expectedCompleted: Float
) {
    @Test
    fun getActiveAndCompletedStats_tasks_returnsExpectedPercentages() {
        // GIVEN - The tasks and independent expected percentages in each data row.
        val input = tasks
        // WHEN - Statistics are calculated.
        val result = getActiveAndCompletedStats(input)
        // THEN - Both percentages match the expected values.
        assertEquals(expectedActive, result.activeTasksPercent, 0f)
        assertEquals(expectedCompleted, result.completedTasksPercent, 0f)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun cases(): List<Array<Any?>> = listOf(
            arrayOf("one active and one completed", listOf(
                Task("Completed", "Description", true, "t1"),
                Task("Active", "Description", false, "t2")
            ), 50f, 50f),
            arrayOf("empty", emptyList<Task>(), 0f, 0f),
            arrayOf("all completed", listOf(
                Task("First", "Description", true, "t1"),
                Task("Second", "Description", true, "t2")
            ), 0f, 100f),
            arrayOf("all active", listOf(Task("Active", "Description", false, "t1")), 100f, 0f),
            arrayOf("null", null, 0f, 0f)
        )
    }
}
