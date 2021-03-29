package fr.comic.magiccamera.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.isseiaoki.simplecropview.CropImageView;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;

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
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, (Bitmap target) -> filters.get(0).processFilter(target));
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(STRUCK);
                    image.quickSave();
                    break;
                case CLARENDON:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, (Bitmap target) -> filters.get(1).processFilter(target));
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(CLARENDON);
                    image.quickSave();
                    break;
                case OLDMAN:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, (Bitmap target) -> filters.get(2).processFilter(target));
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(OLDMAN);
                    image.quickSave();
                    break;
                case MARS:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, (Bitmap target) -> filters.get(3).processFilter(target));
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(MARS);
                    image.quickSave();
                    break;
                case RISE:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, (Bitmap target) -> filters.get(4).processFilter(target));
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(RISE);
                    image.quickSave();
                    break;
                case APRIL:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, (Bitmap target) -> filters.get(5).processFilter(target));
                    mainActivity.setCurrentTask(new ApplyEffectTask(mainActivity, currentEffect, image).execute());
                    mainActivity.setComicType(APRIL);
                    image.quickSave();
                    break;
                case AMAZON:
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{}, (Bitmap target) -> filters.get(6).processFilter(target));
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
        if (effect == Effects.BRIGHTNESS) {
            SeekBar sbBrightness = brightnessLayout.findViewById(R.id.brightnessSeekBar);
            sbBrightness.setProgress(mainActivity.getBrightnessValue());
//            sbBrightness.setProgress(127);

            sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                    image.discard();
                    currentEffect = new ImageEffect(effect.getName(), new String[]{String.valueOf(progress)}, (Bitmap target) ->
                            Retouching.setBrightness(target, progress, mainActivity));
                    image.applyEffect(currentEffect);
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
                    currentEffect = new ImageEffect(effect.getName(), new String[]{String.valueOf(progress)}, (Bitmap target) ->
                            Retouching.setSaturation(target, progress, mainActivity));
                    image.applyEffect(currentEffect);
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