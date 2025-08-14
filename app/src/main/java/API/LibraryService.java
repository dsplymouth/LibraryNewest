package API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.Book;
import Classes.Member;
import Classes.BookDatabaseHelper;

public class LibraryService {
    private static final String BASE_URL = "http://10.240.72.69/comp2000/library";
    private static RequestQueue requestQueue;
    private static final Gson gson = new Gson();

    public interface MemberCallback {
        void onSuccess(Member member);
        void onError(String error);
    }

    public interface MembersListCallback {
        void onSuccess(List<Member> members);
        void onError(String error);
    }

    public interface BookCallback {
        void onSuccess(Book book);
        void onError(String error);
    }

    public interface BooksListCallback {
        void onSuccess(List<Book> books);
        void onError(String error);
    }

    public interface RequestCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public interface RequestsListCallback {
        void onSuccess(List<Request> requests);
        void onError(String error);
    }

    private static void initQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    // get single member
    public static void getMember(Context context, String username, MemberCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/members/" + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Member member = gson.fromJson(response.toString(), Member.class);
                        callback.onSuccess(member);
                    } catch (Exception e) {
                        callback.onError("Error parsing member: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Error retrieving member: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    // get all members
    public static void getAllMembers(Context context, MembersListCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/members";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Member>>(){}.getType();
                        List<Member> members = gson.fromJson(response.toString(), listType);

                        for (Member member : members) {
                            Log.d("API", "Username: " + member.getUsername() + ", Name: " +
                                    member.getFirstname() + " " + member.getLastname() +
                                    ", Email: " + member.getEmail());
                        }

                        callback.onSuccess(members);
                    } catch (Exception e) {
                        callback.onError("Error parsing members: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Error retrieving all members: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    // add new member
    public static void addMember(Context context, Member member) {
        initQueue(context);
        String url = BASE_URL + "/members";

        try {
            JSONObject jsonRequest = new JSONObject(gson.toJson(member));

            Log.d("API", "Sending JSON for add member: " + jsonRequest.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Member added successfully");
                        Log.d("API", "Add member success: " + message);
                    },
                    error -> {
                        String errorMessage = "Error adding member: " + (error.networkResponse != null ?
                                "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                        Log.e("API", errorMessage);
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorBody = new String(error.networkResponse.data);
                            Log.e("API", "Error Body: " + errorBody);
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Error creating JSON for add member: " + e.getMessage());
        }
    }

    // update member
    public static void updateMember(Context context, String username, Member member) {
        initQueue(context);
        String url = BASE_URL + "/members/" + username;

        try {
            JSONObject jsonRequest = new JSONObject(gson.toJson(member));

            Log.d("API", "Sending JSON for update member: " + jsonRequest.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Member updated successfully");
                        Log.d("API", "Update member success: " + message);
                    },
                    error -> {
                        String errorMessage = "Error updating member: " + (error.networkResponse != null ?
                                "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                        Log.e("API", errorMessage);
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorBody = new String(error.networkResponse.data);
                            Log.e("API", "Error Body: " + errorBody);
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Error creating JSON for update member: " + e.getMessage());
        }
    }

    // delete member
    public static void deleteMember(Context context, String username) {
        initQueue(context);
        String url = BASE_URL + "/members/" + username;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Log.d("API", "Delete member success: " + response);
                },
                error -> {
                    String errorMessage = "Error deleting member: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                });

        requestQueue.add(request);
    }

    // get single book
    public static void getBook(Context context, int bookId, BookCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/books/" + bookId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Book book = gson.fromJson(response.toString(), Book.class);
                        callback.onSuccess(book);
                    } catch (Exception e) {
                        callback.onError("Error parsing book: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Error retrieving book: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    // get all books
    public static void getAllBooks(Context context, BooksListCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/books";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Book>>(){}.getType();
                        List<Book> books = gson.fromJson(response.toString(), listType);
                        callback.onSuccess(books);
                    } catch (Exception e) {
                        callback.onError("Error parsing books: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Error retrieving books: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    // add new book
    public static void addBook(Context context, Book book) {
        initQueue(context);
        String url = BASE_URL + "/books";

        try {
            JSONObject jsonRequest = new JSONObject(gson.toJson(book));

            Log.d("API", "Sending JSON for add book: " + jsonRequest.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Book added successfully");
                        Log.d("API", "Add book success: " + message);
                    },
                    error -> {
                        String errorMessage = "Error adding book: " + (error.networkResponse != null ?
                                "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                        Log.e("API", errorMessage);
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorBody = new String(error.networkResponse.data);
                            Log.e("API", "Error Body: " + errorBody);
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Error creating JSON for add book: " + e.getMessage());
        }
    }

    // update book
    public static void updateBook(Context context, int bookId, Book book) {
        initQueue(context);
        String url = BASE_URL + "/books/" + bookId;

        try {
            JSONObject jsonRequest = new JSONObject(gson.toJson(book));

            Log.d("API", "Sending JSON for update book: " + jsonRequest.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Book updated successfully");
                        Log.d("API", "Update book success: " + message);
                    },
                    error -> {
                        String errorMessage = "Error updating book: " + (error.networkResponse != null ?
                                "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                        Log.e("API", errorMessage);
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorBody = new String(error.networkResponse.data);
                            Log.e("API", "Error Body: " + errorBody);
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Error creating JSON for update book: " + e.getMessage());
        }
    }

    // delete book
    public static void deleteBook(Context context, int bookId) {
        initQueue(context);
        String url = BASE_URL + "/books/" + bookId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Log.d("API", "Delete book success: " + response);
                },
                error -> {
                    String errorMessage = "Error deleting book: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                });

        requestQueue.add(request);
    }

    // create book request
    public static void createRequest(Context context, int bookId, String username) {
        initQueue(context);
        String url = BASE_URL + "/requests";

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("book_id", bookId);
            jsonRequest.put("username", username);
            jsonRequest.put("status", "pending");

            Log.d("API", "Sending JSON for create request: " + jsonRequest.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Request created successfully");
                        Log.d("API", "Create request success: " + message);
                    },
                    error -> {
                        String errorMessage = "Error creating request: " + (error.networkResponse != null ?
                                "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                        Log.e("API", errorMessage);
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorBody = new String(error.networkResponse.data);
                            Log.e("API", "Error Body: " + errorBody);
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Error creating JSON for request: " + e.getMessage());
        }
    }

    // get all requests (for staff)
    public static void getAllRequests(Context context, RequestsListCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/requests";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Request>>(){}.getType();
                        List<Request> requests = gson.fromJson(response.toString(), listType);
                        callback.onSuccess(requests);
                    } catch (Exception e) {
                        callback.onError("Error parsing requests: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Error retrieving requests: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    // get member's requests
    public static void getMemberRequests(Context context, String username, RequestsListCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/requests/member/" + username;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Request>>(){}.getType();
                        List<Request> requests = gson.fromJson(response.toString(), listType);
                        callback.onSuccess(requests);
                    } catch (Exception e) {
                        callback.onError("Error parsing requests: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Error retrieving member requests: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    // update request status (approve/deny)
    public static void updateRequestStatus(Context context, int requestId, String status) {
        initQueue(context);
        String url = BASE_URL + "/requests/" + requestId;

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("status", status);

            Log.d("API", "Sending JSON for update request status: " + jsonRequest.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Request updated successfully");
                        Log.d("API", "Update request success: " + message);
                    },
                    error -> {
                        String errorMessage = "Error updating request: " + (error.networkResponse != null ?
                                "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                        Log.e("API", errorMessage);
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorBody = new String(error.networkResponse.data);
                            Log.e("API", "Error Body: " + errorBody);
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Error creating JSON for update request: " + e.getMessage());
        }
    }

    // issue book (for staff)
    public static void issueBook(Context context, Book book) {
        initQueue(context);
        String url = BASE_URL + "/books/" + book.getId() + "/issue";

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("quantity", book.getQuantity() - 1);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest,
                    response -> {
                        String message = response.optString("message", "Book issued successfully");
                        Log.d("API", "Issue book success: " + message);
                    },
                    error -> {
                        String errorMessage = "Error issuing book: " + (error.networkResponse != null ?
                                "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                        Log.e("API", errorMessage);
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("API", "Error creating JSON for issue book: " + e.getMessage());
        }
    }
}