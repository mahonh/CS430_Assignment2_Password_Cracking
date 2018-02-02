public class User
{
    private String firstName;
    private String lastName;
    private String userName;
    private String hashedPassword;
    private String unhashedPassword;
    private String salt;
    private boolean cracked;

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public boolean isCracked()
    {
        return cracked;
    }

    public void setCracked(boolean cracked)
    {
        this.cracked = cracked;
    }

    public String getUnhashedPassword()
    {
        return unhashedPassword;
    }

    public void setUnhashedPassword(String unhashedPassword)
    {
        this.unhashedPassword = unhashedPassword;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}
