package com.google.travelLog.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.travelLog.data.model.category.CategoriesDataResponse;
import com.google.travelLog.data.model.category.CategoryGetResponse;
import com.google.travelLog.data.model.place.PlaceResponse;
import com.google.travelLog.util.GPSTracker;
import com.google.travelLog.util.api.ApiClient;
import com.google.travelLog.util.api.ApiInterface;
import com.google.travellog.R;
import com.google.travellog.databinding.FragmentMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapsFragment";
    private static final int REQUEST_CODE = 1;
    Double latitude, longitude;
    GPSTracker gpsTracker;
    GoogleMap gMap;
    Dialog dialog;
    ArrayList<CategoriesDataResponse> categoryResponseList;
    ArrayList<String> categoryNameList = new ArrayList<>();
    CategoriesDataResponse categorySelected = new CategoriesDataResponse();
    Toolbar toolbar;
    SearchView searchView;
    private FragmentMapsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        toolbar = binding.toolBar;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);

        gpsTracker = new GPSTracker(requireContext());
        checkPermission();
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsTracker.showSettingsAlert();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(MapsFragment.this);

        binding.btnGps.setOnClickListener(v -> getLocation());

        binding.btnLogMyLocation.setOnClickListener(v -> showDialog());
        return view;
    }


    private void getLocation() {
        gpsTracker.getLocation();
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            gMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }


    @SuppressLint("SetTextI18n")
    private void showDialog() {

        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView lati = dialog.findViewById(R.id.lat);
        TextView longi = dialog.findViewById(R.id.longi);

        if (!categoryNameList.isEmpty()) {
            Spinner categorySpinner = dialog.findViewById(R.id.category_spinner);
            ArrayAdapter adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNameList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setPrompt("Select an Item");
            categorySpinner.setAdapter(adapter);

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(requireContext(), categoryResponseList.get(position).id, Toast.LENGTH_SHORT).show();
                    categorySelected = categoryResponseList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        TextInputEditText placeName = dialog.findViewById(R.id.select_name);

        lati.setText("Latitude   : " + latitude);
        longi.setText("Longitude : " + longitude);

        Button updateDetails = dialog.findViewById(R.id.update_btn);
        updateDetails.setOnClickListener(v -> {
            if (placeName.getText().toString().isEmpty() && lati.getText().toString().isEmpty() && longi.getText().toString().isEmpty()) {

                Toast.makeText(requireContext(), "Please enter the values", Toast.LENGTH_SHORT).show();
            } else {
                postPlaces(placeName.getText().toString(), latitude, longitude, categorySelected.id);
//                Log.d(TAG, "showDialog: " + longi.getText().toString() + lati.getText().toString());
            }
        });
        ImageView closeBs = dialog.findViewById(R.id.close_btn);
        closeBs.setOnClickListener(v -> {
            dialog.cancel();
        });
        dialog.show();
    }

    private void getCategories() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<CategoryGetResponse> call = apiInterface.getCategoryResponse();
        call.enqueue(new Callback<CategoryGetResponse>() {
            @Override
            public void onResponse(Call<CategoryGetResponse> call, Response<CategoryGetResponse> response) {
                categoryResponseList = response.body().categoryResponsesList;

                for (int i = 0; i < categoryResponseList.size(); i++) {
                    categoryNameList.add(categoryResponseList.get(i).name);
                }
//                Toast.makeText(requireContext(), categoryResponseList.size() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CategoryGetResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void postPlaces(String placeName, Double lat, Double lon, String categoryId) {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<PlaceResponse> call = apiInterface.postPlaceResponse(placeName, lat, lon, categoryId);
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.code() == 201) {
                    Toast.makeText(requireContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "postPlaces: " + response.body().placeResponseList);
                    navToTimelineFrag();
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

            }
        });

    }


    private void navToTimelineFrag() {
        NavController navController = Navigation.findNavController(binding.getRoot());
        NavDirections action = MapsFragmentDirections.actionMapsFragmentToTimelineFragment();
        navController.navigate(action);
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }


    public void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
        } else {
            requestPermission();
        }
    }


    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            gpsTracker.getLocation();
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint({"MissingPermission"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        getLocation();
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                latitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;
                googleMap.clear();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCategories();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setQueryHint("Search for a Place");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(requireContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);

                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        gMap.addMarker(new MarkerOptions().position(latLng).title(location));

                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.timeline_menu) {
            navToTimelineFrag();
        }
        return super.onOptionsItemSelected(item);
    }
}
