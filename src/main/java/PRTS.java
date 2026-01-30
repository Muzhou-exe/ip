import java.util.Scanner;

public class PRTS {

    private static final String LOGO = """
                      _____                    _____                _____                    _____         \s
                     /\\    \\                  /\\    \\              /\\    \\                  /\\    \\        \s
                    /::\\    \\                /::\\    \\            /::\\    \\                /::\\    \\       \s
                   /::::\\    \\              /::::\\    \\           \\:::\\    \\              /::::\\    \\      \s
                  /::::::\\    \\            /::::::\\    \\           \\:::\\    \\            /::::::\\    \\     \s
                 /:::/\\:::\\    \\          /:::/\\:::\\    \\           \\:::\\    \\          /:::/\\:::\\    \\    \s
                /:::/__\\:::\\    \\        /:::/__\\:::\\    \\           \\:::\\    \\        /:::/__\\:::\\    \\   \s
               /::::\\   \\:::\\    \\      /::::\\   \\:::\\    \\          /::::\\    \\       \\:::\\   \\:::\\    \\  \s
              /::::::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\        /::::::\\    \\    ___\\:::\\   \\:::\\    \\ \s
             /:::/\\:::\\   \\:::\\____\\  /:::/\\:::\\   \\:::\\____\\      /:::/\\:::\\    \\  /\\   \\:::\\   \\:::\\    \\\s
            /:::/  \\:::\\   \\:::|    |/:::/  \\:::\\   \\:::|    |    /:::/  \\:::\\____\\/::\\   \\:::\\   \\:::\\____\\
            \\::/    \\:::\\  /:::|____|\\::/   |::::\\  /:::|____|   /:::/    \\::/    /\\:::\\   \\:::\\   \\::/    /
             \\/_____/\\:::\\/:::/    /  \\/____|:::::\\/:::/    /   /:::/    / \\/____/  \\:::\\   \\:::\\   \\/____/\s
                      \\::::::/    /         |:::::::::/    /   /:::/    /            \\:::\\   \\:::\\    \\    \s
                       \\::::/    /          |::|\\::::/    /   /:::/    /              \\:::\\   \\:::\\____\\   \s
                        \\::/____/           |::| \\::/____/    \\::/    /                \\:::\\  /:::/    /   \s
                         ~~                 |::|  ~|           \\/____/                  \\:::\\/:::/    /    \s
                                            |::|   |                                     \\::::::/    /     \s
                                            \\::|   |                                      \\::::/    /      \s
                                             \\:|   |                                       \\::/    /       \s
                                              \\|___|                                        \\/____/        \s
            
            
            
            
                       
""";

    public static void main(String[] args) {
        System.out.print(LOGO);
        System.out.println("Welcome back, Doctor!");
        System.out.println("What can I do for you?");

        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                continue;
            }

            // default: treat as a task to add
            tasks[taskCount] = input;
            taskCount++;

            System.out.println("added: " + input);
        }
    }
}