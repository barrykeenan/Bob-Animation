package com.keeny.components;

import com.keeny.AppearanceFactory;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: keeny
 * Date: 08-Nov-2004
 * Time: 13:57:24
 * To change this template use File | Settings | File Templates.
 */
public class Leg extends BranchGroup {

    private long phaseDelay;
    private AppearanceFactory materials = new AppearanceFactory();
    private int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

    //Constructor
    public Leg(long phaseDelay) {
        this.phaseDelay = phaseDelay;
        addChild(upperLeg());
    }

    //--------------------------------------------------

    private TransformGroup upperLeg() {

        //used for placing individual components of the leg
        Transform3D offset = new Transform3D();

        //MAKE HIP
        Transform3D hipT3D = new Transform3D();
        TransformGroup hipTG = new TransformGroup();
        hipTG.setTransform(hipT3D);
        hipTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        hipTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Add the hip shape
        hipTG.addChild(new Sphere(0.2f, primflags, 20, materials.cords()));


        //MAKE UPPER LEG
        //Move the cylinder down 0.2 - the size of the sphere
        offset.setTranslation(new Vector3f(0.0f, -0.2f, 0.0f));
        TransformGroup upperLegTG = new TransformGroup();
        upperLegTG.setTransform(offset);
        hipTG.addChild(upperLegTG);
        upperLegTG.addChild(new Cylinder(0.2f, 0.4f, primflags, materials.cords()));

        //Make upper leg 2
        //Move cylinder down 0.4 - the length of the previous shape
        offset.setTranslation(new Vector3f(0.0f, -0.4f, 0.0f));
        TransformGroup upperLeg2TG = new TransformGroup();
        upperLeg2TG.setTransform(offset);
        upperLegTG.addChild(upperLeg2TG);
        upperLeg2TG.addChild(new Cylinder(0.19f, 0.4f, primflags, materials.cords()));


        //ATTACH LOWER LEG
        //Move offset down 0.2 - half the length of the previous shape
        //Move forward a bit as well to make a knee shape
        offset.setTranslation(new Vector3f(0.0f, -0.2f, 0.02f));
        TransformGroup kneeTG = new TransformGroup();
        kneeTG.setTransform(offset);
        upperLeg2TG.addChild(kneeTG);
        kneeTG.addChild(lowerLeg());


        //ANIMATION OF HIP
        // Rotation is about the local Y axis by default
        //Create a transform and rotate it by 90 degrees about the Z axis.
        //This means objects will now be rotated about the X axis.
        Transform3D xAxis = new Transform3D();
        xAxis.rotZ(Math.PI / 2);

        //Make alpha ramp for the hip
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 0 + phaseDelay,
                550, 3, 0,
                450, 3, 0);
        BoundingSphere system = new BoundingSphere();

        // Create the rotation interpolator to rotate the hip
        RotationInterpolator hipInterpolator = new RotationInterpolator(alpha, hipTG, xAxis,
                (float) Math.toRadians(29), (float) Math.toRadians(-12));
        hipInterpolator.setSchedulingBounds(system);
        hipTG.addChild(hipInterpolator);

