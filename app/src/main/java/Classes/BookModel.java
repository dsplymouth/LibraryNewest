package Classes;

public class BookModel {
    private int id;
    private String title;
    private int quantity;

    //reference to data model in comp2000 sql lite
    public BookModel() {}

    public BookModel(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }
    public BookModel(int id, String title, int quantity) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}