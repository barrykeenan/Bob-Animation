package com.keeny.components;

import com.keeny.AppearanceFactory;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: keeny
 * Date: 08-Jan-2005
 * Time: 16:54:13
 * <p/>
 * This class contains all the geometry, primitives, appearances, etc
 * that make up the model of the character. Geometry for character's
 * arms and legs are in their respective clases.
 */
public class Person extends BranchGroup {

    AppearanceFactory materials = new AppearanceFactory();

    /**
     * Constructor - add the torso and pelvis components
     */
    public Person() {
        addChild(torso());
        addChild(pelvis());
    }

    //--------------------------------------------------

    /**
     * Torso - arm objects are added to the torso
     *
     * @return
     */
    private TransformGroup torso() {

        //re-use this transform to place objects
        Transform3D offset = new Transform3D();

        // Create an identity transform and transform group to permit the torso to rotate
        Transform3D torsoT3D = new Transform3D();
        TransformGroup torsoTG = new TransformGroup();
        torsoTG.setTransform(torsoT3D);
        torsoTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        torsoTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Make the trunk - add all other shapes to the trunk
        offset.setScale(new Vector3d(1.0, 1.0, 0.5));
        offset.setTranslation(new Vector3f(0.0f, 0.5f, 0.0f));
        TransformGroup trunkTG = new TransformGroup();
        trunkTG.setTransform(offset);
        torsoTG.addChild(trunkTG);
//        trunkTG.addChild(new Cylinder(0.4f, 1.0f, Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS, Appearances.shirtAppearance()));

        trunkTG.addChild(new Cylinder(0.4f, 1.0f, Primitive.GENERATE_TEXTURE_COORDS, Appearances.shirtAppearance()));

        //Make collar
        offset.setScale(new Vector3d(1.0, 0.3, 1.0));
        offset.setTranslation(new Vector3f(0.0f, 0.5f, 0.0f));
        TransformGroup collarTG = new TransformGroup();
        collarTG.setTransform(offset);
        trunkTG.addChild(collarTG);
        collarTG.addChild(new Sphere(0.4f, Sphere.GENERATE_NORMALS, Appearances.torsoAppearance()));

        //Make left arm
        offset.setScale(new Vector3d(1.0, 1.0, 2.0));
        offset.setTranslation(new Vector3f(-0.37f, 0.42f, 0.0f));
        TransformGroup leftShoulderTG = new TransformGroup();
        leftShoulderTG.setTransform(offset);
        trunkTG.addChild(leftShoulderTG);
        Arm leftArm = new Arm(true);
        leftShoulderTG.addChild(leftArm);

        //Make right arm
        offset.setScale(new Vector3d(1.0, 1.0, 2.0));
        offset.setTranslation(new Vector3f(0.37f, 0.42f, 0.0f));
        TransformGroup rightShoulderTG = new TransformGroup();
        rightShoulderTG.setTransform(offset);
        trunkTG.addChild(rightShoulderTG);
        Arm rightArm = new Arm(false);
        rightShoulderTG.addChild(rightArm);

        //Make the neck
        offset.setScale(new Vector3d(1.0, 1.0, 2.0));
        offset.setTranslation(new Vector3f(0.0f, 0.72f, 0.0f));
        TransformGroup neckTG = new TransformGroup();
        neckTG.setTransform(offset);
        trunkTG.addChild(neckTG);
        neckTG.addChild(new Cone(0.15f, 0.3f, Appearances.skinAppearance()));

        //attach the head
        neckTG.addChild(head());

        //ANIMATION OF TORSO
        // Rotation is about the local Y axis by default
        //Create a transform and rotate it by 90 degrees about the X axis.
        //This means objects will now be rotated about the Z axis.
        Transform3D zAxis = new Transform3D();
        zAxis.rotX(Math.PI / 2);

        //Make alpha ramp for the torso
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 0,
                500, 3, 0,
                500, 3, 0);
        BoundingSphere system = new BoundingSphere();

        // Create the rotation interpolator to rotate the torso
        RotationInterpolator torsoInterpolator = new RotationInterpolator(alpha, torsoTG, zAxis,
                (float) Math.toRadians(2), (float) Math.toRadians(-2));
        torsoInterpolator.setSchedulingBounds(system);
        torsoTG.addChild(torsoInterpolator);

