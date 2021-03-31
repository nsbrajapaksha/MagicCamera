package fr.comic.magiccamera.task;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.comic.magiccamera.R;
import fr.comic.magiccamera.activity.MainActivity;
import fr.comic.magiccamera.fragments.EffectSettingsFragment;
import fr.comic.magiccamera.image.Image;
import fr.comic.magiccamera.image.ImagePack;
import fr.comic.magiccamera.util.Effects;

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
                float size = activity.getResources().getDimension(R.dimen.thumbnail_size);
                thumbnailItem.image = Bitmap.createScaledBitmap(thumbImage, (int) size, (int) size, false);;
                thumbnailItem.filterName = "Normal";
                ThumbnailsManager.addThumb(thumbnailItem);
                thumbnailItemList.add(thumbnailItem);

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

    private void createFilters(List<ThumbnailItem> filterThumbs, List<Filter> filters, Bitmap thumbImage) {
        EffectSettingsFragment effectSettingsFragment = new EffectSettingsFragment(null);
        ThumbnailItem t1 = new ThumbnailItem();
        t1.image = effectSettingsFragment.cartoonImage1(thumbImage);
        t1.filter = filters.get(0);
        t1.filterName = filters.get(0).getName();
        filterThumbs.add(t1);

        ThumbnailItem t2 = new ThumbnailItem();
        t2.image = effectSettingsFragment.cartoonImage2(thumbImage);
        t2.filter = filters.get(0);
        t2.filterName = filters.get(0).getName();
        filterThumbs.add(t2);

        ThumbnailItem t3 = new ThumbnailItem();
        t3.image = effectSettingsFragment.cartoonImage3(thumbImage);
        t3.filter = filters.get(0);
        t3.filterName = filters.get(0).getName();
        filterThumbs.add(t3);

        ThumbnailItem t4 = new ThumbnailItem();
        t4.image = effectSettingsFragment.cartoonImage4(thumbImage);
        t4.filter = filters.get(0);
        t4.filterName = filters.get(0).getName();
        filterThumbs.add(t4);
    }

    private Collection<? extends ThumbnailItem> processThumbs(MainActivity context, List<ThumbnailItem> filterThumbs) {
        Collection<ThumbnailItem> processedThumbs = new ArrayList<>();
        for (int i = 0; i < filterThumbs.size(); i++) {
            // scaling down the image
            float size = context.getResources().getDimension(R.dimen.thumbnail_size);
            filterThumbs.get(i).image = Bitmap.createScaledBitmap(filterThumbs.get(i).image, (int) size, (int) size, false);
//            filterThumbs.get(i).image = filterThumbs.get(i).filter.processFilter(filterThumbs.get(i).image);
            filterThumbs.get(i).image = createThumbImage(filterThumbs.get(i).image, i);
            // cropping circle

            // TODO - think about circular thumbnails
            // thumb.image = GeneralUtils.generateCircularBitmap(thumb.image);
            processedThumbs.add(filterThumbs.get(i));
        }
        return processedThumbs;
    }

    private Bitmap createThumbImage(Bitmap image, int i) {
        Bitmap bitmap;
        switch (i) {
            case 0:
                bitmap= cartoonImage7(image);
                break;
            case 1:
                bitmap= cartoonImage6(image);
                break;
            case 2:
                bitmap = cartoonImage5(image);
                break;
            case 3:
                bitmap = cartoonImage4(image);
                break;
            case 4:
                bitmap = cartoonImage3(image);
                break;
            case 5:
                bitmap = cartoonImage2(image);
                break;
            case 6:
                bitmap = cartoonImage1(image);
                break;
            default:
                bitmap = image;
                break;
        }
        return bitmap;
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