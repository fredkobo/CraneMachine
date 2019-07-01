package fredkobo.co.za;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView controlCircle;
    private ImageView clawImage;
    private Button grabButton;
    private ImageView bigWhiteBox;
    private float boxYPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlCircle = findViewById(R.id.control_circle);
        clawImage = findViewById(R.id.claw);
        grabButton = findViewById(R.id.grab_button);
        bigWhiteBox = findViewById(R.id.large_white_box);
        (findViewById(R.id.large_white_box)).getHeight();
        boxYPosition = (findViewById(R.id.large_white_box)).getHeight();

        controlCircle.setOnTouchListener(new ControlCircleDragListener(controlCircle.getX(), controlCircle.getY()));
        grabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeGrab();
            }
        });
    }

    private void invokeGrab() {
        final ObjectAnimator descendAnimation = ObjectAnimator.ofFloat(clawImage, "translationY", 1100f);
        descendAnimation.setDuration(2000);
        descendAnimation.start();

        final ObjectAnimator presieAscendAnimation = ObjectAnimator.ofFloat(bigWhiteBox, "translationY", -1100f);
        presieAscendAnimation.setDuration(2000);

        final ObjectAnimator presieGoLeftAnimation = ObjectAnimator.ofFloat(bigWhiteBox, "translationX", -500f);
        presieGoLeftAnimation.setDuration(2000);

        final ObjectAnimator presieGoDropAnimation = ObjectAnimator.ofFloat(bigWhiteBox, "translationY", 1000f);
        presieGoDropAnimation.setDuration(2000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                descendAnimation.reverse();
                presieAscendAnimation.start();
            }
        },3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ObjectAnimator goLeftAnimation = ObjectAnimator.ofFloat(clawImage, "translationX", -200f);
                goLeftAnimation.setDuration(2000);
                goLeftAnimation.start();
                presieGoLeftAnimation.start();
            }
        },5000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presieGoDropAnimation.start();
            }
        }, 7000);

    }

    private class ControlCircleDragListener implements View.OnTouchListener{

        boolean isDragging = false;
        float lastX;
        float lastY;
        float deltaX;

        public ControlCircleDragListener(float initalX, float initialY) {
            lastX = initalX;
            lastY = initialY;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN && !isDragging) {
                isDragging = true;
                deltaX = event.getX();
                return true;
            } else if (isDragging) {
                if (action == MotionEvent.ACTION_MOVE) {
                    view.setX(view.getX() + event.getX() - deltaX);
                    view.setY(view.getY());
                    clawImage.setTranslationX(view.getX() + event.getX() - deltaX);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    isDragging = false;
                    lastX = event.getX();
                    lastY = event.getY();
                    return true;
                } else if (action == MotionEvent.ACTION_CANCEL) {
                    view.setX(lastX);
                    view.setY(lastY);
                    isDragging = false;
                    return true;
                }
            }
            return false;
        }
    }
}
