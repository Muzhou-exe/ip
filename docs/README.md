# PRTS User Guide

PRTS is a task management chatbot with a graphical interface. It helps you keep track of your todos, deadlines, and events.

## Quick Start

1. Ensure you have **Java 21** installed.
2. Download the latest `prts.jar` from the [Releases](https://github.com/Muzhou-exe/ip/releases) page.
3. Copy the JAR file into an empty folder.
4. Open a terminal in that folder and run: `java -jar "prts.jar"`

## Features

### Adding a todo: `todo`

Adds a task without any date.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

### Adding a deadline: `deadline`

Adds a task with a due date.

Format: `deadline DESCRIPTION /by DATE`

- `DATE` must be in `yyyy-mm-dd` format (e.g. `2026-03-20`).

Example: `deadline return book /by 2026-03-20`

```
Got it. I've added this task:
  [D][ ] return book (by: Mar 20 2026)
Now you have 2 tasks in the list.
```

### Adding an event: `event`

Adds a task with a start and end time.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
```

### Listing all tasks: `list`

Shows all tasks in the list.

Format: `list`

```
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Mar 20 2026)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

### Marking a task as done: `mark`

Marks a task as completed.

Format: `mark INDEX`

Example: `mark 1`

```
Nice! I've marked this task as done:
  [X] read book
```

### Unmarking a task: `unmark`

Marks a task as not done.

Format: `unmark INDEX`

Example: `unmark 1`

```
OK, I've marked this task as not done yet:
  [ ] read book
```

### Deleting a task: `delete`

Removes a task from the list.

Format: `delete INDEX`

Example: `delete 3`

```
Noted. I've removed this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
```

### Finding tasks by keyword: `find`

Searches for tasks whose descriptions contain the given keyword.

Format: `find KEYWORD`

Example: `find book`

```
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Mar 20 2026)
```

### Undoing the last change: `undo`

Reverts the most recent task list change (add, delete, mark, or unmark).

Format: `undo`

```
Undo successful! Your task list has been restored.
```

### Getting a motivational quote: `cheer`

Displays a random motivational quote for software engineers.

Format: `cheer`

```
Keep going - even the best programmers started out writing 'Hello World'!
```

### Exiting the program: `bye`

Exits the application.

Format: `bye`

## Data Storage

Tasks are saved automatically to `./data/tasks.txt` whenever the list changes. The data is loaded automatically when PRTS starts up. No manual saving is needed.

## Command Summary

| Command | Format |
|---------|--------|
| Todo | `todo DESCRIPTION` |
| Deadline | `deadline DESCRIPTION /by yyyy-mm-dd` |
| Event | `event DESCRIPTION /from START /to END` |
| List | `list` |
| Mark | `mark INDEX` |
| Unmark | `unmark INDEX` |
| Delete | `delete INDEX` |
| Find | `find KEYWORD` |
| Undo | `undo` |
| Cheer | `cheer` |
| Exit | `bye` |
