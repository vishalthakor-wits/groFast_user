package com.wits.grofastUser;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofastUser.Api.responseModels.CustomSpinnerModel;
import com.wits.grofastUser.session.UserDetailSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import retrofit2.Response;

public class CommonUtilities {
    private static FirebaseAnalytics mFirebaseAnalytics;

    public static void handleApiError(String TAG, Response response, Context context) {
        try {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
            UserDetailSession userDetailSession = new UserDetailSession(context);
            Bundle bundle = new Bundle();

            String errorBodyString = response.errorBody().string();
            Gson gson = new Gson();
            JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

            String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
            String status = errorBodyJson.has("status") ? errorBodyJson.get("status").getAsString() : "No status";
            String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

            Log.e(TAG, "onResponse -> status       : " + status);
            Log.e(TAG, "onResponse -> message      : " + message);
            Log.e(TAG, "onResponse -> errorMessage : " + errorMessage);

            bundle.putInt("user_id", userDetailSession.getUserId());
            bundle.putString("user_name", userDetailSession.getName());
            bundle.putString("tag", TAG);
            bundle.putString("status", status);
            bundle.putString("message", message);
            bundle.putString("error_msg", errorMessage);
            mFirebaseAnalytics.logEvent("ApiError", bundle);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "handleApiError: error " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static String getPathFromUri(Context context, Uri uri) {
        String[] projection = {android.provider.MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        cursor.close();
        return imagePath;
    }


    public static String formatDate(String inputDate, String inputFormat, String outputFormat) {
        String outputDate = null;
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
            Date date = inputDateFormat.parse(inputDate);
            outputDate = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }


    public static String showToast(Context context, String message) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_product_add_in_cart, null);
        TextView toastMessage = toastView.findViewById(R.id.toast_product_add_in_cart);
        toastMessage.setText(message);

        FrameLayout frameLayout = new FrameLayout(toastView.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 5, 10, 0);
        toastView.setLayoutParams(layoutParams);

        frameLayout.addView(toastView);

        Toast toast = new Toast(toastView.getContext());
        toast.setView(frameLayout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
        toast.show();
        return message;
    }

    public static void setEditTextListeners(EditText digit1, EditText digit2, EditText digit3, EditText digit4) {
        digit1.requestFocus();
        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    digit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    digit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    digit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        digit2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (digit2.getText().toString().isEmpty()) {
                        digit1.requestFocus();
                    }
                }
                return false;
            }
        });

        digit3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (digit3.getText().toString().isEmpty()) {
                        digit2.requestFocus();
                    }
                }
                return false;
            }
        });

        digit4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (digit4.getText().toString().isEmpty()) {
                        digit3.requestFocus();
                    }
                }
                return false;
            }
        });
    }

    public static void startCountdown(AppCompatButton resend, TextView countDownTimer, Context context, long countDownTime) {
        new CountDownTimer(countDownTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                countDownTimer.setText(timeLeftFormatted);

                resend.setClickable(false);
                resend.setBackgroundDrawable(context.getDrawable(R.drawable.textview_design));
                resend.setTextColor(context.getColor(R.color.default_color));
            }

            @Override
            public void onFinish() {
                resend.setClickable(true);
                countDownTimer.setText("00:00");
                resend.setBackgroundDrawable(context.getDrawable(R.drawable.color_button));
                resend.setTextColor(context.getColor(R.color.button_text_color));
            }
        }.start();
    }

    public static boolean validatePhone(EditText etPhone, Context context) {
        if (etPhone.getText() == null || etPhone.getText().toString().isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_message_enter_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = etPhone.getText().toString();
        if (!(phone.length() == 10 && phone.matches("\\d+"))) {
            Toast.makeText(context, context.getString(R.string.toast_message_valid_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validateReceiverName(String name, Context context) {
        if (name.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_enter_receiver_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.length() < 3) {
            Toast.makeText(context, context.getString(R.string.toast_valid_receiver_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validateAddress(String address, Context context) {
        if (address.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_enter_addres), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.length() < 6) {
            Toast.makeText(context, context.getString(R.string.toast_enter_valid_addres), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validateCountry(String country, Context context) {

        if (country.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_enter_country), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (country.length() < 4) {
            Toast.makeText(context, context.getString(R.string.toast_enter_valid_country), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validateState(String state, Context context) {

        if (state.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_enter_state), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (state.length() < 4) {
            Toast.makeText(context, context.getString(R.string.toast_enter_valid_state), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validateCity(String city, Context context) {

        if (city.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_enter_city), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (city.length() < 4) {
            Toast.makeText(context, context.getString(R.string.toast_enter_valid_city), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validatePincode(String pincode, Context context) {

        if (pincode.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.toast_enter_pincode), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pincode.length() != 6) {
            Toast.makeText(context, context.getString(R.string.toast_enter_valid_pincode), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateFromTimestamp(String isoTimestamp) {
        if (isoTimestamp == null || isoTimestamp.isEmpty()) {
            return "";
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoTimestamp);
        ZonedDateTime istDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return istDateTime.format(dateFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTimeFromTimestamp(String isoTimestamp) {
        if (isoTimestamp == null || isoTimestamp.isEmpty()) {
            return "";
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoTimestamp);
        ZonedDateTime istDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return istDateTime.format(timeFormatter);
    }


    public static boolean validateCustomSpinner(AppCompatSpinner spinner, Context context, int messageId) {
        CustomSpinnerModel spinnerModel = (CustomSpinnerModel) spinner.getSelectedItem();

        if (spinnerModel == null) {
            Toast.makeText(context, "" + context.getString(messageId), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}