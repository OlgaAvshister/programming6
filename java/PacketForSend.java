public class PacketForSend {
    private final String command;
    private final String[] arguments;

    public PacketForSend(String command) {
        String[] prepared = command.split("[ ]+");
        this.command = prepared[0];
        this.arguments = this.preparator(prepared);
    }

    private String[] preparator(String[] from) {
        String[] result;
        if (from.length > 1) {
            result = new String[from.length - 1];
            for (int i = 0; i < result.length; i++) {
                result[i] = from[i + 1];
            }
        } else {
            result = new String[]{};
        }
        return result;
    }

    public String open() {
        StringBuilder sb = new StringBuilder();
        sb.append(command).append(" ");
        String[] result = new String[this.arguments.length + 1];
        for (int i = 0; i < arguments.length; i++) {
            sb.append(arguments[i]).append(" ");
        }
        return sb.toString().trim();
    }
}
