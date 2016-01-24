package datatypes.custom;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.System.in;
import static java.lang.System.setOut;

/**
 * An Example Class to demonstrate usage of datatypes.custom.Trie
 * For sake of simplicity, I will read/write from file as char stream and will use HashMap for "Stores"
 */
public class KeyValueStore {

    /**
     * It will store all the KeyStores as storeName, Trie pair.
     */
    private static Map<String, Trie> storageMap;
    private static String filePath;


    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Usage:\njava -jar KeyValueStore <store_path>");
            return;
        }
        storageMap = new HashMap<String, Trie>();
        filePath = args[0];
        readFromStore();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(in));

        showHelp();
        while (true){
            System.out.print("KeyValueStorage:> ");
            String line = consoleReader.readLine();
            if("exit".equals(line)){ break;}
            String[] commandSeq = line.split("\\s+");
            int len = commandSeq.length;
            if(commandSeq.length > 0) {
                if ("create".equals(commandSeq[0])) {
                    if(len < 2){
                        showHelp();
                    } else {
                        storageMap.put(commandSeq[1], new Trie());
                        System.out.println("Store " + commandSeq[1] + " created");
                    }
                } else if ("delete".equals(commandSeq[0])) {
                    if(len < 2){
                        showHelp();
                    } else {
                        if(storageMap.containsKey(commandSeq[1])){
                            storageMap.remove(commandSeq[1]);
                            System.out.println("Store " + commandSeq[1] + " deleted");
                        }
                        System.out.println("Store " + commandSeq[1] + " not found");
                    }
                } else if ("insert".equals(commandSeq[0])) {
                    if(len < 5){
                        showHelp();
                    } else if(!"into".equals(commandSeq[3])){
                        showHelp();
                    } else {
                        String storeName = commandSeq[4];
                        if(storageMap.containsKey(storeName)){
                            storageMap.get(storeName).put(commandSeq[1], Integer.parseInt(commandSeq[2]));
                            System.out.println(commandSeq[1] + ' ' + commandSeq[2] + " inserted into " + storeName);
                        } else {
                            System.out.println("Store " + commandSeq[1] + " not found");
                        }
                    }
                } else if ("get".equals(commandSeq[0])) {
                    if(len < 4){
                        showHelp();
                    } else if(!"from".equals(commandSeq[2])){
                        showHelp();
                    } else {
                        String storeName = commandSeq[3];
                        if(storageMap.containsKey(storeName)){
                            System.out.println(storageMap.get(storeName).get(commandSeq[1]));;
                        } else {
                            System.out.println("Store " + commandSeq[1] + " not found");
                        }
                    }
                } else if ("exists".equals(commandSeq[0])) {
                    if(len < 4){
                        showHelp();
                    } else if(!"in".equals(commandSeq[2])){
                        showHelp();
                    } else {
                        String storeName = commandSeq[3];
                        if(storageMap.containsKey(storeName)){
                            System.out.println(storageMap.get(storeName).containsKey(commandSeq[1]));;
                        } else {
                            System.out.println("Store " + commandSeq[1] + " not found");
                        }
                    }
                } else {
                    showHelp();
                }

            }
        }

        writeToFile();
    }

    private static void showHelp(){
        System.out.println("\nAvailable commands are:");
        System.out.println("create <store name>\n" +
                "delete <store name>\n" +
                "insert <key> <value> into <store>\n" +
                "get <key> from <store>\n" +
                "exists <key> in <store>");
    }
    private static void readFromStore(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.length() == 0){
                    break;
                }
                String store = line;
                Trie trie = new Trie();
                while ((line = reader.readLine()) != null) {
                    if (line.length() == 0) {
                        break;
                    }
                    String[] keyValue = line.split(" ");
                    trie.put(keyValue[0], Integer.parseInt(keyValue[1]));
                }
                storageMap.put(store, trie);
            }
        } catch (FileNotFoundException ex){
            System.out.println("Provided store file not found");
        } catch (IOException e) {
            System.out.println("Some error occurred while reading from file");
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void writeToFile(){
        Set<String> keySet = storageMap.keySet();
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(filePath));
            for(String key: keySet){
                writer.append(key);
                writer.newLine();
                Trie trie = storageMap.get(key);
                Set<CharSequence> trieKeySet = trie.keySet();
                for(CharSequence trieKey: trieKeySet){
                    String line = trieKey.toString() + ' ' + trie.get(trieKey);
                    writer.append(line);
                    writer.newLine();
                }
                writer.newLine();
            }
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Some error occurred while reading from file");
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException ex){}
            }
        }
    }

}
