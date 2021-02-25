public enum Commands {


    LIST("/list","/list --> list all commands"+"\n"),
    EXIT("/exit","/exit --> exit chat"+"\n"),
    HELP("/help","/help <command> --> get implementation of command mentioned"+"\n"),
    NAME("/name","/name <new_name> --> change name to mentioned after /help"+"\n"),
    PRIVATE("/private","/private <name_of_user> <message> --> speak only to user with name <name_of_user> and with message <message>"+"\n"),
    KICK("/kick","/kick <name> --> kick user with name = <name>");


    private final String value;
    private final String instructions;

    Commands(String command,String instructions) {
        value = command;
        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getValue() {
        return value;
    }
}
