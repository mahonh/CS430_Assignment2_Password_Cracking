import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Start
{
    private static ArrayList<String> dictionaryWords = new ArrayList<String>();
    private static ArrayList<String> mangledDictionaryWords = new ArrayList<String>();
    private static ArrayList<String> userNameMangles = new ArrayList<String>();

    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<String> keys = new ArrayList<String>();

    private static String dictionary = "Given/wordlist.txt";
    private static String key = "Resources/Keys.txt";

    private static long startTime;

    public static void main(String args[])
    {
        Scanner scany = new Scanner(System.in);
        System.out.println("Please enter your file: ");
        String file = scany.nextLine();
        readDictionary();
        readUsersFile(file);
        readKeyFile(key);

        dictionaryAttack();
        //bruteForceAttack();
    }

    private static void dictionaryAttack()
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
            wordMangler(g.getUserName(), userNameMangles);

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

    public static void advancedWordMangler(String word, ArrayList<String> list)
    {
        for (String s : keys) //adds double symbols to start and end of words
        {
            for (String t : keys)
            {
                list.add(word + s + t);
                list.add(s + t + word);
            }
        }



        list.add(word.toUpperCase()); //all uppercase
        list.add(word.toLowerCase()); //all lowercase

        list.add(word.substring(0, 1).toUpperCase() + word.substring(1)); //capitalize first letter

        list.add(word.substring(0, 1).toLowerCase() + word.substring(1).toUpperCase()); //capitalize all except first

        list.add(word.substring(0, word.length() - 1).toLowerCase()
                + word.substring(word.length() - 1, word.length()).toUpperCase()); //capitalize only the last

        list.add(word.substring(0, 1).toUpperCase() + word.substring(1, word.length() - 1).toLowerCase()
                + word.substring(word.length() - 1, word.length()).toUpperCase()); //capitalize only first and last letters

        list.add(word.substring(0, 1).toLowerCase() + word.substring(1, word.length() -  1).toUpperCase()
                + word.substring(word.length() - 1, word.length()).toLowerCase());  //capitalize everything except first and last
    }

    public static void bruteForceAttack()
    {
        System.out.println("BRUTE FORCE");
        for (User x : users)
        {
            String password = x.getHashedPassword();
            String salt = x.getSalt();

            for (int a = 0; a < keys.size(); a++)
            {
                for (int b = 0; b < keys.size(); b++)
                {
                    for (int c = 0; c < keys.size(); c++)
                    {
                        for (int d = 0; d < keys.size(); d++)
                        {
                            for (int e = 0; e < keys.size(); e++)
                            {
                                for (int f = 0; f < keys.size(); f++)
                                {
                                    for (int g = 0; g < keys.size(); g++)
                                    {
                                        for (int h = 0; h < keys.size(); h++)
                                        {
                                            String current = keys.get(a) + keys.get(b) + keys.get(c) + keys.get(d) + keys.get(e)
                                                    + keys.get(f) + keys.get(g) + keys.get(h);

                                            String temp = jcrypt.crypt(salt, current);

                                            System.out.println(current);

                                            if (temp.equals(salt + password))
                                            {
                                                System.out.println(x.getUserName());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private static void wordMangler(String word, ArrayList<String> list)
    {
        StringBuilder reverse = new StringBuilder();
        StringBuilder forward = new StringBuilder();

        StringBuilder upFirst = new StringBuilder();
        StringBuilder lowFirst = new StringBuilder();

        list.add(word); //add basic word to list

        for (String s : keys) //adds single symbol to start and end of words
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

        //rules for vowels
        list.add(word.replace('e', '3'));
        list.add(word.replace('E', '3'));
        list.add(word.replace('l', '1'));
        list.add(word.replace('L', '1'));
        list.add(word.replace('o', '0'));
        list.add(word.replace('O', '0'));
        list.add(word.replace('a', '@'));
        list.add(word.replace('A', '@'));
        list.add(word.replace('s', '$'));
        list.add(word.replace('S', '$'));

        list.add(word.toUpperCase()); //all uppercase
        list.add(word.toLowerCase()); //all lowercase

        list.add(word.substring(0, 1).toUpperCase() + word.substring(1)); //capitalize first letter

        list.add(word.substring(0, 1).toLowerCase() + word.substring(1).toUpperCase()); //capitalize all except first

        list.add(word.substring(0, word.length() - 1).toLowerCase()
                + word.substring(word.length() - 1, word.length()).toUpperCase()); //capitalize only the last

        list.add(word.substring(0, 1).toUpperCase() + word.substring(1, word.length() - 1).toLowerCase()
                + word.substring(word.length() - 1, word.length()).toUpperCase()); //capitalize only first and last letters

        list.add(word.substring(0, 1).toLowerCase() + word.substring(1, word.length() -  1).toUpperCase()
                + word.substring(word.length() - 1, word.length()).toLowerCase());  //capitalize everything except first and last

        //alternating capitalization
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

    public static void wordCapitalizationMangler(String word, ArrayList<String> list)
    {

    }

    private static void readDictionary()
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

    private static void readUsersFile(String file)
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
