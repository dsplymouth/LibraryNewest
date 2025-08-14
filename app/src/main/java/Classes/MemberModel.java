package Classes;

public class MemberModel {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String contact;
    private String membershipEndDate;

    //  new members
    public MemberModel(String username, String firstname, String lastname, String email, String contact, String membershipEndDate) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.membershipEndDate = membershipEndDate;
    }

    //  existing members
    public MemberModel(int id, String username, String firstname, String lastname, String email, String contact, String membershipEndDate) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.membershipEndDate = membershipEndDate;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getMembershipEndDate() { return membershipEndDate; }
    public void setMembershipEndDate(String membershipEndDate) { this.membershipEndDate = membershipEndDate; }

    public String getFullName() { return firstname + " " + lastname; }
}