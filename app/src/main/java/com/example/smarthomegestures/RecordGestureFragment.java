package com.example.smarthomegestures;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.smarthomegestures.databinding.FragmentRecordGestureBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

public class RecordGestureFragment extends Fragment {

    private FragmentRecordGestureBinding binding;
    private ProcessCameraProvider cameraProvider;
    private GestureSelectionViewModel viewModel;
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;

    private final String lineEnd = "\r\n";
    private final String twoHyphens = "--";
    private final String boundary = "*****";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(getContext(),
                            "Permissions denied. You can't record without camera permissions.",
                            Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRecordGestureBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(GestureSelectionViewModel.class);

        // TODO: add an 'upload' action after the recording is done
        binding.videoCaptureButton.setOnClickListener(v -> {
            Log.d("buttonRecordGesture", "Record gesture");
            captureVideo();
        });

        if (checkPermissions()) {
            startCamera();
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);
            } catch (Exception e) {
                Log.e("startCamera", "Use case binding failed", e);
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private void uploadVideo(final File file) {
        Optional<GestureOption> optionalGesture = viewModel.getSelectedGestureOption().getValue();
        if (optionalGesture.isEmpty())
        {
            return;
        }

        final String endpoint = optionalGesture.get().gestureEndpoint();

        if (file == null || !file.exists()) {
            Log.d("uploadVideo", "file does NOT exist");
            return;
        }

        Log.d("uploadVideo", "file exists");
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                String ipAddress = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("server_address", "127.0.0.1");
                URL serverUrl = new URL("http://" + ipAddress + ":50001/upload/"+endpoint);
                connection = (HttpURLConnection) serverUrl.openConnection();
                Log.d("uploadVideo", "created connection");

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("file", file.getName());

                DataOutputStream outputPost = new DataOutputStream(connection.getOutputStream());
                outputPost.writeBytes(twoHyphens + boundary + lineEnd);

                outputPost.writeBytes("Content-Disposition: form-data; name=\"" +
                        "video" + "\";filename=\"" +
                        file.getName() + "\"" + lineEnd);
                outputPost.writeBytes(lineEnd);

                Files.copy(file.toPath(), outputPost);

                outputPost.writeBytes(lineEnd);
                outputPost.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                int serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("uploadVideo", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                StringBuilder result = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = br.readLine();
                while (line != null)
                {
                    result.append(line);
                    line = br.readLine();
                }

                Log.d("uploadVideo", "Server message: " + result);

                if (serverResponseCode == 200) {
                    getActivity().runOnUiThread(() -> {
                        String msg = "File Upload Completed.\nSee uploaded file here : \n"
                                + " http://" + ipAddress + ":50001/uploads/"
                                + endpoint;
                        Log.d("uploadVideo", msg);

                        Toast.makeText(getContext(), "File Upload Complete.",
                                Toast.LENGTH_SHORT).show();
                    });
                }

                outputPost.flush();
                outputPost.close();
            } catch (IOException e) {
                Log.e("uploadVideo", "exception while uploading: " + e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void captureVideo() {
        if (videoCapture == null)
            return;

        binding.videoCaptureButton.setEnabled(false);

        // stop the current recording if one is going
        if (recording != null) {
            recording.stop();
            recording = null;
            return;
        }

        Optional<GestureOption> optionalGesture = viewModel.getSelectedGestureOption().getValue();
        if (optionalGesture.isEmpty())
        {
            return;
        }

        final String name = optionalGesture.get().videoName();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        // What does this one do? Do I want this path?
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video");

        // TODO: change to a temp path that I can copy from to the server then remove the original
        MediaStoreOutputOptions mediaOptions = new MediaStoreOutputOptions
                .Builder(getActivity().getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setDurationLimitMillis(5000)
                .setContentValues(contentValues)
                .build();

        recording = videoCapture.getOutput()
                .prepareRecording(getContext(), mediaOptions)
                .start(ContextCompat.getMainExecutor(getContext()), recordEvent -> {
                    if (recordEvent instanceof VideoRecordEvent.Start) {
                        Log.d("videoStart", "start recording");
                        binding.videoCaptureButton.setText(R.string.stop_capture);
                        binding.videoCaptureButton.setEnabled(true);
                    }
                    else if (recordEvent instanceof VideoRecordEvent.Finalize) {
                        final VideoRecordEvent.Finalize finalize = (VideoRecordEvent.Finalize) recordEvent;
                        if (finalize.hasError() && finalize.getError() != VideoRecordEvent.Finalize.ERROR_DURATION_LIMIT_REACHED) {
                            if (recording != null) {
                                recording.close();
                                recording = null;
                            }
                            Log.e("videoFinalize", "Video capture ended with error: " + finalize.getError());
                        } else {
                            final String message = "Video capture succeeded: " + finalize.getOutputResults().getOutputUri();
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            Log.d("videoFinalize", message);
                            final File savedFile = new File(finalize.getOutputResults().getOutputUri().getPath());
                            uploadVideo(savedFile);
                        }
                        binding.videoCaptureButton.setText(R.string.start_capture);
                        binding.videoCaptureButton.setEnabled(true);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        videoCapture = null;
        if (recording != null) {
            recording.stop();
            recording = null;
        }
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            cameraProvider = null;
        }
        binding = null;
    }

}