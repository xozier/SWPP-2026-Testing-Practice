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

**Undefined behavior** (optional)
- None within the declared input types.
