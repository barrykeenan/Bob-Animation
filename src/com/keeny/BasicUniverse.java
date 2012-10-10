package com.keeny;

import javax.media.j3d.*;
import javax.vecmath.Vector3f;

public class BasicUniverse extends Object {
    public VirtualUniverse universe;
    Locale locale;
    BranchGroup viewPlatformRoot;
    TransformGroup viewPlatformTransform;
    ViewPlatform viewPlatform;
    View view;
    Canvas3D canvas;
    float zViewDistance;

    public BasicUniverse(Canvas3D extCanvas, float zViewDist) {
        canvas = extCanvas;
        zViewDistance = zViewDist;
        universe = new VirtualUniverse();
        locale = new Locale(universe);
        viewPlatformRoot = new BranchGroup();
        viewPlatformTransform = new TransformGroup();
        viewPlatformTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        viewPlatformTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        setupViewPlatform();
        viewPlatformRoot.addChild(viewPlatformTransform);
        setupView(extCanvas);
        view.attachViewPlatform(viewPlatform);
        locale.addBranchGraph(viewPlatformRoot);
    }

    private void setupView(Canvas3D extCanvas) {
        view = new View();
        view.addCanvas3D(extCanvas);
        view.setPhysicalBody(new PhysicalBody());
        view.setPhysicalEnvironment(new PhysicalEnvironment());
    }

    private void setupViewPlatform()
            // Translate the view platform back by zViewDistance metres
    {
        viewPlatform = new ViewPlatform();
        setViewingDistance(zViewDistance);
        viewPlatformTransform.addChild(viewPlatform);
    }

    public void setViewingDistance(float viewDistance) {
        zViewDistance = viewDistance;
        Transform3D transform = new Transform3D();
        transform.set(new Vector3f(0.0f, 0.0f, zViewDistance));
        viewPlatformTransform.setTransform(transform);
    }

    public TransformGroup getViewPlatformTransform() {
        return viewPlatformTransform;
    }

    public View getView() {
        return view;
    }

    public void addBranchGraph(BranchGroup bg) {
        locale.addBranchGraph(bg);
    }
}