        return torsoTG;

    }

    //--------------------------------------------------

    private TransformGroup head() {

        //re-use this transform to place objects
        Transform3D offset = new Transform3D();

        // Create a transform group for the head
        Transform3D headoffset = new Transform3D();
        TransformGroup headTG = new TransformGroup();
        headTG.setTransform(headoffset);
        headTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        headTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Make face - attach all subsequent shapes to this TG
        Transform3D faceT3D = new Transform3D();
        faceT3D.setScale(new Vector3d(1.8, 1.8, 1.8));
        faceT3D.setTranslation(new Vector3f(0.0f, 0.33f, 0.05f));
        TransformGroup faceTG = new TransformGroup();
        faceTG.setTransform(faceT3D);
        headTG.addChild(faceTG);
        faceTG.addChild(new Sphere(0.25f, Sphere.GENERATE_NORMALS |
                Sphere.GENERATE_TEXTURE_COORDS, 20, Appearances.faceAppearance()));

        //Make left ear
        offset.setTranslation(new Vector3f(-0.2f, -0.04f, -0.01f));
        TransformGroup leftEarTG = new TransformGroup();
        leftEarTG.setTransform(offset);
        faceTG.addChild(leftEarTG);
        leftEarTG.addChild(new Sphere(0.09f, Sphere.GENERATE_NORMALS, Appearances.skinAppearance()));

        //Make right ear
        offset.setTranslation(new Vector3f(0.2f, -0.04f, -0.01f));
        TransformGroup rightEarTG = new TransformGroup();
        rightEarTG.setTransform(offset);
        faceTG.addChild(rightEarTG);
        rightEarTG.addChild(new Sphere(0.09f, Sphere.GENERATE_NORMALS, Appearances.skinAppearance()));

        //Make hair
        Transform3D hairRotate = new Transform3D();
        hairRotate.rotX(-Math.PI / 4.0);
        Transform3D hairT3D = new Transform3D();
        hairT3D.setScale(new Vector3d(1.0, 0.8, 1.0));
        hairT3D.setTranslation(new Vector3f(0.0f, 0.18f, 0.0f));
        hairRotate.mul(hairT3D);
        TransformGroup hairTG = new TransformGroup();
        hairTG.setTransform(hairRotate);
        faceTG.addChild(hairTG);
        Sphere test = new Sphere(0.4f, Sphere.GENERATE_NORMALS |
                Sphere.GENERATE_TEXTURE_COORDS, 20, Appearances.hairAppearance());
        hairTG.addChild(test);

        //Make band
        Transform3D bandRotate = new Transform3D();
        bandRotate.rotX(-Math.PI / 5.0);
        Transform3D bandT3D = new Transform3D();
        bandT3D.setTranslation(new Vector3f(0.0f, -0.055f, 0.01f));
        bandRotate.mul(bandT3D);
        TransformGroup bandTG = new TransformGroup();
        bandTG.setTransform(bandRotate);
        faceTG.addChild(bandTG);
        bandTG.addChild(new Cylinder(0.25f, 0.05f, Appearances.hatAppearance()));

        //Make peak
        Transform3D peakT3D = new Transform3D();
        peakT3D.setTranslation(new Vector3f(0.0f, 0.09f, 0.2f));
        peakT3D.setScale(new Vector3d(0.8, 1.0, 1.0));
        TransformGroup peakTG = new TransformGroup();
        peakTG.setTransform(peakT3D);
        faceTG.addChild(peakTG);
        peakTG.addChild(new Cone(0.25f, 0.06f, Appearances.hatAppearance()));

        //Make left eye
        Transform3D leftEyeRotate = new Transform3D();
        leftEyeRotate.rotX(-Math.PI / 2.0);
        Transform3D leftEyeT3D = new Transform3D();
        leftEyeT3D.setTranslation(new Vector3f(-0.07f, -0.011f, 0.25f));
        leftEyeT3D.mul(leftEyeRotate);
        TransformGroup leftEyeTG = new TransformGroup();
        leftEyeTG.setTransform(leftEyeT3D);
        faceTG.addChild(leftEyeTG);
        leftEyeTG.addChild(new Cylinder(0.05f, 0.001f, Appearances.eyeAppearance()));
        leftEyeTG.addChild(new Cylinder(0.055f, 0.025f, Appearances.eyeTransAppearance()));

        //Make leftHook
        Transform3D leftHookRotateX = new Transform3D();
        leftHookRotateX.rotX(-Math.PI / 2.0);
        Transform3D leftHookRotateY = new Transform3D();
        leftHookRotateY.rotY(Math.PI / 5.0);
        Transform3D leftHookT3D = new Transform3D();
        leftHookT3D.setTranslation(new Vector3f(-0.19f, 0.005f, 0.15f));
        leftHookRotateY.mul(leftHookRotateX);
        leftHookT3D.mul(leftHookRotateY);
        TransformGroup leftHookTG = new TransformGroup();
        leftHookTG.setTransform(leftHookT3D);
        faceTG.addChild(leftHookTG);
        leftHookTG.addChild(new Cylinder(0.003f, 0.25f, Appearances.eyeAppearance()));
        leftHookTG.addChild(new Cylinder(0.0031f, 0.251f, Appearances.eyeTransAppearance()));

        //Make right eye
        Transform3D rightEyeRotate = new Transform3D();
        rightEyeRotate.rotX(-Math.PI / 2.0);
        Transform3D rightEyeT3D = new Transform3D();
        rightEyeT3D.setTranslation(new Vector3f(0.07f, -0.011f, 0.25f));
        rightEyeT3D.mul(rightEyeRotate);
        TransformGroup rightEyeTG = new TransformGroup();
        rightEyeTG.setTransform(rightEyeT3D);
        faceTG.addChild(rightEyeTG);
        rightEyeTG.addChild(new Cylinder(0.05f, 0.001f, Appearances.eyeAppearance()));
        rightEyeTG.addChild(new Cylinder(0.055f, 0.025f, Appearances.eyeTransAppearance()));

        //Make rightHook
        Transform3D rightHookRotateX = new Transform3D();
        rightHookRotateX.rotX(-Math.PI / 2.0);
        Transform3D rightHookRotateY = new Transform3D();
        rightHookRotateY.rotY(-Math.PI / 5.0);
        Transform3D rightHookT3D = new Transform3D();
        rightHookT3D.setTranslation(new Vector3f(0.19f, 0.005f, 0.15f));
        rightHookRotateY.mul(rightHookRotateX);
        rightHookT3D.mul(rightHookRotateY);
        TransformGroup rightHookTG = new TransformGroup();
        rightHookTG.setTransform(rightHookT3D);
        faceTG.addChild(rightHookTG);
        rightHookTG.addChild(new Cylinder(0.003f, 0.25f, Appearances.eyeAppearance()));
        rightHookTG.addChild(new Cylinder(0.0031f, 0.251f, Appearances.eyeTransAppearance()));


        //Make bridge
        Transform3D bridgeRotate = new Transform3D();
        bridgeRotate.rotZ(-Math.PI / 2.0);
        Transform3D bridgeT3D = new Transform3D();
        bridgeT3D.setTranslation(new Vector3f(0.0f, 0.008f, 0.25f));
        bridgeT3D.mul(bridgeRotate);
        TransformGroup bridgeTG = new TransformGroup();
        bridgeTG.setTransform(bridgeT3D);
        faceTG.addChild(bridgeTG);
        bridgeTG.addChild(new Cylinder(0.002f, 0.05f, Appearances.eyeAppearance()));
        bridgeTG.addChild(new Cylinder(0.0021f, 0.051f, Appearances.eyeTransAppearance()));

        //Make the nose
        offset.setTranslation(new Vector3f(0.0f, -0.045f, 0.24f));
        TransformGroup noseTG = new TransformGroup();
        noseTG.setTransform(offset);
        faceTG.addChild(noseTG);
        noseTG.addChild(new Cone(0.05f, 0.08f, Appearances.skinAppearance()));

        //ANIMATION OF HEAD
        // Rotation is about the local Y axis by default
        //Create a transform and rotate it by 90 degrees about the Z axis.
        //This means objects will now be rotated about the X axis.
        Transform3D xAxis = new Transform3D();
        xAxis.rotZ(Math.PI / 2);

        //Make alpha ramp for the head
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 0,
                500, 1, 0,
                500, 1, 0);
        BoundingSphere system = new BoundingSphere();

        // Create the rotation interpolator to rotate the head
        RotationInterpolator headInterpolator = new RotationInterpolator(alpha, headTG, xAxis,
                (float) Math.toRadians(-6), (float) Math.toRadians(0));
        headInterpolator.setSchedulingBounds(system);
        headTG.addChild(headInterpolator);

        return headTG;

    }

    //--------------------------------------------------

    /**
     * Pelvis - leg objects are added to the pelvis
     *
     * @return
     */
    private BranchGroup pelvis() {

        BranchGroup localRoot = new BranchGroup();
        //Re-use to place objects
        Transform3D offset = new Transform3D();

        int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

        //Make pelvis
        offset.setScale(new Vector3d(1.0, 0.8, 0.5));
        offset.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
        TransformGroup pelvisTG = new TransformGroup();
        pelvisTG.setTransform(offset);
        localRoot.addChild(pelvisTG);
        pelvisTG.addChild(new Sphere(0.4f, primflags, 20, materials.cords()));

        //Make belt
        offset.setScale(new Vector3d(1.0, 1.0, 0.5));
        offset.setTranslation(new Vector3f(0.0f, -0.05f, 0.0f));
        TransformGroup beltTG = new TransformGroup();
        beltTG.setTransform(offset);
        localRoot.addChild(beltTG);
        beltTG.addChild(new Cylinder(0.41f, 0.1f, primflags, materials.cords()));

        //Make left leg - move down by offset
        offset.setScale(new Vector3d(1.0, 1.0, 1.0));
        offset.setTranslation(new Vector3f(-0.21f, -0.16f, -0.01f));
        TransformGroup leftLegTG = new TransformGroup();
        leftLegTG.setTransform(offset);
        localRoot.addChild(leftLegTG);
        Leg leftLeg = new Leg(0);
        leftLegTG.addChild(leftLeg);

        //Make right leg
        offset.setScale(new Vector3d(1.0, 1.0, 1.0));
        offset.setTranslation(new Vector3f(0.21f, -0.16f, -0.01f));
        TransformGroup rightLegTG = new TransformGroup();
        rightLegTG.setTransform(offset);
        localRoot.addChild(rightLegTG);
        //Delay the right leg by 550 - the time it takes for the
        //left leg to reach its lowest angle (-12 degrees)
        Leg rightLeg = new Leg(550);
        rightLegTG.addChild(rightLeg);

        return localRoot;

    }


}
