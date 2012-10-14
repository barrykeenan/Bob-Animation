package com.keeny;

import com.sun.j3d.utils.image.TextureLoader;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import java.awt.*;


/**
 * Created with IntelliJ IDEA.
 * User: keeny
 * Date: 14/10/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */

public class AppearanceFactory {

    private static Color3f black = rgb(0, 0, 0);

    public AppearanceFactory() {
    }

    public Appearance cords() {
        Color3f navy = rgb(100, 100, 250);

        return textureAppearance(navy, "cords.jpg");
    }

    public Appearance skin() {
        Color3f skin = rgb(255, 181, 145);

        return mattAppearance(skin);
    }

    public Appearance hair() {
        Color3f ginger = rgb(255, 128, 0);

        Appearance appearance = textureAppearance(ginger, "hair.jpg");

        // Scale the texture so it's about the same size as the original file
        TextureAttributes texAttr = appearance.getTextureAttributes();
        Transform3D textureTrans = new Transform3D();
        textureTrans.setScale(4);
        texAttr.setTextureTransform(textureTrans);

        appearance.setTextureAttributes(texAttr);

        return appearance;
    }

    private Appearance mattAppearance(Color3f diffuseColor){
        // ambientColor, emissiveColor, diffuseColor, specularColor, shininess
        Material material = new Material(black, black, diffuseColor, black, 20.0f);
        material.setColorTarget(Material.AMBIENT_AND_DIFFUSE);

        Appearance appearance = new Appearance();
        appearance.setMaterial(material);

        return appearance;
    }

    private Appearance textureAppearance(Color3f baseColour, String filename) {
        Appearance appearance = mattAppearance(baseColour);

        // Create a texture from the image file
        TextureLoader tl = new TextureLoader(filename, null);
        // Attach the texture to the appearance
        if (tl != null) {
            Texture cords = tl.getTexture();
            appearance.setTexture(cords);
        }

        // Set blend mode
        TextureAttributes textAttr = new TextureAttributes();
        textAttr.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(textAttr);

        return appearance;
    }

    private static Color3f rgb(int r, int g, int b){
        return new Color3f(new Color(r, g, b));
    }

}