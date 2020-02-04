package com.example.arcorefilter;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private ModelRenderable modelRenderable;
    private boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomARFragment customARFragment = (CustomARFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        ModelRenderable.builder()
                .setSource(this, Uri.parse("sunglasses.sfb"))
                .build()
                .thenAccept(modelRenderable1 -> {
                   modelRenderable = modelRenderable1;
                   modelRenderable.setShadowCaster(false);
                   modelRenderable.setShadowReceiver(false);
                });

        customARFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable ==null)
                return;

            Frame frame = customARFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for(AugmentedFace augmentedFace: augmentedFaces){

                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);

                isAdded = true;
            }
        });
    }
}
