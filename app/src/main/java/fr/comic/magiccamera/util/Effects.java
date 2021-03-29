package fr.comic.magiccamera.util;

/**
 * This enum is used for generating the layout (see EffectSettingsFragment.java).
 */
public enum Effects {
    BRIGHTNESS("Brightness"),
    NORMAL("Normal"),
    STRUCK("Struck"),
    CLARENDON("Clarendon"),
    OLDMAN("OldMan"),
    MARS("Mars"),
    RISE("Rise"),
    APRIL("April"),
    AMAZON("Amazon"),
    CONTRAST("Contrast"),
    SATURATION("Saturation"),
    ENHANCE("Enhance"),
    TO_GRAY("To gray"),
    INVERT("Invert"),
    CHANGE_HUE("Change hue"),
    KEEP_HUE("Keep hue"),
    BLUR("Blur"),
    SHARPEN("Sharpen"),
    NEON("Neon"),
    CLAHE("CLAHE"),
    
    //used for benchmarking :
    GAUSS_MIN("Gaussian blur 3x3"),
    GAUSS_MAX("Gaussian blur 25x25"),
    MEAN_MIN("Mean blur 3x3"),
    MEAN_MAX("Mean blur 25x25"),
    SHARPEN_MIN("Sharpen 3x3"),
    SHARPEN_MAX("Sharpen 13x13"),
    NEON_SOBEL("Sobel filter"),
    NEON_PREWITT("Prewitt filter"),
    LAPLACE("Laplacian filter"),
    MACRO("demissio lol"),

    CROP11("Crop1:1"),
    CROP23("Crop2:3"),
    CROP32("Crop3:2"),
    CROP43("Crop4:3"),
    CROP34("Crop3:4"),
    CROP916("Crop9:16"),
    CROP169("Crop16:9"),
    CROP_CUSTOM("CropCustom"),
    CROP("Crop"),
    COMIC("Comic");


    private String name = "";

    Effects(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
