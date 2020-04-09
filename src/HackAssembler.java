import java.io.*;

public class HackAssembler {
    public static void main(String[] args) {
        // 1.Initialization (Parser and Symbol Table)
        Parser parser = new Parser();
        SymbolTable st = new SymbolTable();
        Code c = new Code();

        String filePath = args[0];
        String destPath = args[0].substring(0, args[0].length() - 3) + "hack";

        try {
            File file = new File(destPath);
            if (file.createNewFile()) {
                System.out.println("Create a new file: " + destPath.substring(destPath.lastIndexOf("/") + 1));
            } else {
                System.out.println("File has already existed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 2. Read all commands, only paying attention to labels and updating Symbol Table
            // First pass. Add the label symbols, like (LOOP)
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String str;
            int lineNumber = -1;
            int startIndex = 16;
            while ((str = bufferedReader.readLine()) != null) {
                // Remove all the white space and comments
                str = str.replaceAll("//.*", "").replace(" ", "");
                if (!str.isEmpty()) {
                    // First pass
                    // Add the label symbols into symbol tables. e.g.(LOOP)
                    if (str.startsWith("(") && !st.contains(str.substring(1, str.length() - 1))) {
                        st.addEntry(str.substring(1, str.length() - 1), lineNumber + 1);
                    } else {
                        lineNumber++;
                    }
                }
            }
            bufferedReader.close();

            // Second pass (can't using only one pass, like @sys.init appear earlier but (sys.init) appeared later
            bufferedReader = new BufferedReader((new FileReader(filePath)));
            while ((str = bufferedReader.readLine()) != null) {
                str = str.replaceAll("//.*", "").replace(" ", "");
                if (!str.isEmpty()) {
                    // Add the var. symbols into symbol tables. e.g. @sum
                    if (str.startsWith("@") && str.toLowerCase().equals(str) && !st.contains(str.substring(1))
                            && Character.isLetter(str.charAt(1))) {
                        st.addEntry(str.substring(1), startIndex++);
                    }
                }
            }
            bufferedReader.close();

            // 3. Restart reading and translating commands and main loop.
            String line;
            bufferedReader = new BufferedReader(new FileReader(filePath));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destPath));
            while ((str = bufferedReader.readLine()) != null) {
                str = str.replaceAll("//.*", "").replace(" ", "");
                if (!str.isEmpty()) {
                    lineNumber--;
                    if (parser.getCommandType(str) == Parser.CommandType.A_COMMAND) {
                        int val;
                        if (st.contains(parser.symbol(str))) {
                            val = st.getValue(parser.symbol(str));
                        } else {
                            val = Integer.parseInt(parser.symbol(str));
                        }
                        line = String.format("%16s", Integer.toBinaryString(val)).replace(" ", "0");
                    } else if (parser.getCommandType(str) == Parser.CommandType.C_COMMAND) {
                        String dest = parser.dest(str);
                        String comp = parser.comp(str);
                        String jump = parser.jump(str);
                        line = "111" + c.getComp(comp) + c.getDest(dest) + c.getJump(jump);
                    } else {
                        continue;
                    }
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the last blank line
        try {
            RandomAccessFile raf = new RandomAccessFile(destPath, "rw");
            long length = raf.length();
            byte b;
            do {
                length -= 1;
                raf.seek(length);
                b = raf.readByte();
            } while (b != 10 && length > 0);
            raf.setLength(length);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
