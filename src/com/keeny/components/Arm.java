package com.keeny.components;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3f;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: keeny
 * Date: 08-Nov-2004
 * Time: 13:57:24
 * To change this template use File | Settings | File Templates.
 */
public class Arm extends BranchGroup {

    //globals
    private boolean left;

    //--------------------------------------------------

    //Constructor
    public Arm(boolean left) {

        this.left = left;
        addChild(upperArm());
    }

    //--------------------------------------------------

    private TransformGroup upperArm() {

        //Re-use to place objects
        Transform3D offset = new Transform3D();

        //MAKE SHOUDLER
        // Create a transform group + identity transform to permit the shoulder to rotate
        Transform3D shoulderT3D = new Transform3D();
        TransformGroup shoulderTG = new TransformGroup();
        shoulderTG.setTransform(shoulderT3D);
        shoulderTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        shoulderTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Set shoulder angle depending on left or right
        TransformGroup shoulderAngleTG = new TransformGroup();
        shoulderTG.addChild(shoulderAngleTG);
        Transform3D shoulderRot = new Transform3D();
        shoulderRot.rotZ(left ? Math.toRadians(-19) : Math.toRadians(19));
        shoulderAngleTG.setTransform(shoulderRot);
        shoulderAngleTG.addChild(new Sphere(0.13f, Sphere.GENERATE_NORMALS, Appearances.torsoAppearance()));

        //Make upper Arm
        offset.setTranslation(new Vector3f(0.0f, -0.18f, 0.0f));
        TransformGroup upperArmTG = new TransformGroup();
        upperArmTG.setTransform(offset);
        shoulderAngleTG.addChild(upperArmTG);
        upperArmTG.addChild(new Cylinder(0.13f, 0.4f, Appearances.torsoAppearance()));

        //Make upper Arm 2 - add to the above TG
        offset.setTranslation(new Vector3f(0.0f, -0.3f, 0.0f));
        TransformGroup upperArm2TG = new TransformGroup();
        upperArm2TG.setTransform(offset);
        upperArmTG.addChild(upperArm2TG);
        upperArm2TG.addChild(new Cylinder(0.11f, 0.3f, Appearances.skinAppearance()));

        //Add elbow + lower arm
        TransformGroup elbowTG = new TransformGroup();
        offset.setTranslation(new Vector3f(0.0f, -0.15f, -0.01f));
        elbowTG.setTransform(offset);
        upperArm2TG.addChild(elbowTG);
        elbowTG.addChild(lowerArm());

        //ANIMATION OF SHOUDLER
        // Rotation is about the local Y axis by default
        //Create a transform and rotate it by 90 degrees about the Z axis.
        //This means objects will now be rotated about the X axis.
        Transform3D xAxis = new Transform3D();
        xAxis.rotZ(Math.PI / 2);

        //Make alpha ramp for the shoulder - sinc left arm with right leg
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, left ? 0 : 500,
                500, 3, 0,
                500, 3, 0);
        BoundingSphere system = new BoundingSphere();

        // Create the rotation interpolator to rotate the shoulder
        RotationInterpolator shoulderInterpolator = new RotationInterpolator(alpha, shoulderTG, xAxis,
                (float) Math.toRadians(-20), (float) Math.toRadians(15));
        shoulderInterpolator.setSchedulingBounds(system);
        shoulderTG.addChild(shoulderInterpolator);

        return shoulderTG;

    }

    //--------------------------------------------------

    private TransformGroup lowerArm() {

        //Re-use to place objects
        Transform3D offset = new Transform3D();

        //MAKE ELBOW
        // Create a transform group + identity transform to permit the elbow to rotate
        Transform3D elbowT3D = new Transform3D();
        TransformGroup elbowTG = new TransformGroup();
        elbowTG.setTransform(elbowT3D);
        elbowTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        elbowTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Make elbow
        TransformGroup elbowAngleTG = new TransformGroup();
        Transform3D elbowAngleT3D = new Transform3D();
        elbowAngleT3D.setRotation(new AxisAngle4d(0, 0, 1,
                left ? Math.toRadians(16) : Math.toRadians(-16)));
        elbowAngleTG.setTransform(elbowAngleT3D);
        elbowTG.addChild(elbowAngleTG);
        elbowAngleTG.addChild(new Sphere(0.11f, Sphere.GENERATE_NORMALS, Appearances.skinAppearance()));

        //Make lower Arm - add it to the elbowAngleTG NOT the global localRoot
        offset.setTranslation(new Vector3f(0.0f, -0.13f, 0.01f));
        TransformGroup lowerArmTG = new TransformGroup();
        lowerArmTG.setTransform(offset);
        elbowAngleTG.addChild(lowerArmTG);
        lowerArmTG.addChild(new Cylinder(0.095f, 0.3f, Appearances.skinAppearance()));

        //Make lower Arm 2 - add to the above TG
        offset.setTranslation(new Vector3f(0.0f, -0.3f, 0.0f));
        TransformGroup lowerArm2TG = new TransformGroup();
        lowerArm2TG.setTransform(offset);
        lowerArmTG.addChild(lowerArm2TG);
        lowerArm2TG.addChild(new Cylinder(0.085f, 0.3f, Appearances.skinAppearance()));

        //Add the hand
        lowerArm2TG.addChild(hand());

        //ANIMATION OF ELBOW
        // Rotation is about the local Y axis by default
        //Create a transform and rotate it by 90 degrees about the Z axis.
        //This means objects will now be rotated about the X axis.
        Transform3D xAxis = new Transform3D();
        xAxis.rotZ(Math.PI / 2);

        //Make alpha ramp for the shoulder
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 500,
                200, 2, 100,
                200, 2, 500);
        BoundingSphere system = new BoundingSphere();

        // Create the rotation interpolator to rotate the elbow
        RotationInterpolator elbowInterpolator = new RotationInterpolator(alpha, elbowTG, xAxis,
                (float) Math.toRadians(10), (float) Math.toRadians(30));
        elbowInterpolator.setSchedulingBounds(system);
        elbowTG.addChild(elbowInterpolator);

        return elbowTG;

    }

    //--------------------------------------------------

    private TransformGroup hand() {

        // load the object file
        ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);
        Scene s = null;
        try {
            s = loader.load("hand2.obj");
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        //get the hand object from the scene and put it into a branchgroup
        BranchGroup handBG = s.getSceneGroup();
        //extract the shape so that you can set its appearance
        Shape3D hand = (Shape3D) handBG.getChild(0);
        hand.setAppearance(Appearances.skinAppearance());

        //do the transformation
        Transform3D handRotX = new Transform3D();
        handRotX.rotX(-1.7);
        Transform3D handRotZ = new Transform3D();
        Transform3D offset = new Transform3D();
        offset.setScale(0.45);
        if (left) {
            handRotZ.rotZ(-0.8);
            offset.setTranslation(new Vector3f(0.05f, -0.16f, -0.07f));

        } else {
            handRotZ.rotZ(1.8);
            offset.setTranslation(new Vector3f(-0.09f, -0.16f, -0.02f));

        }


        handRotX.mul(handRotZ);
        offset.mul(handRotX);
        TransformGroup handTG = new TransformGroup();
        handTG.setTransform(offset);
        handTG.addChild(handBG);

        return handTG;
    }

    //--------------------------------------------------

}
