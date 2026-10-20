package com.example.swpp.tododapp.tasks

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.Task
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksFragmentTest {
    private lateinit var repository: FakeAndroidTestRepository

    @Before
    fun setup() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanup() = runTest { ServiceLocator.resetRepository() }

    @Test
    fun tasksFragment_clickFirstTask_navigatesToItsDetails() = runTest {
        // GIVEN - Two distinct tasks and a controller that records navigation.
        repository.saveTask(Task("TITLE1", "DESCRIPTION1", false, "id1"))
        repository.saveTask(Task("TITLE2", "DESCRIPTION2", true, "id2"))
        val controller = mock(NavController::class.java)
        launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme).use { scenario ->
            scenario.onFragment { Navigation.setViewNavController(it.requireView(), controller) }
            // WHEN - The first task is clicked.
            onView(withId(R.id.tasks_list)).perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("TITLE1")), click()
                )
            )
            // THEN - Navigation targets exactly the selected task.
            verify(controller).navigate(
                TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1")
            )
        }
    }
}
