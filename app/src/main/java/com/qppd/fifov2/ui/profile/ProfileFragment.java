package com.qppd.fifov2.ui.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qppd.fifov2.Classes.Profile;
import com.qppd.fifov2.Classes.User;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.Functionz.UserMethods;
import com.qppd.fifov2.Libs.Imagez.ImageBase64;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.Libs.Sqlz.DB;
import com.qppd.fifov2.Libs.Validatorz.ValidatorClass;
import com.qppd.fifov2.R;
import com.qppd.fifov2.Tasks.ProfileTask;
import com.qppd.fifov2.Tasks.RegisterTask;
import com.qppd.fifov2.databinding.FragmentProfileBinding;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int RESULT_OK = -1;

    private FragmentProfileBinding binding;
    private View root;

    private UserFunctions userFunctions;
    private SharedPreferencesClass sharedPreferences;
    private ImageBase64 imageBase64;
    private UserMethods userMethods;
    private DateTimeClass dateTimeClass;
    private DBHandler dbHandler;

    private CircleImageView imgProfile;
    private TextView txtId;
    private TextView txtUsername;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtAge;
    private EditText edtBirthdate;
    private EditText edtPhone;
    private EditText edtIncome;
    private RadioButton radStudent;
    private RadioButton radEmployed;
    private RadioButton radUnemployed;
    private RadioButton radParent;

    private int radValue = 0;

    private Button btnSave;
    private Button btnUpdate;

    private Calendar calBirthdate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        initComponents();
        loadImage();

        loadProfile();

        return root;
    }

    private void loadImage() {
        if(sharedPreferences.getString("profile_image", "").equals("")){
            imgProfile.setImageResource(R.drawable.applogo);
        }else{

            Bitmap image = imageBase64.deCode(sharedPreferences.getString("profile_image", ""));
            imgProfile.setImageBitmap(image);
        }


    }

    private void loadProfile() {

        Profile profile;
        String birthdate;
        int month;
        int dayOfMonth;
        int year;

        profile = dbHandler.getProfile(UserGlobal.getUser().getId());

        birthdate = profile.getBirthdate();

        month = Integer.parseInt(birthdate.split("/")[0]);
        dayOfMonth = Integer.parseInt(birthdate.split("/")[1]);
        year = Integer.parseInt(birthdate.split("/")[2]);

        int profileType = profile.getType();
        switch (profileType) {
            case 0:
                radStudent.setChecked(true);
                break;
            case 1:
                radEmployed.setChecked(true);
                break;
            case 2:
                radUnemployed.setChecked(true);
                break;
        }
        //userFunctions.showMessage(profile.getId() + "");
        txtId.setText(String.valueOf(profile.getId()));
        edtFirstname.setText(profile.getFirstname());
        edtLastname.setText(profile.getLastname());
        edtBirthdate.setText(birthdate);
        edtAge.setText("" + dateTimeClass.calculateAge(year, month, dayOfMonth));
        edtPhone.setText(profile.getPhone());
        edtIncome.setText(profile.getIncome());

        if (txtId.getText().toString().isEmpty()) {
            btnSave.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }


    }

    private void initComponents() {

        userFunctions = new UserFunctions(getContext());
        sharedPreferences = new SharedPreferencesClass(getContext());
        imageBase64 = new ImageBase64();
        userMethods = new UserMethods();
        dateTimeClass = new DateTimeClass("MM/dd/YYYY");
        dbHandler = new DBHandler(getContext());

        calBirthdate = Calendar.getInstance();

        imgProfile = root.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);
        txtId = root.findViewById(R.id.txtProfileId);
        txtId.setVisibility(View.GONE);
        txtUsername = root.findViewById(R.id.txtUsername);
        txtUsername.setText(UserGlobal.getUser().getUsername());
        edtFirstname = root.findViewById(R.id.edtFirstname);
        edtLastname = root.findViewById(R.id.edtLastname);
        edtAge = root.findViewById(R.id.edtAge);
        edtAge.setEnabled(false);
        edtBirthdate = root.findViewById(R.id.edtBirthdate);
        edtBirthdate.setOnClickListener(this);
        edtPhone = root.findViewById(R.id.edtPhone);
        edtIncome = root.findViewById(R.id.edtIncome);

        radStudent = root.findViewById(R.id.radStudent);
        radStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radValue = 0;
                }


            }
        });
        radEmployed = root.findViewById(R.id.radEmployed);
        radEmployed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radValue = 1;
                }
            }
        });
        radUnemployed = root.findViewById(R.id.radUnemployed);
        radUnemployed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    radValue = 2;
                }
            }
        });

        radParent = root.findViewById(R.id.radParent);
        radParent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radValue = 3;
                }
            }
        });

        btnSave = root.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edtBirthdate:
                showDatePickerDialog();
                break;
            case R.id.btnSave:
                attemptSave();
                break;
            case R.id.btnUpdate:

                attempUpdate();
                loadProfile();
                break;
            case R.id.imgProfile:
                requestStoragePermission();
                break;
        }

    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            openImagePicker();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(this)
                        .asBitmap()
                        .load(selectedImageUri)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                // Set the Bitmap to the CircleImageView
                                //imgProfile.setImageBitmap(resource);
                                String encoded_image = imageBase64.enCode(resource);
                                sharedPreferences.putString("profile_image", encoded_image);
                                loadImage();

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                // Handle any cleanup here
                            }
                        });
            }
        }
    }


    void attemptSave() {

        // Reset errors.
        edtFirstname.setError(null);
        edtLastname.setError(null);
        edtAge.setError(null);
        edtBirthdate.setError(null);
        edtPhone.setError(null);
        edtIncome.setError(null);
        edtIncome.setError(null);

        boolean cancel = false;
        View focusView = null;

        String firstname = edtFirstname.getText().toString();
        String lastname = edtLastname.getText().toString();
        String age = edtAge.getText().toString();
        String birthdate = edtBirthdate.getText().toString();
        String phone = edtPhone.getText().toString();
        String income = edtIncome.getText().toString();
        //String confirm_password = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(firstname)) {
            edtFirstname.setError("Firstname is required!");
            focusView = edtFirstname;
            cancel = true;
        } else if (!ValidatorClass.validateLetterOnly(firstname)) {
            edtFirstname.setError("Invalid! Firstname must not contain any number and symbol!");
            focusView = edtFirstname;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastname)) {
            edtLastname.setError("Lastname is required!");
            focusView = edtLastname;
            cancel = true;
        } else if (!ValidatorClass.validateLetterOnly(lastname)) {
            edtLastname.setError("Invalid! Lastname must not contain any number and symbol!");
            focusView = edtLastname;
            cancel = true;
        }

        if (TextUtils.isEmpty(age)) {
            edtAge.setError("Age is required!");
            focusView = edtAge;
            cancel = true;
        } else if (!ValidatorClass.validateNumberOnly(age)) {
            edtAge.setError("Invalid! Age must not contain any number and symbol!");
            focusView = edtAge;
            cancel = true;
        }

        if (TextUtils.isEmpty(birthdate)) {
            edtBirthdate.setError("Birthdate is required!");
            focusView = edtBirthdate;
            cancel = true;
        } else if (!ValidatorClass.validateDateOnly(birthdate, "MM/dd/YYYY")) {
            edtBirthdate.setError("Invalid! Birthdate must not contain any letter or invalid date symbol!");
            focusView = edtBirthdate;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone no. is required!");
            focusView = edtPhone;
            cancel = true;
        } else if (!ValidatorClass.validatePhoneOnly(phone)) {
            edtPhone.setError("Invalid! Phone No. is not a philippines valid sim number!");
            focusView = edtPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(income)) {
            edtIncome.setError("Income is required!");
            focusView = edtIncome;
            cancel = true;
        } else if (!ValidatorClass.validateNumberOnly(income)) {
            edtIncome.setError("Invalid! Salary/Income/Allowance must not contain any letter or invalid symbol!");
            focusView = edtIncome;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {
            Profile profile = new Profile(UserGlobal.getUser().getId(), firstname, lastname, birthdate,
                    phone, userMethods.formatNumber(income), radValue);
//
            ProfileTask profileTask = new ProfileTask(getContext(), profile, userFunctions);
            profileTask.execute((Void) null);

        }
    }

    void attempUpdate() {

        // Reset errors.
        edtFirstname.setError(null);
        edtLastname.setError(null);
        edtAge.setError(null);
        edtBirthdate.setError(null);
        edtPhone.setError(null);
        edtIncome.setError(null);
        edtIncome.setError(null);

        boolean cancel = false;
        View focusView = null;

        String id = txtId.getText().toString();
        String firstname = edtFirstname.getText().toString();
        String lastname = edtLastname.getText().toString();
        String age = edtAge.getText().toString();
        String birthdate = edtBirthdate.getText().toString();
        String phone = edtPhone.getText().toString();
        String income = edtIncome.getText().toString();
        //String confirm_password = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(firstname)) {
            edtFirstname.setError("Firstname is required!");
            focusView = edtFirstname;
            cancel = true;
        } else if (!ValidatorClass.validateLetterOnly(firstname)) {
            edtFirstname.setError("Invalid! Firstname must not contain any number and symbol!");
            focusView = edtFirstname;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastname)) {
            edtLastname.setError("Lastname is required!");
            focusView = edtLastname;
            cancel = true;
        } else if (!ValidatorClass.validateLetterOnly(lastname)) {
            edtLastname.setError("Invalid! Lastname must not contain any number and symbol!");
            focusView = edtLastname;
            cancel = true;
        }

        if (TextUtils.isEmpty(age)) {
            edtAge.setError("Age is required!");
            focusView = edtAge;
            cancel = true;
        } else if (!ValidatorClass.validateNumberOnly(age)) {
            edtAge.setError("Invalid! Age must not contain any number and symbol!");
            focusView = edtAge;
            cancel = true;
        }

        if (TextUtils.isEmpty(birthdate)) {
            edtBirthdate.setError("Birthdate is required!");
            focusView = edtBirthdate;
            cancel = true;
        } else if (!ValidatorClass.validateDateOnly(birthdate, "MM/dd/YYYY")) {
            edtBirthdate.setError("Invalid! Birthdate must not contain any letter or invalid date symbol!");
            focusView = edtBirthdate;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone no. is required!");
            focusView = edtPhone;
            cancel = true;
        } else if (!ValidatorClass.validatePhoneOnly(phone)) {
            edtPhone.setError("Invalid! Phone No. is not a philippines valid sim number!");
            focusView = edtPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(income)) {
            edtIncome.setError("Income is required!");
            focusView = edtIncome;
            cancel = true;
        } else if (!ValidatorClass.validateNumberOnly(income)) {
            edtIncome.setError("Invalid! Salary/Income/Allowance must not contain any letter or invalid symbol!");
            focusView = edtIncome;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {
            Profile profile = new Profile(Integer.parseInt(id), UserGlobal.getUser().getId(), firstname, lastname, birthdate,
                    phone, userMethods.formatNumber(income), radValue);

            if(dbHandler.updateProfile(profile)){
                userFunctions.showMessage("Profile saved successfully!");
                sharedPreferences.putBoolean("isFirstTime", false);
                loadProfile();
            }
            else{
                userFunctions.showMessage("Saving profile failed! Please try again!");
            }

//
//            ProfileTask profileTask = new ProfileTask(getContext(), profile, userFunctions);
//            profileTask.execute((Void) null);

        }
    }


    private void showDatePickerDialog() {
        int year = calBirthdate.get(Calendar.YEAR);
        int month = calBirthdate.get(Calendar.MONTH);
        int dayOfMonth = calBirthdate.get(Calendar.DAY_OF_MONTH);
        // Create a DatePickerDialog and set the selected date to the EditText when a date is chosen
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edtBirthdate.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                        edtAge.setText("" + dateTimeClass.calculateAge(year, month + 1, dayOfMonth));
                    }
                }, year, month, dayOfMonth);


        datePickerDialog.show();
    }
}