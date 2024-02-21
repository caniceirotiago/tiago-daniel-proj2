public class main {
    public static void main(String[] args) {
        String userBaseUrl = "http://localhost:8080/tiago-daniel-proj2/rest/user/add";
        String taskBaseUrl = "http://localhost:8080/tiago-daniel-proj2/rest/task";

        UserClient userClient = new UserClient(userBaseUrl);
        TaskClient taskClient = new TaskClient(taskBaseUrl);

        if (args.length > 0) {
            String action = args[0];
            switch (action) {
                case "add_users":
                    if (args.length == 2) {
                        int numberOfUsers = Integer.parseInt(args[1]);
                        userClient.addUserRandomly(numberOfUsers);
                    } else {
                        System.out.println("Uso: add_users <numero_de_utilizadores>");
                    }
                    break;
                case "add_tasks":
                    if (args.length == 4) {
                        String username = args[1];
                        String password = args[2];
                        int numberOfTasks = Integer.parseInt(args[3]);
                        taskClient.addRandomTasksToUser(username, password, numberOfTasks);
                    } else {
                        System.out.println("Uso: add_tasks <username> <password> <numero_de_tarefas>");
                    }
                    break;
                default:
                    System.out.println("Ação não reconhecida.");
                    break;
            }
        } else {
            System.out.println("Por favor, especifique uma ação.");
        }
    }
}
