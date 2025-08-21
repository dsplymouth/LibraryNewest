package API;

import android.content.Context;
import android.util.Log;
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
import java.util.List;
import Classes.Member;

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

    public interface RequestsListCallback {
        void onSuccess(List<Request> requests);

        void onError(String error);

    }

    public interface RequestCallback {
        void onSuccess(String message);

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
        // simple get request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Member member = gson.fromJson(response.toString(), Member.class);
                        callback.onSuccess(member);
                    } catch (Exception e) {
                        callback.onError("Error parsing member: " + e.getMessage());
                    }
                },
                error -> {// had issues with error handling before needed to know if it was method or server side
                    String errorMessage = "Error retrieving member: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    // get all members
    //added error handling to know which method is messing my system up
    public static void getAllMembers(Context context, MembersListCallback callback) {
        initQueue(context);
        String url = BASE_URL + "/members";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Type listType = new TypeToken<List<Member>>() {
                        }.getType();
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

            Log.d("API", "Sending JSON for add member: " + jsonRequest);

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

            Log.d("API", "Sending JSON for update member: " + jsonRequest);

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
                response -> Log.d("API", "Delete member success: " + response),
                error -> {
                    String errorMessage = "Error deleting member: " + (error.networkResponse != null ?
                            "Status Code: " + error.networkResponse.statusCode : "Unknown error");
                    Log.e("API", errorMessage);
                });

        requestQueue.add(request);
    }
}