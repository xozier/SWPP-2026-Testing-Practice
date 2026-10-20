# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return CLEAR for completed tasks, an empty string for undated active tasks, and D-N, D-Day or D+N for dated active tasks. N is the full calendar-day distance, including month, year and leap-day changes.

**Pre-conditions**
- today is a valid LocalDate; dueDate is null or any valid LocalDate.
- Completion overrides every due-date state. The supplied today value fixes the clock for each test.

**Post-conditions**
- Success cases
  - Return exactly `"CLEAR"` for completedWithDate. `[Unit]`
    - Tip: Given dueDate=2026-10-23, today=2026-10-20, isCompleted=true / When dueDateLabel is called / Then return exactly `"CLEAR"`. Test: `DueDateUtilsTest.dueDateLabel_completedWithDate_returnsClear`.
  - Return exactly `"CLEAR"` for completedWithoutDate. `[Unit]`
    - Tip: Given dueDate=null, today=2026-10-20, isCompleted=true / When dueDateLabel is called / Then return exactly `"CLEAR"`. Test: `DueDateUtilsTest.dueDateLabel_completedWithoutDate_returnsClear`.
  - Return exactly `""` for activeWithoutDate. `[Unit]`
    - Tip: Given dueDate=null, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `""`. Test: `DueDateUtilsTest.dueDateLabel_activeWithoutDate_returnsEmpty`.
  - Return exactly `"D-Day"` for dueToday. `[Unit]`
    - Tip: Given dueDate=2026-10-20, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-Day"`. Test: `DueDateUtilsTest.dueDateLabel_dueToday_returnsDDay`.
  - Return exactly `"D-3"` for dueInThreeDays. `[Unit]`
    - Tip: Given dueDate=2026-10-23, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-3"`. Test: `DueDateUtilsTest.dueDateLabel_dueInThreeDays_returnsDMinus3`.
  - Return exactly `"D+3"` for overdueThreeDays. `[Unit]`
    - Tip: Given dueDate=2026-10-17, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D+3"`. Test: `DueDateUtilsTest.dueDateLabel_overdueThreeDays_returnsDPlus3`.
