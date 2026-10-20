package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    @Test
    fun dueDateLabel_completedWithDate_returnsClear() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-23")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, true)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("CLEAR", actual)
    }

    @Test
    fun dueDateLabel_completedWithoutDate_returnsClear() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = null
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, true)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("CLEAR", actual)
    }

    @Test
    fun dueDateLabel_activeWithoutDate_returnsEmpty() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = null
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("", actual)
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-20")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-Day", actual)
    }

    @Test
    fun dueDateLabel_dueInThreeDays_returnsDMinus3() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-23")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-3", actual)
    }

    @Test
    fun dueDateLabel_overdueThreeDays_returnsDPlus3() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-17")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D+3", actual)
    }

    @Test
    fun dueDateLabel_dueYesterday_returnsDPlus1() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-19")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D+1", actual)
    }

    @Test
    fun dueDateLabel_dueTomorrow_returnsDMinus1() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-21")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-1", actual)
    }

    @Test
    fun dueDateLabel_oneDayBeforeFullMonth_returnsDMinus30() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-11-19")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-30", actual)
    }

    @Test
    fun dueDateLabel_exactlyOneMonthAhead_returnsDMinus31() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-11-20")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-31", actual)
    }

    @Test
    fun dueDateLabel_oneDayAfterFullMonth_returnsDMinus32() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-11-21")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-32", actual)
    }

    @Test
    fun dueDateLabel_oneDayBeforeMonthOverdue_returnsDPlus29() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-09-21")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D+29", actual)
    }

    @Test
    fun dueDateLabel_exactlyOneMonthOverdue_returnsDPlus30() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-09-20")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D+30", actual)
    }

    @Test
    fun dueDateLabel_oneDayAfterMonthOverdue_returnsDPlus31() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-09-19")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D+31", actual)
    }

    @Test
    fun dueDateLabel_oneDayBeforeFullYear_returnsDMinus364() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2027-10-19")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-364", actual)
    }

    @Test
    fun dueDateLabel_exactlyOneYearAhead_returnsDMinus365() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2027-10-20")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-365", actual)
    }

    @Test
    fun dueDateLabel_oneDayAfterFullYear_returnsDMinus366() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2027-10-21")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-366", actual)
    }

    @Test
    fun dueDateLabel_dayBeforeLeapDay_returnsDDay() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2028-02-28")
        val dueDate: LocalDate? = LocalDate.parse("2028-02-28")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-Day", actual)
    }

    @Test
    fun dueDateLabel_dueOnLeapDay_returnsDMinus1() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2028-02-28")
        val dueDate: LocalDate? = LocalDate.parse("2028-02-29")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-1", actual)
    }

    @Test
    fun dueDateLabel_dayAfterLeapDay_returnsDMinus2() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2028-02-28")
        val dueDate: LocalDate? = LocalDate.parse("2028-03-01")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-2", actual)
    }

    @Test
    fun dueDateLabel_beforeYearEnd_returnsDPlus1() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-12-31")
        val dueDate: LocalDate? = LocalDate.parse("2026-12-30")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D+1", actual)
    }

    @Test
    fun dueDateLabel_onYearEnd_returnsDDay() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-12-31")
        val dueDate: LocalDate? = LocalDate.parse("2026-12-31")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-Day", actual)
    }

    @Test
    fun dueDateLabel_afterYearEnd_returnsDMinus1() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-12-31")
        val dueDate: LocalDate? = LocalDate.parse("2027-01-01")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, false)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("D-1", actual)
    }

    @Test
    fun dueDateLabel_completedOverdue_returnsClear() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-19")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, true)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("CLEAR", actual)
    }

    @Test
    fun dueDateLabel_completedDueToday_returnsClear() {
        // GIVEN - Exact dates and expected labels from the reviewed contract.
        val today = LocalDate.parse("2026-10-20")
        val dueDate: LocalDate? = LocalDate.parse("2026-10-20")
        // WHEN - The label is calculated.
        val actual = dueDateLabel(dueDate, today, true)
        // THEN - The independent calendar-day oracle must match.
        assertEquals("CLEAR", actual)
    }
}
