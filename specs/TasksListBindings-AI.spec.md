# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Show the current task label in the row. An active task without a due date, or a null task, must leave no visible label. Every rebind must reflect the new task state.

**Pre-conditions**
- Use a real TextView on the Android main thread; it may contain a previous label.
- Each Task fixture has title="Task", description="Description", id="t1" unless another id is stated.
- now is the device local date captured immediately before binding. For example, if now=2026-10-20, now.plusDays(3)=2026-10-23 and now.minusDays(3)=2026-10-17. Tests use these exact relative offsets and fixed expected strings so they work on any run date.

**Post-conditions**
- Success cases
  - Binding activeDueToday produces text `"D-Day"` and visibility `VISIBLE`. `[Integration]`
    - Tip: Given active task with dueDate=now / When setDueDateLabel is called on the main thread / Then expect text `"D-Day"` and visibility `VISIBLE`. Test: `TasksListBindingsTest.setDueDateLabel_activeDueToday_showsDDay`.
  - Binding activeDueInThreeDays produces text `"D-3"` and visibility `VISIBLE`. `[Integration]`
    - Tip: Given active task with dueDate=now.plusDays(3) / When setDueDateLabel is called on the main thread / Then expect text `"D-3"` and visibility `VISIBLE`. Test: `TasksListBindingsTest.setDueDateLabel_activeDueInThreeDays_showsDMinus3`.
  - Binding activeOverdueThreeDays produces text `"D+3"` and visibility `VISIBLE`. `[Integration]`
    - Tip: Given active task with dueDate=now.minusDays(3) / When setDueDateLabel is called on the main thread / Then expect text `"D+3"` and visibility `VISIBLE`. Test: `TasksListBindingsTest.setDueDateLabel_activeOverdueThreeDays_showsDPlus3`.
  - Binding completedWithDate produces text `"CLEAR"` and visibility `VISIBLE`. `[Integration]`
    - Tip: Given completed task with dueDate=now.plusDays(3) / When setDueDateLabel is called on the main thread / Then expect text `"CLEAR"` and visibility `VISIBLE`. Test: `TasksListBindingsTest.setDueDateLabel_completedWithDate_showsClear`.
  - Binding completedWithoutDate produces text `"CLEAR"` and visibility `VISIBLE`. `[Integration]`
    - Tip: Given completed task with dueDate=null / When setDueDateLabel is called on the main thread / Then expect text `"CLEAR"` and visibility `VISIBLE`. Test: `TasksListBindingsTest.setDueDateLabel_completedWithoutDate_showsClear`.
  - Binding activeWithoutDateFresh produces no visible label: empty text `""` or visibility `GONE`/`INVISIBLE`. `[Integration]`
    - Tip: Given fresh empty visible TextView and active task with dueDate=null / When setDueDateLabel is called on the main thread / Then expect no visible label: empty text `""` or visibility `GONE`/`INVISIBLE`. Test: `TasksListBindingsTest.setDueDateLabel_activeWithoutDateFresh_showsNoLabel`.
- Error cases
  - None. A null task is valid and must leave no visible label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_nullTaskFresh_showsNoLabel`.
- Edge cases
  - Binding nullTaskFresh produces no visible label: empty text `""` or visibility `GONE`/`INVISIBLE`. `[Integration]`
    - Tip: Given fresh empty visible TextView and task=null / When setDueDateLabel is called on the main thread / Then expect no visible label: empty text `""` or visibility `GONE`/`INVISIBLE`. Test: `TasksListBindingsTest.setDueDateLabel_nullTaskFresh_showsNoLabel`.

**Undefined behavior** (optional)
- The tutorial does not require a particular hiding strategy. Empty text, GONE or INVISIBLE are valid ways to show no label. A test retries its fixture once if the local date changes during the call, then fails explicitly if it changes again.
