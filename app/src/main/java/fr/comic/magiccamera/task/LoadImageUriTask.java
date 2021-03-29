package fr.comic.magiccamera.task;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.comic.magiccamera.R;
import fr.comic.magiccamera.activity.MainActivity;
import fr.comic.magiccamera.image.Image;
import fr.comic.magiccamera.image.ImagePack;

/**
 * Load asynchronously a new Image from Uri with async task
 */
public class LoadImageUriTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<MainActivity> activityWeakReference; //MainActivity reference
    private Image image;
    private ImagePack imagePack;
    private Uri source;
    private boolean first;

    List<ThumbnailItem> thumbnailItemList = new ArrayList<>();

    public LoadImageUriTask(MainActivity activity, Uri source) {
        this(activity, source, false);
    }

    public LoadImageUriTask(MainActivity activity, Uri source, boolean first) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.source = source;
        this.first = first;
    }

    //Work to do before heavy task
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) { //Prevent memory leaks
            return;
        }

        if (!first) {
            activity.hideEffectsList();
        }
        activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE); //Show progressBar
        activity.setImageLoading(true);
    }

    //Heavy task to do
    @Override
    protected Void doInBackground(Void... voids) {
        MainActivity activity = activityWeakReference.get();
        while (!isCancelled()) { //Prevent cancelled task by task.cancel()
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            try {
                image = new Image(source, activity); //load and create Image
                imagePack = new ImagePack(image, MainActivity.PREVIEWS_WIDTH, MainActivity.PREVIEWS_HEIGHT);
//                EffectsFragment.createPreviews(imagePack, activity); //create previews

                ThumbnailsManager.clearThumbs();
                thumbnailItemList.clear();

                Bitmap thumbImage  = Bitmap.createScaledBitmap(image.getBitmap(), image.getWidth(), image.getHeight(), false);

                // add normal bitmap first
                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImage;
                thumbnailItem.filterName = "Normal";
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(activity);

                List<ThumbnailItem> filterThumbs = new ArrayList<>(10);
                for (Filter filter : filters) {
                    ThumbnailItem tI = new ThumbnailItem();
                    tI.image = thumbImage;
                    tI.filter = filter;
                    tI.filterName = filter.getName();
                    filterThumbs.add(tI);
                }

                thumbnailItemList.addAll(processThumbs(activity, filterThumbs));

            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
                return null;
            }

            return null;
        }

        return null;
    }

    private Collection<? extends ThumbnailItem> processThumbs(MainActivity context, List<ThumbnailItem> filterThumbs) {
        Collection<ThumbnailItem> processedThumbs = new ArrayList<>();
        for (ThumbnailItem thumb : filterThumbs) {
            // scaling down the image
            float size = context.getResources().getDimension(R.dimen.thumbnail_size);
            thumb.image = Bitmap.createScaledBitmap(thumb.image, (int) size, (int) size, false);
            thumb.image = thumb.filter.processFilter(thumb.image);
            // cropping circle

            // TODO - think about circular thumbnails
            // thumb.image = GeneralUtils.generateCircularBitmap(thumb.image);
            processedThumbs.add(thumb);
        }
        return processedThumbs;
    }


    //Update image when heavy task is finished
    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (!first) {
            activity.showEffectsList();
        }
        //***Linked to main activity ***/
        activity.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        activity.setImagePack(imagePack);
        activity.updateIv();
//        activity.showPreviews();
        activity.showPreviewsNew(thumbnailItemList);

        activity.setImageLoading(false);
        activity.setDefaultFunction();
    }

    @Override
    protected void onCancelled(Void voids) { //Something wrong happenend return to firstActivity
        Toast.makeText(activityWeakReference.get(), "Something went wrong, file may be corrupted", Toast.LENGTH_LONG).show();
        activityWeakReference.get().setImageLoading(false);
        activityWeakReference.get().finish();
    }
}