package prts;

import java.util.List;
import prts.task.Task;

/**
 * Handles user interaction by returning strings for GUI display.
 */
public class Ui {

    public String getWelcome() {
        return "Welcome back, Doctor!\nWhat can I do for you?";
    }

    public String getBye() {
        return "Bye. Hope to see you again soon!";
    }

    public String getListString(TaskList tasks) {
        if (tasks.size() == 0) return "The task list is currently empty.";
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 1; i <= tasks.size(); i++) {
            sb.append(i).append(".").append(tasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String getAddedString(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.";
    }

    public String getDeletedString(Task task, int taskCount) {
        return "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.";
    }

    public String getMarkedString(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    public String getUnmarkedString(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    public String getFindResultString(List<Task> matches) {
        if (matches.isEmpty()) return "No matching tasks found.";
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(i + 1).append(".").append(matches.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String getErrorString(String msg) {
        return "Error: " + msg;
    }

    public String getCheerString(String msg) {
        return msg;
    }
}