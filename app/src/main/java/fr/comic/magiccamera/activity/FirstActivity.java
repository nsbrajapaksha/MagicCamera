package fr.comic.magiccamera.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.io.File;

import fr.comic.magiccamera.R;
import fr.comic.magiccamera.util.Utils;

/**
 * First activity showed by the app, to choose the first picture to edit.
 */
public class FirstActivity extends AppCompatActivity {

    // load native image filters library
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private String function = "comic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    @Override
    public void onBackPressed() {
        //go back to home page if return is pressed, because even if the app come back in this activity, it's not suppose to navigate with previous activity.
        moveTaskToBack(true);
    }

    public void showBottomSheetDialog(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        LinearLayout camera = bottomSheetDialog.findViewById(R.id.cameraText);
        LinearLayout gallery = bottomSheetDialog.findViewById(R.id.galleryText);

        if (camera != null) {
            camera.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                camera(v);
            });
        }

        if (gallery != null) {
            gallery.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                gallery(v);
            });
        }
        bottomSheetDialog.show();
    }

    public void gallery(View v) {
        ActivityIO.startGalleryActivityWithPermissions(this);
    }

    public void camera(View v) {
        ActivityIO.startCameraActivityWithPermissions(this);
    }

    /**
     * Load image from internal storage
     *
     * @param reqCode    request code to identify user's choice
     * @param resultCode result to load image
     * @param data       Event given by user to display something
     */
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == ActivityIO.REQUEST_GET_SINGLE_FILE) { // Intent from gallery, containing Uri of a the picture selected.
            if (resultCode == RESULT_OK) {
                try {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setData(data.getData());
                    intent.putExtra("function", function);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong loading from gallery", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }

        if (reqCode == ActivityIO.REQUEST_TAKE_PHOTO) {//Intent from camera.
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("function", function);
                intent.setData(Uri.fromFile(new File(Utils.CAMERA_LAST_BITMAP_PATH)));
                startActivity(intent);
            } else {
                Toast.makeText(this, "You haven't took picture", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ActivityIO.REQUEST_READ_EXTERNAL_STORAGE: {
                if (ActivityIO.readPermissionResult(this, permissions, grantResults)) {
                    ActivityIO.startGalleryActivityWithPermissions(this);
                }
                return;
            }
            case ActivityIO.REQUEST_CAMERA: {
                if (ActivityIO.cameraPermissionResult(this, permissions, grantResults)) {
                    ActivityIO.startCameraActivityWithPermissions(this);
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);    // other 'case' lines to check for other
                // permissions this app might request.
        }
    }


    public void saturation(View view) {
        function = "saturation";
        showBottomSheetDialog(view);
    }

    public void brightness(View view) {
        function = "brightness";
        showBottomSheetDialog(view);
    }

    public void cropPhotos(View view) {
        function = "crop";
        showBottomSheetDialog(view);
    }

    public void comicFilter(View view) {
        function = "comic";
        showBottomSheetDialog(view);
    }
}
