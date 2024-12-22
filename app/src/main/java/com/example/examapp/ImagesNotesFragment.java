package com.example.examapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ImagesNotesFragment extends Fragment {

    static final int PICK_IMAGE_REQUEST = 1; // For uploading image
    static final int CAPTURE_IMAGE_REQUEST = 2; // For capturing image

    GridView gridViewImages;
    Button btnUpload, btnCapture, btnDelete;
    ImageAdapter imageAdapter; // this custom adapter to display images
    ArrayList<Bitmap> imageList;

    public ImagesNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images_notes, container, false);

        gridViewImages = view.findViewById(R.id.gridViewImages);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnDelete = view.findViewById(R.id.btnDelete);


        // Initialize image list and adapter
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(getActivity(), imageList);
        gridViewImages.setAdapter(imageAdapter);

        btnUpload.setOnClickListener(v -> openFileChooser());
        btnCapture.setOnClickListener(v -> openCamera());
        btnDelete.setOnClickListener(v -> deleteSelectedImage());


        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); //This is a MIME type filter, so it broadens the selection to anything that matches the image type (e.g., .jpg, .png, etc.)
                                            // from any available provider (not just the external storage).
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
    }
    private void deleteSelectedImage() {
        if (!imageList.isEmpty()) {
            imageList.remove(imageList.size() - 1); // Example: delete the last image
            imageAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Image deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "No images to delete", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                // Image selected from gallery
                // we can't just do imageView.setImageURI(data.getData()); here
                Bitmap selectedImage = ImageUtils.getBitmapFromUri(getActivity(), data.getData());
                imageList.add(selectedImage);
                imageAdapter.notifyDataSetChanged();
            } else if (requestCode == CAPTURE_IMAGE_REQUEST && data != null) {
                // Image captured from camera
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                imageList.add(capturedImage);
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    // A custom adapter class to display images in GridView
    private class ImageAdapter extends BaseAdapter {
        private final Activity context;
        private final ArrayList<Bitmap> images;

        public ImageAdapter(Activity context, ArrayList<Bitmap> images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(images.get(position));
            return imageView;
        }
    }
}
