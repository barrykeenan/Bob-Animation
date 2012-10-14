package com.keeny.components;

import com.keeny.AppMain;
import com.sun.j3d.utils.image.TextureLoader;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: keeny
 * Date: 08-Jan-2005
 * Time: 16:12:03
 * <p/>
 * Holder for all the appearances used in the model.
 * <p/>
 * TextureLoaders need a component to load files into - any textures
 * here use an instance of AppMain for this purpose.
 */
public class Appearances {

    private static Color3f black = rgb(0, 0, 0);


    private static Color3f rgb(int r, int g, int b){
        return new Color3f(new Color(r, g, b));
    }

    //--------------------------------------------------

    public static Appearance hatAppearance() {
        Appearance appear = new Appearance();
        Material material = new Material();

        Color rgb = new Color(15, 100, 15);
        Color3f green = new Color3f(rgb);
        material.setAmbientColor(green);

        rgb = new Color(255, 255, 255);
        Color3f white = new Color3f(rgb);
        material.setDiffuseColor(white);
        material.setSpecularColor(white);
        material.setShininess(128.0f);

        appear.setMaterial(material);
        appear.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.4f));

        return appear;
    }

    //--------------------------------------------------

    public static Appearance eyeAppearance() {
        Appearance appear = new Appearance();
        Material material = new Material();

        Color rgb = new Color(180, 180, 180);
        Color3f white = new Color3f(rgb);
        material.setAmbientColor(white);
        material.setShininess(128.0f);

        appear.setMaterial(material);

        return appear;
    }

    //--------------------------------------------------

    public static Appearance eyeTransAppearance() {
        Appearance appear = new Appearance();
        Material material = new Material();

        Color rgb = new Color(0, 0, 140);
        Color3f blue = new Color3f(rgb);
        material.setAmbientColor(blue);

        rgb = new Color(255, 255, 255);
        Color3f white = new Color3f(rgb);
        material.setDiffuseColor(white);
        material.setSpecularColor(white);
        material.setShininess(128.0f);

        appear.setMaterial(material);
        appear.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f));

        return appear;
    }

    //--------------------------------------------------

    public static Appearance shoeAppearance() {
        Appearance appear = new Appearance();
        Material material = new Material();

        Color rgb = new Color(64, 0, 0);
        Color3f brown = new Color3f(rgb);
        material.setAmbientColor(brown);
        appear.setMaterial(material);

        return appear;
    }

    //--------------------------------------------------

    public static Appearance grassAppearance() {

        Appearance appear = new Appearance();
        // Create a texture from the image file
        TextureLoader tex = new TextureLoader("grass.jpg", AppMain.theApp);
        if (tex != null)
            appear.setTexture(tex.getTexture());
        return appear;

    }

    //--------------------------------------------------

    public static Appearance beltAppearance() {
        Appearance appear = new Appearance();
        Material material = new Material();

        Color rgb = new Color(0, 0, 50);
        Color3f cords = new Color3f(rgb);
        material.setAmbientColor(cords);
        appear.setMaterial(material);

        return appear;
    }

    private static Appearance mattAppearance(Color3f diffuseColor){
        // ambientColor, emissiveColor, diffuseColor, specularColor, shininess
        Material skinMat = new Material(black, black, diffuseColor, black, 20.0f);
        skinMat.setColorTarget(Material.AMBIENT_AND_DIFFUSE);

        Appearance appearance = new Appearance();
        appearance.setMaterial(skinMat);

        return appearance;
    }
}
