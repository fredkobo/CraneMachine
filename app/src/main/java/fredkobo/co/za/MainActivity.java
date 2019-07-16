package fredkobo.co.za;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView controlCircle;
    private View clawImage;
    private Button grabButton;
    private ImageView bigWhiteBox;
    private View arm;
    private ImageView leftClaw;
    private ImageView rightClaw;
    private ImageView bigWhiteBoxGrabbed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlCircle = findViewById(R.id.control_circle);
        clawImage = findViewById(R.id.claw);
        grabButton = findViewById(R.id.grab_button);
        bigWhiteBox = findViewById(R.id.large_white_box);
        bigWhiteBoxGrabbed = findViewById(R.id.large_white_box_grabbed);
        arm = findViewById(R.id.arm);
        leftClaw = findViewById(R.id.claw_left);
        rightClaw = findViewById(R.id.claw_right);

        controlCircle.setOnTouchListener(new ControlCircleDragListener(controlCircle.getX(), controlCircle.getY()));
        grabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeGrab();
            }
        });
    }

    private void invokeGrab() {
        openClaw();

        final ObjectAnimator descendAnimation = ObjectAnimator.ofFloat(clawImage, "translationY", 500f);
        descendAnimation.setDuration(2000);
        descendAnimation.start();

        arm.setX(clawImage.getX() + (clawImage.getWidth()/2));
        final ObjectAnimator armDescendAnimation = ObjectAnimator.ofFloat(arm, "translationY", 500f);
        armDescendAnimation.setDuration(2000);
        armDescendAnimation.start();

        final ObjectAnimator presieAscendAnimation = ObjectAnimator.ofFloat(bigWhiteBox, "translationY", -500f);
        presieAscendAnimation.setDuration(2000);

        final ObjectAnimator presieGoLeftAnimation = ObjectAnimator.ofFloat(bigWhiteBox, "translationX", -450f);
        presieGoLeftAnimation.setDuration(2000);

        final ObjectAnimator presieGoDropAnimation = ObjectAnimator.ofFloat(bigWhiteBox, "translationY", 1000f);
        presieGoDropAnimation.setDuration(1500);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                descendAnimation.reverse();
                armDescendAnimation.reverse();
                presieAscendAnimation.start();
                closeClaw();
            }
        },3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ObjectAnimator goLeftAnimation = ObjectAnimator.ofFloat(clawImage, "translationX", -60f);
                goLeftAnimation.setDuration(2000);
                goLeftAnimation.start();
                presieGoLeftAnimation.start();
            }
        },5000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presieGoDropAnimation.start();
                openClaw();
            }
        }, 7000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeClaw();
            }
        }, 7500);

        final Animation animShake = AnimationUtils.loadAnimation(this,R.anim.shake);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bigWhiteBoxGrabbed.setVisibility(View.VISIBLE);
                bigWhiteBoxGrabbed.startAnimation(animShake);
            }
        }, 8000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),PrizeActivity.class));
            }
        }, 9000);

    }

    private void openClaw() {
        leftClaw.animate().rotation(35).start();
        rightClaw.animate().rotation(-35).start();
    }

    private void closeClaw() {
        leftClaw.animate().rotation(-35).start();
        rightClaw.animate().rotation(35).start();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recreate:
                recreate();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
