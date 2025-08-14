package Classes;

import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("username")
    private String username;

    @SerializedName("book_title")
    private String book_title;

    @SerializedName("issue_date")
    private String issue_date;

    @SerializedName("return_date")
    private String return_date;

    public Book() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

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
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", quantity=" + quantity +
                ", username='" + username + '\'' +
                ", book_title='" + book_title + '\'' +
                ", issue_date='" + issue_date + '\'' +
                ", return_date='" + return_date + '\'' +
                '}';
    }
}