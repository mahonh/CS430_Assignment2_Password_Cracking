import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Start
{
    public static ArrayList<String> dictionaryWords = new ArrayList<String>();
    public static ArrayList<String> mangledDictionaryWords = new ArrayList<String>();
    public static ArrayList<User> users = new ArrayList<User>();
    public static String dictionary = "Given/wordlist.txt";
    public static long startTime;

    public static void main(String args[])
    {
        Scanner scany = new Scanner(System.in);
        System.out.println("Please enter your file: ");
        String file = scany.nextLine();
        readDictionary();
        readUsersFile(file);
    }

    public static void wordMangler(String word)
    {

    }

    public static void readDictionary()
    {
        try (BufferedReader buffer = new BufferedReader(new FileReader(dictionary)))
        {
            String line;
            while ((line = buffer.readLine()) != null)
                dictionaryWords.add(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readUsersFile(String file)
    {
        try (BufferedReader buffer = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = buffer.readLine()) != null)
            {
                String[] components = line.split(":|\\s+");
                User temp = new User();
                temp.setUserName(components[0]);
                temp.setFirstName(components[4]);
                temp.setLastName(components[5]);
                temp.setSalt(components[1].substring(0, 2));
                temp.setHashedPassword(components[1].substring(2));
                users.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
