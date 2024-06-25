package com.wits.grofastUser.Details;

import static com.wits.grofastUser.Api.RetrofitService.domain;
import static com.wits.grofastUser.CommonUtilities.getPathFromUri;
import static com.wits.grofastUser.CommonUtilities.handleApiError;
import static com.wits.grofastUser.CommonUtilities.setEditTextListeners;
import static com.wits.grofastUser.CommonUtilities.startCountdown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.OtpInterface;
import com.wits.grofastUser.Api.interfaces.UserInterface;
import com.wits.grofastUser.Api.responseClasses.EditProfileResponse;
import com.wits.grofastUser.Api.responseClasses.LoginResponse;
import com.wits.grofastUser.Api.responseModels.UserModel;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;
import com.wits.grofastUser.session.UserDetailSession;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private CircleImageView showProfileImage;
    private AppCompatButton addProfileImage, editProfileImage, updateProfile, changephonenumber;
    private UserDetailSession userDetailSession;
    private MultipartBody.Part image;
    private File imageFile;
    private final String TAG = "EditProfile";
    private RadioButton radioMale, radioFemale, radioOther;
    private TextInputEditText etName, etEmail;
    private TextView tvPhone;
    NestedScrollView scrollView;
    LinearLayout loadingOverlay;
    private UserActivitySession userActivitySession;
    private final int defaultImage = R.drawable.account;
    private boolean isRemoveProfile = false;
    private long COUNTDOWN_TIME_MILLIS = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.edit_profile_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_edit_profile);

        userDetailSession = new UserDetailSession(getApplicationContext());
        userActivitySession = new UserActivitySession(getApplicationContext());
        showProfileImage = findViewById(R.id.show_profile_image);
        addProfileImage = findViewById(R.id.add_profile_image);
        editProfileImage = findViewById(R.id.edit_profile_image);
        updateProfile = findViewById(R.id.saveButton);
        changephonenumber = findViewById(R.id.change_phone_number);

        scrollView = findViewById(R.id.nestedScrollView);
        loadingOverlay = findViewById(R.id.loading_edit);

        radioMale = findViewById(R.id.rb_male);
        radioFemale = findViewById(R.id.rb_female);
        radioOther = findViewById(R.id.rb_other);

        etName = findViewById(R.id.edit_name);
        tvPhone = findViewById(R.id.show_phone_no);
        etEmail = findViewById(R.id.edit_email);

        tvPhone.setText(userDetailSession.getPhoneNo());
        etName.setText(userDetailSession.getName());
        etEmail.setText(userDetailSession.getEmail());
        String image = userDetailSession.getImage();
        Glide.with(getApplicationContext()).load(domain+image).placeholder(defaultImage).into(showProfileImage);

        if (image != null) {
            showEditProfileButton();
        } else showAddProfileButton();

        switch (userDetailSession.getGender()) {
            case "Male":
                radioMale.setChecked(true);
                break;
            case "Female":
                radioFemale.setChecked(true);
                break;
            case "Other":
                radioOther.setChecked(true);
                break;
        }

        addProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        changephonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenChangePhoneNumberDialog();
            }
        });
    }

    private void OpenChangePhoneNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.change_phone_number_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        ImageView close = dialogView.findViewById(R.id.close_change_phone_number);
        EditText phone = dialogView.findViewById(R.id.edit_phone_no);
        AppCompatButton changenumber = dialogView.findViewById(R.id.change_number);
        String currentPhoneNumber = userDetailSession.getPhoneNo();
        phone.setText(currentPhoneNumber);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        changenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPhoneNumber = phone.getText().toString().trim();
                if (newPhoneNumber.equals(currentPhoneNumber)) {
                    Toast.makeText(EditProfile.this, getString(R.string.toast_message_new_phone), Toast.LENGTH_SHORT).show();
                } else {
                    sendOtp(newPhoneNumber, dialog);
                }
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void openOtpPage(String phone) {
        AppCompatButton resentOtp, continueButton;
        TextView phoneNo, countDownTimer;
        EditText digit1, digit2, digit3, digit4;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.otp_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        ImageView close_change_phone_number;


        close_change_phone_number = dialogView.findViewById(R.id.close_change_phone_number);
        close_change_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        phoneNo = dialogView.findViewById(R.id.otp_phone_no);
        phoneNo.setText(phone);

        digit1 = dialogView.findViewById(R.id.otp_digit1);
        digit2 = dialogView.findViewById(R.id.otp_digit2);
        digit3 = dialogView.findViewById(R.id.otp_digit3);
        digit4 = dialogView.findViewById(R.id.otp_digit4);

        resentOtp = dialogView.findViewById(R.id.resend_otp_button);
        continueButton = dialogView.findViewById(R.id.Continue_otp_page);
        countDownTimer = dialogView.findViewById(R.id.countdown_timer);

        startCountdown(resentOtp, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
        setEditTextListeners(digit1, digit2, digit3, digit4);

        resentOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer.getText().toString().equals("00:00")) {
                    loadingOverlay.setVisibility(View.VISIBLE);
                    startCountdown(resentOtp, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
                    sendOtp(phone, null);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_message_resend_otp), Toast.LENGTH_SHORT).show();
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = digit1.getText().toString().trim() + digit2.getText().toString().trim() + digit3.getText().toString().trim() + digit4.getText().toString().trim();
                loadingOverlay.setVisibility(View.VISIBLE);
                Integer userOtp = Integer.parseInt(enteredOtp);
                Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(OtpInterface.class).verifyPhoneUpdateOtp(phone, userOtp);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        loadingOverlay.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            LoginResponse otpVerifyResponse = response.body();
                            updatePhoneNo(otpVerifyResponse.getPhone_no(), dialog);
                        } else {
                            handleApiError(TAG, response, getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_profile_image_heading).setItems(new String[]{getString(R.string.change_image), getString(R.string.remove_image)}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openGallery();
                        break;
                    case 1:
                        showProfileImage.setImageResource(R.drawable.account);
                        showAddProfileButton();
                        image = null;
                        isRemoveProfile = true;
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void updateUserProfile() {
        String selectedGender = null;

        if (radioMale.isChecked()) {
            selectedGender = radioMale.getText().toString();
        } else if (radioFemale.isChecked()) {
            selectedGender = radioFemale.getText().toString();
        } else if (radioOther.isChecked()) {
            selectedGender = radioOther.getText().toString();
        }

        String uname = etName.getText().toString().trim();
        String uemail = etEmail.getText().toString().trim();

        if (uname.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_name), etName);
            return;
        }

        if (selectedGender == null) {
            showToastAndFocus(getString(R.string.toast_message_select_gender), etName);
            return;
        }

        if (uemail.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_email), etName);
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
            showToastAndFocus(getString(R.string.toast_message_enter_valid_email), etName);
            return;
        }


        RequestBody phoneNo = RequestBody.create(MediaType.parse("text/plain"), tvPhone.getText().toString());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), etName.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());

        if (selectedGender != null) {
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), selectedGender);
            Call<EditProfileResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(UserInterface.class).updateProfile(phoneNo, name, email, gender, image);
            scrollView.setVisibility(View.GONE);
            updateProfile.setVisibility(View.GONE);
            loadingOverlay.setVisibility(View.VISIBLE);

            removeProfile();

            call.enqueue(new Callback<EditProfileResponse>() {
                @Override
                public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                    scrollView.setVisibility(View.VISIBLE);
                    updateProfile.setVisibility(View.VISIBLE);
                    loadingOverlay.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        EditProfileResponse editProfileResponse = response.body();
                        UserModel userModel = editProfileResponse.getUserProfile();

                        if (userModel != null) {
                            userDetailSession.setPhoneNo(userModel.getPhone_no());
                            userDetailSession.setImage(userModel.getImage());
                            userDetailSession.setName(userModel.getName());
                            userDetailSession.setEmail(userModel.getEmail());
                            userDetailSession.setGender(userModel.getGender());
                        }
                        Toast.makeText(EditProfile.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else handleApiError(TAG, response, getApplicationContext());
                }

                @Override
                public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                    t.printStackTrace();
                    scrollView.setVisibility(View.VISIBLE);
                    updateProfile.setVisibility(View.VISIBLE);
                    loadingOverlay.setVisibility(View.GONE);
                }
            });
        }
    }

    private void showToastAndFocus(String message, View view) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        showKeyboard(view);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    private void showEditProfileButton() {
        addProfileImage.setVisibility(View.GONE);
        editProfileImage.setVisibility(View.VISIBLE);
    }

    private void showAddProfileButton() {
        editProfileImage.setVisibility(View.GONE);
        addProfileImage.setVisibility(View.VISIBLE);
    }

    private void removeProfile() {
        Call<Void> call = RetrofitService.getClient(userActivitySession.getToken()).create(UserInterface.class).removeUserProfile();
        if (isRemoveProfile) {
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful())
                        handleApiError(TAG, response, getApplicationContext());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void sendOtp(String phone, AlertDialog dialog) {
        Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(OtpInterface.class).sendPhoneUpdateOtp(phone);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loadingOverlay.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    LoginResponse otpSendResponse = response.body();
                    if (otpSendResponse != null) {
                        openOtpPage(otpSendResponse.getPhone_no());
                        Toast.makeText(getApplicationContext(), "" + otpSendResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (dialog != null) dialog.dismiss();
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                loadingOverlay.setVisibility(View.GONE);
            }
        });
    }

    private void updatePhoneNo(String phone, AlertDialog dialog) {
        Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(UserInterface.class).updateuserPhoneNo(phone);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse phoneUpdateResponse = response.body();

                    userDetailSession.setPhoneNo(phoneUpdateResponse.getPhone_no());
                    tvPhone.setText(phoneUpdateResponse.getPhone_no());
                    Toast.makeText(EditProfile.this, "" + phoneUpdateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: updated phone no " + phoneUpdateResponse.getPhone_no());
                    dialog.dismiss();
                } else handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                showProfileImage.setImageBitmap(bitmap);
                showEditProfileButton();
                imageFile = new File(getPathFromUri(getApplicationContext(), data.getData()));
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scrollView.setVisibility(View.VISIBLE);
        updateProfile.setVisibility(View.VISIBLE);
        loadingOverlay.setVisibility(View.GONE);
    }
}