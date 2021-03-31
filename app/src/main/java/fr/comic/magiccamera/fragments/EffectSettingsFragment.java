package fr.comic.magiccamera.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.isseiaoki.simplecropview.CropImageView;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import fr.comic.magiccamera.R;
import fr.comic.magiccamera.activity.MainActivity;
import fr.comic.magiccamera.filters.Retouching;
import fr.comic.magiccamera.image.ImageEffect;
import fr.comic.magiccamera.image.Image;
import fr.comic.magiccamera.task.ApplyEffectTask;
import fr.comic.magiccamera.util.Effects;

import static fr.comic.magiccamera.util.Effects.AMAZON;
import static fr.comic.magiccamera.util.Effects.APRIL;
import static fr.comic.magiccamera.util.Effects.CLARENDON;
import static fr.comic.magiccamera.util.Effects.CROP11;
import static fr.comic.magiccamera.util.Effects.CROP169;
import static fr.comic.magiccamera.util.Effects.CROP23;
import static fr.comic.magiccamera.util.Effects.CROP32;
import static fr.comic.magiccamera.util.Effects.CROP34;
import static fr.comic.magiccamera.util.Effects.CROP43;
import static fr.comic.magiccamera.util.Effects.CROP916;
import static fr.comic.magiccamera.util.Effects.CROP_CUSTOM;
import static fr.comic.magiccamera.util.Effects.MARS;
import static fr.comic.magiccamera.util.Effects.NORMAL;
import static fr.comic.magiccamera.util.Effects.OLDMAN;
import static fr.comic.magiccamera.util.Effects.RISE;
import static fr.comic.magiccamera.util.Effects.STRUCK;

/**
 * Effect settings class handle the execution and generation of fragments seekbars
 */
public class EffectSettingsFragment extends Fragment {

    //Contains the cancel/confirm buttons + the settings list.
    private RelativeLayout settingsLayout;
    private ConstraintLayout brightnessLayout;
    private ConstraintLayout cropListLayout;
    private ConstraintLayout saturationLayout;
    //The effect settings list only (containing buttons, seekbars etc).
    private LinearLayout settingsList;

    //The context of the main activity.
    private MainActivity mainActivity;
    //The image we modify.
    private Image image;

    private ImageEffect currentEffect;

    Queue<ImageEffect> macro;

    private TextView customText;
    private TextView text_1_1;
    private TextView text_4_3;
    private TextView text_3_4;
    private TextView text_3_2;
    private TextView text_2_3;
    private TextView text_16_9;
    private TextView text_9_16;

    public EffectSettingsFragment(Queue<ImageEffect> macro) {
        this.macro = macro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        setHasOptionsMenu(true); //change toolbar
        image = mainActivity.getImage();

        Bundle args = getArguments();

        //Get the desired effect
        Effects effect = (Effects) args.getSerializable("effect");

        settingsLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_effect_settings, null);

        //Those params are used to align the settings widgets to the bottom of the screen.
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = 200;
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //Create the layout containing the widgets.
        settingsList = new LinearLayout(super.getContext());
        settingsList.setOrientation(LinearLayout.VERTICAL);
        settingsList.setLayoutParams(rlp);
        List<Filter> filters = FilterPack.getFilterPack(mainActivity);

