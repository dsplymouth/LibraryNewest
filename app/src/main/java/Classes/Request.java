package Classes;

public class Request {
    private int id;
    private int bookId;
    private String username;
    private String requestDate;
    private String bookTitle;
    private String memberName;

    public Request() {}

    public Request(int bookId, String username, String bookTitle, String memberName) {
        this.bookId = bookId;
        this.username = username;
        this.bookTitle = bookTitle;
        this.memberName = memberName;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
}