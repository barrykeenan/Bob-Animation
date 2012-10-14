package com.keeny;

import com.sun.j3d.utils.image.TextureLoader;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
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

        Appearance appearance = mattAppearance(navy);

        // Create a texture from the image file
        TextureLoader tl = new TextureLoader("cords.jpg", null);
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

    public Appearance skin() {
        Color3f skin = rgb(255, 181, 145);

        return mattAppearance(skin);
    }

    private Appearance mattAppearance(Color3f diffuseColor){
        // ambientColor, emissiveColor, diffuseColor, specularColor, shininess
        Material material = new Material(black, black, diffuseColor, black, 20.0f);
        material.setColorTarget(Material.AMBIENT_AND_DIFFUSE);

        Appearance appearance = new Appearance();
        appearance.setMaterial(material);

        return appearance;
    }

    private Appearance plasticAppearance(Color3f diffuseColor){
        // ambientColor, emissiveColor, diffuseColor, specularColor, shininess
        Material material = new Material(black, black, diffuseColor, black, 120.0f);
        material.setColorTarget(Material.AMBIENT_AND_DIFFUSE);

        Appearance appearance = new Appearance();
        appearance.setMaterial(material);

        return appearance;
    }

    private static Color3f rgb(int r, int g, int b){
        return new Color3f(new Color(r, g, b));
    }

}