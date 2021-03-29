package fr.comic.magiccamera.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.chrisbanes.photoview.PhotoView;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.io.File;
import java.util.List;
import java.util.Queue;

import fr.comic.magiccamera.R;
import fr.comic.magiccamera.filters.Retouching;
import fr.comic.magiccamera.fragments.EffectSettingsFragment;
import fr.comic.magiccamera.fragments.EffectsFragment;
import fr.comic.magiccamera.fragments.MacrosFragment;
import fr.comic.magiccamera.image.Image;
import fr.comic.magiccamera.image.ImageEffect;
import fr.comic.magiccamera.image.ImagePack;
import fr.comic.magiccamera.task.ExportImageTask;
import fr.comic.magiccamera.task.LoadImageUriTask;
import fr.comic.magiccamera.util.Effects;
import fr.comic.magiccamera.util.Utils;

import static fr.comic.magiccamera.util.Effects.BRIGHTNESS;
import static fr.comic.magiccamera.util.Effects.COMIC;
import static fr.comic.magiccamera.util.Effects.CROP;
import static fr.comic.magiccamera.util.Effects.CROP_CUSTOM;
import static fr.comic.magiccamera.util.Effects.NORMAL;
import static fr.comic.magiccamera.util.Effects.SATURATION;

public class MainActivity extends AppCompatActivity {

    public final static int PREVIEWS_WIDTH = 200; //TODO
    public final static int PREVIEWS_HEIGHT = 200;

    private EffectsFragment effectsListFragment;
    private EffectSettingsFragment effectSettingsFragment;
    private MacrosFragment macrosFragment;
    private FragmentManager fragmentManager;
    private AsyncTask currentTask; //Current asyncTask


    //Image currently modified.
    private ImagePack editionPack;

    private PhotoView iv;
    private CropImageView ivCrop;

    private TextView comicText;
    private TextView cropText;
    private TextView brightnessText;
    private TextView saturationText;
    private int brightnessValue = 127;
    private int saturationValue = 127;
    private Effects cropType = CROP_CUSTOM;
    private Effects comicType = NORMAL;

    private boolean imageLoading = false;

    // load native image filters library
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private String function = "comic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = findViewById(R.id.photoView);
        ivCrop = findViewById(R.id.photoViewCrop);


        //Allow more zooming
        iv.setMaximumScale(10);

        //Init fragments
        effectsListFragment = new EffectsFragment();
        macrosFragment = new MacrosFragment(); //only one instanciation of fragments

        //Used for fragment transactions
        fragmentManager = getSupportFragmentManager();

        inflateEffectsList();

        comicText = findViewById(R.id.comictext);
        cropText = findViewById(R.id.cropText);
        brightnessText = findViewById(R.id.brightnessText);
        saturationText = findViewById(R.id.saturationText);

        comicText.setOnClickListener(v -> {
            if (comicText.getCurrentTextColor() != Color.WHITE && !imageLoading) {
                selectFromTab(0);
                inflateEffectSettings(COMIC);
//                deflateEffectSettings();
            }
        });
        cropText.setOnClickListener(v -> {
            if (cropText.getCurrentTextColor() != Color.WHITE && !imageLoading) {
                selectFromTab(1);
                inflateEffectSettings(CROP);
            }
        });
        brightnessText.setOnClickListener(v -> {
            if (brightnessText.getCurrentTextColor() != Color.WHITE && !imageLoading) {
                selectFromTab(2);
                getImage().quickSave();
                inflateEffectSettings(BRIGHTNESS);
            }
        });
        saturationText.setOnClickListener(v -> {
            if (saturationText.getCurrentTextColor() != Color.WHITE && !imageLoading) {
                selectFromTab(3);
                getImage().quickSave();
                inflateEffectSettings(SATURATION);
            }
        });

