import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Unix password cracker. Uses Dictionary and Brute force attacks.
 * @author Chad Mahon
 * @version 1.0
 */

public class Start
{
    private static ArrayList<String> dictionaryWords = new ArrayList<String>();
    private static ArrayList<String> mangledDictionaryWords = new ArrayList<String>();
    private static ArrayList<User> bruteForceUsers = new ArrayList<User>();

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
        readKeyFile(key);

        startTime = System.currentTimeMillis();
        readUsersFile(file);

        dictionaryAttack();

        int count = 0;
        for (User x : users)
        {
            if (!x.isCracked())
            {
                count++;
                bruteForceUsers.add(x);
            }
        }

        System.out.println("\n" + (users.size() - count) + " Passwords Cracked, " + count + " Passwords Remaining");

        if (bruteForceUsers.size() != 0)
            bruteForceAttack();
    }

    /**
     * Dictionary attack algorithm. Goes through and mangles the dictionary words. Then attempts each word with the
     * JCrypt function to see if it matches the hashed password.
     */
    private static void dictionaryAttack()
    {
        System.out.println("\nDICTIONARY ATTACK\n");

        for (User x : users) //Goes through all of the users and creates mangled words from their first, last, and user names
        {
            StringBuilder reverseFN = new StringBuilder();
            StringBuilder reverseLN = new StringBuilder();
            StringBuilder reverseUN = new StringBuilder();
            reverseFN.append(x.getFirstName());
            reverseLN.append(x.getLastName());
            reverseUN.append(x.getUserName());

            wordMangler(x.getFirstName(), mangledDictionaryWords);
            wordMangler(x.getLastName(), mangledDictionaryWords);
            wordMangler(x.getUserName(), mangledDictionaryWords);

            wordMangler(reverseFN.reverse().toString(), mangledDictionaryWords);
            wordMangler(reverseLN.reverse().toString(), mangledDictionaryWords);
            wordMangler(reverseUN.reverse().toString(), mangledDictionaryWords);
        }

        for (String y : dictionaryWords) //Goes through all of the dictionary words and creates mangled words from them
        {
            StringBuilder reverse = new StringBuilder();
            reverse.append(y);

            wordMangler(y, mangledDictionaryWords);
            wordMangler(reverse.reverse().toString(), mangledDictionaryWords);
        }

        for (User x : users) //Goes through each user object and test all mangled words against the user's hashed password
        {
            String password = x.getHashedPassword();
            String salt = x.getSalt();

            boolean notfound = true;

            while (notfound)
            {
                for (String z : mangledDictionaryWords) //Goes through all mangled words until/if there is a match
                {
                    String temp = jcrypt.crypt(salt, z); //calls JCrypt hash function

                    if (temp.equals(salt + password))
                    {
                        System.out.println("User: " + x.getUserName() + " Password: " + z);
                        x.setCracked(true);
                        break; //if there is a match, the user is complete and we move on to another user
                    }
                }
                notfound = false;
            }
        }
        System.out.println("\nTOTAL TIME: " + (System.currentTimeMillis() - startTime) + " Milliseconds");
    }

    /**
     * Word mangler algorithm. Performs various mangles to words and adds them to an ArrayList.
     * @param word - Word to be mangled
     * @param list - List to add mangled word
     */
    private static void wordMangler(String word, ArrayList<String> list)
    {
        StringBuilder reverse = new StringBuilder();
        StringBuilder forward = new StringBuilder();

        list.add(word); //add basic word to list

        for (String s : keys) //adds single symbol to start and end of words
        {
            String wordFirst = word + s; //add symbol to end
            String wordLast = s + word; //add symbol to start

            list.add(wordFirst);
            list.add(wordLast);

            manglerCapitalizationHelper(wordFirst, list); //Performs capitalization rules for a word with a
                                                          // symbol added to the end
            manglerCapitalizationHelper(wordLast, list); //Performs capitalization rules for a word with a
                                                         // symbol added to the start
        }

        list.add(word.substring(1)); //remove first character
        list.add(word.substring(0, word.length() - 1)); //remove last character

        reverse.append(word);
        reverse.reverse();

        forward.append(word);
        list.add(forward.toString() + word); //duplicate word

        list.add(forward.toString() + reverse.toString()); //forward reflect
        list.add(reverse.toString() + forward.toString()); //reverse reflect

        //rules for vowels and odd symbols
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

        manglerCapitalizationHelper(word, list);
    }

    /**
     * Capitalization helper for wordMangler method. Provides various capitalization rules for any word.
     * @param word - Word to apply rules to
     * @param list - List to add newly created words to
     */
    private static void manglerCapitalizationHelper(String word, ArrayList<String> list)
    {
        StringBuilder upFirst = new StringBuilder();
        StringBuilder lowFirst = new StringBuilder();

        list.add(word.toUpperCase()); //all uppercase
        list.add(word.toLowerCase()); //all lowercase

        list.add(word.substring(0, 1).toUpperCase() + word.substring(1)); //capitalize first character

        list.add(word.substring(0, 1).toLowerCase() + word.substring(1).toUpperCase()); //capitalize all except first character

        list.add(word.substring(0, word.length() - 1).toLowerCase()
                + word.substring(word.length() - 1, word.length()).toUpperCase()); //capitalize only the last character

        list.add(word.substring(0, 1).toUpperCase() + word.substring(1, word.length() - 1).toLowerCase()
                + word.substring(word.length() - 1, word.length()).toUpperCase()); //capitalize only first and last characters

        list.add(word.substring(0, 1).toLowerCase() + word.substring(1, word.length() -  1).toUpperCase()
                + word.substring(word.length() - 1, word.length()).toLowerCase());  //capitalize everything except first
                                                                                    // and last characters

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

    /**
     * Reads the dictionary file into an ArrayList
     */
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

    /**
     * Reads the user's file with Unix lines. Separates the needed components from each line and creates a User object.
     * @param file - Location of user's file
     */
    private static void readUsersFile(String file)
    {
        try (BufferedReader buffer = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = buffer.readLine()) != null)
            {
                String[] components = line.split(":|\\s+");
                User temp = new User();
                temp.setUserName(components[0]); //user name
                temp.setFirstName(components[4]); //First name
                temp.setLastName(components[5]); //Last name
                temp.setSalt(components[1].substring(0, 2)); //Salt
                temp.setHashedPassword(components[1].substring(2)); //Hashed password
                users.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the Key file with all symbols that can be created using the keyboard.
     * @param file - Location of key file
     */
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

    /**
     * Brute force algorithm. Goes through every possible combination of 8 byte passwords.
     * Only used if the Dictionary Attack did not produce results.
     */
    public static void bruteForceAttack()
    {
        System.out.println("\nBRUTE FORCE ATTACK\n");

        masterloop:
        for (User x : bruteForceUsers)
        {
            String password = x.getHashedPassword();
            String salt = x.getSalt();

            for (int z = 0; z < keys.size(); z++)
            {
                String current = keys.get(z);

                String temp = jcrypt.crypt(salt, current);

                if (temp.equals(salt + password))
                {
                    System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                    break;
                }
            }
            System.out.println("One length character passwords complete");

            twoloop:
            for (int m = 0; m < keys.size(); m++)
            {
                for (int z = 0; z < keys.size(); z++)
                {
                    String current = keys.get(m) + keys.get(z);

                    String temp = jcrypt.crypt(salt, current);

                    if (temp.equals(salt + password))
                    {
                        System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                        break twoloop;
                    }
                }
            }
            System.out.println("Two length character passwords complete");

            threeloop:
            for (int n = 0; n < keys.size(); n++)
            {
                for (int p = 0; p < keys.size(); p++)
                {
                    for (int z = 0; z < keys.size(); z++)
                    {
                        String current = keys.get(n) + keys.get(p) + keys.get(z);

                        String temp = jcrypt.crypt(salt, current);

                        if (temp.equals(salt + password))
                        {
                            System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                            break threeloop;
                        }
                    }
                }
            }
            System.out.println("Three length character passwords complete");

            fourloop:
            for (int q = 0; q < keys.size(); q++)
            {
                for (int n = 0; n < keys.size(); n++)
                {
                    for (int p = 0; p < keys.size(); p++)
                    {
                        for (int z = 0; z < keys.size(); z++)
                        {
                            String current = keys.get(q) + keys.get(n) + keys.get(p) + keys.get(z);

                            String temp = jcrypt.crypt(salt, current);

                            if (temp.equals(salt + password))
                            {
                                System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                                break fourloop;
                            }
                        }
                    }
                }
            }
            System.out.println("Four length character passwords complete");

            fiveloop:
            for (int u = 0; u < keys.size(); u++)
            {
                for (int q = 0; q < keys.size(); q++)
                {
                    for (int n = 0; n < keys.size(); n++)
                    {
                        for (int p = 0; p < keys.size(); p++)
                        {
                            for (int z = 0; z < keys.size(); z++)
                            {
                                String current = keys.get(u) + keys.get(q) + keys.get(n) + keys.get(p) + keys.get(z);

                                String temp = jcrypt.crypt(salt, current);

                                if (temp.equals(salt + password))
                                {
                                    System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                                    break fiveloop;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Five length character passwords complete");

            sixloop:
            for (int w = 0; w < keys.size(); w++)
            {
                for (int u = 0; u < keys.size(); u++)
                {
                    for (int q = 0; q < keys.size(); q++)
                    {
                        for (int n = 0; n < keys.size(); n++)
                        {
                            for (int p = 0; p < keys.size(); p++)
                            {
                                for (int z = 0; z < keys.size(); z++)
                                {
                                    String current = keys.get(w) + keys.get(u) + keys.get(q) + keys.get(n) + keys.get(p) + keys.get(z);

                                    String temp = jcrypt.crypt(salt, current);

                                    if (temp.equals(salt + password))
                                    {
                                        System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                                        break sixloop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Six length character passwords complete");

            sevenloop:
            for (int g = 0; g < keys.size(); g++)
            {
                for (int w = 0; w < keys.size(); w++)
                {
                    for (int u = 0; u < keys.size(); u++)
                    {
                        for (int q = 0; q < keys.size(); q++)
                        {
                            for (int n = 0; n < keys.size(); n++)
                            {
                                for (int p = 0; p < keys.size(); p++)
                                {
                                    for (int z = 0; z < keys.size(); z++)
                                    {
                                        String current = keys.get(g) + keys.get(w) + keys.get(u) + keys.get(q) + keys.get(n) + keys.get(p) + keys.get(z);

                                        String temp = jcrypt.crypt(salt, current);

                                        if (temp.equals(salt + password))
                                        {
                                            System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                                            break sevenloop;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Seven length character passwords complete");

            eightloop:
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

                                            if (temp.equals(salt + password))
                                            {
                                                System.out.println("\nUser: " + x.getUserName() + " Password: " + current);
                                                break eightloop;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Eight length character passwords complete");
        }
    }
}
