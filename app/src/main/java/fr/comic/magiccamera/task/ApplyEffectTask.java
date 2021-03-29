package fr.comic.magiccamera.task;

import android.os.AsyncTask;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Queue;

import fr.comic.magiccamera.R;
import fr.comic.magiccamera.activity.MainActivity;
import fr.comic.magiccamera.image.ImageEffect;
import fr.comic.magiccamera.image.Image;

/**
 * General Async task any filter function from this project as an async ApplyEffectTask
 */
public class ApplyEffectTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<MainActivity> activityWeakReference; //MainActivity reference
    private ImageEffect effect;
    private Queue<ImageEffect> effects;
    private Image image;

    public ApplyEffectTask(MainActivity activity, ImageEffect effect, Image image) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.effect = effect;
        this.image = image;
    }

    public ApplyEffectTask(MainActivity activity, Queue<ImageEffect> effects, Image image) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.effects = effects;
        this.image = image;
    }

    //Work to do before heavy task
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) { //Prevent memory leaks
            return;
        }
        activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE); //Show progressBar
    }

    //Heavy task to do
    @Override
    protected Void doInBackground(Void... voids) {
        MainActivity activity = activityWeakReference.get();
        while (!isCancelled()) { //Prevent cancelled task by task.cancel()
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            if (effect == null) //apply several effects
                image.applyEffects(effects);//Apply effects
            else
                image.applyEffect(effect);//Apply effect
            return null;
        }

        return null;
    }


    //Update image when heavy task is finished
    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        //***Linked to main activity ***/
        activity.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    //User cancelled effect
    @Override
    protected void onCancelled() {
        super.onCancelled();
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.getImage().discard(); //Abort current effect
        activity.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE); //Hide progressbar

    }
}