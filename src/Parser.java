public class Parser {

    public enum CommandType {
        A_COMMAND, C_COMMAND, L_COMMAND, LABELS
    }

    public CommandType getCommandType(String str) {
        if (str.startsWith("@")) {
            return CommandType.A_COMMAND;
        } else if (str.startsWith("(")) {
            return CommandType.L_COMMAND;
        } else {
            return CommandType.C_COMMAND;
        }
    }

    public String symbol(String str) {
        if (str.startsWith("@") && Character.isDigit(str.charAt(1))) {
            // @X
            if (Integer.parseInt(str.substring(1)) > 15) {
                return str.substring(1);
            }
            return "R" + str.substring(1);
        } else if (str.startsWith("@") && !Character.isDigit(str.charAt(1))) {
            return str.substring(1);
        } else {
            return str.substring(1, str.lastIndexOf(")"));
        }
    }

    public String dest(String str) {
        if (str.contains("=")) {
            return str.substring(0, str.indexOf('='));
        } else {
            return "null";
        }
    }

    public String comp(String str) {
        if (str.contains("=") && str.contains(";")) {
            // dest = comp;jump
            return str.substring(str.indexOf("=") + 1, str.indexOf(";"));
        } else if (str.contains("=") && !str.contains(";")) {
            // dest = comp
            return str.substring(str.indexOf("=") + 1);
        } else {
            // comp;jump
            return str.substring(0, str.indexOf(";"));
        }
    }

    public String jump(String str) {
        if (str.contains(";")) {
            return str.substring(str.indexOf(";") + 1);
        } else {
            return "null";
        }
    }
}
