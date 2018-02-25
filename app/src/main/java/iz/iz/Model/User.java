package iz.iz.Model;

/**
 * Created by abdulahiosoble on 2/14/18.
 */

public class User {
    private String name;
    private String number;
    private String password;
    private String owner;
    private String id;

    public User() {
    }

    public User(String name, String number, String password, String owner, String id) {
        this.name = name;
        this.number = number;
        this.password = password;
        this.owner = owner;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