- Error cases
  - None. All nullable due dates, valid LocalDate values and Boolean states are accepted. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeWithoutDate_returnsEmpty`.
- Edge cases
  - Return exactly `"D+1"` for dueYesterday. `[Unit]`
    - Tip: Given dueDate=2026-10-19, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D+1"`. Test: `DueDateUtilsTest.dueDateLabel_dueYesterday_returnsDPlus1`.
  - Return exactly `"D-1"` for dueTomorrow. `[Unit]`
    - Tip: Given dueDate=2026-10-21, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-1"`. Test: `DueDateUtilsTest.dueDateLabel_dueTomorrow_returnsDMinus1`.
  - Return exactly `"D-30"` for oneDayBeforeFullMonth. `[Unit]`
    - Tip: Given dueDate=2026-11-19, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-30"`. Test: `DueDateUtilsTest.dueDateLabel_oneDayBeforeFullMonth_returnsDMinus30`.
  - Return exactly `"D-31"` for exactlyOneMonthAhead. `[Unit]`
    - Tip: Given dueDate=2026-11-20, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-31"`. Test: `DueDateUtilsTest.dueDateLabel_exactlyOneMonthAhead_returnsDMinus31`.
  - Return exactly `"D-32"` for oneDayAfterFullMonth. `[Unit]`
    - Tip: Given dueDate=2026-11-21, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-32"`. Test: `DueDateUtilsTest.dueDateLabel_oneDayAfterFullMonth_returnsDMinus32`.
  - Return exactly `"D+29"` for oneDayBeforeMonthOverdue. `[Unit]`
    - Tip: Given dueDate=2026-09-21, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D+29"`. Test: `DueDateUtilsTest.dueDateLabel_oneDayBeforeMonthOverdue_returnsDPlus29`.
  - Return exactly `"D+30"` for exactlyOneMonthOverdue. `[Unit]`
    - Tip: Given dueDate=2026-09-20, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D+30"`. Test: `DueDateUtilsTest.dueDateLabel_exactlyOneMonthOverdue_returnsDPlus30`.
  - Return exactly `"D+31"` for oneDayAfterMonthOverdue. `[Unit]`
    - Tip: Given dueDate=2026-09-19, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D+31"`. Test: `DueDateUtilsTest.dueDateLabel_oneDayAfterMonthOverdue_returnsDPlus31`.
  - Return exactly `"D-364"` for oneDayBeforeFullYear. `[Unit]`
    - Tip: Given dueDate=2027-10-19, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-364"`. Test: `DueDateUtilsTest.dueDateLabel_oneDayBeforeFullYear_returnsDMinus364`.
  - Return exactly `"D-365"` for exactlyOneYearAhead. `[Unit]`
    - Tip: Given dueDate=2027-10-20, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-365"`. Test: `DueDateUtilsTest.dueDateLabel_exactlyOneYearAhead_returnsDMinus365`.
  - Return exactly `"D-366"` for oneDayAfterFullYear. `[Unit]`
    - Tip: Given dueDate=2027-10-21, today=2026-10-20, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-366"`. Test: `DueDateUtilsTest.dueDateLabel_oneDayAfterFullYear_returnsDMinus366`.
  - Return exactly `"D-Day"` for dayBeforeLeapDay. `[Unit]`
    - Tip: Given dueDate=2028-02-28, today=2028-02-28, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-Day"`. Test: `DueDateUtilsTest.dueDateLabel_dayBeforeLeapDay_returnsDDay`.
  - Return exactly `"D-1"` for dueOnLeapDay. `[Unit]`
    - Tip: Given dueDate=2028-02-29, today=2028-02-28, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-1"`. Test: `DueDateUtilsTest.dueDateLabel_dueOnLeapDay_returnsDMinus1`.
  - Return exactly `"D-2"` for dayAfterLeapDay. `[Unit]`
    - Tip: Given dueDate=2028-03-01, today=2028-02-28, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-2"`. Test: `DueDateUtilsTest.dueDateLabel_dayAfterLeapDay_returnsDMinus2`.
  - Return exactly `"D+1"` for beforeYearEnd. `[Unit]`
    - Tip: Given dueDate=2026-12-30, today=2026-12-31, isCompleted=false / When dueDateLabel is called / Then return exactly `"D+1"`. Test: `DueDateUtilsTest.dueDateLabel_beforeYearEnd_returnsDPlus1`.
  - Return exactly `"D-Day"` for onYearEnd. `[Unit]`
    - Tip: Given dueDate=2026-12-31, today=2026-12-31, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-Day"`. Test: `DueDateUtilsTest.dueDateLabel_onYearEnd_returnsDDay`.
  - Return exactly `"D-1"` for afterYearEnd. `[Unit]`
    - Tip: Given dueDate=2027-01-01, today=2026-12-31, isCompleted=false / When dueDateLabel is called / Then return exactly `"D-1"`. Test: `DueDateUtilsTest.dueDateLabel_afterYearEnd_returnsDMinus1`.
  - Return exactly `"CLEAR"` for completedOverdue. `[Unit]`
    - Tip: Given dueDate=2026-10-19, today=2026-10-20, isCompleted=true / When dueDateLabel is called / Then return exactly `"CLEAR"`. Test: `DueDateUtilsTest.dueDateLabel_completedOverdue_returnsClear`.
  - Return exactly `"CLEAR"` for completedDueToday. `[Unit]`
    - Tip: Given dueDate=2026-10-20, today=2026-10-20, isCompleted=true / When dueDateLabel is called / Then return exactly `"CLEAR"`. Test: `DueDateUtilsTest.dueDateLabel_completedDueToday_returnsClear`.

**Undefined behavior** (optional)
- None within the declared input types.