        //Generate the view and set the listeners for the desired effect
        if (effect != null) {
            switch (effect) {
                case BRIGHTNESS:
                    brightnessLayout = (ConstraintLayout) inflater.inflate(R.layout.brightness_layout, null);
                    simpleEffectView(effect);
                    break;
                case SATURATION:
                    saturationLayout = (ConstraintLayout) inflater.inflate(R.layout.saturation_layout, null);
                    simpleEffectView(effect);
                    break;
                case CROP:
                    cropListLayout = (ConstraintLayout) inflater.inflate(R.layout.crop_list, null);
                    simpleEffectView(effect);
                    break;
                case NORMAL:
                    image.discard();
                    mainActivity.setComicType(NORMAL);
                    image.quickSave();
                    break;
                case STRUCK:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, this::cartoonImage7);
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(STRUCK);
                    image.quickSave();
                    break;
                case CLARENDON:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, this::cartoonImage6);
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(CLARENDON);
                    image.quickSave();
                    break;
                case OLDMAN:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, this::cartoonImage5);
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(OLDMAN);
                    image.quickSave();
                    break;
                case MARS:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, this::cartoonImage4);
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(MARS);
                    image.quickSave();
                    break;
                case RISE:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, this::cartoonImage3);
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(RISE);
                    image.quickSave();
                    break;
                case APRIL:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, this::cartoonImage2);
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(APRIL);
                    image.quickSave();
                    break;
                case AMAZON:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, this::cartoonImage1);
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(AMAZON);
                    image.quickSave();
                    break;
                case MACRO:
                    //no currentEffect but macro is not null
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, macro, image).execute());
                    break;
                default:
                    break;
            }
        }

        //We add the settings list to the existing layout.
        settingsLayout.addView(settingsList);

        if (brightnessLayout != null && effect != null && effect.equals(Effects.BRIGHTNESS)) {
            return brightnessLayout;
        } else if (saturationLayout != null && effect != null && effect.equals(Effects.SATURATION)) {
            return saturationLayout;
        } else if (cropListLayout != null && effect != null && effect.equals(Effects.CROP)) {
            return cropListLayout;
        } else {
            return settingsLayout;
        }
    }

    public Bitmap cartoonImage1(Bitmap img) {
        Mat procMat = new Mat();
        Bitmap bmp32 = img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, procMat);

        Mat orig = new Mat();
        Mat gray = new Mat();
        Mat edges = new Mat();
        Mat color = new Mat();
        Mat cartoon = new Mat();

        Imgproc.cvtColor(procMat, orig, Imgproc.COLOR_BGRA2BGR);
        Imgproc.cvtColor(procMat, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);

        Mat kernel_sharpening = new Mat(3, 3, CvType.CV_8SC1);
        kernel_sharpening.put(0, 0, -1, -1, -1, -1, 9, -1, -1, -1, -1);

        Imgproc.filter2D(gray, gray, gray.depth(), kernel_sharpening);

        Imgproc.medianBlur(gray, gray, 9);
        Size s = new Size(7, 7);
        Imgproc.GaussianBlur(gray, gray, s, 0);

        Imgproc.adaptiveThreshold(gray, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 25, 6);
        Imgproc.bilateralFilter(orig, color, 1, 700, 700);
        Core.bitwise_and(color, color, cartoon, edges);

        orig.release();
        gray.release();
        edges.release();
        color.release();
        kernel_sharpening.release();

        Utils.matToBitmap(cartoon, bmp32);
        image.setBitmap(bmp32);
        ImageView imgView = mainActivity.findViewById(R.id.photoViewCrop);
        imgView.setImageBitmap(bmp32);

        return bmp32;
    }

    public Bitmap cartoonImage3(Bitmap img) {
        Mat procMat = new Mat();
        Bitmap bmp32 = img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, procMat);

        Mat orig = new Mat();
        Mat gray = new Mat();
        Mat edges = new Mat();
        Mat color = new Mat();

        Imgproc.cvtColor(procMat, orig, Imgproc.COLOR_BGRA2BGR);
        Imgproc.cvtColor(procMat, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);

        Mat kernel_sharpening = new Mat(3, 3, CvType.CV_8SC1);
        kernel_sharpening.put(0, 0, -1, -1, -1, -1, 9, -1, -1, -1, -1);

        Imgproc.filter2D(gray, gray, gray.depth(), kernel_sharpening);

        Imgproc.medianBlur(gray, gray, 9);
        Size s = new Size(7, 7);
        Imgproc.GaussianBlur(gray, gray, s, 0);

        Imgproc.adaptiveThreshold(gray, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 25, 6);
        reduceColorsGray(edges, 5);
        Utils.matToBitmap(edges, bmp32);

        orig.release();
        gray.release();
        edges.release();
        color.release();
        kernel_sharpening.release();
        image.setBitmap(bmp32);
        ImageView imgView = mainActivity.findViewById(R.id.photoViewCrop);
        imgView.setImageBitmap(bmp32);

        return bmp32;
    }

    public Bitmap cartoonImage4(Bitmap img) {
        Mat procMat = new Mat();
        Bitmap bmp32 = img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, procMat);

        Mat orig = new Mat();
        Mat gray = new Mat();
        Mat edges = new Mat();
        Mat color = new Mat();
        Mat cartoon = new Mat();

        Imgproc.cvtColor(procMat, orig, Imgproc.COLOR_BGRA2BGR);
        Imgproc.cvtColor(procMat, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);

        Mat kernel_sharpening = new Mat(3, 3, CvType.CV_8SC1);
        kernel_sharpening.put(0, 0, -1, -1, -1, -1, 9, -1, -1, -1, -1);

        Imgproc.filter2D(gray, gray, gray.depth(), kernel_sharpening);

        Imgproc.medianBlur(gray, gray, 9);
        Size s = new Size(7, 7);
        Imgproc.GaussianBlur(gray, gray, s, 0);

        Imgproc.adaptiveThreshold(gray, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 25, 6);
        Imgproc.bilateralFilter(orig, color, 1, 700, 700);
        Core.bitwise_and(gray, gray, cartoon, edges);

        orig.release();
        gray.release();
        edges.release();
        color.release();
        kernel_sharpening.release();

        Utils.matToBitmap(cartoon, img);
        image.setBitmap(img);
        ImageView imgView = mainActivity.findViewById(R.id.photoViewCrop);
        imgView.setImageBitmap(img);

        return img;
    }

    public Bitmap cartoonImage5(Bitmap img) {
        Mat procMat = new Mat();
        Bitmap bmp32 = img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, procMat);

        Mat orig = new Mat();
        Mat gray = new Mat();
        Mat gaussed = new Mat();
        Mat inverted = new Mat();
