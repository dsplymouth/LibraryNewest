package Classes;

public class NotifItem {
    private static int nextId = 1;
    private int id;
    private String title;
    private String message;
    private String timestamp;
    private String type;
    private String username;
    private boolean isRead;

    public NotifItem(String title, String message, String timestamp, String type) {
        this.id = nextId++;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.isRead = false;
    }
 //constructor as used in book
    public NotifItem(String title, String message, String timestamp, String type, String username) {
        this.id = nextId++;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.username = username;
        this.isRead = false;
    }
    //getters and setters like th eothers
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}