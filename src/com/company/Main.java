package com.company;
import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import java.lang.*;
import java.util.stream.Collectors;

public class Main {
    private static Map<String,Integer> Sort(Map<String,Integer> Words){
        Map<String, Integer> sortedMap = Words.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));
        return sortedMap;
    }
    private static void Parse(Map<String,Integer> sortedMap,int WordCount){
        String eol = System.getProperty("line.separator");
        try (Writer writer = new FileWriter("somefile.csv")) {
            for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                Integer value = entry.getValue();
                Float freq= (value/(float)WordCount);
                writer.append(entry.getKey())
                        .append(" ")
                        .append(value.toString())
                        .append(" ")
                        .append(freq.toString())
                        .append(eol);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = null;
        try
        {
            if (args.length == 0) {
                reader = new BufferedReader(new InputStreamReader(System.in));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
            }
        }
        catch (IOException e)
        {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        }
        String line;
        Map<String,Integer> Words= new HashMap<>();
        int WordCount=0;
        while(!(line = reader.readLine()).isEmpty()) {
            //StringBuilder line = new StringBuilder(reader.readLine());
            line=line+" ";
            int len = line.length();
            int start=0,end=0;
            for (int i = 0; i < len; i++) {
                if (!Character.isLetterOrDigit(line.charAt(i))) {
                    end=i;
                    if (start<end) {
                        WordCount+=1;
                        System.out.println(line.substring(start,end).toString());
                        Integer k=Words.get(line.substring(start,end).toString());
                        Words.put(line.substring(start,end).toString(),k==null?1:k+1);
                    }
                    start=i+1;

                }
            }
        }
        Map<String, Integer> sortedMap = Sort(Words);
        Parse(sortedMap,WordCount);






        if (null != reader) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