//        Mat multiplied = new Mat();
        Mat out = new Mat();

        Imgproc.cvtColor(procMat, orig, Imgproc.COLOR_BGRA2BGR);
        Imgproc.cvtColor(procMat, gray, Imgproc.COLOR_BGR2GRAY);
        Mat invertcolormatrix = new Mat(gray.rows(), gray.cols(), gray.type(), new Scalar(255, 255, 255));
        Core.subtract(invertcolormatrix, gray, inverted);

        //Applying GaussianBlur on the Image
        Imgproc.GaussianBlur(inverted, gaussed, new Size(25, 25), 0, 0);
//        Core.multiply(invertcolormatrix, gaussed, multiplied);
        Core.divide(gaussed, inverted, out, 255);


        Utils.matToBitmap(out, bmp32);
        image.setBitmap(bmp32);
        ImageView imgView = mainActivity.findViewById(R.id.photoViewCrop);
        imgView.setImageBitmap(bmp32);

        return bmp32;
    }

    public Bitmap cartoonImage6(Bitmap img) {
        Mat procMat = new Mat();
        Mat orig = new Mat();
        Mat edges = new Mat();
        Mat gray = new Mat();
        Mat color = new Mat();
        Mat cartoon = new Mat();
        Bitmap bmp32 = img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, procMat);

        Imgproc.cvtColor(procMat, orig, Imgproc.COLOR_BGRA2BGR);
        Imgproc.cvtColor(procMat, gray, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Imgproc.adaptiveThreshold(gray, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 9, 5);
        Imgproc.bilateralFilter(orig, color, 9, 200, 200);
        Core.bitwise_and(color, color, cartoon, edges);

        Utils.matToBitmap(cartoon, bmp32);
        image.setBitmap(bmp32);
        ImageView imgView = mainActivity.findViewById(R.id.photoViewCrop);
        imgView.setImageBitmap(bmp32);

        return bmp32;
    }

    public Bitmap cartoonImage7(Bitmap img) {
        Mat procMat = new Mat();
        Bitmap bmp32 = img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, procMat);

        Mat orig = new Mat();
        Mat gray = new Mat();

        Imgproc.cvtColor(procMat, orig, Imgproc.COLOR_BGRA2RGB);
        Imgproc.cvtColor(orig, gray, Imgproc.COLOR_BGRA2GRAY);
        Mat result = reduceColorsGray(gray, 5);

        Utils.matToBitmap(result, bmp32);
        image.setBitmap(bmp32);
        ImageView imgView = mainActivity.findViewById(R.id.photoViewCrop);
        imgView.setImageBitmap(bmp32);

        return bmp32;
    }

    public Bitmap cartoonImage2(Bitmap img) {
        Mat procMat = new Mat();
        Bitmap bmp32 = img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, procMat);

        Mat orig = new Mat();
        Mat out1 = new Mat();
        Imgproc.cvtColor(procMat, orig, Imgproc.COLOR_BGRA2RGB);
        Photo.stylization(orig, out1, 60, 0.07f);

        Utils.matToBitmap(out1, bmp32);
        image.setBitmap(bmp32);
        ImageView imgView = mainActivity.findViewById(R.id.photoViewCrop);
        imgView.setImageBitmap(bmp32);

        return bmp32;
    }

    private Mat createLUT(int numColors) {
        // When numColors=1 the LUT will only have 1 color which is black.
        if (numColors < 0 || numColors > 256) {
            System.out.println("Invalid Number of Colors. It must be between 0 and 256 inclusive.");
            return null;
        }

        Mat lookupTable = Mat.zeros(new Size(1, 256), CvType.CV_8UC1);

        int startIdx = 0;
        for (int x = 0; x < 256; x += 256.0 / numColors) {
            lookupTable.put(x, 0, x);

            for (int y = startIdx; y < x; y++) {
                if (lookupTable.get(y, 0)[0] == 0) {
                    lookupTable.put(y, 0, lookupTable.get(x, 0));
                }
            }
            startIdx = x;
        }
        return lookupTable;
    }

    private Mat reduceColorsGray(Mat img, int numColors) {
        Mat LUT = createLUT(numColors);

        Imgproc.applyColorMap(img, img, LUT);

        return img;
    }

    /**
     * Generate a basic layout with the name of the effect and a seekbar.
     *
     * @param effect the name of the effect.
     */
    public void simpleEffectView(final Effects effect) {
        //Set the settings layout
//        TextView tv = new TextView(super.getContext());
//        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        tv.setText(effect.getName());
        CropImageView cropImageView = mainActivity.findViewById(R.id.photoViewCrop);
        if (effect == Effects.BRIGHTNESS) {
            SeekBar sbBrightness = brightnessLayout.findViewById(R.id.brightnessSeekBar);
            sbBrightness.setProgress(mainActivity.getBrightnessValue());
//            sbBrightness.setProgress(127);

            sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                    image.discard();
//                    currentEffect = new ImageEffect(effect.getName(), new String[]{String.valueOf(progress)}, (Bitmap target) ->
//                            Retouching.setBrightness(target, progress, mainActivity));
//                    image.applyEffect(currentEffect);
                    Bitmap bitmap = Retouching.setBrightnessBmp(image.getBitmap(), progress, mainActivity);
                    image.setBitmap(bitmap);
                    cropImageView.setImageBitmap(bitmap);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mainActivity.setBrightnessValue(seekBar.getProgress());
                }
            });
        } else if (effect == Effects.SATURATION) {
            SeekBar sbSaturation = saturationLayout.findViewById(R.id.saturationSeekBar);
            sbSaturation.setProgress(mainActivity.getSaturationValue());
//            sbSaturation.setProgress(127);

            sbSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                    image.discard();
//                    currentEffect = new ImageEffect(effect.getName(), new String[]{String.valueOf(progress)}, (Bitmap target) ->
//                            Retouching.setSaturation(target, progress, mainActivity));
//                    image.applyEffect(currentEffect);

                    Bitmap bitmap = Retouching.setSaturationBmp(image.getBitmap(), progress, mainActivity);
                    image.setBitmap(bitmap);
                    cropImageView.setImageBitmap(bitmap);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mainActivity.setSaturationValue(seekBar.getProgress());
                }
            });
        } else if (effect == Effects.CROP) {
            listeners(cropListLayout);
        }
    }

    private Bitmap increaseBrightness(Bitmap bitmap, int value){

        Mat src = new Mat(bitmap.getHeight(),bitmap.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(bitmap,src);
        src.convertTo(src,-1,1,value);
        Bitmap result = Bitmap.createBitmap(src.cols(),src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src,result);
        return result;
    }

    private void listeners(View view) {
        final MainActivity main = (MainActivity) getActivity();
        if (main == null) return;

        LinearLayout cropCustom = view.findViewById(R.id.cropCustom);
        LinearLayout crop_1_1 = view.findViewById(R.id.crop_1_1);
        LinearLayout crop_4_3 = view.findViewById(R.id.crop_4_3);
        LinearLayout crop_3_4 = view.findViewById(R.id.crop_3_4);
        LinearLayout crop_3_2 = view.findViewById(R.id.crop_3_2);
        LinearLayout crop_2_3 = view.findViewById(R.id.crop_2_3);
        LinearLayout crop_16_9 = view.findViewById(R.id.crop_16_9);
        LinearLayout crop_9_16 = view.findViewById(R.id.crop_9_16);

        customText = view.findViewById(R.id.customText);
        text_1_1 = view.findViewById(R.id.text_1_1);
        text_4_3 = view.findViewById(R.id.text_4_3);
        text_3_4 = view.findViewById(R.id.text_3_4);
        text_3_2 = view.findViewById(R.id.text_3_2);
        text_2_3 = view.findViewById(R.id.text_2_3);
        text_16_9 = view.findViewById(R.id.text_16_9);
        text_9_16 = view.findViewById(R.id.text_9_16);

        selectCropType(main.getCropType());
        CropImageView cropImageView = main.findViewById(R.id.photoViewCrop);

        cropCustom.setOnClickListener(v -> {
            if (customText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP_CUSTOM);
                main.setCropType(CROP_CUSTOM);
                cropImageView.setCropMode(CropImageView.CropMode.FREE);
            }
        });
        crop_1_1.setOnClickListener(v -> {
            if (text_1_1.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP11);
                main.setCropType(CROP11);
                cropImageView.setCropMode(CropImageView.CropMode.SQUARE);
            }
        });
        crop_4_3.setOnClickListener(v -> {
            if (text_4_3.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP43);
                main.setCropType(CROP43);
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
            }

        });
        crop_3_4.setOnClickListener(v -> {
            if (text_3_4.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP34);
                main.setCropType(CROP34);
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);
            }
        });
        crop_3_2.setOnClickListener(v -> {
            if (text_3_2.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP32);
                main.setCropType(CROP32);
                cropImageView.setCustomRatio(3, 2);
            }
        });
        crop_2_3.setOnClickListener(v -> {
            if (text_2_3.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP23);
                main.setCropType(CROP23);
                cropImageView.setCustomRatio(2, 3);
            }
        });
        crop_16_9.setOnClickListener(v -> {
            if (text_16_9.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP169);
                main.setCropType(CROP169);
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
            }
        });
        crop_9_16.setOnClickListener(v -> {
            if (text_9_16.getCurrentTextColor() != android.graphics.Color.WHITE) {
                selectCropType(CROP916);
                main.setCropType(CROP916);
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);
            }
        });
    }

    /**
     * select from tab
     *
     * @param effect
     */
    public void selectCropType(Effects effect) {
        switch (effect) {
            case CROP_CUSTOM:
                customText.setTextColor(android.graphics.Color.WHITE);
                text_1_1.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_4_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_4.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_2.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_2_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_16_9.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_9_16.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CROP11:
                customText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_1_1.setTextColor(android.graphics.Color.WHITE);
                text_4_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_4.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_2.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_2_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_16_9.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_9_16.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CROP43:
                customText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_1_1.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_4_3.setTextColor(android.graphics.Color.WHITE);
                text_3_4.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_2.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_2_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_16_9.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_9_16.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CROP34:
                customText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_1_1.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_4_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_4.setTextColor(android.graphics.Color.WHITE);
                text_3_2.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_2_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_16_9.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_9_16.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CROP32:
                customText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_1_1.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_4_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_4.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_2.setTextColor(android.graphics.Color.WHITE);
                text_2_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_16_9.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_9_16.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CROP23:
                customText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_1_1.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_4_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_4.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_2.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_2_3.setTextColor(android.graphics.Color.WHITE);
                text_16_9.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_9_16.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CROP169:
                customText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_1_1.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_4_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_4.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_2.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_2_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_16_9.setTextColor(android.graphics.Color.WHITE);
                text_9_16.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CROP916:
                customText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_1_1.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_4_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_4.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_3_2.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_2_3.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_16_9.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                text_9_16.setTextColor(android.graphics.Color.WHITE);
                break;
            default:
                break;
        }
    }

    /**
     * Empty the actionBar when opening an effect.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear(); //hide main ToolBar
        assert (getActivity() != null); //avoid warnings
    }

}