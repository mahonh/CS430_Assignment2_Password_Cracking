import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Start
{
    public static ArrayList<String> dictionaryWords = new ArrayList<String>();
    public static ArrayList<String> mangledDictionaryWords = new ArrayList<String>();
    public static ArrayList<String> userNameMangles = new ArrayList<String>();

    public static ArrayList<User> users = new ArrayList<User>();
    public static ArrayList<String> keys = new ArrayList<String>();

    public static String dictionary = "Given/wordlist.txt";
    public static String key = "Resources/Keys.txt";

    public static long startTime;

    public static void main(String args[])
    {
        Scanner scany = new Scanner(System.in);
        System.out.println("Please enter your file: ");
        String file = scany.nextLine();
        readDictionary();
        readUsersFile(file);
        readKeyFile(key);

        dictionaryAttack();
    }

    public static void dictionaryAttack()
    {
        startTime = System.currentTimeMillis();

        for (String y : dictionaryWords)
        {
            StringBuilder reverse = new StringBuilder();
            reverse.append(y);

            wordMangler(y, mangledDictionaryWords);
            wordMangler(reverse.reverse().toString(), mangledDictionaryWords);
        }

        for (User g : users)
        {
            wordMangler(g.getFirstName(), userNameMangles);
            wordMangler(g.getLastName(), userNameMangles);

            StringBuilder reverse = new StringBuilder();
            reverse.append(g);
            wordMangler(reverse.reverse().toString(), userNameMangles);
        }

        System.out.println(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        for (User x : users)
        {
            String password = x.getHashedPassword();
            String salt = x.getSalt();

            boolean notfound = true;

            masterloop:
            while (notfound)
            {
                for (String u : userNameMangles)
                {
                    String temp = jcrypt.crypt(salt, u);

                    if (temp.equals(salt + password))
                    {
                        System.out.println(x.getUserName());
                        notfound = false;
                        break masterloop;
                    }
                }

                for (String z : mangledDictionaryWords)
                {
                    String temp = jcrypt.crypt(salt, z);

                    if (temp.equals(salt + password))
                    {
                        System.out.println(x.getUserName());
                        notfound = false;
                        break masterloop;
                    }
                }
                notfound = false;
            }
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }

    public static void bruteForceAttack()
    {

    }

    public static void wordMangler(String word, ArrayList<String> list)
    {
        StringBuilder reverse = new StringBuilder();
        StringBuilder forward = new StringBuilder();

        StringBuilder upFirst = new StringBuilder();
        StringBuilder lowFirst = new StringBuilder();

        list.add(word);

        for (String s : keys)
        {
            list.add(word + s);
            list.add(s + word);
        }

        list.add(word.substring(1)); //remove first character
        list.add(word.substring(0, word.length() - 1)); //remove last character

        reverse.append(word);
        reverse.reverse();
        list.add(reverse.toString()); //reverse word

        forward.append(word);
        list.add(forward.toString() + word); //duplicate word

        list.add(forward.toString() + reverse.toString()); //forward reflect
        list.add(reverse.toString() + forward.toString()); //reverse reflect

        list.add(word.toUpperCase());
        list.add(word.toLowerCase());

        list.add(word.substring(0, 1).toUpperCase() + word.substring(1)); //capitalize first letter

        list.add(word.substring(0, 1).toLowerCase() + word.substring(1).toUpperCase()); //capitalize all except first

        list.add(word.substring(0, word.length() - 1).toLowerCase() + word.substring(word.length() - 1, word.length()).toUpperCase());

        for (int i = 0; i < word.length(); i++)
        {
            if (i % 2 == 0)
            {
                lowFirst.append(word.substring(i, i + 1).toLowerCase());
                upFirst.append(word.substring(i, i + 1).toUpperCase());
            }

            else
                {
                    lowFirst.append(word.substring(i, i + 1).toUpperCase());
                    upFirst.append(word.substring(i, i + 1).toLowerCase());
                }
        }

        list.add(upFirst.toString());
        list.add(lowFirst.toString());
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

    private static void readKeyFile(String file)
    {
        try {
            Scanner scan = new Scanner(new File(file));
            while (scan.hasNext())
                keys.add(scan.next());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
