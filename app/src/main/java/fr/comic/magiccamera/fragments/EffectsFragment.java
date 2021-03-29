package fr.comic.magiccamera.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.ArrayList;
import java.util.List;

import fr.comic.magiccamera.R;
import fr.comic.magiccamera.activity.MainActivity;
import fr.comic.magiccamera.image.ImagePack;
import fr.comic.magiccamera.util.Effects;

import static fr.comic.magiccamera.util.Effects.AMAZON;
import static fr.comic.magiccamera.util.Effects.APRIL;
import static fr.comic.magiccamera.util.Effects.CLARENDON;
import static fr.comic.magiccamera.util.Effects.MARS;
import static fr.comic.magiccamera.util.Effects.NORMAL;
import static fr.comic.magiccamera.util.Effects.OLDMAN;
import static fr.comic.magiccamera.util.Effects.RISE;
import static fr.comic.magiccamera.util.Effects.STRUCK;

public class EffectsFragment extends Fragment {

    private ImageView imgBrightness;
    private ImageView imgContrast;
    private ImageView imgSaturation;
    private ImageView imgEnhance;
    private ImageView imgToGray;
    private ImageView imgInvert;
    private ImageView imgChangeHue;
    private ImageView imgKeepHue;

    private TextView normalText;
    private TextView struckText;
    private TextView clarendonText;
    private TextView oldManText;
    private TextView marsText;
    private TextView riseText;
    private TextView aprilText;
    private TextView amazonText;


    /**
     * Update previews view. lol
     *
     * @param pack The ImagePack
     */
    public void showPreviews(ImagePack pack) {
        ArrayList<ImagePack.Preview> list = pack.getPreviewsList();

        imgBrightness.setImageBitmap(list.get(0).image.getBitmap());
        imgContrast.setImageBitmap(list.get(1).image.getBitmap());
        imgSaturation.setImageBitmap(list.get(2).image.getBitmap());
        imgEnhance.setImageBitmap(list.get(3).image.getBitmap());
        imgToGray.setImageBitmap(list.get(4).image.getBitmap());
        imgInvert.setImageBitmap(list.get(5).image.getBitmap());
        imgChangeHue.setImageBitmap(list.get(6).image.getBitmap());
        imgKeepHue.setImageBitmap(list.get(7).image.getBitmap());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_effects_list, null);
        listeners(view);
        return view;

    }


    private void listeners(View view) {
        final MainActivity main = (MainActivity) getActivity();
        if (main == null) return;

        LinearLayout effectNormal = view.findViewById(R.id.effectNormal);
        LinearLayout effectStruck = view.findViewById(R.id.effectStruck);
        LinearLayout effectClarendon = view.findViewById(R.id.effectClarendon);
        LinearLayout effectOldMan = view.findViewById(R.id.effectOldMan);
        LinearLayout effectMars = view.findViewById(R.id.effectMars);
        LinearLayout effectRise = view.findViewById(R.id.effectRise);
        LinearLayout effectApril = view.findViewById(R.id.effectApril);
        LinearLayout effectAmazon = view.findViewById(R.id.effectAmazon);


        imgBrightness = view.findViewById(R.id.imgBrightness);
        imgContrast = view.findViewById(R.id.imgContrast);
        imgSaturation = view.findViewById(R.id.imgSaturation);
        imgEnhance = view.findViewById(R.id.imgEnhance);
        imgToGray = view.findViewById(R.id.imgToGray);
        imgInvert = view.findViewById(R.id.imgInvert);
        imgChangeHue = view.findViewById(R.id.imgChangeHue);
        imgKeepHue = view.findViewById(R.id.imgKeepHue);

        normalText = view.findViewById(R.id.normalText);
        struckText = view.findViewById(R.id.struckText);
        clarendonText = view.findViewById(R.id.clarendonText);
        oldManText = view.findViewById(R.id.oldManText);
        marsText = view.findViewById(R.id.marsText);
        riseText = view.findViewById(R.id.riseText);
        aprilText = view.findViewById(R.id.aprilText);
        amazonText = view.findViewById(R.id.amazontext);


        effectMars.setOnClickListener(v -> {
            if (marsText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(MARS);
                selectFilter(MARS);
            }
        });
        effectRise.setOnClickListener(v -> {
            if (riseText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(RISE);
                selectFilter(RISE);
            }
        });
        effectNormal.setOnClickListener(v -> {
            if (normalText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(NORMAL);
                selectFilter(NORMAL);
            }

        });
        effectClarendon.setOnClickListener(v -> {
            if (clarendonText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(CLARENDON);
                selectFilter(CLARENDON);
            }
        });
        effectStruck.setOnClickListener(v -> {
            if (struckText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(STRUCK);
                selectFilter(STRUCK);
            }
        });
        effectOldMan.setOnClickListener(v -> {
            if (oldManText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(OLDMAN);
                selectFilter(OLDMAN);
            }
        });
        effectApril.setOnClickListener(v -> {
            if (aprilText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(APRIL);
                selectFilter(APRIL);
            }
        });
        effectAmazon.setOnClickListener(v -> {
            if (amazonText.getCurrentTextColor() != android.graphics.Color.WHITE) {
                main.inflateEffectSettings(AMAZON);
                selectFilter(AMAZON);
            }
        });
    }

    /**
     * select from tab
     *
     * @param effect
     */
    public void selectFilter(Effects effect) {
        switch (effect) {
            case NORMAL:
                normalText.setTextColor(android.graphics.Color.WHITE);
                struckText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                clarendonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                oldManText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                marsText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                riseText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                aprilText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                amazonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case STRUCK:
                normalText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                struckText.setTextColor(android.graphics.Color.WHITE);
                clarendonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                oldManText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                marsText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                riseText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                aprilText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                amazonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case CLARENDON:
                normalText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                struckText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                clarendonText.setTextColor(android.graphics.Color.WHITE);
                oldManText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                marsText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                riseText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                aprilText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                amazonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case OLDMAN:
                normalText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                struckText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                clarendonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                oldManText.setTextColor(android.graphics.Color.WHITE);
                marsText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                riseText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                aprilText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                amazonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case MARS:
                normalText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                struckText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                clarendonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                oldManText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                marsText.setTextColor(android.graphics.Color.WHITE);
                riseText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                aprilText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                amazonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case RISE:
                normalText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                struckText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                clarendonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                oldManText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                marsText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                riseText.setTextColor(android.graphics.Color.WHITE);
                aprilText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                amazonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case APRIL:
                normalText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                struckText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                clarendonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                oldManText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                marsText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                riseText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                aprilText.setTextColor(android.graphics.Color.WHITE);
                amazonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                break;
            case AMAZON:
                normalText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                struckText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                clarendonText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                oldManText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                marsText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                riseText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                aprilText.setTextColor(android.graphics.Color.parseColor("#9a9a9a"));
                amazonText.setTextColor(android.graphics.Color.WHITE);
                break;
            default:
                break;
        }
    }

    public void showPreviewsNew(List<ThumbnailItem> list) {
        imgBrightness.setImageBitmap(list.get(0).image);
        imgContrast.setImageBitmap(list.get(1).image);
        imgSaturation.setImageBitmap(list.get(2).image);
        imgEnhance.setImageBitmap(list.get(3).image);
        imgToGray.setImageBitmap(list.get(4).image);
        imgInvert.setImageBitmap(list.get(5).image);
        imgChangeHue.setImageBitmap(list.get(6).image);
        imgKeepHue.setImageBitmap(list.get(7).image);
    }
}