        return hipTG;

    }

    //--------------------------------------------------

    private TransformGroup lowerLeg() {

        Transform3D offset = new Transform3D();

        //MAKE KNEE
        // Create a TG for the first part of the knee animation
        Transform3D kneeT3D1 = new Transform3D();
        TransformGroup kneeTG1 = new TransformGroup();
        kneeTG1.setTransform(kneeT3D1);
        kneeTG1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        kneeTG1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        // Create another TG for the second part of the knee rotation
        Transform3D kneeT3D = new Transform3D();
        TransformGroup kneeTG = new TransformGroup();
        kneeTG.setTransform(kneeT3D);
        kneeTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        kneeTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //add to the first kneeTG - anything added to this TG will be animated
        kneeTG1.addChild(kneeTG);

        //Add the knee shape
        kneeTG.addChild(new Sphere(0.19f, primflags, 20, materials.cords()));

        //MAKE LOWER LEG
        //Lower leg component 1
        offset.setTranslation(new Vector3f(0.0f, -0.2f, -0.02f));
        TransformGroup lowerLegTG = new TransformGroup();
        lowerLegTG.setTransform(offset);
        kneeTG.addChild(lowerLegTG);
        lowerLegTG.addChild(new Cylinder(0.19f, 0.4f, primflags, materials.cords()));

        //Lower leg component 2
        offset.setTranslation(new Vector3f(0.0f, -0.5f, 0.0f));
        TransformGroup lowerLeg2TG = new TransformGroup();
        lowerLeg2TG.setTransform(offset);
        lowerLegTG.addChild(lowerLeg2TG);
        lowerLeg2TG.addChild(new Cylinder(0.21f, 0.6f, primflags, materials.cords()));


        //ATTACH FOOT
        lowerLeg2TG.addChild(foot());


        //ANIMATION OF KNEE
        // Rotation is about the local Y axis by default
        //Create a transform and rotate it by 90 degrees about the Z axis.
        //This means objects will now be rotated about the X axis.
        Transform3D xAxis = new Transform3D();
        xAxis.rotZ(Math.PI / 2);

        //Make alpha ramp for FIRST portion of knee ramp
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 0 + phaseDelay,
                150, 3, 0, /*Increase is shorter and ramped*/
                300, 1, 550); /*Decrease is longer and almost linear*/

        //Make alpha ramp for SECOND portion of knee ramp - delay by 450
        Alpha alpha2 = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 450 + phaseDelay,
                300, 3, 0, /*Increase is longer and ramped*/
                250, 3, 450); /*Decrease is shorter and ramped*/

        // FIRST rotation interpolator for the knee
        BoundingSphere system = new BoundingSphere();
        RotationInterpolator kneeInterpolator = new RotationInterpolator(alpha, kneeTG1, xAxis,
                (float) Math.toRadians(-3), (float) Math.toRadians(-14));
        kneeInterpolator.setSchedulingBounds(system);

        kneeTG1.addChild(kneeInterpolator);

        // SECOND rotation interpolator for the knee
        RotationInterpolator kneeInterpolator2 = new RotationInterpolator(alpha2, kneeTG, xAxis,
                (float) Math.toRadians(-3), (float) Math.toRadians(-50));
        kneeInterpolator2.setSchedulingBounds(system);

        kneeTG.addChild(kneeInterpolator2);

        return kneeTG1;
    }

    //--------------------------------------------------

    private TransformGroup foot() {

        Transform3D offset = new Transform3D();

        //MAKE FOOT
        //We need three TG's for animation of the ankle
        // 1
        Transform3D ankleT3D1 = new Transform3D();
        TransformGroup ankleTG1 = new TransformGroup();
        ankleTG1.setTransform(ankleT3D1);
        ankleTG1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ankleTG1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //2
        Transform3D ankleT3D2 = new Transform3D();
        TransformGroup ankleTG2 = new TransformGroup();
        ankleTG2.setTransform(ankleT3D2);
        ankleTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ankleTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        //add to the first ankleTG
        ankleTG1.addChild(ankleTG2);

        //3
        Transform3D ankleT3D3 = new Transform3D();
        TransformGroup ankleTG3 = new TransformGroup();
        ankleTG3.setTransform(ankleT3D3);
        ankleTG3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ankleTG3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        //add to the previous ankleTG
        ankleTG2.addChild(ankleTG3);

        //Scale transform to get size of foot right
        Transform3D scaleT3D = new Transform3D();
        scaleT3D.setScale(new Vector3d(1.19, 1.19, 1.19));
        TransformGroup scaleTG = new TransformGroup();
        scaleTG.setTransform(scaleT3D);
        ankleTG3.addChild(scaleTG);

        //Make top
        offset.setScale(new Vector3d(0.62, 0.4, 1.0));
        offset.setTranslation(new Vector3f(0.0f, -0.2f, 0.1f));
        TransformGroup topTG = new TransformGroup();
        topTG.setTransform(offset);
        scaleTG.addChild(topTG);
        topTG.addChild(new Sphere(0.27f, Sphere.GENERATE_NORMALS, Appearances.shoeAppearance()));

        //Make the sole
        offset.setScale(new Vector3d(0.62, 1.0, 1.0));
        offset.setTranslation(new Vector3f(0.0f, -0.23f, 0.1f));
        TransformGroup soleTG = new TransformGroup();
        soleTG.setTransform(offset);
        scaleTG.addChild(soleTG);
        soleTG.addChild(new Cylinder(0.27f, 0.07f, Appearances.shoeAppearance()));

        //ANIMATION OF ANKLE
        // Rotation is about the local Y axis by default
        //Create a transform and rotate it by 90 degrees about the Z axis.
        //This means objects will now be rotated about the X axis.
        Transform3D xAxis = new Transform3D();
        xAxis.rotZ(Math.PI / 2);

        //Make alpha ramp for FIRST portion of ankle ramp
        //Total Ramp Duration - 1000
        //Duration - 500, Delay - 500
        Alpha alpha1 = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 50 + phaseDelay,
                400, 3, 0, /*Increase is long*/
                100, 3, 500); /*Decrease is short*/

        //Make alpha ramp for SECOND portion of ankle ramp
        //Time of ramp - 150, Delay - 850
        Alpha alpha2 = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 550 + phaseDelay,
                75, 3, 0, /*Increase is longer and ramped*/
                75, 3, 850); /*Decrease is shorter and ramped*/

        //Make alpha ramp for THIRD portion of ankle ramp - delay by 450
        //Time of Ramp - 350, Delay - 650
        Alpha alpha3 = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 700 + phaseDelay,
                150, 3, 0, /*Increase is shorter and ramped*/
                200, 3, 650); /*Decrease is longer and ramped*/

        // FIRST rotation interpolator for the ankle
        BoundingSphere system = new BoundingSphere();
        RotationInterpolator ankleInterpolator1 = new RotationInterpolator(alpha1, ankleTG1, xAxis,
                (float) Math.toRadians(0), (float) Math.toRadians(15));
        ankleInterpolator1.setSchedulingBounds(system);
        ankleTG1.addChild(ankleInterpolator1);

        // SECOND rotation interpolator for the ankle
        RotationInterpolator ankleInterpolator2 = new RotationInterpolator(alpha2, ankleTG2, xAxis,
                (float) Math.toRadians(0), (float) Math.toRadians(-9));
        ankleInterpolator2.setSchedulingBounds(system);
        ankleTG2.addChild(ankleInterpolator2);

        // THIRD rotation interpolator for the ankle
        RotationInterpolator ankleInterpolator3 = new RotationInterpolator(alpha3, ankleTG3, xAxis,
                (float) Math.toRadians(0), (float) Math.toRadians(7));
        ankleInterpolator3.setSchedulingBounds(system);
        ankleTG3.addChild(ankleInterpolator3);

        return ankleTG1;
    }

    //--------------------------------------------------

}
