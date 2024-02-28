package edu.sjsu.android.fitnessify;

import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profile extends Fragment {

    //объявление переменных
    TextInputLayout name, email, contact;
    FirebaseFirestore fireStore;
    FirebaseAuth fAuth;
    String consumerId;
    ImageView profileImg;
    Button updateBtn, signOutBtn;
    EditText unitKg, unitCm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    // profile data метод
    public void setProfileData() {
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            consumerId = currentUser.getUid();
            DocumentReference referenceDocument = fireStore.collection("user").document(consumerId);
            referenceDocument.addSnapshotListener((result, error) -> {
                if (error != null) {
                    // Обработка ошибок
                    return;
                }
                if (result != null && result.exists()) {
                    // вес в кг
                    unitKg.setText(result.getString("weight"));
                    // рост в см
                    unitCm.setText(result.getString("height"));
                    Objects.requireNonNull(email.getEditText()).setText(result.getString("email"));
                    Objects.requireNonNull(contact.getEditText()).setText(result.getString("contact"));
                    Objects.requireNonNull(name.getEditText()).setText(result.getString("name"));
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        // поля просмотра фрагментов
        signOutBtn = fragmentView.findViewById(R.id.sign_out_button);
        unitKg = fragmentView.findViewById(R.id.weight_label);
        unitCm = fragmentView.findViewById(R.id.height_label);
        name = fragmentView.findViewById(R.id.full_name_profile);
        email = fragmentView.findViewById(R.id.profile_email);
        contact = fragmentView.findViewById(R.id.contact_profile);
        updateBtn = fragmentView.findViewById(R.id.update_button);
        profileImg = fragmentView.findViewById(R.id.user_image);

        setProfileData();

        updateBtn.setOnClickListener(res -> {
            String profileName = Objects.requireNonNull(name.getEditText()).getText().toString();
            String profileEmail = Objects.requireNonNull(email.getEditText()).getText().toString();
            String profileContact = Objects.requireNonNull(contact.getEditText()).getText().toString();
            String profileWeight = unitKg.getText().toString();
            String profileHeight = unitCm.getText().toString();
            FirebaseUser currentUser = fAuth.getCurrentUser();
            if (currentUser != null) {
                consumerId = currentUser.getUid();
                DocumentReference referenceDocument = fireStore.collection("user").document(consumerId);
                Map<String, Object> consumer = new HashMap<>();
                consumer.put(getString(R.string.l_weight), profileWeight);
                consumer.put(getString(R.string.l_height), profileHeight);
                consumer.put(getString(R.string.email_id), profileEmail);
                consumer.put(getString(R.string.your_name), profileName);
                consumer.put(getString(R.string.l_contact), profileContact);
                referenceDocument.set(consumer).addOnSuccessListener(result -> Toast.makeText(getActivity(), R.string.profile_updated, Toast.LENGTH_SHORT).show());
            }
        });

        signOutBtn.setOnClickListener(res -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("consumer", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.email_id), "");
            editor.apply();
            Toast.makeText(getActivity(), R.string.signed_out, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        return fragmentView;
    }
}