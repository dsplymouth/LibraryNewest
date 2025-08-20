package Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationDatabaseHelper {    private static final String PREF_NAME = "NotificationsPrefs";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final Gson gson = new Gson();
    private static final int MAX_NOTIFICATIONS = 50; // arbitrary limit for now
    private static final String DEFAULT_MESSAGE = "No notifications"; // hardcoded for testing
    // create notification for new book request only staff
    public static void createRequestNotification(Context context, String memberUsername, String bookTitle) {
        Log.d("NotificationDebug", "Creating request notification for: " + memberUsername + " - " + bookTitle);

        String title = "New Book Request";
        String message = "Member '" + memberUsername + "' has requested '" + bookTitle + "'";
        String timestamp = getCurrentTimestamp();

        NotifItem notification = new NotifItem(title, message, timestamp, "request", memberUsername);
        saveNotification(context, notification);

        Log.d("NotificationDebug", "Created request notification for: " + memberUsername + " - " + bookTitle);
    }
    // notification for if their request is approved or denied only member
    public static void createApprovalNotification(Context context, String memberUsername, String bookTitle, boolean approved) {
        String title = approved ? "Request Approved" : "Request Denied";
        String message = approved ?
                "Your request for '" + bookTitle + "' has been approved. Please collect within 3 days." :
                "Your request for '" + bookTitle + "' has been denied.";
        String timestamp = getCurrentTimestamp();

        NotifItem notification = new NotifItem(title, message, timestamp, "approval", memberUsername);
        saveNotification(context, notification);
    }

    // have notif for overdue
    public static void createOverdueNotification(Context context, String memberUsername, String bookTitle, int daysOverdue) {
        String title = "Book Overdue";
        String message = "Book '" + bookTitle + "' is " + daysOverdue + " day(s) overdue. Please return immediately.";
        String timestamp = getCurrentTimestamp();

        NotifItem notification = new NotifItem(title, message, timestamp, "overdue", memberUsername);
        saveNotification(context, notification);
    }

    // all notifs for staff
    public static List<NotifItem> getStaffNotifications(Context context) {
        List<NotifItem> allNotifications = getAllNotifications(context);
        List<NotifItem> staffNotifications = new ArrayList<>();
        // filter for only request notifications

        // staff sees all book requests from members
        // loop through to get only request notifications
        for (NotifItem notification : allNotifications) {
            if ("request".equals(notification.getType())) {
                staffNotifications.add(notification);
            }
        }

        return staffNotifications;    }
    // get notifications for only a member
    public static List<NotifItem> getMemberNotifications(Context context, String username) {
        List<NotifItem> allNotifications = getAllNotifications(context);
        List<NotifItem> memberNotifications = new ArrayList<>();

        // loop through all notifications to find user's notifications
        for (NotifItem notification : allNotifications) {
            if (username.equals(notification.getUsername()) &&
                    ("approval".equals(notification.getType()) || "overdue".equals(notification.getType()))) {
                memberNotifications.add(notification);
            }
        }

        return memberNotifications;
    }

    // save notification to shared preferences
    private static void saveNotification(Context context, NotifItem notification) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<NotifItem> notifications = getAllNotifications(context);

        // check for duplicate notifications so not to overload
        // quick fix for duplicate notifications - had issues before
        String currentTime = getCurrentTimestamp();
        boolean isDuplicate = false;

        for (NotifItem existing : notifications) {
            if (existing.getTitle().equals(notification.getTitle()) &&
                    existing.getMessage().equals(notification.getMessage()) &&
                    existing.getUsername().equals(notification.getUsername()) &&
                    existing.getType().equals(notification.getType())) {
                isDuplicate = true;
                break;
            }        }
        if (!isDuplicate) {
            // add new notification at the beginning
            notifications.add(0, notification);

            // limit is 50 notifications
            // limit to 50 to prevent memory issues
            if (notifications.size() > MAX_NOTIFICATIONS) {
                notifications = notifications.subList(0, MAX_NOTIFICATIONS);
            }

            String json = gson.toJson(notifications);
            prefs.edit().putString(KEY_NOTIFICATIONS, json).apply();

            Log.d("NotificationDebug", "Saved notification. Total count: " + notifications.size());
        } else {
            Log.d("NotificationDebug", "Duplicate notification detected, not saving");
        }
    }

    private static List<NotifItem> getAllNotifications(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_NOTIFICATIONS, "[]");
        Type type = new TypeToken<List<NotifItem>>(){}.getType();
        List<NotifItem> notifications = gson.fromJson(json, type);
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        return notifications;
    }
    // read notifs
    public static void markAsRead(Context context, int notificationId) {
        List<NotifItem> notifications = getAllNotifications(context);
        // find and mark the specific notification as read
        for (NotifItem notification : notifications) {
            if (notification.getId() == notificationId) {
                notification.setRead(true);
                break;
            }        }
        saveAllNotifications(context, notifications);
    }

    public static void markAllStaffNotificationsAsRead(Context context) {
        List<NotifItem> allNotifications = getAllNotifications(context);
        boolean changed = false;

        // mark all request notifications as read for staff
        for (NotifItem notification : allNotifications) {
            if ("request".equals(notification.getType()) && !notification.isRead()) {
                notification.setRead(true);
                changed = true;
            }
        }

        if (changed) {
            saveAllNotifications(context, allNotifications);
        }
    }

    // mark all notifications as read for member
    public static void markAllMemberNotificationsAsRead(Context context, String username) {
        List<NotifItem> allNotifications = getAllNotifications(context);
        boolean changed = false;

        // mark all member's notifications as read
        for (NotifItem notification : allNotifications) {
            if (username.equals(notification.getUsername()) &&
                    ("approval".equals(notification.getType()) || "overdue".equals(notification.getType())) &&
                    !notification.isRead()) {
                notification.setRead(true);
                changed = true;
            }
        }

        if (changed) {
            saveAllNotifications(context, allNotifications);
        }
    }    // shared preferences for notifs
    private static void saveAllNotifications(Context context, List<NotifItem> notifications) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(notifications);
        prefs.edit().putString(KEY_NOTIFICATIONS, json).apply();
    }
    // remove duplicate notifications
    public static void removeDuplicateNotifications(Context context) {
        List<NotifItem> allNotifications = getAllNotifications(context);
        List<NotifItem> uniqueNotifications = new ArrayList<>();
        // loop to remove duplicates
        for (NotifItem notification : allNotifications) {
            boolean isDuplicate = false;
            for (NotifItem existing : uniqueNotifications) {
                if (existing.getTitle().equals(notification.getTitle()) &&
                        existing.getMessage().equals(notification.getMessage()) &&
                        existing.getUsername().equals(notification.getUsername()) &&
                        existing.getType().equals(notification.getType())) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                uniqueNotifications.add(notification);
            }
        }

        if (uniqueNotifications.size() != allNotifications.size()) {
            Log.d("NotificationDebug", "Removed " + (allNotifications.size() - uniqueNotifications.size()) + " duplicate notifications");
            saveAllNotifications(context, uniqueNotifications);
        }
    }
    // get notification count for staff
    public static int getStaffNotificationCount(Context context) {
        return getStaffNotifications(context).size();
    }
    // get unread notification count for staff
    public static int getStaffUnreadNotificationCount(Context context) {
        List<NotifItem> staffNotifications = getStaffNotifications(context);
        int unreadCount = 0;

        // count unread notifications for badge display
        for (NotifItem notification : staffNotifications) {
            if (!notification.isRead()) {
                unreadCount++;
            }
        }
        return unreadCount;
    }
    // get unread notification count for member
    public static int getMemberUnreadNotificationCount(Context context, String username) {
        List<NotifItem> memberNotifications = getMemberNotifications(context, username);
        int unreadCount = 0;

        // count unread notifications for member
        for (NotifItem notification : memberNotifications) {
            if (!notification.isRead()) {
                unreadCount++;
            }
        }
        return unreadCount;
    }

    // remove request notification for member
    public static void removeRequestNotification(Context context, String memberUsername, String bookTitle) {
        List<NotifItem> allNotifications = getAllNotifications(context);
        List<NotifItem> filteredNotifications = new ArrayList<>();

        // find and remove the specific request notification
        for (NotifItem notification : allNotifications) {
            if (!("request".equals(notification.getType()) &&
                    memberUsername.equals(notification.getUsername()) &&
                    notification.getMessage().contains(bookTitle))) {
                filteredNotifications.add(notification);
            }
        }

        if (filteredNotifications.size() != allNotifications.size()) {
            Log.d("NotificationDebug", "Removed request notification for " + memberUsername + " - " + bookTitle);
            saveAllNotifications(context, filteredNotifications);
        }
    }


    // get current time
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}