package Classes;

public class Book {
    private int id;
    private String username;
    private String book_title;
    private String issue_date;
    private String return_date;


    public Book() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBookTitle() { return book_title; }
    public void setBookTitle(String book_title) { this.book_title = book_title; }

    public String getIssueDate() { return issue_date; }
    public void setIssueDate(String issue_date) { this.issue_date = issue_date; }

    public String getReturnDate() { return return_date; }
    public void setReturnDate(String return_date) { this.return_date = return_date; }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", book_title='" + book_title + '\'' +
                ", issue_date='" + issue_date + '\'' +
                ", return_date='" + return_date + '\'' +
                '}';
    }
}