        try {
            if (getIntent().getExtras() != null && getIntent().getExtras().getString("function") != null) {
                function = getIntent().getExtras().getString("function");
            }
            new LoadImageUriTask(this, getIntent().getData(), true).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Can't load first picture", Toast.LENGTH_LONG).show();
            // Rare situation, then go back to FirstActivity :
            startActivity(new Intent(this, FirstActivity.class));
        }
    }

    /**
     * select from tab
     *
     * @param index
     */
    public void selectFromTab(int index) {
        switch (index) {
            case 0:
                comicText.setTextColor(Color.WHITE);
                cropText.setTextColor(Color.parseColor("#9a9a9a"));
                brightnessText.setTextColor(Color.parseColor("#9a9a9a"));
                saturationText.setTextColor(Color.parseColor("#9a9a9a"));
                break;
            case 1:
                comicText.setTextColor(Color.parseColor("#9a9a9a"));
                cropText.setTextColor(Color.WHITE);
                brightnessText.setTextColor(Color.parseColor("#9a9a9a"));
                saturationText.setTextColor(Color.parseColor("#9a9a9a"));
                break;
            case 2:
                comicText.setTextColor(Color.parseColor("#9a9a9a"));
                cropText.setTextColor(Color.parseColor("#9a9a9a"));
                brightnessText.setTextColor(Color.WHITE);
                saturationText.setTextColor(Color.parseColor("#9a9a9a"));
                break;
            case 3:
                comicText.setTextColor(Color.parseColor("#9a9a9a"));
                cropText.setTextColor(Color.parseColor("#9a9a9a"));
                brightnessText.setTextColor(Color.parseColor("#9a9a9a"));
                saturationText.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }


    /**
     * Update previews UI
     */
    public void showPreviews() {
        effectsListFragment.showPreviews(editionPack);
    }


    /**
     * Refresh bitmap inside the PhotoView
     */
    public void updateIv() {
        iv.setImageBitmap(editionPack.getMainImage().getBitmap());
        ivCrop.setImageBitmap(editionPack.getMainImage().getBitmap());
    }

    /**
     * @return Instance of the main image, the image in the PhotoView at the center of the screen
     */
    public Image getImage() {
        return editionPack.getMainImage();
    }

    /**
     * @return Instance of the image pacj currently edited.
     */
    public ImagePack getImagePack() {
        return editionPack;
    }

    /**
     * Set currently edite {@link ImagePack}.
     *
     * @param imagePack the pack
     */
    public void setImagePack(ImagePack imagePack) {
        this.editionPack = imagePack;
        macrosFragment.resetCounter();
    }

    public void setCurrentTask(AsyncTask currentTask) {
        this.currentTask = currentTask;
    }

    public void cancelCurrentTask() {
        if (this.currentTask != null)
            this.currentTask.cancel(true);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else if (effectSettingsFragment != null && effectSettingsFragment.isVisible()) {
            if (currentTask != null) currentTask.cancel(true); //Cancel task if running
            getImage().discard();
            deflateEffectSettings();
        } else {
            moveTaskToBack(true);
        }
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
                    new LoadImageUriTask(this, data.getData()).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "You did not pick an image", Toast.LENGTH_SHORT).show();
            }
        }

        if (reqCode == ActivityIO.REQUEST_TAKE_PHOTO) {//Intent from camera.
            if (resultCode == RESULT_OK) {
                try {
                    new LoadImageUriTask(this, Uri.fromFile(new File(Utils.CAMERA_LAST_BITMAP_PATH))).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "You did not take a picture", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ActivityIO.REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (ActivityIO.writePermissionResult(this, permissions, grantResults)) {
                    exportImage();
                }
                return;
            }
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

    /**
     * Inflates the list of effects at the bottom of the screen w/ listeners.
     */
    public void inflateEffectsList() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_effects_container, effectsListFragment);
        fragmentTransaction.commit();
    }

    public void hideEffectsList() {
        findViewById(R.id.fragment_effects_container).setVisibility(View.GONE);
    }

    public void showEffectsList() {
        findViewById(R.id.fragment_effects_container).setVisibility(View.VISIBLE);
    }

    /**
     * Inflates the settings (seekbars, buttons...) of a specific effect w/ listeners.
     *
     * @param effect the enum of the effect
     */
    public void inflateEffectSettings(Effects effect) {
        inflateEffectSettings(effect, null);
    }

    /**
     * Inflates the settings (seekbars, buttons...) of a specific effect w/ listeners.
     *
     * @param effect the enum of the effect
     * @param macro  additional paramter for MACRO option
     */
    public void inflateEffectSettings(Effects effect, Queue<ImageEffect> macro) {
        if (effect.equals(CROP) || effect.equals(BRIGHTNESS) || effect.equals(SATURATION)) {
            hideEffectsList();
        } else {
            showEffectsList();
        }
        effectSettingsFragment = new EffectSettingsFragment(macro);

        Bundle args = new Bundle();
        args.putSerializable("effect", effect);
        effectSettingsFragment.setArguments(args);


        //resize image view :
//        ConstraintSet constraintSet = new ConstraintSet();
//        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
//        constraintSet.clone(constraintLayout);
//        constraintSet.connect(R.id.photoView, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
//        constraintSet.applyTo(constraintLayout);


        //fragment switch :
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_settings_container, effectSettingsFragment);
        fragmentTransaction.commit();
    }

    public void deflateEffectSettings() {
        //resize image view :
        ConstraintSet constraintSet = new ConstraintSet();
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.photoView, ConstraintSet.BOTTOM, R.id.guideline3, ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(constraintLayout);

        //fragment switch :
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(effectSettingsFragment);
        fragmentTransaction.commit();
        currentTask = null;
        effectSettingsFragment = null;
        showEffectsList();
    }

    private void exportImage() {
        try {
            Image image;
            if (getImage().getHeight() > getImage().getWidth()) {
                Bitmap bitmap = getImage().getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                getImage().setBitmap(Utils.rotateBitmap(bitmap, 270));
                image = new Image(Utils.rotateBitmap(bitmap, 270));
            } else {
                image = getImage();
            }

            ivCrop.crop(image.getUri()).execute(new CropCallback() {
                @Override
                public void onSuccess(Bitmap cropped) {
                    Bitmap bitmap = cropped.copy(Bitmap.Config.ARGB_8888, true);
                    new ExportImageTask(MainActivity.this, new Image(bitmap)).execute();
                }

                @Override
                public void onError(Throwable e) {

                }
            });
//            new ExportImageTask(this, getImage()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Save cannot be performed", Toast.LENGTH_LONG).show();
        }
    }

    public void applyEffects(Image image) {
        ImageEffect currentEffect = new ImageEffect(BRIGHTNESS.getName(), new String[]{String.valueOf(brightnessValue)}, (Bitmap target) ->
                Retouching.setBrightness(target, brightnessValue, this));
        image.applyEffect(currentEffect);

        currentEffect = new ImageEffect(SATURATION.getName(), new String[]{String.valueOf(saturationValue)}, (Bitmap target) ->
                Retouching.setSaturation(target, saturationValue, this));
        image.applyEffect(currentEffect);
        List<Filter> filters = FilterPack.getFilterPack(this);

        switch (comicType) {
            case STRUCK:
                filters.get(0).processFilter(image.getBitmap());
                image.quickSave();
                break;
            case CLARENDON:
                filters.get(1).processFilter(image.getBitmap());
                image.quickSave();
                break;
            case OLDMAN:
                filters.get(2).processFilter(image.getBitmap());
                image.quickSave();
                break;
            case MARS:
                filters.get(3).processFilter(image.getBitmap());
                image.quickSave();
                break;
            case RISE:
                filters.get(4).processFilter(image.getBitmap());
                image.quickSave();
                break;
            case APRIL:
                filters.get(5).processFilter(image.getBitmap());
                image.quickSave();
                break;
            case AMAZON:
                filters.get(6).processFilter(image.getBitmap());
                image.quickSave();
                break;
            default:
                image.quickSave();
                break;
        }
    }

    public void exportImage(View view) {
        if (!imageLoading) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                exportImage();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Permission is needed to save image", Toast.LENGTH_LONG).show();
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                }

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ActivityIO.REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
    }

    public void goToHomePage(View view) {
        startActivity(new Intent(this, FirstActivity.class));
    }

    public int getBrightnessValue() {
        return this.brightnessValue;
    }

    public void setBrightnessValue(int value) {
        this.brightnessValue = value;
    }

    public int getSaturationValue() {
        return this.saturationValue;
    }

    public void setSaturationValue(int value) {
        this.saturationValue = value;
    }

    public void showPreviewsNew(List<ThumbnailItem> thumbnailItemList) {
        effectsListFragment.showPreviewsNew(thumbnailItemList);
    }

    public Effects getCropType() {
        return this.cropType;
    }

    public void setCropType(Effects cropType) {
        this.cropType = cropType;
    }

    public Effects getComicType() {
        return comicType;
    }

    public void setComicType(Effects comicType) {
        this.comicType = comicType;
    }

    public void setDefaultFunction() {
        if ("crop".equals(function)) {
            selectFromTab(1);
            inflateEffectSettings(CROP);
        } else if ("brightness".equals(function)) {
            selectFromTab(2);
            getImage().quickSave();
            inflateEffectSettings(BRIGHTNESS);
        } else if ("saturation".equals(function)) {
            selectFromTab(3);
            getImage().quickSave();
            inflateEffectSettings(SATURATION);
        }
    }

    public boolean isImageLoading() {
        return imageLoading;
    }

    public void setImageLoading(boolean imageLoading) {
        this.imageLoading = imageLoading;
    }
}
