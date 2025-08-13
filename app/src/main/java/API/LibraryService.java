package API;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.Book;
import Classes.Member;
import android.widget.Toast;
import Classes.BookDatabaseHelper;
import Classes.BookModel;

public class LibraryService {
    private static final String BASE_URL = "http://10.240.72.69/comp2000/library";
    private static RequestQueue requestQueue;
    private static final Gson gson = new Gson();

    // callback interface for member validation
    public interface MemberCallback {
        void onSuccess(Member member);
        void onError(String error);
    }

    // Initialize RequestQueue if needed
    private static void initQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static void getAllMembers(Context context) {
        initQueue(context);
        String url = BASE_URL + "/members";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Type listType = new TypeToken<List<Member>>(){}.getType();
                    List<Member> members = gson.fromJson(response.toString(), listType);

                    // Log each member's details
                    for (Member member : members) {
                        Log.d("API", "Username: " + member.getUsername() +
                                ", Name: " + member.getFirstname() + " " + member.getLastname() +
                                ", Email: " + member.getEmail());
                    }
                },
                error -> {
                    Log.e("API", "Error retrieving all members: " + error.getMessage());
                }
        );

        requestQueue.add(request);
    }

    // get member by username
    public static void getMember(Context context, String username) {
        initQueue(context);
        String url = BASE_URL + "/members/" + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Member member = gson.fromJson(response.toString(), Member.class);

                    Log.d("API", "Username: " + member.getUsername() +
                            ", Name: " + member.getFirstname() + " " + member.getLastname() +
                            ", Email: " + member.getEmail());
                },
                error -> {
                    Log.e("API", "Error retrieving member by username: " + error.getMessage());
                }
        );

        requestQueue.add(request);
    }

    // get member by username or email (with callback for login validation)
    public static void getMember(final Context context, String usernameOrEmail, final MemberCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/members";

        Log.d("API", "Searching for: " + usernameOrEmail);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Member>>(){}.getType();
                        List<Member> members = gson.fromJson(response.toString(), listType);

                        // Find member by username OR email (case insensitive)
                        Member foundMember = null;
                        for (Member member : members) {
                            if (member.getUsername().equalsIgnoreCase(usernameOrEmail) ||
                                    member.getEmail().equalsIgnoreCase(usernameOrEmail)) {
                                foundMember = member;
                                break;
                            }
                        }

                        if (foundMember != null) {
                            Log.d("API", "Found member: " + foundMember.getUsername() + ", Name: " + foundMember.getFirstname());
                            Toast.makeText(context, "Welcome, " + foundMember.getFirstname() + "!", Toast.LENGTH_SHORT).show();
                            callback.onSuccess(foundMember);
                        } else {
                            Log.e("API", "No member found with: " + usernameOrEmail);
                            callback.onError("Invalid username or email");
                        }
                    } catch (Exception e) {
                        Log.e("API", "Error: " + e.getMessage());
                        callback.onError("Invalid data");
                    }
                },
                error -> {
                    Log.e("API", "Error: " + error.getMessage());
                    callback.onError(error.getMessage());
                }
        );

        requestQueue.add(request);
    }

    //add new user
    public static void addMember(Context context, Member member) {
        initQueue(context);
        String url = BASE_URL + "/members";

        try {
            JSONObject jsonRequest = new JSONObject(gson.toJson(member));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Member added successfully");
                        Log.d("API", message);
                    },
                    error -> {
                        Log.e("API", "Error adding member: " + error.getMessage());
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Invalid JSON format: " + e.getMessage());
        }
    }

    //update a member
    public static void updateMember(Context context, String username, Member member) {
        initQueue(context);
        String url = BASE_URL + "/members/" + username;

        try {
            JSONObject jsonRequest = new JSONObject(gson.toJson(member));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Member updated successfully");
                        Log.d("API", message);
                    },
                    error -> {
                        String errorMessage = "Unknown error";

                        if (error.networkResponse != null) {
                            errorMessage = "Status Code: " + error.networkResponse.statusCode;
                            String responseBody = new String(error.networkResponse.data);
                            Log.e("API", "Error Body: " + responseBody);
                        }

                        Log.e("API", "Error updating member: " + errorMessage);
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Invalid JSON format: " + e.getMessage());
        }
    }

    //Delete a member
    public static void deleteMember(Context context, String username) {
        initQueue(context);
        String url = BASE_URL + "/members/" + username;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> Log.d("API", "Member deleted successfully"),
                error -> Log.e("API", "Error deleting member: " + error.getMessage())
        );

        requestQueue.add(request);
    }

    // ................... BOOK ENDPOINTS .................

    //Get books issed to a member
    public static void getBooksForMember(Context context, String username) {
        initQueue(context);
        String url = BASE_URL + "/books/" + username;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Type listType = new TypeToken<List<Book>>(){}.getType();
                    List<Book> books = gson.fromJson(response.toString(), listType);

                    // Log each book's details
                    for (Book book : books) {
                        Log.d("API", "Book: " + book.getBookTitle() +
                                ", Issued to: " + book.getUsername() +
                                ", Issue Date: " + book.getIssueDate() +
                                ", Return Date: " + book.getReturnDate());
                    }
                },
                error -> Log.e("API", "Error retrieving books: " + error.getMessage())
        );

        requestQueue.add(request);
    }

    //issue book to member
    public static void issueBook(Context context, Book book) {
        initQueue(context);
        String url = BASE_URL + "/books";

        try {
            JSONObject jsonRequest = new JSONObject(gson.toJson(book));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Book issued successfully");
                        Log.d("API", message);
                    },
                    error -> Log.e("API", "Error issuing book to member: " + error.getMessage())
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Invalid JSON format: " + e.getMessage());
        }
    }

    //remove book issued to member
    public static void returnBook(Context context, String username, String bookTitle) {
        initQueue(context);
        String url = BASE_URL + "/books";

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("username", username);
            jsonRequest.put("book_title", bookTitle);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Book returned successfully");
                        Log.d("API", message);
                    },
                    error -> Log.e("API", "Error returning book: " + error.getMessage())
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Invalid JSON format: " + e.getMessage());
        }
    }

    // sync books from API to local database
    public static void syncBooksFromAPI(final Context context, final BookDatabaseHelper dbHelper) {
        initQueue(context);
        String url = BASE_URL + "/books";

        Log.d("API", "Syncing books from API");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Book>>(){}.getType();
                        List<Book> apiBooks = gson.fromJson(response.toString(), listType);

                        // loop each book from API
                        for (Book apiBook : apiBooks) {
                            String title = apiBook.getBookTitle();

                            // check if book already exists in local database
                            if (!dbHelper.bookExists(title)) {
                                // add new book with quantity 0 abnd we can work with it to make it say unavailable
                                BookModel newBook = new BookModel(title, 0);
                                dbHelper.addBook(newBook);
                                Log.d("API", "Added book to local DB: " + title);
                            }
                        }

                        Toast.makeText(context, "Synced " + apiBooks.size() + " books from API", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("API", "Error syncing books: " + e.getMessage());
                        Toast.makeText(context, "Error syncing books", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("API", "Error getting books from API: " + error.getMessage());
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(request);
    }

}