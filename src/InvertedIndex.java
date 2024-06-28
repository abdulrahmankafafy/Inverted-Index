/*

you should create 10 files in the project directory named file1-file2...file10
and fill them with whatever words you want then search for whatever you want,
thank you.
*/

import java.io.*;
import java.util.*;

public class InvertedIndex {

    //maps strings (words) to DictEntry objects to store the inverted index for a collection of documents.
    private static Map<String, DictEntry> index = new HashMap<String, DictEntry>();

    public static void main(String[] args) {
        //The program takes in a list of files that will be processed
        String[] files = {"file1.txt", "file2.txt", "file3.txt", "file4.txt", "file5.txt", "file6.txt", "file7.txt", "file8.txt", "file9.txt", "file10.txt"};
        buildIndex(files);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word: ");
        String word = scanner.next().toLowerCase();
        if (index.containsKey(word)) {
            DictEntry entry = index.get(word);
            System.out.println("Term frequency: " + entry.term_freq);
            System.out.println("Document frequency: " + entry.doc_freq);
            System.out.println("Posting list:");
            Posting posting = entry.pList;
            while (posting != null) {
                System.out.println(posting.docId + " (" + posting.dtf + ")");
                posting = posting.next;
            }
        } else {
            System.out.println("No such term in the index.");
        }
    }
        // it takes the list of files to be indexed
    private static void buildIndex(String[] files) {
        //loops through each file in the array, reading it's contents line by line.

        //it splits the line into individual words using the split method and stores them in an array called words.
        for (String file : files) {
            try {
                //This line creates a new BufferedReader object that reads data from the specified file using a FileReader object.
                //BufferedReader is a class that reads text from a character stream, and FileReader is a class that reads characters from a file.
                // Together, they allow you to read text data from a file character by character or line by line
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                // this line is to change the file names to only number,
                // as an example it changes file1 to 1. so that it can be saved in an integer variable
                int docId = Integer.parseInt(file.replaceAll("[^0-9]", ""));
                // read each line and put it in the line variable
                while ((line = reader.readLine()) != null) {
                    // Split the 'line' variable into an array of individual words
                    //with the argument "\\s+". This regular expression pattern matches one or more white space characters (spaces, tabs, etc.).

                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        // Remove the . and , and these characters
                        word = word.replaceAll("[^a-zA-Z0-9]", "");
                        // Check if the resulting 'word' is not empty
                        if (!word.isEmpty()) {
                            // If the 'word' is not already in the index, create a new entry for it
                            if (!index.containsKey(word)) {
                                index.put(word, new DictEntry());
                            }
                            // Get the existing entry for the 'word' in the index
                            DictEntry entry = index.get(word);
                            // Increment the term frequency count for the 'word' in the 'entry' object
                            entry.term_freq++;
                            // If the current document ID is not already in the posting list for the 'word', add it as a new posting
                            //ya3ny law masalan kan posting f doc1 abl keda w geh f 1 tany seebo zy ma howa
                            if (entry.pList == null || entry.pList.docId != docId) {
                                Posting posting = new Posting();
                                posting.docId = docId;
                                entry.doc_freq++;
                                posting.next = entry.pList;
                                entry.pList = posting;
                            }
                            // If the current document ID is already in the posting list for the 'word', increment its frequency count
                            else {
                                entry.pList.dtf++;
                            }
                        }
                    }
                }
                // Close the BufferedReader object
                reader.close();
            }
            // If there is an IOException, print the stack trace
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DictEntry {
        //the number of documents in which the word appears
        public int doc_freq = 0;

        //the number of times the word appears in all documents
        public int term_freq = 0;

        //a linked list representing the documents in which the word appears
        public Posting pList = null;
    }

    public static class Posting {
        // next is a pointer to the next Posting in the linked list
        public Posting next = null;

        //the ID of the document in which the word appears
        public int docId;

        //the number of times the word appears in the current document
        public int dtf = 1;
    }

}