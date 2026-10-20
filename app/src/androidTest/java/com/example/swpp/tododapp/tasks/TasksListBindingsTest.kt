package com.example.swpp.tododapp.tasks

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.Task
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private lateinit var repository: FakeAndroidTestRepository

    @Before
    fun setup() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanup() = runTest { ServiceLocator.resetRepository() }

    private fun task(completed: Boolean, due: LocalDate?, id: String = "t1") =
        Task("Task", "Description", completed, id, due)

    private fun assertShown(view: TextView, expected: String) {
        assertEquals(expected, view.text.toString())
        assertEquals(View.VISIBLE, view.visibility)
    }

    private fun assertNoLabel(view: TextView) {
        assertFalse("Stale visible label: ${view.text}",
            view.visibility == View.VISIBLE && view.text.isNotEmpty())
    }

    // Construct and bind on the main thread. Rebuild once if the date rolls over.
    private fun onStableDate(action: (TextView, LocalDate) -> Unit) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            repeat(2) {
                val now = LocalDate.now()
                val view = TextView(instrumentation.targetContext)
                try {
                    action(view, now)
                    if (LocalDate.now() == now) return@runOnMainSync
                } catch (failure: AssertionError) {
                    if (LocalDate.now() == now) throw failure
                }
            }
            fail("Local date changed during both attempts; no assertion was skipped")
        }
    }

    @Test
    fun setDueDateLabel_activeDueToday_showsDDay() {
        // GIVEN - A real TextView and the reviewed activeDueToday fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            view.visibility = View.INVISIBLE
            setDueDateLabel(view, task(false, now))
            // THEN - Check the exact visible label or absence.
            assertShown(view, "D-Day")
        }
    }

    @Test
    fun setDueDateLabel_activeDueInThreeDays_showsDMinus3() {
        // GIVEN - A real TextView and the reviewed activeDueInThreeDays fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            view.visibility = View.INVISIBLE
            setDueDateLabel(view, task(false, now.plusDays(3)))
            // THEN - Check the exact visible label or absence.
            assertShown(view, "D-3")
        }
    }

    @Test
    fun setDueDateLabel_activeOverdueThreeDays_showsDPlus3() {
        // GIVEN - A real TextView and the reviewed activeOverdueThreeDays fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            view.visibility = View.INVISIBLE
            setDueDateLabel(view, task(false, now.minusDays(3)))
            // THEN - Check the exact visible label or absence.
            assertShown(view, "D+3")
        }
    }

    @Test
    fun setDueDateLabel_completedWithDate_showsClear() {
        // GIVEN - A real TextView and the reviewed completedWithDate fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            view.visibility = View.INVISIBLE
            setDueDateLabel(view, task(true, now.plusDays(3)))
            // THEN - Check the exact visible label or absence.
            assertShown(view, "CLEAR")
        }
    }

    @Test
    fun setDueDateLabel_completedWithoutDate_showsClear() {
        // GIVEN - A real TextView and the reviewed completedWithoutDate fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            view.visibility = View.INVISIBLE
            setDueDateLabel(view, task(true, null))
            // THEN - Check the exact visible label or absence.
            assertShown(view, "CLEAR")
        }
    }

    @Test
    fun setDueDateLabel_activeWithoutDateFresh_showsNoLabel() {
        // GIVEN - A real TextView and the reviewed activeWithoutDateFresh fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            setDueDateLabel(view, task(false, null))
            // THEN - Check the exact visible label or absence.
            assertNoLabel(view)
        }
    }

    @Test
    fun setDueDateLabel_nullTaskFresh_showsNoLabel() {
        // GIVEN - A real TextView and the reviewed nullTaskFresh fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            setDueDateLabel(view, null)
            // THEN - Check the exact visible label or absence.
            assertNoLabel(view)
        }
    }

    @Test
    fun setDueDateLabel_uncheckWithoutDate_showsNoLabel() {
        // GIVEN - A real TextView and the reviewed uncheckWithoutDate fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            setDueDateLabel(view, task(true, null))
            assertShown(view, "CLEAR")
            // WHEN - Rebind the same view to its new state.
            setDueDateLabel(view, task(false, null))
            // THEN - The new state replaces the old label.
            assertNoLabel(view)
        }
    }

    @Test
    fun setDueDateLabel_uncheckDueToday_showsDDay() {
        // GIVEN - A real TextView and the reviewed uncheckDueToday fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            setDueDateLabel(view, task(true, now))
            assertShown(view, "CLEAR")
            // WHEN - Rebind the same view to its new state.
            setDueDateLabel(view, task(false, now))
            // THEN - The new state replaces the old label.
            assertShown(view, "D-Day")
        }
    }

    @Test
    fun setDueDateLabel_checkDatedTask_showsClear() {
        // GIVEN - A real TextView and the reviewed checkDatedTask fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            setDueDateLabel(view, task(false, now.plusDays(3)))
            assertShown(view, "D-3")
            // WHEN - Rebind the same view to its new state.
            setDueDateLabel(view, task(true, now.plusDays(3)))
            // THEN - The new state replaces the old label.
            assertShown(view, "CLEAR")
        }
    }

    @Test
    fun setDueDateLabel_recycleForUndatedTask_showsNoLabel() {
        // GIVEN - A real TextView and the reviewed recycleForUndatedTask fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            setDueDateLabel(view, task(false, now.plusDays(3), "a"))
            assertShown(view, "D-3")
            // WHEN - Rebind the same view to its new state.
            setDueDateLabel(view, task(false, null, "b"))
            // THEN - The new state replaces the old label.
            assertNoLabel(view)
        }
    }

    @Test
    fun setDueDateLabel_rebindToNull_showsNoLabel() {
        // GIVEN - A real TextView and the reviewed rebindToNull fixture.
        onStableDate { view, now ->
            // WHEN - Bind the task on the main thread.
            setDueDateLabel(view, task(true, null))
            assertShown(view, "CLEAR")
            // WHEN - Rebind the same view to its new state.
            setDueDateLabel(view, null)
            // THEN - The new state replaces the old label.
            assertNoLabel(view)
        }
    }

    @Test
    fun tasksList_uncheckUndatedCompletedTask_removesClear() = runTest {
        // GIVEN - A real row showing CLEAR for a completed task without a due date.
        repository.saveTask(Task("Task", "Description", true, "t1", null))
        launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme).use {
            val label = allOf(withId(R.id.due_date_text), hasSibling(withText("Task")))
            onView(label).check(matches(withText("CLEAR")))
            // WHEN - The user unchecks its checkbox.
            onView(allOf(withId(R.id.complete_checkbox), hasSibling(withText("Task"))))
                .perform(click())
            // THEN - No stale visible CLEAR remains in the row.
            onView(label).check(matches(anyOf(withText(""),
                not(withEffectiveVisibility(Visibility.VISIBLE)))))
        }
    }

    @Test
    fun tasksList_checkUndatedActiveTask_showsClear() = runTest {
        // GIVEN - An active task without a due date.
        repository.saveTask(Task("Task", "Description", false, "t1", null))
        launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme).use {
            // WHEN - The user completes its task.
            onView(allOf(withId(R.id.complete_checkbox), hasSibling(withText("Task"))))
                .perform(click())
            // THEN - The row displays CLEAR.
            onView(allOf(withId(R.id.due_date_text), hasSibling(withText("Task"))))
                .check(matches(withText("CLEAR"))).check(matches(isDisplayed()))
        }
    }
}
