package com.example.swpp.tododapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.Task
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest {
    private lateinit var repository: FakeAndroidTestRepository

    @Before
    fun setup() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanup() = runTest { ServiceLocator.resetRepository() }

    @Test
    fun taskDetailFragment_activeTask_displaysTitleDescriptionAndUncheckedStatus() = runTest {
        // GIVEN - An active task in the fake repository.
        val task = Task("Task Title", "Task Description", false, "active-task")
        repository.saveTask(task)
        // WHEN - Its detail fragment is launched.
        launchFragmentInContainer<TaskDetailFragment>(
            TaskDetailFragmentArgs(task.id).toBundle(), R.style.AppTheme
        ).use {
            // THEN - Its exact details and active status are displayed.
            onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
                .check(matches(withText("Task Title")))
            onView(withId(R.id.task_detail_description_text)).check(matches(isDisplayed()))
                .check(matches(withText("Task Description")))
            onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isDisplayed()))
                .check(matches(not(isChecked())))
        }
    }
}
