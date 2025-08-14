package Classes;

public class BookModel {
    private int id;
    private String title;
    private int quantity;


    public BookModel(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }

    //
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


    public int getTotalQuantity() { return quantity; }
    public int getIssuedQuantity() { return 0; }
    public int getAvailableQuantity() { return quantity; }
}